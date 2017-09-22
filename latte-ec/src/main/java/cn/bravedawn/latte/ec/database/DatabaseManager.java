package cn.bravedawn.latte.ec.database;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * Created by 冯晓 on 2017/9/22.
 */

public class DatabaseManager {

    private DaoSession mDaoSession = null;
    private UserProfileDao mDao = null;

    private DatabaseManager(){
    }

    private static final class Holder{
        private static final DatabaseManager INSTANCE = new DatabaseManager();
    }

    public static DatabaseManager getInstance(){
        return Holder.INSTANCE;
    }

    private void initDao(Context context) {
        final ReleaseOpenHelper helper = new ReleaseOpenHelper(context, "fast_ec.db");
        final Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
        mDao = mDaoSession.getUserProfileDao();
    }

    public DatabaseManager init(Context context){
        initDao(context);
        return this;
    }

    public final UserProfileDao getDao(){
        return mDao;
    }

}
