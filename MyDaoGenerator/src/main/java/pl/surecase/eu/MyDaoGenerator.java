package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static Schema schema;

    public static void main(String args[]) throws Exception {
        schema = new Schema(1, "greendao");

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
        session.addLongProperty("account_id");
        session.addLongProperty("company_id");
        session.addStringProperty("name");
        session.addStringProperty("description");
        session.addStringProperty("session_date");
        session.addStringProperty("created_at");
        session.addStringProperty("updated_at");
        session.addIntProperty("is_chosen");

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

        new DaoGenerator().generateAll(schema, args[0]);
    }
}