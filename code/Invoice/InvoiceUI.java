package Invoice;

import Connection.DataConnection;
import Mode.Mode;
import Format.Format;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

class InvoicePanel extends JPanel {
    private JTable tableInvoice;
    private DefaultTableModel dtm;

    public InvoicePanel() {
        setLayout(new GridLayout(1, 1));

        tableInvoice = new JTable();
        String[] nameColumns = { "Mã hóa đơn", "Tổng tiền", "Ngày giao dịch", "Mã khách hàng" };
        dtm = new DefaultTableModel(nameColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadData();

        tableInvoice.setModel(dtm);
        tableInvoice.setRowSelectionAllowed(true);
        add(new JScrollPane(tableInvoice));

        setVisible(true);
    }

    public void loadData() {
        dtm.setRowCount(0);

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();

            String sql = "select * from invoice";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                Object[] row = { rs.getString("idInvoice"),
                        Format.normalizeNumber(String.valueOf(rs.getBigDecimal("totalAmount"))), rs.getString("date"),
                        rs.getString("idCustomer") };
                dtm.addRow(row);
            }

        } catch (SQLException e) {
            Logger.getLogger(InvoiceUI.class.getName()).log(Level.SEVERE, null, e);
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

    public JTable getTableInvoice() {
        return tableInvoice;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }
}

class ButtonPanelInvoice extends JPanel {
    private JButton addBt, delBt, detailBt, reloadBt;

    public void initActionReload(InvoicePanel invoicePanel) {
        reloadBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                invoicePanel.loadData();
            }
        });
    }

    public void initActionDetail(InvoicePanel invoicePanel) {
        detailBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int row = invoicePanel.getTableInvoice().getSelectedRow();

                if (row != -1) {
                    String idInvoice = String.valueOf(invoicePanel.getTableInvoice().getValueAt(row, 0));
                    String idCustomer = String.valueOf(invoicePanel.getTableInvoice().getValueAt(row, 3));
                    InvoiceDetailUI.showMenuDetail(idInvoice, idCustomer, Mode.SHOW, invoicePanel);
                }
            }
        });
    }

    public void initActionAdd(InvoicePanel invoicePanel) {
        addBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                InvoiceDetailUI.showMenuDetail("", "", Mode.ADD, invoicePanel);
            }
        });
    }

    public void initActionDel(InvoicePanel invoicePanel) {
        delBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int row = invoicePanel.getTableInvoice().getSelectedRow();
                if (row != -1) {
                    String id = (String) invoicePanel.getTableInvoice().getValueAt(row, 0);
                    InvoiceBusiness.deleteInvoice(id);
                    invoicePanel.loadData();
                }
            }
        });
    }

    public ButtonPanelInvoice(InvoicePanel invoicePanel) {
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        reloadBt = new JButton("Làm mới");
        addBt = new JButton("Thêm");
        delBt = new JButton("Xóa");
        detailBt = new JButton("Chi tiết");

        add(reloadBt);
        add(addBt);
        add(delBt);
        add(detailBt);

        initActionReload(invoicePanel);
        initActionDetail(invoicePanel);
        initActionAdd(invoicePanel);
        initActionDel(invoicePanel);

        setVisible(true);
    }

    public JButton getAddBt() {
        return addBt;
    }

    public JButton getDelBt() {
        return delBt;
    }
}

class SearchPanel extends JPanel {
    private JTextArea text;
    private JLabel label;
    private JButton searchBt;

    public void initActionSearch(InvoicePanel invoicePanel) {
        searchBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String keyword = text.getText();
                if (keyword.equals("") == false) {
                    ArrayList<Invoice> list = InvoiceBusiness.showListInvoice(keyword);

                    DefaultTableModel dtm = invoicePanel.getDtm();
                    dtm.setRowCount(0);
                    for (Invoice i : list) {
                        Object[] row = { i.getIdInvoice(), Format.normalizeNumber(String.valueOf(i.getTotal())),
                                i.getDate(), i.getIdCustomer() };
                        dtm.addRow(row);
                    }
                    invoicePanel.getTableInvoice().setModel(dtm);
                }
            }
        });
    }

    public SearchPanel(InvoicePanel invoicePanel) {
        setBorder(new TitledBorder("Nhập thông tin tìm kiếm"));
        setLayout(new FlowLayout(FlowLayout.LEFT));

        label = new JLabel("Từ khóa");
        text = new JTextArea(1, 25);
        searchBt = new JButton("Tìm kiếm");

        add(label);
        add(text);
        add(searchBt);

        initActionSearch(invoicePanel);

        setVisible(true);
    }

    public JTextArea getText() {
        return text;
    }

    public JButton getSearchBt() {
        return searchBt;
    }
}

public class InvoiceUI {
    public static void showMenu() {
        JFrame menuInvoice = new JFrame("Hóa đơn");
        menuInvoice.setSize(600, 600);
        menuInvoice.setLayout(new BorderLayout());

        InvoicePanel invoicePanel = new InvoicePanel();
        menuInvoice.add(invoicePanel, BorderLayout.CENTER);

        SearchPanel searchPanel = new SearchPanel(invoicePanel);
        menuInvoice.add(searchPanel, BorderLayout.NORTH);

        ButtonPanelInvoice buttonPanelInvoice = new ButtonPanelInvoice(invoicePanel);
        menuInvoice.add(buttonPanelInvoice, BorderLayout.SOUTH);

        menuInvoice.setLocationRelativeTo(null);
        menuInvoice.setVisible(true);
    }
}