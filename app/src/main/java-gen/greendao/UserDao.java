package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table USER.
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Account_id = new Property(0, Long.class, "account_id", true, "ACCOUNT_ID");
        public final static Property Company_id = new Property(1, Long.class, "company_id", false, "COMPANY_ID");
        public final static Property Department_id = new Property(2, Long.class, "department_id", false, "DEPARTMENT_ID");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Email = new Property(4, String.class, "email", false, "EMAIL");
        public final static Property Username = new Property(5, String.class, "username", false, "USERNAME");
        public final static Property Phone = new Property(6, String.class, "phone", false, "PHONE");
        public final static Property Created_at = new Property(7, String.class, "created_at", false, "CREATED_AT");
        public final static Property Updated_at = new Property(8, String.class, "updated_at", false, "UPDATED_AT");
    };


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'USER' (" + //
                "'ACCOUNT_ID' INTEGER PRIMARY KEY ," + // 0: account_id
                "'COMPANY_ID' INTEGER," + // 1: company_id
                "'DEPARTMENT_ID' INTEGER," + // 2: department_id
                "'NAME' TEXT," + // 3: name
                "'EMAIL' TEXT," + // 4: email
                "'USERNAME' TEXT," + // 5: username
                "'PHONE' TEXT," + // 6: phone
                "'CREATED_AT' TEXT," + // 7: created_at
                "'UPDATED_AT' TEXT);"); // 8: updated_at
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'USER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long account_id = entity.getAccount_id();
        if (account_id != null) {
            stmt.bindLong(1, account_id);
        }
 
        Long company_id = entity.getCompany_id();
        if (company_id != null) {
            stmt.bindLong(2, company_id);
        }
 
        Long department_id = entity.getDepartment_id();
        if (department_id != null) {
            stmt.bindLong(3, department_id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(5, email);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(6, username);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(7, phone);
        }
 
        String created_at = entity.getCreated_at();
        if (created_at != null) {
            stmt.bindString(8, created_at);
        }
 
        String updated_at = entity.getUpdated_at();
        if (updated_at != null) {
            stmt.bindString(9, updated_at);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // account_id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // company_id
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // department_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // email
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // username
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // phone
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // created_at
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // updated_at
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setAccount_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCompany_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setDepartment_id(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEmail(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUsername(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPhone(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCreated_at(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUpdated_at(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setAccount_id(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getAccount_id();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
