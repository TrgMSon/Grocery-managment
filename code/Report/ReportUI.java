package Report;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import Mode.Mode;
import Format.Format;
import Connection.DataConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;;

// làm thông báo khi chưa nhập ngày trong báo cáo, làm thêm phần báo cáo năm, làm bản word project

class ButtonPanel extends JPanel {
    private JRadioButton monthOpt, dayOpt;

    public ButtonPanel(ReportPanel reportPanel) {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        monthOpt = new JRadioButton("Tháng");
        dayOpt = new JRadioButton("Ngày");
        dayOpt.setSelected(true);
        initActionDay(monthOpt, dayOpt, reportPanel);
        initActionMonth(monthOpt, dayOpt, reportPanel);

        add(dayOpt);
        add(monthOpt);

        setVisible(true);
    }

    public void initActionMonth(JRadioButton monthOpt, JRadioButton dayOpt, ReportPanel reportPanel) {
        monthOpt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (monthOpt.isSelected()) {
                    dayOpt.setSelected(false);
                }
                else {
                    monthOpt.setSelected(true);
                }
                reportPanel.setModeString("tháng");
                reportPanel.getTarget().setText("Tháng");
                reportPanel.getHeader().setText("Doanh thu theo tháng");
            }
        });
    }

    public void initActionDay(JRadioButton monthOpt, JRadioButton dayOpt, ReportPanel reportPanel) {
        dayOpt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (dayOpt.isSelected()) {
                    monthOpt.setSelected(false);
                }
                else {
                    dayOpt.setSelected(true);
                }
                reportPanel.setModeString("ngày");
                reportPanel.getTarget().setText("Ngày");
                reportPanel.getHeader().setText("Doanh thu theo ngày");
            }
        });
    }

    public JRadioButton getDayOpt() {
        return dayOpt;
    }

    public JRadioButton getMonthOpt() {
        return monthOpt;
    }
}

class PanelOldStock extends JPanel {
    private JTable tableCustomer;
    private DefaultTableModel dtm;
    private int duration;

