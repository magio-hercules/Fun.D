package com.example.offstand;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class DBInfoRepository {

    private DBInfoDao mDBInfoDao;
    private LiveData<List<DBInfo>> mAllDBInfos;

    DBInfoRepository(Application application) {
        DB db = DB.getDatabase(application);
        mDBInfoDao = db.dbInfoDao();
        mAllDBInfos = mDBInfoDao.getRank();
    }

    // 룸은 별도의 스레드에 대해 모든 쿼리를 실행한다.
    // 관찰된 LiveData는 데이터가 변경되면 관찰자에게 통지한다.
    LiveData<List<DBInfo>> getAllDBInfos() {
        return mAllDBInfos;
    }

    // UI가 아닌 스레드에 이 call을 걸어야 한다. 그렇지 않으면 앱이 다운될 것이다.
    // 이렇게, 방에서는 메인에서 장시간 실행 작업을 하지 않도록 한다.
    // 스레드, UI 차단.
    void insert(DBInfo dbInfo) {
        new insertAsyncTask(mDBInfoDao).execute(dbInfo);
    }

    private static class insertAsyncTask extends AsyncTask<DBInfo, Void, Void> {

        private DBInfoDao mAsyncTaskDao;

        public insertAsyncTask(DBInfoDao mDBInfoDao) {
            mAsyncTaskDao = mDBInfoDao;
        }

        @Override
        protected Void doInBackground(DBInfo... dbInfos) {
            mAsyncTaskDao.insert(dbInfos[0]);
            return null;
        }
    }
}
