package Warehouse;

import Mode.Mode;
import Format.Format;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.Date;
import java.util.Random;

class Inform {
    private static JButton okBt;
    private static JLabel lbInform;
    private static JPanel pButton, pInform;
    private static JDialog inform;

    public static void initInform(JFrame menuWarehouseDetail, String message) {
        inform = new JDialog(menuWarehouseDetail, "Thông báo", true);
        inform.setLayout(new GridLayout(2, 1));
        inform.setSize(300, 200);
        inform.setLocationRelativeTo(menuWarehouseDetail);

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
}

class WarehouseInfor extends JPanel {
    private JLabel lbId, lbAddress, lbCapacity, lbReceiveDate, lbCurrentCapacity;
    private JTextField txtId, txtAddress, txtCapacity, txtCurrentCapacity;
    private JDateChooser lastReceiveDate;
    private JPanel pId, pAddress, pCapacity, pReceiveDate, pCurrentCapacity;

    public void initpCurrentCapacity(String idWarehouse) {
        lbCurrentCapacity = new JLabel("Sức chứa đã dùng");
        txtCurrentCapacity = new JTextField(25);
        txtCurrentCapacity
                .setText(Format.normalizeNumber(String.valueOf(WarehouseBusiness.getCurrentCapacity(idWarehouse))));
        txtCurrentCapacity.setEditable(false);

        pCurrentCapacity = new JPanel();
        pCurrentCapacity.setLayout(new GridLayout(1, 2));
        pCurrentCapacity.add(lbCurrentCapacity);
        pCurrentCapacity.add(txtCurrentCapacity);
    }

    public void initpReceiveDate() {
        lbReceiveDate = new JLabel("Ngày nhập kho");
        lastReceiveDate = new JDateChooser();
        lastReceiveDate.setSize(500, 500);

        pReceiveDate = new JPanel();
        pReceiveDate.setLayout(new GridLayout(1, 2));
        pReceiveDate.add(lbReceiveDate);
        pReceiveDate.add(lastReceiveDate);
    }

    public void initpId(String idWarehouse) {
        lbId = new JLabel("Mã kho");
        txtId = new JTextField(25);
        txtId.setEditable(false);
        txtId.setText(idWarehouse);

        pId = new JPanel();
        pId.setLayout(new GridLayout(1, 2));
        pId.add(lbId);
        pId.add(txtId);
    }

    public void initpAddress(String idWarehouse) {
        lbAddress = new JLabel("Địa chỉ");
        txtAddress = new JTextField(25);
        txtAddress.setEditable(false);

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "select address from warehouse where idWarehouse = '" + idWarehouse + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                txtAddress.setText(rs.getString("address"));
            }
        } catch (SQLException e) {
            Logger.getLogger(WarehouseDetailUI.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        pAddress = new JPanel();
        pAddress.setLayout(new GridLayout(1, 2));
        pAddress.add(lbAddress);
        pAddress.add(txtAddress);
    }

    public void initpCapacity(String idWarehouse) {
        lbCapacity = new JLabel("Sức chứa tối đa");
        txtCapacity = new JTextField(25);
        txtCapacity.setEditable(false);

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "select maxCapacity from warehouse where idWarehouse = '" + idWarehouse + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                txtCapacity.setText(Format.normalizeNumber(String.valueOf(rs.getString("maxCapacity"))));
            }
        } catch (SQLException e) {
            Logger.getLogger(WarehouseDetailUI.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        pCapacity = new JPanel();
        pCapacity.setLayout(new GridLayout(1, 2));
        pCapacity.add(lbCapacity);
        pCapacity.add(txtCapacity);
    }

    public WarehouseInfor(String idWarehouse, Mode mode) {
        setSize(600, 300);
        setLayout(new GridLayout(3, 1));
        setBorder(new TitledBorder("Thông tin kho"));

        initpId(idWarehouse);
        initpAddress(idWarehouse);
        initpCapacity(idWarehouse);

        add(pId);
        add(pAddress);
        add(pCapacity);
        if (mode == Mode.ADD || mode == Mode.RECEIVE) {
            setLayout(new GridLayout(4, 1));
            initpReceiveDate();
            add(pReceiveDate);
        }

        else if (mode == Mode.EDIT) {
            txtAddress.setEditable(true);
            txtCapacity.setText(Format.originalForm(txtCapacity.getText()));
            txtCapacity.setEditable(true);
        }

        else {
            setLayout(new GridLayout(4, 1));
            initpCurrentCapacity(idWarehouse);
            add(pCurrentCapacity);
        }

        setVisible(true);
    }

    public JTextField getTxtIdW() {
        return txtId;
    }

    public JTextField getTxtAddress() {
        return txtAddress;
    }

    public JTextField getTxtCapacity() {
        return txtCapacity;
    }

    public JDateChooser getlastReceiveDate() {
        return lastReceiveDate;
    }
}

class WarehouseDetailPanel extends JPanel {
    private JTable warehouseDetailTable;
    private DefaultTableModel dtm;

