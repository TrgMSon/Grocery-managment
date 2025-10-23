package Invoice;

import java.math.BigDecimal;

public class InvoiceDetail {
    private String nameProduct, idProduct, idInvoice;
    private int quantity;
    private BigDecimal costProduct;

    public InvoiceDetail() {

    }

    public InvoiceDetail(String nameProduct, String idProduct, String idInvoice, int quantity, BigDecimal costProduct) {
        this.nameProduct = nameProduct;
        this.idInvoice = idInvoice;
        this.idProduct = idProduct;
        this.costProduct = costProduct;
        this.quantity = quantity;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getIdInvoice() {
        return idInvoice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCostProduct() {
        return costProduct;
    }

    public void setCostProduct(BigDecimal cost) {
        costProduct = cost;
    }
}