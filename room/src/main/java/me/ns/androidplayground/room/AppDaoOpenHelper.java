package me.ns.androidplayground.room;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import me.ns.androidplayground.room.greendao.DaoMaster;
import me.ns.androidplayground.room.greendao.DaoSession;


/**
 * アプリケーション固有DaoOpenHelper
 * <p>
 * Created by shintaro.nosaka on 16/08/31.
 */
public class AppDaoOpenHelper extends DaoMaster.OpenHelper {

    /**
     * DB名
     */
    private static final String DB_NAME = "smart_tag";

    /**
     * {@link SQLiteDatabase}
     */
    private SQLiteDatabase mSQLiteDatabase = null;

    /**
     * {@link DaoMaster}
     */
    private DaoMaster mDaoMaster = null;

    /**
     * {@link DaoSession}
     */
    private DaoSession mDaoSession = null;

    /**
     * {@link Context}
     */
    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     * @param factory {@link SQLiteDatabase.CursorFactory}
     */
    public AppDaoOpenHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory);
        mContext = context;
        this.getDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     // なし
    }

    /**
     * {@link SQLiteDatabase}取得
     *
     * @return {@link SQLiteDatabase}
     */
    public SQLiteDatabase getDatabase() {
        if (this.mSQLiteDatabase == null) {
            this.mSQLiteDatabase = this.getWritableDatabase();
        }

        return this.mSQLiteDatabase;
    }

    /**
     * {@link DaoMaster}取得
     *
     * @return {@link DaoMaster}
     */
    public DaoMaster getDaoMaster() {
        if (this.mDaoMaster == null) {
            this.mDaoMaster = new DaoMaster(this.getDatabase());
        }

        return this.mDaoMaster;
    }

    /**
     * {@link DaoSession}取得
     *
     * @return {@link DaoSession}
     */
    public DaoSession getDaoSession() {
        if (this.mDaoSession == null) {
            this.mDaoSession = this.getDaoMaster().newSession();
        }

        return this.mDaoSession;
    }

}
