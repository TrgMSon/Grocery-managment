package Invoice;

import Mode.Mode;
import Format.Format;
import Warehouse.WarehouseDetailBusiness;
import Customer.Customer;
import Customer.CustomerBusiness;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import Connection.DataConnection;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JDialog;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;

class ButtonPanel extends JPanel {
    private JButton closeBt, acptBt, okBt;
    private JDialog inform;
    private JLabel lbInform;
    private JPanel pButton, pInform;

    public void initInform(JFrame menuInvoiceDetail, String message) {
        inform = new JDialog(menuInvoiceDetail, "Thông báo", true);
        inform.setLayout(new GridLayout(2, 1));
        inform.setSize(300, 200);
        inform.setLocationRelativeTo(menuInvoiceDetail);

        okBt = new JButton("Đóng");
        okBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                inform.dispose();
            }
        });
        pButton = new JPanel();
        pButton.setLayout(new FlowLayout(FlowLayout.CENTER));
        pButton.add(okBt);

        pInform = new JPanel();
        pInform.setLayout(new FlowLayout(FlowLayout.CENTER));
        lbInform = new JLabel(message);
        pInform.add(lbInform);

        inform.add(pInform);
        inform.add(pButton);
        inform.setVisible(true);
    }

    public void initActionClose(JFrame menuInvoiceDetail) {
        closeBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                menuInvoiceDetail.setVisible(false);
            }
        });
    }

    public void initActionAccept(JFrame menuInvoiceDetail, ProductPanel productPanel, InforPanel inforPanel,
            InvoicePanel invoicePanel) {
        acptBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Random rand = new Random();
                String idInvoice = "I" + rand.nextInt(10000);
                while (InvoiceDetailBusiness.isExist(idInvoice)) {
                    idInvoice = "I" + rand.nextInt(10000);
                }
                String nameCustomer = "", phone = "";

                String idCustomer = CustomerBusiness.findIdCustomer(inforPanel.getTxtPhone().getText());
                if (idCustomer.compareTo("") == 0) {
                    idCustomer = "C" + rand.nextInt(10000);
                    while (CustomerBusiness.isExist(idCustomer)) {
                        idCustomer = "C" + rand.nextInt(10000);
                    }
                    nameCustomer = inforPanel.getTxtNameCustomer().getText();
                    phone = inforPanel.getTxtPhone().getText();
                    if (CustomerBusiness.validInforCustomer(idCustomer, nameCustomer, phone) == false) {
                        initInform(menuInvoiceDetail, "Vui lòng nhập đầy đủ thông tin khách hàng");
                        return;
                    } else {
                        CustomerBusiness.addCustomer(new Customer(idCustomer, nameCustomer, phone));
                    }
                }

                Date dateInvoice = inforPanel.getDateChooser().getDate();
                if (dateInvoice == null) {
                    initInform(menuInvoiceDetail, "Vui lòng nhập ngày giao dịch");
                    return;
                }
                SimpleDateFormat normalize = new SimpleDateFormat("yyyy-MM-dd");
                String date = normalize.format(dateInvoice);

                BigDecimal total = new BigDecimal("0");
                Invoice invoice = new Invoice(idInvoice, idCustomer, date, total);
                InvoiceBusiness.addInvoice(invoice);

                JTable table = productPanel.getTableProduct();
                for (int i = 0; i < table.getRowCount(); i++) {
                    if (table.getValueAt(i, 5) != null) {
                        String idProduct = (String) table.getValueAt(i, 0);
                        
                        Integer quantity;
                        String tmp = (String) table.getValueAt(i, 5);
                        if (tmp.equals("")) {
                            quantity = 0;
                        }
                        else {
                            quantity = Integer.parseInt((String) table.getValueAt(i, 5));
                        }

                        if (quantity == 0) continue;

                        BigDecimal cost = new BigDecimal(Format.originalForm(String.valueOf(table.getValueAt(i, 2))));
                        InvoiceDetailBusiness.addInvoiceDetail(idInvoice, idProduct, (String) table.getValueAt(i, 1), cost, quantity);

                        if (WarehouseDetailBusiness.decreaseQuantityInStock(idProduct, quantity, false) == true) {
                            BigDecimal qty = new BigDecimal(String.valueOf(quantity));
                            BigDecimal adding = cost.multiply(qty);
                            total = total.add(adding);
                            WarehouseDetailBusiness.decreaseQuantityInStock(idProduct, quantity, true);
                        } else {
                            initInform(menuInvoiceDetail, "Không đủ sản phẩm, vui lòng thử lại");
                            InvoiceBusiness.deleteInvoice(idInvoice);
                            productPanel.loadtData();
                            return;
                        }
                    }
                }
                if (total.equals(new BigDecimal("0"))) {
                    initInform(menuInvoiceDetail, "Vui lòng nhập số lượng sản phẩm");
                    InvoiceBusiness.deleteInvoice(idInvoice);
                    productPanel.loadtData();
                    return;
                }

                invoice.setTotal(total);
                InvoiceBusiness.updateInvoice(invoice);

                menuInvoiceDetail.setVisible(false);
                invoicePanel.loadData();
            }
        });
    }

    public ButtonPanel(JFrame menuInvoiceDetail, Mode mode, ProductPanel productPanel, InforPanel inforPanel,
            InvoicePanel invoicePanel) {
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        if (mode == Mode.ADD) {
            acptBt = new JButton("Xác nhận");
            add(acptBt);
            initActionAccept(menuInvoiceDetail, productPanel, inforPanel, invoicePanel);
        }

        closeBt = new JButton("Đóng");
        add(closeBt);
        initActionClose(menuInvoiceDetail);
    }
}

