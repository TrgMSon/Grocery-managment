package Warehouse;

public class Warehouse {
    private String idWarehouse, address;
    private int maxCapacity;

    public Warehouse() {

    }

    public Warehouse(String idWarehouse, String address, int maxCapacity) {
        this.idWarehouse = idWarehouse;
        this.address = address;
        this.maxCapacity = maxCapacity;
    }

    public String getIdWarehouse() {
        return idWarehouse;
    }

    public void setIdWarehouse(String idWarehouse) {
        this.idWarehouse = idWarehouse;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
