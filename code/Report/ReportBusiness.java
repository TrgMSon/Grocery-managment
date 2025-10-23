package Report;

import java.sql.Connection;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import Mode.Mode;
import Connection.DataConnection;

public class ReportBusiness {
    public static BigDecimal getIncome(String keyword) {
        BigDecimal result = new BigDecimal("0");

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT totalAmount FROM invoice WHERE date LIKE '%?%' ";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, )
        }
    }
}