    public String getStateProduct(int quantityInStock) {
        if (quantityInStock >= 1 && quantityInStock <= 5)
            return "Sắp hết hàng";
        else if (quantityInStock > 5)
            return "Còn hàng";
        return "Hết hàng";
    }

    public void loadData(String idWarehouse) {
        dtm.setRowCount(0);

        ArrayList<WarehouseDetail> list = WarehouseDetailBusiness.showWarehouseDetail(idWarehouse);
        for (WarehouseDetail i : list) {
            Object[] row = { i.getIdWarehouse(), i.getIdProduct(), i.getNameProduct(),
                    WarehouseDetailBusiness.getOrigin(i.getIdProduct()), i.getLastReceiveDate(),
                    String.valueOf(i.getQuantityInStock()),
                    getStateProduct(i.getQuantityInStock()) };
            dtm.addRow(row);
        }
    }

    public WarehouseDetailPanel(String idWarehouse) {
        setLayout(new GridLayout(1, 1));

        warehouseDetailTable = new JTable();
        String[] nameColumns = { "Mã kho", "Mã sản phẩm", "Tên", "Xuất xứ", "Ngày cuối nhập kho", "Số lượng sẵn có",
                "Trạng thái" };
        dtm = new DefaultTableModel(nameColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadData(idWarehouse);

        warehouseDetailTable.setModel(dtm);
        warehouseDetailTable.setRowSelectionAllowed(true);
        add(new JScrollPane(warehouseDetailTable));

        setVisible(true);
    }

    public JTable getTableWarehouseDetail() {
        return warehouseDetailTable;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }
}

class AddingFrame extends JFrame {
    private JButton acptBt, closeBt;
    private JPanel button;

