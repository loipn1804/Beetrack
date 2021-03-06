package dne.beetrack.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import greendao.DaoMaster;
import greendao.DaoSession;
import io.fabric.sdk.android.Fabric;

/**
 * Created by USER on 3/3/2016.
 */
public class MyApplication extends MultiDexApplication {

    private String db_name = "qltsdb";
    public DaoSession daoSession;

    @Override
    public void onCreate() {
        MultiDex.install(getApplicationContext());
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        int current_version = getAppVersionCode();

        SharedPreferences preferences = getSharedPreferences("versioncode", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int previous_version = preferences.getInt("version", current_version);
        if (previous_version == current_version) {
            editor.putInt("version", current_version);

            String current_ver_name = getAppVersionName();

            String previous_ver_name = preferences.getString("ver_name", current_ver_name);

            if (previous_ver_name.equals(current_ver_name)) {
                editor.putString("ver_name", current_ver_name);
            } else {
                deleteDatabase();
                editor.putString("ver_name", current_ver_name);
            }
        } else {
            deleteDatabase();
            editor.putInt("version", current_version);
        }
        editor.commit();

        setupDatabase();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .memoryCacheSizePercentage(50)
                .threadPoolSize(3)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new WeakMemoryCache())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        //.imageDecoder(new NutraBaseImageDecoder(true))
                        // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, db_name, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        if (daoSession != null) {
            return daoSession;
        } else {
            setupDatabase();
            return daoSession;
        }
    }

    private void deleteDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, db_name, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoMaster.dropAllTables(db, true);
        daoMaster.createAllTables(db, true);
    }

    public int getAppVersionCode() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public String getAppVersionName() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
