package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table WAREHOUSE.
 */
public class Warehouse {

    private Long warehouse_id;
    private String warehouse_code;
    private String warehouse_name;

    public Warehouse() {
    }

    public Warehouse(Long warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public Warehouse(Long warehouse_id, String warehouse_code, String warehouse_name) {
        this.warehouse_id = warehouse_id;
        this.warehouse_code = warehouse_code;
        this.warehouse_name = warehouse_name;
    }

    public Long getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(Long warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getWarehouse_code() {
        return warehouse_code;
    }

    public void setWarehouse_code(String warehouse_code) {
        this.warehouse_code = warehouse_code;
    }

    public String getWarehouse_name() {
        return warehouse_name;
    }

    public void setWarehouse_name(String warehouse_name) {
        this.warehouse_name = warehouse_name;
    }

}
