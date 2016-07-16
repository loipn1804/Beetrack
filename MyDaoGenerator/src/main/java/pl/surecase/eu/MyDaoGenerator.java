package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static Schema schema;

    public static void main(String args[]) throws Exception {
        schema = new Schema(2, "greendao");

        Entity user = schema.addEntity("User");
        user.addLongProperty("account_id").primaryKey();
        user.addLongProperty("company_id");
        user.addLongProperty("department_id");
        user.addStringProperty("name");
        user.addStringProperty("email");
        user.addStringProperty("username");
        user.addStringProperty("phone");
        user.addStringProperty("created_at");
        user.addStringProperty("updated_at");

        Entity session = schema.addEntity("Session");
        session.addLongProperty("session_id").primaryKey();
        session.addLongProperty("company_id");
        session.addStringProperty("name");
        session.addStringProperty("description");
        session.addStringProperty("session_date");
        session.addStringProperty("created_at");
        session.addStringProperty("updated_at");
        session.addIntProperty("is_chosen");
        session.addIntProperty("f_completed");

        Entity asset = schema.addEntity("Asset");
        asset.addLongProperty("asset_id").primaryKey();
        asset.addStringProperty("asset_code");
        asset.addLongProperty("company_id");
        asset.addLongProperty("department_id");
        asset.addLongProperty("category_id");
        asset.addLongProperty("sub_category_id");
        asset.addLongProperty("warehouse_id");
        asset.addStringProperty("seri");
        asset.addStringProperty("warehouse_seri");
        asset.addStringProperty("user_using");
        asset.addStringProperty("name");
        asset.addIntProperty("f_active");
        asset.addStringProperty("created_at");
        asset.addStringProperty("updated_at");
        asset.addStringProperty("department_name");
        asset.addLongProperty("session_id");
        asset.addIntProperty("status");
        asset.addIntProperty("is_scan");
        asset.addStringProperty("category_name");
        asset.addStringProperty("sub_category_name");
        asset.addStringProperty("warehouse_name");

        Entity category = schema.addEntity("Category");
        category.addLongProperty("category_id").primaryKey();
        category.addStringProperty("category_name");

        Entity sub_category = schema.addEntity("SubCategory");
        sub_category.addLongProperty("sub_category_id").primaryKey();
        sub_category.addLongProperty("category_id");
        sub_category.addStringProperty("sub_category_name");

        Entity department = schema.addEntity("Department");
        department.addLongProperty("department_id").primaryKey();
        department.addStringProperty("department_code");
        department.addStringProperty("department_name");
        department.addStringProperty("description");

        Entity warehouse = schema.addEntity("Warehouse");
        warehouse.addLongProperty("warehouse_id").primaryKey();
        warehouse.addStringProperty("warehouse_code");
        warehouse.addStringProperty("warehouse_name");

        new DaoGenerator().generateAll(schema, args[0]);
    }
}