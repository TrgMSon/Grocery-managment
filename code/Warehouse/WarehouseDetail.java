package Warehouse;

public class WarehouseDetail {
    private String idWarehouse, idProduct, nameProduct, lastReceiveDate;
    private int quantityInStock;

    public WarehouseDetail() {

    }

    public WarehouseDetail(String idWarehouse, String idProduct, String nameProduct, String lastReceiveDate, int quantityInStock) {
        this.idWarehouse = idWarehouse;
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.lastReceiveDate = lastReceiveDate;
        this.quantityInStock = quantityInStock;
    }

    public String getIdWarehouse() {
        return idWarehouse;
    }

    public void setIdWarehouse(String idWarehouse) {
        this.idWarehouse = idWarehouse;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getLastReceiveDate() {
        return lastReceiveDate;
    }

    public void setLastReceiveDate(String lastReceiveDate) {
        this.lastReceiveDate = lastReceiveDate;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}