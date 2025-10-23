package Customer;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import Connection.DataConnection;
import Invoice.InvoiceDetailBusiness;

public class CustomerBusiness {
    public static ArrayList<Customer> showListCustomer(String keyword) {
        ArrayList<Customer> list = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DataConnection.setConnect(); 
        
            String target;
            if (keyword.contains("C")) target = "idCustomer";
            else if (keyword.charAt(0) >= '0' && keyword.charAt(0) <= '9') target = "phone";
            else target = "name";

            String sql = "Select * from customer where " + target + " = '" + keyword + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                list.add(new Customer(rs.getString("idCustomer"), rs.getString("name"), rs.getString("phone")));
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomerBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static boolean addCustomer(Customer obj) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            
            String sql = "insert into customer(idCustomer, name, phone) values(?, ?, ?)";
            PreparedStatement psm = conn.prepareStatement(sql);
            
            psm.setString(1, obj.getId());
            psm.setString(2, obj.getName());
            psm.setString(3, obj.getPhone());

            return psm.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(CustomerBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static boolean deleteCustomer(String idCustomer) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();

            String sql = "delete from customer where idCustomer = '" + idCustomer + "'";
            Statement stm = conn.createStatement();

            return stm.executeUpdate(sql) > 0;
        } catch(SQLException e) {
            Logger.getLogger(CustomerBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }
        
        return false;
    }

    public static boolean updateCustomer(Customer obj) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();

            String sql = "update customer set name = ?, phone = ? where idCustomer = ?";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(3, obj.getId());
            psm.setString(1, obj.getName());
            psm.setString(2, obj.getPhone());
            
            return psm.executeUpdate() > 0;

        } catch(SQLException e) {
            Logger.getLogger(CustomerBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return false;
    }

    public static Customer showCustomerDetail(String idCustomer) {
        Connection conn = null;
        
        Customer obj = new Customer();
        try {
            conn = DataConnection.setConnect();

            String sql = "select * from customer where idCustomer = '" + idCustomer + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            
            while (rs.next()) {
                obj.setId(rs.getString("idCustomer"));
                obj.setName(rs.getString("name"));
                obj.setPhone(rs.getString("phone"));
            }
        } catch(SQLException e) {
            Logger.getLogger(CustomerBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return obj;
    }

    public static boolean validInforCustomer(String idCustomer, String name, String phone) {
        if (idCustomer.equals("") || name.equals("") || phone.equals("")) return false;
        return true;
    }

    public static String findIdCustomer(String phone) {
        String id = "";

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "select idCustomer from customer where phone = '" + phone + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) id = rs.getString("idCustomer");
        } catch(SQLException e) {
            Logger.getLogger(InvoiceDetailBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return id;
    }

    public static boolean isExist(String idCustomer) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM customer WHERE idCustomer = '" + idCustomer + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) return true;
        } catch(SQLException e) {
            Logger.getLogger(InvoiceDetailBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return false;
    }

    public static boolean isExist(Customer obj) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM customer WHERE name = ? and phone = ?";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, obj.getName());
            psm.setString(2, obj.getPhone());
            ResultSet rs = psm.executeQuery();

            if (rs.next()) return true;
        } catch(SQLException e) {
            Logger.getLogger(InvoiceDetailBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return false;
    }

    public static boolean isPhoneExist(String phone) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM customer WHERE phone = ?";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, phone);
            ResultSet rs = psm.executeQuery();

            if (rs.next()) return true;
        } catch(SQLException e) {
            Logger.getLogger(InvoiceDetailBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return false;
    }

    public static boolean isValidPhone(String phone) {
        if (phone.matches("\\d{10}")) return true;
        return false;
    }
}