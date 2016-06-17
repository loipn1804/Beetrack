package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static Schema schema;

    public static void main(String args[]) throws Exception {
        schema = new Schema(3, "greendao");

        Entity user = schema.addEntity("User");
        user.addLongProperty("user_id").primaryKey();
        user.addStringProperty("email");

        new DaoGenerator().generateAll(schema, args[0]);
    }
}