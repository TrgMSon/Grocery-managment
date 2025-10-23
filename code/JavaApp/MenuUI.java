package JavaApp;

import Invoice.InvoiceUI;
import Product.ProductUI;
import Warehouse.WarehouseUI;
import Customer.CustomerUI;
import Report.ReportUI;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class MenuUI {
    public static void main(String args[]) throws ParseException {
        JFrame menu = new JFrame("Hệ thống quản lý bán hàng cho cửa hàng thú cưng");
        menu.setSize(600, 600);
        menu.setLayout(new GridLayout(5, 1));

        JButton customerBt = new JButton("Khách hàng");
        menu.add(customerBt);
        customerBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                CustomerUI.showMenu();
            }
        });

        JButton productBt = new JButton("Sản phẩm");
        menu.add(productBt);
        productBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ProductUI.showMenu();
            }
        });

        JButton invoiceBt = new JButton("Hóa đơn");
        menu.add(invoiceBt);
        invoiceBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                InvoiceUI.showMenu();
            }
        });

        JButton warehouseBt = new JButton("Kho");
        menu.add(warehouseBt);
        warehouseBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                WarehouseUI.showMenu();
            }
        });

        JButton reportBt = new JButton("Xem báo cáo");
        menu.add(reportBt);
        reportBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ReportUI.showMenu();
            }
        });

        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setResizable(true);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
    }
}