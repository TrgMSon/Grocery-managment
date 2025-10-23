package Product;

import Connection.DataConnection;
import Format.Format;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.math.BigDecimal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import java.util.ArrayList;

class ButtonDetailProduct extends JPanel {
    private JPanel pButton, pInform;
    private JButton acptBt, closeBt, okBt;
    private JDialog inform;
    private JLabel lbInform;

    public void initInform(JFrame menuProduct, String message) {
        inform = new JDialog(menuProduct, "Thông báo", true);
        inform.setLayout(new GridLayout(2, 1));
        inform.setSize(300, 200);
        inform.setLocationRelativeTo(menuProduct);

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

    public void initActionClose() {
        closeBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
            }
        });
    }

    public void initActionAccept(ProductPanel productPanel, DetailProduct formEdit) {
        acptBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Product obj = new Product();
                obj.setName(formEdit.getTxtName().getText());
                obj.setOrigin(formEdit.getTxtOrigin().getText());
                if (formEdit.getTxtCost().getText().equals("") == false) {
                    obj.setCost(new BigDecimal(formEdit.getTxtCost().getText()));
                }
                obj.setType(String.valueOf(formEdit.getTxtType().getSelectedItem()));

                if (ProductBusiness.isValidProduct(obj) == false) {
                    initInform(formEdit, "Vui lòng nhập đủ thông tin sản phẩm");
                    return;
                }

                if (formEdit.getTxtId().getText().compareTo("") == 0) {
                    Random rand = new Random();
                    String tmp;
                    if (String.valueOf(formEdit.getTxtType().getSelectedItem())
                            .compareTo("Đồ dùng cho thú cưng") == 0) {
                        tmp = "DD";
                    } else {
                        tmp = "TC";
                    }

                    String idProduct = tmp + String.format("%04d", rand.nextInt(10000));
                    while (ProductBusiness.isExist(tmp)) {
                        idProduct = tmp + String.format("%04d", rand.nextInt(10000));
                    }
                    obj.setIdProduct(idProduct);

                    if (ProductBusiness.isExist(obj)) {
                        initInform(formEdit, "Thông tin đẫ tồn tại, vui lòng thử lại");
                        return;
                    }

                    ProductBusiness.addProduct(obj);
                    productPanel.loadData();
                }

                else {
                    obj.setIdProduct(formEdit.getTxtId().getText());
                    ProductBusiness.updateProduct(obj);
                    productPanel.loadData();
                }

                formEdit.setVisible(false);
            }
        });
    }

    public ButtonDetailProduct(ProductPanel productPanel, DetailProduct formEdit) {
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        acptBt = new JButton("Xác nhận");
        initActionAccept(productPanel, formEdit);
        add(acptBt);

        closeBt = new JButton("Đóng");
        closeBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                formEdit.setVisible(false);
            }
        });
        add(closeBt);

        setVisible(true);
    }
}

class DetailProduct extends JFrame {
    private JPanel panelId, panelName, panelCost, panelOrigin, panelType;
    private JTextArea id, name, cost, origin;
    private JComboBox<String> type;
    private JLabel lbId, lbName, lbCost, lbOrigin, lbType;

    public DetailProduct(ProductPanel productPanel) {
        setTitle("Thông tin sản phẩm");
        setSize(600, 300);
        setLayout(new GridLayout(6, 1));

        panelId = new JPanel();
        panelId.setLayout(new FlowLayout(FlowLayout.LEFT));
        lbId = new JLabel("Mã sản phẩm");
        id = new JTextArea(2, 35);
        panelId.add(lbId);
        panelId.add(id);

        panelName = new JPanel();
        panelName.setLayout(new FlowLayout(FlowLayout.LEFT));
        lbName = new JLabel("Tên");
        name = new JTextArea(2, 35);
        panelName.add(lbName);
        panelName.add(name);

        panelCost = new JPanel();
        panelCost.setLayout(new FlowLayout(FlowLayout.LEFT));
        lbCost = new JLabel("Giá (VND)");
        cost = new JTextArea(2, 35);
        panelCost.add(lbCost);
        panelCost.add(cost);

        panelOrigin = new JPanel();
        panelOrigin.setLayout(new FlowLayout(FlowLayout.LEFT));
        lbOrigin = new JLabel("Xuất xứ");
        origin = new JTextArea(2, 35);
        panelOrigin.add(lbOrigin);
        panelOrigin.add(origin);

        panelType = new JPanel();
        panelType.setLayout(new FlowLayout(FlowLayout.LEFT));
        lbType = new JLabel("Loại");
        String[] options = { "Đồ dùng cho thú cưng", "Thú cưng" };
        type = new JComboBox<String>(options);
        panelType.add(lbType);
        panelType.add(type);

        add(panelId);
        add(panelName);
        add(panelCost);
        add(panelOrigin);
        add(panelType);

        setLocationRelativeTo(null);
    }

    public JTextArea getTxtId() {
        return id;
    }

