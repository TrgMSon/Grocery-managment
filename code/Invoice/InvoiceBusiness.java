package Invoice;

import Connection.DataConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;

public class InvoiceBusiness {
    public static boolean addInvoice(Invoice invoice) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();

            String sql = "insert into invoice(idInvoice, totalAmount, date, idCustomer) values(?, ?, ?, ?)";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, invoice.getIdInvoice());
            psm.setBigDecimal(2, invoice.getTotal());
            psm.setString(3, invoice.getDate());
            psm.setString(4, invoice.getIdCustomer());

            return psm.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(InvoiceBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static boolean updateInvoice(Invoice invoice) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();

            String sql = "update invoice set totalAmount = ?, date = ? where idInvoice = ?";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setBigDecimal(1, invoice.getTotal());
            psm.setString(2, invoice.getDate());
            psm.setString(3, invoice.getIdInvoice());

            return psm.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(InvoiceBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static boolean deleteInvoice(String idInvoice) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();

            String sql = "delete from invoice where idInvoice = '" + idInvoice + "'";
            PreparedStatement psm = conn.prepareStatement(sql);

            return psm.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(InvoiceBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static ArrayList<Invoice> showListInvoice(String keyword) {
        ArrayList<Invoice> list = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();

            String target;
            if (keyword.contains("C"))
                target = "idCustomer";
            else if (keyword.contains("I"))
                target = "idInvoice";
            else if (keyword.contains("-"))
                target = "date";
            else
                target = "totalAmount";

            String sql = "select * from invoice where " + target + " = '" + keyword + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                list.add(new Invoice(rs.getString("idInvoice"), rs.getString("idCustomer"), rs.getString("date"),
                        rs.getBigDecimal("totalAmount")));
            }
        } catch (SQLException e) {
            Logger.getLogger(InvoiceBusiness.class.getName()).log(Level.SEVERE, null, e);
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
}