class InforPanel extends JPanel {
    private JLabel lbIdInvoice, lbIdCustomer, lbNameCustomer, lbPhone, lbDate;
    private JTextField txtIdInvoice, txtIdCustomer, txtNameCustomer, txtPhone, txtDate;
    private JDateChooser txtChooseDate;
    private JPanel pIdInvoice, pIdCustomer, pNamecustomer, pPhone, pDate;

    public void initChooseDate() {
        lbDate = new JLabel("Ngày giao dịch");
        txtChooseDate = new JDateChooser();
        txtChooseDate.setSize(500, 500);

        pDate = new JPanel();
        pDate.setLayout(new GridLayout(1, 2));
        pDate.add(lbDate);
        pDate.add(txtChooseDate);
    }

    public void initDate(String idInvoice) {
        lbDate = new JLabel("Ngày giao dịch");
        txtDate = new JTextField(25);
        txtDate.setEditable(false);

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "select date from invoice where idInvoice = '" + idInvoice + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next()) {
                txtDate.setText(rs.getString("date"));
            }
        } catch (SQLException e) {
            Logger.getLogger(InvoiceDetail.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        pDate = new JPanel();
        pDate.setLayout(new GridLayout(1, 2));
        pDate.add(lbDate);
        pDate.add(txtDate);
    }

    public void initpPhone(String idCustomer) {
        lbPhone = new JLabel("Số điện thoại");
        txtPhone = new JTextField(25);
        txtPhone.setEditable(false);

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "select phone from customer where idCustomer = '" + idCustomer + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next()) {
                txtPhone.setText(rs.getString("phone"));
            }
        } catch (SQLException e) {
            Logger.getLogger(InvoiceDetail.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        pPhone = new JPanel();
        pPhone.setLayout(new GridLayout(1, 2));
        pPhone.add(lbPhone);
        pPhone.add(txtPhone);
    }

    public void initpIdInvoice(String idInvoice) {
        lbIdInvoice = new JLabel("Mã hóa đơn");
        txtIdInvoice = new JTextField(25);
        txtIdInvoice.setEditable(false);
        txtIdInvoice.setText(idInvoice);

        pIdInvoice = new JPanel();
        pIdInvoice.setLayout(new GridLayout(1, 2));
        pIdInvoice.add(lbIdInvoice);
        pIdInvoice.add(txtIdInvoice);
    }

    public void initpIdCustomer(String idCustomer) {
        lbIdCustomer = new JLabel("Mã khách hàng");
        txtIdCustomer = new JTextField(25);
        txtIdCustomer.setEditable(false);
        txtIdCustomer.setText(idCustomer);

        pIdCustomer = new JPanel();
        pIdCustomer.setLayout(new GridLayout(1, 2));
        pIdCustomer.add(lbIdCustomer);
        pIdCustomer.add(txtIdCustomer);
    }

    public void initpNameCustomer(String idCustomer) {
        lbNameCustomer = new JLabel("Tên");
        txtNameCustomer = new JTextField(25);
        txtNameCustomer.setEditable(false);

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "select name from customer where idCustomer = '" + idCustomer + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next()) {
                txtNameCustomer.setText(rs.getString("name"));
            }
        } catch (SQLException e) {
            Logger.getLogger(InvoiceDetail.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        pNamecustomer = new JPanel();
        pNamecustomer.setLayout(new GridLayout(1, 2));
        pNamecustomer.add(lbNameCustomer);
        pNamecustomer.add(txtNameCustomer);
    }

    public InforPanel(String idInvoice, String idCustomer, Mode mode) {
        setSize(600, 300);
        setLayout(new GridLayout(5, 1));
        setBorder(new TitledBorder("Thông tin hóa đơn"));

        initpIdInvoice(idInvoice);
        initpIdCustomer(idCustomer);
        initpNameCustomer(idCustomer);
        initpPhone(idCustomer);

        add(pIdInvoice);
        add(pIdCustomer);
        add(pNamecustomer);
        add(pPhone);

        if (mode == Mode.ADD) {
            initChooseDate();
            add(pDate);
        } else {
            initDate(idInvoice);
            add(pDate);
        }

        setVisible(true);
    }

    public JTextField getTxtDate() {
        return txtDate;
    }

    public JTextField getTxtPhone() {
        return txtPhone;
    }

    public JTextField getTxtNameCustomer() {
        return txtNameCustomer;
    }

    public JDateChooser getDateChooser() {
        return txtChooseDate;
    }
}

