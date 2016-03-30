package com.thunisoft.sswy.mobile.datasource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;

import com.thunisoft.sswy.mobile.pojo.TSd;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    /** 存储所有创建表语句的文件目录 */
    private static final String CREATE_SQL_PATH = "sql" + File.separatorChar + "create";
    /** 存储所有初始化表语句的文件目录 */
    public static final String INIT_SQL_PATH = "sql" + File.separatorChar + "init";
    /** 存储所有更新表语句的文件目录 */
    private static final String UPDATE_SQL_PATH = "sql" + File.separatorChar + "update";

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "SSWY_DB.db";

    private Context context = null;

    private static DBHelper dbHelper = null;
    
    

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static void init(Context context) {
        Log.d(TAG, "初始化！");
        if (dbHelper == null) {
            synchronized (DBHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new DBHelper(context);
                    dbHelper.getWritableDatabase();
                }
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "开始创建数据库！");

        AssetManager assetsMamager = context.getAssets();
        try {
            // 创建表
            String[] createFiles = assetsMamager.list(CREATE_SQL_PATH);
            for (String createFile : createFiles) {
                String filePath = CREATE_SQL_PATH + File.separatorChar + createFile;
                Log.d(TAG, "正在创建表：" + filePath);
                String sql = getSQL(filePath);
                db.execSQL(sql);
            }
            // 初始化表
            String[] initFiles = assetsMamager.list(INIT_SQL_PATH);
            for (String initFile : initFiles) {
                String filePath = INIT_SQL_PATH + File.separatorChar + initFile;
                Log.d(TAG, "正在初始化表：" + filePath);
                List<String> sqlList = getSqlList(getSQL(filePath));
                db.beginTransaction();
                try {
                    for (String sql : sqlList) {
                        db.execSQL(sql);
                    }
                    db.setTransactionSuccessful(); // 设置事务标志为成功
                } catch (Exception e) {
                    Log.e(TAG, "初始化表失败：", e);
                    throw new RuntimeException(e);
                } finally {
                    db.endTransaction(); // 结束事务
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "初始化数据库失败！", e);
            throw new RuntimeException(e);
        }

        Log.d(TAG, "结束创建数据库！");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.d(TAG, "开始更新数据库结构！(" + oldVersion + "->" + newVersion + ")");
            AssetManager assetsMamager = context.getAssets();
            try {
            	db.beginTransaction();
                for (int i = oldVersion + 1; i <= newVersion; i++) {
                    String sqlFolder = UPDATE_SQL_PATH + File.separatorChar + i;
                    String[] sqlFiles = assetsMamager.list(sqlFolder);
                    for (String sqlFile : sqlFiles) {
                        String sqlPath = sqlFolder + File.separatorChar + sqlFile;
                        Log.d(TAG, "正在执行更新SQL：" + sqlPath);
                        List<String> sqlList = getSqlList(getSQL(sqlPath));
                        for (String sql : sqlList) {
                            db.execSQL(sql);
                        }
                    }
                }
                //删除添加的文书，每次升级执行此操作
                String sqlSD = " delete from  t_sd ";
                db.execSQL(sqlSD);
                db.setTransactionSuccessful();
            } catch (IOException e) {
                Log.e(TAG, "更新数据库失败！", e);
                throw new RuntimeException(e);
            } finally {
                db.endTransaction(); 
            }
            Log.d(TAG, "结束更新数据库结构！");
        }
    }

    private String getSQL(String path) {
        return getSQL(context, path);
    }

    public static String getSQL(Context context, String path) {
        AssetManager assetsMamager = context.getAssets();
        try {
            InputStream in = assetsMamager.open(path);
            // 获取文件的字节数
            int len = in.available();
            // 创建byte数组
            byte[] buffer = new byte[len];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            Log.e(TAG, "读取SQL文件失败!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将字符串中的多条sql语句转换成存放单条sql语句的List
     * 
     * @param sqls
     *            多条sql语句（;分隔）
     * @return
     */
    public static List<String> getSqlList(String sqls) {
        List<String> sqlList = new ArrayList<String>();
        if (sqls == null || sqls.trim().length() == 0) {
            return sqlList;
        }
        String[] sqlArr = sqls.split(";");
        for (String sql : sqlArr) {
            if (sql != null && sql.trim().length() > 0) {
                sqlList.add(sql + ";");
            }
        }
        return sqlList;
    }

    public static void beginTransaction() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
    }

    public static void setTransactionSuccessful() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.setTransactionSuccessful();
    }

    public static void endTransaction() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.endTransaction();
    }

    public static Cursor query(String sql, String[] args) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(sql, args);
    }

    public static Cursor query(String table, String[] columns, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(table, columns, selection, selectionArgs, null, null, null);
    }

    public static boolean insert(String table, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.insert(table, null, values) > 0;
    }

    public static boolean update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.update(table, values, whereClause, whereArgs) > 0;
    }

    public static boolean clearTable(String table) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(table, null, null) > 0;
    }

    public static boolean delete(String table, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(table, whereClause, whereArgs) > 0;
    }

    public static void execSQL(String sql, String[] bindArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql, bindArgs);
    }
}
