package Warehouse;

import Connection.DataConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WarehouseBusiness {
    public static boolean addWarehouse(Warehouse warehouse) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();

            String sql = "insert into warehouse(idWarehouse, address, maxCapacity) values(?, ?, ?)";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, warehouse.getIdWarehouse());
            psm.setString(2, warehouse.getAddress());
            psm.setInt(3, warehouse.getMaxCapacity());

            return psm.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(WarehouseBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static boolean deleteWarehouse(String idWarehouse) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "delete from warehouse where idWarehouse = '" + idWarehouse + "'";
            Statement stm = conn.createStatement();

            return stm.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            Logger.getLogger(WarehouseBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static Warehouse showWarehouse(String idWarehouse) {
        Warehouse warehouse = new Warehouse();

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "select * from warehouse where idWarehouse = '" + idWarehouse + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                warehouse.setIdWarehouse(idWarehouse);
                warehouse.setAddress(rs.getString("address"));
                warehouse.setMaxCapacity(rs.getInt("maxCapacity"));
            }
        } catch (SQLException e) {
            Logger.getLogger(WarehouseBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return warehouse;
    }

    public static ArrayList<Warehouse> showListWarehouse(String keyword) {
        ArrayList<Warehouse> list = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();

            String target;
            if (keyword.contains("W"))
                target = "idWarehouse";
            else if (keyword.charAt(0) >= '0' && keyword.charAt(0) <= '9')
                target = "maxCapacity";
            else
                target = "address";

            String sql = "SELECT * FROM warehouse WHERE " + target + " = '" + keyword + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                list.add(new Warehouse(rs.getString("idWarehouse"), rs.getString("address"), rs.getInt("maxCapacity")));
            }
        } catch (SQLException e) {
            Logger.getLogger(WarehouseBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static int getCurrentCapacity(String idWarehouse) {
        int sum = 0;

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT quantityInStock FROM warehousedetail WHERE idWarehouse = '" + idWarehouse + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                sum += rs.getInt("quantityInStock");
            }
        } catch (SQLException e) {
            Logger.getLogger(WarehouseBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return sum;
    }

    public static boolean updateWarehouse(Warehouse warehouse) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "UPDATE warehouse SET address = ?, maxCapacity = ? WHERE idWarehouse = ?";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, warehouse.getAddress());
            psm.setInt(2, warehouse.getMaxCapacity());
            psm.setString(3, warehouse.getIdWarehouse());

            return psm.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(WarehouseBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static boolean isValidAddress(String address) {
        List<String> validCities = Arrays.asList(
                "Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Hải Phòng", "Cần Thơ",
                "Huế", "Nha Trang", "Đà Lạt", "Vũng Tàu", "Buôn Ma Thuột",
                "Quy Nhơn", "Thanh Hóa", "Nam Định", "Thái Nguyên", "Hạ Long",
                "Pleiku", "Phan Thiết", "Rạch Giá", "Long Xuyên", "Bạc Liêu",
                "Sóc Trăng", "Tuy Hòa", "Vinh", "Tam Kỳ", "Bắc Ninh");

        return validCities.contains(address);
    }

    public static boolean isExist(String idWarehouse) {
        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM warehouse WHERE idWarehouse = '" + idWarehouse + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) return true;
        } catch (SQLException e) {
            Logger.getLogger(WarehouseBusiness.class.getName()).log(Level.SEVERE, null, e);
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
