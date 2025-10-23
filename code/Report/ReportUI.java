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
import javax.swing.JButton;
import Mode.Mode;

class ButtonPanel extends JPanel {
    private JRadioButton monthOpt, dayOpt, yearOpt;
    
    public ButtonPanel(ReportPanel reportPanel) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        
        monthOpt = new JRadioButton("Tháng");
        dayOpt = new JRadioButton("Ngày");
        dayOpt.setSelected(true);
        yearOpt = new JRadioButton("Năm");
        initActionDay(monthOpt, dayOpt, yearOpt, reportPanel);
        initActionMonth(monthOpt, dayOpt, yearOpt, reportPanel);
        initActionYear(monthOpt, dayOpt, yearOpt, reportPanel);

        add(dayOpt);
        add(monthOpt);
        add(yearOpt);

        setVisible(true);
    }

    public void initActionMonth(JRadioButton monthOpt, JRadioButton dayOpt, JRadioButton yearOpt, ReportPanel reportPanel) {
        monthOpt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dayOpt.setSelected(false);
                yearOpt.setSelected(false);
                reportPanel.setMode(Mode.MONTH);
            }
        });
    }

    public void initActionDay(JRadioButton monthOpt, JRadioButton dayOpt, JRadioButton yearOpt, ReportPanel reportPanel) {
        dayOpt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                monthOpt.setSelected(false);
                yearOpt.setSelected(false);
                reportPanel.setMode(Mode.DAY);
            }
        });
    }

    public void initActionYear(JRadioButton monthOpt, JRadioButton dayOpt, JRadioButton yearOpt, ReportPanel reportPanel) {
        yearOpt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                monthOpt.setSelected(false);
                dayOpt.setSelected(false);
                reportPanel.setMode(Mode.YEAR);
            }
        });
    }

    public JRadioButton getDayOpt() {
        return dayOpt;
    }

    public JRadioButton getMonthOpt() {
        return monthOpt;
    }

    public JRadioButton getYearOpt() {
        return yearOpt;
    }
}

class ReportPanel extends JPanel {
    // doanh thu theo năm/tháng/ngày, các hàng: doanh thu, 3 sản phẩm bán chạy, so sánh với năm/tháng/ngày trước

    private JLabel header, income, bestSeller, preIncome, target;
    private JTextField txtIncome, txtBestSeller, txtPreIncome, txtTarget;
    private JPanel pHeader, pIncome, pBestSeller, pPreIncome, pTarget;
    private JButton search;
    private Mode mode = Mode.DAY;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getMode() {
        if (mode == Mode.DAY) return "ngày";
        else if (mode == Mode.MONTH) return "tháng";
        return  "năm";
    }

    public ReportPanel() {
        setLayout(new GridLayout(5, 1));

        pHeader = new JPanel();
        pHeader.setLayout(new FlowLayout(FlowLayout.CENTER));
        header = new JLabel("Doanh thu theo " + getMode());
        pHeader.add(header);
        add(pHeader);

        pTarget = new JPanel();
        pTarget.setLayout(new FlowLayout(FlowLayout.LEFT));
        target = new JLabel(getMode().substring(0, 1).toUpperCase() + getMode().substring(1).toLowerCase());
        txtTarget = new JTextField(25);
        search = new JButton("Tìm kiếm");
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
        pBestSeller.setLayout(new FlowLayout(FlowLayout.LEFT));
        bestSeller = new JLabel("Sản phẩm bán chạy nhất");
        txtBestSeller = new JTextField(25);
        pBestSeller.add(bestSeller);
        pBestSeller.add(txtBestSeller);
        add(pBestSeller);

        pPreIncome = new JPanel();
        pPreIncome.setLayout(new FlowLayout(FlowLayout.LEFT));
        preIncome = new JLabel("Doanh thu " + getMode() + " trước");
        txtPreIncome = new JTextField(25);
        pPreIncome.add(preIncome);
        pPreIncome.add(txtPreIncome);
        add(pPreIncome);

        setVisible(true);
    }

    public void initActionSearch(JButton search) {
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String key = txtTarget.getText();
            }
        });
    }
}

public class ReportUI {
    public static void showMenu() {
        JFrame menuReport = new JFrame("Báo cáo");
        menuReport.setSize(600, 300);
        menuReport.setLayout(new BorderLayout());

        ReportPanel reportPanel = new ReportPanel();
        menuReport.add(reportPanel, BorderLayout.CENTER);

        ButtonPanel buttonPanel = new ButtonPanel(reportPanel);
        menuReport.add(buttonPanel, BorderLayout.NORTH);

        menuReport.setLocationRelativeTo(null);
        menuReport.setVisible(true);
    }
}
