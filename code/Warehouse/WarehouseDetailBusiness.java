package Warehouse;

import Connection.DataConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.swing.JTable;

import java.util.ArrayList;
import java.util.logging.Level;

class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }
}

public class WarehouseDetailBusiness {
    public static ArrayList<WarehouseDetail> showWarehouseDetail(String idWarehouse) {
        ArrayList<WarehouseDetail> list = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "select * from warehousedetail where idWarehouse = '" + idWarehouse + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                String idW = rs.getString("idWarehouse");
                String idP = rs.getString("idProductW");
                String name = rs.getString("nameProduct");
                String lrd = rs.getString("lastReceiveDate");
                int qty = rs.getInt("quantityInStock");
                list.add(new WarehouseDetail(idW, idP, name, lrd, qty));
            }
        } catch (SQLException e) {
            Logger.getLogger(WarehouseDetailBusiness.class.getName()).log(Level.SEVERE, null, e);
        }

        return list;
    }

    public static String getOrigin(String idProduct) {
        String origin = "";

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "select origin from product where idProduct = '" + idProduct + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                origin = rs.getString("origin");
            }
        } catch (SQLException e) {
            Logger.getLogger(WarehouseDetailBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return origin;
    }

    public static boolean addWarehouseDetail(WarehouseDetail warehouseDetail) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "insert into warehousedetail(idWarehouse, idProductW, nameProduct, lastReceiveDate, quantityInStock) values(?, ?, ?, ?, ?)";
            PreparedStatement psm = conn.prepareStatement(sql);

            psm.setString(1, warehouseDetail.getIdWarehouse());
            psm.setString(2, warehouseDetail.getIdProduct());
            psm.setString(3, warehouseDetail.getNameProduct());
            psm.setString(4, warehouseDetail.getLastReceiveDate());
            psm.setInt(5, warehouseDetail.getQuantityInStock());

            return psm.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(WarehouseDetailBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static boolean decreaseQuantityInStock(String idProduct, int quantity, boolean ableToUpdate) {
        Connection conn = null;

        try {
            ArrayList<Pair<String, Integer>> list = new ArrayList<>();

            conn = DataConnection.setConnect();
            String sql = "SELECT quantityInStock, idWarehouse FROM warehousedetail where idProductW = ? " +
                    "ORDER BY quantityInStock ASC";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, idProduct);
            ResultSet rs = psm.executeQuery();
            while (rs.next()) {
                Pair<String, Integer> item = new Pair<>(rs.getString("idWarehouse"), rs.getInt("quantityInStock"));
                list.add(item);
            }

            for (Pair<String, Integer> i : list) {
                if (quantity >= i.getValue()) {
                    if (ableToUpdate) {
                        sql = "UPDATE warehousedetail SET quantityInStock = 0 WHERE idWarehouse = ? and idProductW = ?";
                        PreparedStatement psm2 = conn.prepareStatement(sql);
                        psm2.setString(1, i.getKey());
                        psm2.setString(2, idProduct);
                        psm2.executeUpdate();
                    }
                    quantity -= i.getValue();
                } else {
                    if (ableToUpdate) {
                        sql = "UPDATE warehousedetail SET quantityInStock = quantityInStock - ? WHERE idWarehouse = ? and idProductW = ?";
                        PreparedStatement psm2 = conn.prepareStatement(sql);
                        psm2.setInt(1, quantity);
                        psm2.setString(2, i.getKey());
                        psm2.setString(3, idProduct);
                        psm2.executeUpdate();
                    }
                    quantity = 0;
                }
                if (quantity == 0) break;
            }
            return quantity == 0;

        } catch (SQLException e) {
            Logger.getLogger(WarehouseDetailBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static boolean updateWarehouseDetail(WarehouseDetail warehouseDetail, String lrd) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "update warehousedetail set quantityInStock = quantityInStock + ?, lastReceiveDate = ? where idWarehouse = ? and idProductW = ?";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setInt(1, warehouseDetail.getQuantityInStock());
            psm.setString(2, lrd);
            psm.setString(3, warehouseDetail.getIdWarehouse());
            psm.setString(4, warehouseDetail.getIdProduct());

            return psm.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(WarehouseDetailBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static boolean deleteWarehouseDetail(String idWarehouse, String idProduct) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "delete from warehousedetail where idWarehouse = '" + idWarehouse + "' and idProductW = '"
                    + idProduct + "'";
            Statement stm = conn.createStatement();

            return stm.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            Logger.getLogger(WarehouseDetailBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static boolean isFullCapacity(String idWarehouse, int maxCapacity, ProductPanel productPanel) {
        int sum = 0;

        JTable table = productPanel.getTableProduct();
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i, 5) != null) {
                int quantityInStock = Integer.parseInt((String) table.getValueAt(i, 5));
                sum += quantityInStock;
                if (sum > maxCapacity) return true;
            }
        }

        return false;
    }

    public static boolean isFullCapacity(String idWarehouse, int maxCapacity) {
        Connection conn = null;

        try {
            int sum = 0;

            conn = DataConnection.setConnect();
            String sql = "SELECT quantityInStock FROM warehousedetail WHERE idWarehouse = ?";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, idWarehouse);
            ResultSet rs = psm.executeQuery();

            while (rs.next()) {
                sum += rs.getInt("quantityInStock");
                if (sum > maxCapacity) return true;
            }
        } catch (SQLException e) {
            Logger.getLogger(WarehouseDetailBusiness.class.getName()).log(Level.SEVERE, null, e);
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
}