    public void initActionClose(ProductPanel productPanel) {
        closeBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                productPanel.loadtData();
                setVisible(false);
            }
        });
    }

    public void initActionAccept(JFrame menuWarehouseDetail, ProductPanel productPanel, WarehouseInfor warehouseInfor2,
            WarehousePanel warehousePanel, WarehouseDetailPanel warehouseDetailPanel) {
        acptBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String idWarehouse = warehouseInfor2.getTxtIdW().getText();
                int maxCapacity = Integer.parseInt(warehouseInfor2.getTxtCapacity().getText());
                if (WarehouseDetailBusiness.isFullCapacity(idWarehouse, maxCapacity, productPanel)) {
                    Inform.initInform(menuWarehouseDetail, "Kho đã đạt sức chứa tối đa, vui lòng thử lại");
                    productPanel.loadtData();
                    return;
                }

                Date date = warehouseInfor2.getlastReceiveDate().getDate();
                if (date == null) {
                    Inform.initInform(menuWarehouseDetail, "Vui lòng chọn ngày nhập kho");
                    return;
                }
                SimpleDateFormat normalize = new SimpleDateFormat("yyyy-MM-dd");
                String lastReceiveDate = normalize.format(date);

                boolean hasChoosen = false;
                JTable table = productPanel.getTableProduct();
                for (int i = 0; i < table.getRowCount(); i++) {
                    if (table.getValueAt(i, 5) != null) {
                        String idProduct = (String) table.getValueAt(i, 0);
                        String nameProduct = (String) table.getValueAt(i, 1);

                        int quantityInStock;
                        String quantity = (String) table.getValueAt(i, 5);
                        if (quantity.equals("")) {
                            quantityInStock = 0;
                        } else {
                            quantityInStock = Integer.parseInt((String) table.getValueAt(i, 5));
                        }

                        if (quantityInStock > 0) {
                            WarehouseDetail warehouseDetail = new WarehouseDetail(idWarehouse, idProduct, nameProduct,
                                    lastReceiveDate,
                                    quantityInStock);
                            if (WarehouseDetailBusiness.updateWarehouseDetail(warehouseDetail,
                                    lastReceiveDate) == false) {
                                WarehouseDetailBusiness.addWarehouseDetail(warehouseDetail);
                            }
                            hasChoosen = true;
                        }
                    }
                }

                warehousePanel.loadData();
                warehouseDetailPanel.loadData(idWarehouse);
                productPanel.loadtData();

                if (hasChoosen == false) {
                    Inform.initInform(menuWarehouseDetail, "Vui lòng chọn sản phẩm");
                    return;
                }
                setVisible(false);
            }
        });
    }

    public void initButton(ProductPanel productPanel, JFrame menuWarehouseDetail, WarehouseInfor warehouseInfor2,
            Mode mode, WarehousePanel warehousePanel, WarehouseDetailPanel warehouseDetailPanel) {
        acptBt = new JButton("Xác nhận");
        closeBt = new JButton("Đóng");

        button = new JPanel();
        button.setLayout(new FlowLayout(FlowLayout.RIGHT));
        button.add(acptBt);
        button.add(closeBt);

        initActionClose(productPanel);
        initActionAccept(menuWarehouseDetail, productPanel, warehouseInfor2, warehousePanel, warehouseDetailPanel);
    }

    public AddingFrame(ProductPanel productPanel, JFrame menuWarehouseDetail, WarehouseInfor warehouseInfor,
            Mode mode, WarehousePanel warehousePanel, WarehouseDetailPanel warehouseDetailPanel) {
        setTitle("Thêm sản phẩm");
        setSize(950, 600);
        setLayout(new BorderLayout());
        String idWarehouse = warehouseInfor.getTxtIdW().getText();
        String address = warehouseInfor.getTxtAddress().getText();
        String capacity = warehouseInfor.getTxtCapacity().getText();
        WarehouseInfor warehouseInfor2 = new WarehouseInfor(idWarehouse, Mode.RECEIVE);
        warehouseInfor2.getTxtAddress().setText(address);
        warehouseInfor2.getTxtCapacity().setText(capacity);

        initButton(productPanel, menuWarehouseDetail, warehouseInfor2, mode, warehousePanel, warehouseDetailPanel);

        add(warehouseInfor2, BorderLayout.NORTH);
        add(productPanel, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
    }
}

class ButtonPanelWarehouse extends JPanel {
    private JButton closeBt, acptBt, addBt, delBt, reloadBt;

