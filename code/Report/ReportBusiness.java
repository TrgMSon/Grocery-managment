package Report;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.math.BigDecimal;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
import Connection.DataConnection;

public class ReportBusiness {
    public static BigDecimal getIncome(String keyword) {
        BigDecimal result = new BigDecimal("0");

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM invoice WHERE date LIKE ?";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, "%" + keyword + "%");
            ResultSet rs = psm.executeQuery();

            while (rs.next()) {
                BigDecimal totalAmount = new BigDecimal(rs.getString("totalAmount"));
                result = result.add(totalAmount);
            }
        } catch (SQLException e) {
            Logger.getLogger(ReportBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return result;
    }

    public static String getBestSeller(String key) {
        String ans = "";
        ArrayList<String> bestSellers = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = """
                            SELECT p.name, SUM(it.quantity) AS sumQty FROM product AS p
                             JOIN invoicedetail AS it ON p.idProduct = it.idProduct
                             JOIN invoice AS i ON i.idInvoice = it.idInvoice 
                             WHERE i.date LIKE ?
                             GROUP BY p.idProduct
                             ORDER BY sumQty DESC, p.name ASC
                             LIMIT 3;
                    """;

            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, "%" + key + "%");

            ResultSet rs = psm.executeQuery();
            while (rs.next()) {
                String bestSeller = rs.getString("p.name") + ", số lượng: " + rs.getString("sumQty");
                bestSellers.add(bestSeller);
            }

            for (String i : bestSellers) {
                ans += i + "\n";
            }
        } catch (SQLException e) {
            Logger.getLogger(ReportBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return ans;
    }
}