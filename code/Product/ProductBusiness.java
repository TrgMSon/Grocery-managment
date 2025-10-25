package Product;

import Connection.DataConnection;
import Product.ProductBusiness;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Arrays;

public class ProductBusiness {
    public static void addProduct(Product product) {
        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "insert into product(idProduct, name, cost, origin, type) values(?, ?, ?, ?, ?)";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, product.getIdProduct());
            psm.setString(2, product.getName());
            psm.setBigDecimal(3, product.getCost());
            psm.setString(4, product.getOrigin());
            psm.setString(5, product.getType());
            psm.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(ProductBusiness.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static boolean deleteProduct(String idProduct) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();

            String sql = "delete from Product where idProduct = '" + idProduct + "'";
            Statement stm = conn.createStatement();

            return stm.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            Logger.getLogger(ProductBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return false;
    }

    public static boolean updateProduct(Product obj) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();

            String sql = "update product set name = ?, cost = ?, origin = ?, type = ? where idProduct = ?";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, obj.getName());
            psm.setBigDecimal(2, obj.getCost());
            psm.setString(3, obj.getOrigin());
            psm.setString(4, obj.getType());
            psm.setString(5, obj.getIdProduct());

            return psm.executeUpdate() > 0;

        } catch (SQLException e) {
            Logger.getLogger(ProductBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return false;
    }

    public static Product showProductDetail(String idProduct) {
        Connection conn = null;

        Product obj = new Product();
        try {
            conn = DataConnection.setConnect();

            String sql = "select * from product where idProduct = '" + idProduct + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                obj.setIdProduct(rs.getString("idProduct"));
                obj.setName(rs.getString("name"));
                obj.setOrigin(rs.getString("origin"));
                obj.setType(rs.getString("type"));
                obj.setCost(rs.getBigDecimal("cost"));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return obj;
    }

    public static ArrayList<Product> showListProduct(String keyword) {
        ArrayList<Product> list = new ArrayList<>();
        ArrayList<String> nation = new ArrayList<>(Arrays.asList(
                "Việt Nam", "Hàn Quốc", "Mỹ", "Iran", "Đức", "Nhật", "Nga", "Pháp", "Thái Lan",
                "Hà Lan", "Úc", "Anh", "Châu Phi", "Trung Quốc"));

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();

            String target;
            if (keyword.contains("DD") || keyword.contains("TC"))
                target = "idProduct";
            else if (keyword.compareTo("Đồ dùng cho thú cưng") == 0) {
                target = "type";
            } else if (keyword.compareTo("Thú cưng") == 0) {
                target = "type";
            } else if (keyword.charAt(0) >= '0' && keyword.charAt(0) <= '9')
                target = "cost";
            else if (nation.contains(keyword))
                target = "origin";
            else
                target = "name";

            String sql = "select * from product where " + target + " = '" + keyword + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                list.add(new Product(rs.getString("idProduct"), rs.getString("name"), rs.getString("origin"),
                        rs.getString("type"), rs.getBigDecimal("cost")));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return list;
    }

    public static boolean isExist(String idProduct) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM product WHERE idProduct = '" + idProduct + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next())
                return true;
        } catch (SQLException e) {
            Logger.getLogger(ProductBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return false;
    }

    public static boolean isExist(Product obj) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM product WHERE name = ? and cost = ? and origin = ? and type = ?";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, obj.getName());
            psm.setBigDecimal(2, obj.getCost());
            psm.setString(3, obj.getOrigin());
            psm.setString(4, obj.getType());
            ResultSet rs = psm.executeQuery();

            if (rs.next())
                return true;
        } catch (SQLException e) {
            Logger.getLogger(ProductBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return false;
    }

    public static boolean isValidProduct(Product obj) {
        if (obj.getName().equals("") || obj.getOrigin().equals("") || obj.getType().equals("") ||
                String.valueOf(obj.getCost()).equals("0"))
            return false;
        return true;
    }
}