    public void initActionReload(WarehousePanel warehousePanel, WarehouseDetailPanel warehouseDetailPanel,
            WarehouseInfor warehouseInfor) {
        reloadBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                warehouseDetailPanel.loadData(warehouseInfor.getTxtIdW().getText());
                warehousePanel.loadData();
            }
        });
    }

    public void initActionDelete(WarehousePanel warehousePanel, WarehouseDetailPanel warehouseDetailPanel) {
        delBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int row = warehouseDetailPanel.getTableWarehouseDetail().getSelectedRow();
                if (row != -1) {
                    String idWarehouse = (String) warehouseDetailPanel.getTableWarehouseDetail().getValueAt(row, 0);
                    String idProduct = (String) warehouseDetailPanel.getTableWarehouseDetail().getValueAt(row, 1);

                    WarehouseDetailBusiness.deleteWarehouseDetail(idWarehouse, idProduct);
                    warehouseDetailPanel.loadData(idWarehouse);
                    warehousePanel.loadData();
                }
            }
        });
    }

    public void initActionAdd(Mode mode, JFrame menuWarehouseDetail, ProductPanel productPanel,
            WarehouseInfor warehouseInfor,
            WarehousePanel warehousePanel, WarehouseDetailPanel warehouseDetailPanel) {
        addBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                AddingFrame addingFrame = new AddingFrame(productPanel, menuWarehouseDetail, warehouseInfor, mode,
                        warehousePanel, warehouseDetailPanel);
                addingFrame.setLocationRelativeTo(null);
                addingFrame.setVisible(true);
            }
        });
    }

    public void initActionClose(JFrame menuWarehouseDetail) {
        closeBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                menuWarehouseDetail.setVisible(false);
            }
        });
    }

    public void initActionAccept(Mode mode, JFrame menuWarehouseDetail, ProductPanel productPanel,
            WarehouseInfor warehouseInfor, WarehousePanel warehousePanel, WarehouseDetailPanel warehouseDetailPanel) {
        acptBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String idWarehouse = "";
                String address = "";
                int maxCapacity = 0;
                if (mode == Mode.ADD) {
                    Random rand = new Random();
                    idWarehouse = "W" + rand.nextInt(10000);
                    while (WarehouseBusiness.isExist(idWarehouse)) {
                        idWarehouse = "W" + rand.nextInt(10000);
                    }
                    address = warehouseInfor.getTxtAddress().getText();
                    maxCapacity = Integer.parseInt(warehouseInfor.getTxtCapacity().getText());

                    if (WarehouseBusiness.isValidAddress(address.trim()) == false) {
                        Inform.initInform(menuWarehouseDetail, "Địa chỉ không hợp lệ, vui lòng thử lại");
                        return;
                    }

                    if (WarehouseDetailBusiness.isFullCapacity(idWarehouse, maxCapacity, productPanel)) {
                        Inform.initInform(menuWarehouseDetail, "Kho đã đạt sức chứa tối đa, vui lòng thử lại");
                        productPanel.loadtData();
                        return;
                    }

                    Date date = warehouseInfor.getlastReceiveDate().getDate();
                    if (date == null) {
                        Inform.initInform(menuWarehouseDetail, "Vui lòng chọn ngày nhập kho");
                        return;
                    }
                    SimpleDateFormat normalize = new SimpleDateFormat("yyyy-MM-dd");
                    String lastReceiveDate = normalize.format(date);

                    WarehouseBusiness.addWarehouse(new Warehouse(idWarehouse, address, maxCapacity));
                    JTable table = productPanel.getTableProduct();
                    for (int i = 0; i < table.getRowCount(); i++) {
                        if (table.getValueAt(i, 5) != null) {
                            String idProduct = (String) table.getValueAt(i, 0);
                            String nameProduct = (String) table.getValueAt(i, 1);

                            int quantityInStock;
                            String quantity = (String) table.getValueAt(i, 5);
                            if (quantity.equals("")) {
                                quantityInStock = 0;
                            } else {
                                quantityInStock = Integer.parseInt((String) table.getValueAt(i, 5));
                            }

                            if (quantityInStock > 0) {
                                WarehouseDetail warehouseDetail = new WarehouseDetail(idWarehouse, idProduct,
                                        nameProduct,
                                        lastReceiveDate,
                                        quantityInStock);

                                WarehouseDetailBusiness.addWarehouseDetail(warehouseDetail);
                            }
                        }
                    }
                } else if (mode == Mode.EDIT) {
                    idWarehouse = warehouseInfor.getTxtIdW().getText();
                    address = warehouseInfor.getTxtAddress().getText();
                    maxCapacity = Integer.parseInt(warehouseInfor.getTxtCapacity().getText());

                    if (WarehouseBusiness.isValidAddress(address.trim()) == false) {
                        Inform.initInform(menuWarehouseDetail, "Địa chỉ không hợp lệ, vui lòng thử lại");
                        return;
                    }

                    if (WarehouseDetailBusiness.isFullCapacity(idWarehouse, maxCapacity, productPanel)) {
                        Inform.initInform(menuWarehouseDetail, "Kho đã đạt sức chứa tối đa, vui lòng thử lại");
                        return;
                    }

                    WarehouseBusiness.updateWarehouse(new Warehouse(idWarehouse, address, maxCapacity));
                }

                warehousePanel.loadData();
                menuWarehouseDetail.setVisible(false);
            }
        });
    }

    public ButtonPanelWarehouse(JFrame menuWarehouseDetail, Mode mode, ProductPanel productPanel,
            WarehouseInfor warehouseInfor, WarehousePanel warehousePanel, WarehouseDetailPanel warehouseDetailPanel) {
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        if (mode != Mode.ADD) {
            reloadBt = new JButton("Làm mới");
            add(reloadBt);
            initActionReload(warehousePanel, warehouseDetailPanel, warehouseInfor);
        }

        if (mode == Mode.ADD || mode == Mode.EDIT) {
            acptBt = new JButton("Xác nhận");
            add(acptBt);
            initActionAccept(mode, menuWarehouseDetail, productPanel, warehouseInfor,
                    warehousePanel, warehouseDetailPanel);
        }

        if (mode == Mode.EDIT) {
            addBt = new JButton("Thêm");
            add(addBt);
            initActionAdd(mode, menuWarehouseDetail, productPanel, warehouseInfor, warehousePanel,
                    warehouseDetailPanel);

            delBt = new JButton("Xóa");
            add(delBt);
            initActionDelete(warehousePanel, warehouseDetailPanel);
        }

        closeBt = new JButton("Đóng");
        add(closeBt);
        initActionClose(menuWarehouseDetail);
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
            Logger.getLogger(WarehouseDetailUI.class.getName()).log(Level.SEVERE, null, e);
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

public class WarehouseDetailUI {
    public static void showWarehouseDetail(String idWarehouse, Mode mode, WarehousePanel warehousePanel) {
        JFrame menuWarehouseDetail = new JFrame("Chi tiết kho");
        menuWarehouseDetail.setSize(1100, 600);
        menuWarehouseDetail.setLayout(new BorderLayout());

        WarehouseInfor warehouseInfor = new WarehouseInfor(idWarehouse, mode);
        menuWarehouseDetail.add(warehouseInfor, BorderLayout.NORTH);

        WarehouseDetailPanel warehouseDetailPanel = new WarehouseDetailPanel(idWarehouse);
        ProductPanel productPanel = new ProductPanel();

        if (mode == Mode.SHOW) {
            menuWarehouseDetail.setTitle("Thông tin kho");
            menuWarehouseDetail.add(warehouseDetailPanel, BorderLayout.CENTER);
        }

        else if (mode == Mode.EDIT) {
            menuWarehouseDetail.setTitle("Sửa thông tin kho");
            menuWarehouseDetail.add(warehouseDetailPanel, BorderLayout.CENTER);
        }

        else if (mode == Mode.ADD) {
            menuWarehouseDetail.setTitle("Thêm kho");
            menuWarehouseDetail.setSize(1100, 600);
            menuWarehouseDetail.add(productPanel, BorderLayout.CENTER);
            warehouseInfor.getTxtAddress().setEditable(true);
            warehouseInfor.getTxtCapacity().setEditable(true);
        }

        ButtonPanelWarehouse buttonPanel = new ButtonPanelWarehouse(menuWarehouseDetail, mode,
                productPanel, warehouseInfor,
                warehousePanel, warehouseDetailPanel);
        menuWarehouseDetail.add(buttonPanel, BorderLayout.SOUTH);

        menuWarehouseDetail.setLocationRelativeTo(null);
        menuWarehouseDetail.setVisible(true);
    }
}