    public PanelOldStock(int duration) {
        this.duration = duration;
        setLayout(new GridLayout(1, 1));

        tableCustomer = new JTable();
        String[] nameColumns = { "Mã sản phẩm", "Tên", "Giá (VND)", "Ngày cuối nhập kho", "Số lượng", "Mã kho",
                "Địa chỉ kho" };
        dtm = new DefaultTableModel(nameColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadData();

        tableCustomer.setModel(dtm);
        tableCustomer.setRowSelectionAllowed(true);
        add(new JScrollPane(tableCustomer));

        setVisible(true);
    }

    public void loadData() {
        dtm.setRowCount(0);

        LocalDate now = LocalDate.now();
        LocalDate old = now.minusDays(duration);
        DateTimeFormatter normalize = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String oldDate = normalize.format(old);

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();

            String sql = """
                    SELECT p.idProduct, wd.nameProduct AS nameproduct, p.cost, wd.lastReceiveDate, wd.quantityInStock, w.idWarehouse AS warehouseId, w.address
                     FROM warehousedetail AS wd
                     JOIN warehouse AS w ON w.idWarehouse = wd.idWarehouse
                     JOIN product AS p ON p.idProduct = wd.idProductW
                     WHERE wd.lastReceiveDate <= ?;
                                                            """;
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, oldDate);
            ResultSet rs = psm.executeQuery();

            while (rs.next()) {
                Object[] row = { rs.getString("idProduct"), rs.getString("nameproduct"),
                        Format.normalizeNumber(String.valueOf(rs.getBigDecimal("cost"))),
                        rs.getString("lastReceiveDate"), rs.getInt("quantityInStock"),
                        rs.getString("warehouseId"), rs.getString("address") };
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            Logger.getLogger(ReportUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public JTable getTableCustomer() {
        return tableCustomer;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }
}

class OldStock extends JFrame {
    private JPanel pButton;
    private JButton closeBt;

    public OldStock() {
        setLayout(new BorderLayout());
        setSize(1050, 200);
        setTitle("Hàng tồn kho");

        pButton = new JPanel();
        pButton.setLayout(new FlowLayout(FlowLayout.CENTER));
        closeBt = new JButton("Đóng");
        initActionClose(closeBt);
        pButton.add(closeBt);
        add(pButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void initActionClose(JButton closeBt) {
        closeBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
            }
        });
    }
}

class ReportPanel extends JPanel {
    // doanh thu theo năm/tháng/ngày, các hàng: doanh thu, 3 sản phẩm bán chạy, so

    private JLabel header, income, bestSeller, OldStock, target;
    private JTextField txtIncome, txtTarget, duration;
    private JTextArea txtBestSeller;
    private JPanel pHeader, pIncome, pBestSeller, pOldStock, pTarget;
    private JButton search, showOldStock;
    private Mode mode = Mode.DAY;
    private String modeString;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void getModeString() {
        if (mode == Mode.DAY)
            modeString = "ngày";
        else modeString = "tháng";
    }

    public void setModeString(String modeString) {
        this.modeString = modeString;
    }

    public JLabel getTarget() {
        return target;
    }

    public JLabel getHeader() {
        return header;
    }

    public ReportPanel() {
        setLayout(new GridLayout(5, 1));
        getModeString();

        pHeader = new JPanel();
        pHeader.setLayout(new FlowLayout(FlowLayout.CENTER));
        header = new JLabel("Doanh thu theo " + modeString);
        pHeader.add(header);
        add(pHeader);

        pTarget = new JPanel();
        pTarget.setLayout(new FlowLayout(FlowLayout.LEFT));
        target = new JLabel(modeString.substring(0, 1).toUpperCase() + modeString.substring(1).toLowerCase());
        txtTarget = new JTextField(25);
        search = new JButton("Tìm kiếm");
        initActionSearch(search);
        pTarget.add(target);
        pTarget.add(txtTarget);
        pTarget.add(search);
        add(pTarget);

        pIncome = new JPanel();
        pIncome.setLayout(new FlowLayout(FlowLayout.LEFT));
        income = new JLabel("Doanh thu");
        txtIncome = new JTextField(25);
        pIncome.add(income);
        pIncome.add(txtIncome);
        add(pIncome);

        pBestSeller = new JPanel();
        pBestSeller.setLayout(new BorderLayout());
        pBestSeller.setSize(100, 4000);
        bestSeller = new JLabel("Sản phẩm bán chạy nhất");
        txtBestSeller = new JTextArea(10, 50);
        txtBestSeller.setEditable(false);
        pBestSeller.add(bestSeller, BorderLayout.BEFORE_FIRST_LINE);
        pBestSeller.add(txtBestSeller, BorderLayout.BEFORE_LINE_BEGINS);
        add(pBestSeller);

        pOldStock = new JPanel();
        pOldStock.setLayout(new FlowLayout(FlowLayout.LEFT));
        OldStock = new JLabel("Số ngày tồn kho tối đa");
        duration = new JTextField(9);
        duration.setText("70");
        showOldStock = new JButton("Xem danh sách hàng tồn kho");
        initActionShow(showOldStock, duration);
        pOldStock.add(OldStock);
        pOldStock.add(duration);
        pOldStock.add(showOldStock);
        add(pOldStock);

        setVisible(true);
    }

    public void initActionShow(JButton showOldStock, JTextField duration) {
        showOldStock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                OldStock oldStock = new OldStock();
                PanelOldStock panelOldStock = new PanelOldStock(Integer.parseInt(duration.getText()));
                oldStock.add(panelOldStock, BorderLayout.CENTER);
            }
        });
    }

    public void initActionSearch(JButton search) {
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String key = txtTarget.getText();
                String totalIncome = String.valueOf(ReportBusiness.getIncome(key));
                String bestSellers = ReportBusiness.getBestSeller(key);
                txtIncome.setText(Format.normalizeNumber(totalIncome) + " VND");
                txtBestSeller.setText(bestSellers);
            }
        });
    }
}

public class ReportUI {
    public static void showMenu() {
        JFrame menuReport = new JFrame("Báo cáo");
        menuReport.setSize(600, 500);
        menuReport.setLayout(new BorderLayout());

        ReportPanel reportPanel = new ReportPanel();
        menuReport.add(reportPanel, BorderLayout.CENTER);

        ButtonPanel buttonPanel = new ButtonPanel(reportPanel);
        menuReport.add(buttonPanel, BorderLayout.NORTH);

        menuReport.setLocationRelativeTo(null);
        menuReport.setVisible(true);
    }
}