    public JTextArea getTxtName() {
        return name;
    }

    public JTextArea getTxtCost() {
        return cost;
    }

    public JTextArea getTxtOrigin() {
        return origin;
    }

    public JComboBox<String> getTxtType() {
        return type;
    }
}

class ProductPanel extends JPanel {
    private JTable tableProduct;
    private DefaultTableModel dtm;

    public void loadData() {
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
            Logger.getLogger(ProductUI.class.getName()).log(Level.SEVERE, null, e);
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
        String[] nameColumns = { "Mã sản phẩm", "Tên", "Giá (VND)", "Xuất xứ", "Loại" };
        dtm = new DefaultTableModel(nameColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadData();

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

class ButtonPanel extends JPanel {
    private JButton addBt, delBt, editBt, reloadBt;

    public ButtonPanel(ProductPanel productPanel) {
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        reloadBt = new JButton("Làm mới");
        addBt = new JButton("Thêm");
        delBt = new JButton("Xóa");
        editBt = new JButton("Sửa");

        add(reloadBt);
        add(addBt);
        add(delBt);
        add(editBt);

        reloadBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                productPanel.loadData();
            }
        });

        addBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                DetailProduct formEdit = new DetailProduct(productPanel);
                ButtonDetailProduct buttonDetailProduct = new ButtonDetailProduct(productPanel, formEdit);
                formEdit.add(buttonDetailProduct);
                formEdit.getTxtId().setEditable(false);
                formEdit.setVisible(true);
            }
        });

        delBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int row = productPanel.getTableProduct().getSelectedRow();

                if (row != -1) {
                    String id = String.valueOf(productPanel.getTableProduct().getValueAt(row, 0));
                    ProductBusiness.deleteProduct(id);
                    productPanel.loadData();
                }
            }
        });

        editBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int row = productPanel.getTableProduct().getSelectedRow();
                if (row != -1) {
                    DetailProduct formEdit = new DetailProduct(productPanel);
                    ButtonDetailProduct buttonDetailProduct = new ButtonDetailProduct(productPanel, formEdit);
                    formEdit.add(buttonDetailProduct);
                    String id = String.valueOf(productPanel.getTableProduct().getValueAt(row, 0));
                    Product obj = ProductBusiness.showProductDetail(id);

                    formEdit.getTxtId().setEditable(false);
                    formEdit.getTxtId().setText(obj.getIdProduct());
                    formEdit.getTxtName().setText(obj.getName());
                    formEdit.getTxtCost().setText(String.valueOf(obj.getCost()));
                    formEdit.getTxtOrigin().setText(obj.getOrigin());
                    formEdit.getTxtType().setSelectedItem(obj.getType());

                    formEdit.setVisible(true);
                }
            }
        });

        setVisible(true);
    }

    public JButton getAddBt() {
        return addBt;
    }

    public JButton getDelBt() {
        return delBt;
    }

    public JButton getEditBt() {
        return editBt;
    }
}

class SearchPanel extends JPanel {
    private JTextArea text;
    private JLabel label;
    private JButton searchBt;

    public void initActionSearch(ProductPanel productPanel) {
        searchBt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String keyword = text.getText();
                ArrayList<Product> list = ProductBusiness.showListProduct(keyword);

                DefaultTableModel dtm = productPanel.getDtm();
                dtm.setRowCount(0);
                for (Product i : list) {
                    Object[] row = { i.getIdProduct(), i.getName(), Format.normalizeNumber(String.valueOf(i.getCost())),
                            i.getOrigin(), i.getType() };
                    dtm.addRow(row);
                }
                productPanel.getTableProduct().setModel(dtm);
            }
        });
    }

    public SearchPanel(ProductPanel productPanel) {
        setBorder(new TitledBorder("Nhập thông tin tìm kiếm"));
        setLayout(new FlowLayout(FlowLayout.LEFT));

        label = new JLabel("Từ khóa");
        text = new JTextArea(1, 25);
        searchBt = new JButton("Tìm kiếm");

        add(label);
        add(text);
        add(searchBt);

        initActionSearch(productPanel);

        setVisible(true);
    }

    public JTextArea getText() {
        return text;
    }

    public JButton getSearchBt() {
        return searchBt;
    }
}

public class ProductUI {
    public static void showMenu() {
        JFrame menuProduct = new JFrame("Sản phẩm");
        menuProduct.setSize(800, 600);
        menuProduct.setLayout(new BorderLayout());

        ProductPanel productPanel = new ProductPanel();
        SearchPanel searchPanel = new SearchPanel(productPanel);
        ButtonPanel buttonPanel = new ButtonPanel(productPanel);

        menuProduct.add(productPanel, BorderLayout.CENTER);
        menuProduct.add(searchPanel, BorderLayout.NORTH);
        menuProduct.add(buttonPanel, BorderLayout.SOUTH);

        menuProduct.setLocationRelativeTo(null);
        menuProduct.setVisible(true);
    }
}