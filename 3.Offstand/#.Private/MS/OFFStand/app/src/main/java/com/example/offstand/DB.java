package com.example.offstand;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import io.reactivex.annotations.NonNull;

/**
 * 백엔드 입니다. 데이터베이스. 이것은 오픈헬퍼에 의해 행해지곤 했다.
 * 이것이 코멘트가 거의 없다는 사실은 그 시원함을 강조한다.
 */

@Database(entities = {DBInfo.class}, version = 1)
public abstract class DB extends RoomDatabase {

    public abstract DBInfoDao dbInfoDao();

    // 변수에 대한 원자 액세스를 보장하기 위해 인스턴스
    private static volatile DB INSTANCE;

    static DB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DB.class, "DBInfo_database")
                            // 마이그레이션 개체가 없는 경우 마이그레이션 대신 wips 및 reguilding
                            // 마이그레이션은 이 codelab 일부가 아니다.
                            .fallbackToDestructiveMigration()
                            .addCallback(sDataBaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * onOpen 메서드를 재정의하여 데이터베이스를 채우십시오.
     * 이 샘플의 경우 데이터베이스를 생성하거나 열 때마다 삭제함
     * <p>
     * 데이터베이스가 처음 생성되었을 때만 데이터베이스를 채우십시오.
     * RoomDatabase override.콜백()#onCreate
     */

    private static RoomDatabase.Callback sDataBaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase sqldb) {

            super.onOpen(sqldb);
            // 앱 재시작을 통해 데이터를 유지하려면
            // 다음 행을 설명하십시오.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };


    /**
     * 백그라운드에 데이터베이스를 채우십시오.
     * 더 많은 단어로 시작하려면, 그냥 추가하십시오.
     */

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final DBInfoDao mDao;

        PopulateDbAsync(DB db) {
            mDao = db.dbInfoDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // 매번 깨끗한 데이터베이스로 앱을 시작하십시오.
            // 작성에만 전념할 경우 필요하지 않음.

            mDao.deleteAll();

            DBInfo dbInfo = new DBInfo(R.drawable.score01, R.drawable.character01, "고니", "3", "4");
            mDao.insert(dbInfo);
            dbInfo = new DBInfo(R.drawable.score02, R.drawable.character02, "짝귀", "7", "8");
            mDao.insert(dbInfo);
            dbInfo = new DBInfo(R.drawable.score03, R.drawable.character03, "아귀", "11", "12");
            mDao.insert(dbInfo);
            dbInfo = new DBInfo(R.drawable.score04, R.drawable.character04, "정마담", "11", "12");
            mDao.insert(dbInfo);
            return null;
        }
    }
}
