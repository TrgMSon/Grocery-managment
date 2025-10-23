package Product;

import java.math.BigDecimal;

public class Product {
    private String idProduct, name, origin, type;
    private BigDecimal cost;
    
    public Product() {
        cost = new BigDecimal("0");
    }

    public Product(String idProduct, String name, String origin, String type, BigDecimal cost) {
        this.idProduct = idProduct;
        this.name = name;
        this.origin = origin;
        this.type = type;
        this.cost = cost;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String id) {
        idProduct = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}