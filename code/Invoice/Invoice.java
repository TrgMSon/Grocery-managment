package Invoice;

import java.math.BigDecimal;

public class Invoice {
    private String idInvoice, idCustomer, date;
    private BigDecimal totalAmount;
    
    public Invoice() {

    }

    public Invoice(String idInvoice, String idCustomer, String date, BigDecimal totalAmount) {
        this.idInvoice = idInvoice;
        this.idCustomer = idCustomer;
        this.date = date;
        this.totalAmount = totalAmount;
    }

    public String getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(String idInvoice) {
        this.idInvoice = idInvoice;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return totalAmount;
    }

    public void setTotal(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}