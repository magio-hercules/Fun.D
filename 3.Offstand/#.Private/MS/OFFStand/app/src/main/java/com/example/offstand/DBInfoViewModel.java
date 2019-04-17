package com.example.offstand;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class DBInfoViewModel extends AndroidViewModel {

    private DBInfoRepository mRepository;
    // LiveData를 사용하고 getAlphatedWords가 반환하는 내용을 캐싱하면 다음과 같은 몇 가지 이점이 있다.
    // - 데이터(변경 폴링 대신)에 관찰자를 배치하고 업데이트만 할 수 있다.
    // 데이터가 실제로 변경될 때 UI.
    // - 리포지토리는 ViewModel을 통해 UI에서 완전히 분리된다.
    private LiveData<List<DBInfo>> mAllDBInfos;

    public DBInfoViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DBInfoRepository(application);
        mAllDBInfos = mRepository.getAllDBInfos();
    }

    LiveData<List<DBInfo>> getmAllDBInfos() {
        return mAllDBInfos;
    }

    void insert(DBInfo dbInfo) {
        mRepository.insert(dbInfo);
    }
}