class InvoiceDetailPanel extends JPanel {
    private JTable invoiceDetailTable;
    private DefaultTableModel dtm;

    public void loadData(String idInvoice) {
        dtm.setRowCount(0);

        ArrayList<InvoiceDetail> list = InvoiceDetailBusiness.showInvoiceDetail(idInvoice);
        for (InvoiceDetail i : list) {
            Object[] row = { i.getIdProduct(), i.getNameProduct(),
                    Format.normalizeNumber(String.valueOf(i.getCostProduct())),
                    String.valueOf(i.getQuantity()) };
            dtm.addRow(row);
        }
    }

    public InvoiceDetailPanel(String idInvoice, String idCustomer) {
        setLayout(new GridLayout(1, 1));

        invoiceDetailTable = new JTable();
        String[] nameColumns = { "Mã sản phẩm", "Tên", "Giá (VND)", "Số lượng" };
        dtm = new DefaultTableModel(nameColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadData(idInvoice);

        invoiceDetailTable.setModel(dtm);
        invoiceDetailTable.setRowSelectionAllowed(true);
        add(new JScrollPane(invoiceDetailTable));

        setVisible(true);
    }
}

class ProductPanel extends JPanel {
    private JTable tableProduct;
    private DefaultTableModel dtm;

    public void loadtData() {
        dtm.setRowCount(0);

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "select * from product";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                Object[] row = { rs.getString("idProduct"), rs.getString("name"),
                        Format.normalizeNumber(String.valueOf(rs.getBigDecimal("cost"))),
                        rs.getString("origin"), rs.getString("type") };
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            Logger.getLogger(InvoiceDetailUI.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }
    }

    public ProductPanel() {
        setLayout(new GridLayout(1, 1));

        tableProduct = new JTable();
        String[] namecolumns = { "Mã sản phẩm", "Tên", "Giá (VND)", "Xuất xứ", "Loại", "Số lượng" };
        dtm = new DefaultTableModel(namecolumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        loadtData();

        tableProduct.setModel(dtm);
        tableProduct.setRowSelectionAllowed(true);
        add(new JScrollPane(tableProduct));

        setVisible(true);
    }

    public JTable getTableProduct() {
        return tableProduct;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }
}

public class InvoiceDetailUI {
    public static void showMenuDetail(String idInvoice, String idCustomer, Mode mode, InvoicePanel invoicePanel) {
        JFrame menuInvoiceDetail = new JFrame("Chi tiết hóa đơn");
        menuInvoiceDetail.setSize(600, 600);
        menuInvoiceDetail.setLayout(new BorderLayout());

        InforPanel inforPanel = new InforPanel(idInvoice, idCustomer, mode);
        menuInvoiceDetail.add(inforPanel, BorderLayout.NORTH);

        InvoiceDetailPanel invoiceDetailPanel = new InvoiceDetailPanel(idInvoice, idCustomer);
        ProductPanel productPanel = new ProductPanel();

        if (mode == Mode.SHOW) {
            menuInvoiceDetail.add(invoiceDetailPanel, BorderLayout.CENTER);
        }

        else if (mode == Mode.ADD) {
            menuInvoiceDetail.setTitle("Thêm hóa đơn");
            menuInvoiceDetail.setSize(950, 600);
            menuInvoiceDetail.add(productPanel, BorderLayout.CENTER);
            // inforPanel.getTxtDate().setEditable(true);
            inforPanel.getTxtPhone().setEditable(true);
            inforPanel.getTxtNameCustomer().setEditable(true);
        }

        ButtonPanel buttonPanel = new ButtonPanel(menuInvoiceDetail, mode, productPanel, inforPanel, invoicePanel);
        menuInvoiceDetail.add(buttonPanel, BorderLayout.SOUTH);

        menuInvoiceDetail.setLocationRelativeTo(null);
        menuInvoiceDetail.setVisible(true);
    }
}