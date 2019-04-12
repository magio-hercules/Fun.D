package com.example.offstand;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Room Magic은 Java 메서드 호출을 SQL 조회에 매핑하는 이 파일에 있다.
 * <p>
 * 날짜와 같은 복잡한 데이터 유형을 사용하는 경우 유형 변환기도 제공해야 한다.
 * 이 예제를 기본으로 유지하기 위해 변환기가 필요한 유형은 사용하지 않는다.
 * 설명서 참조:
 * https://developer. android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
public interface DBInfoDao {

    // LiveData는 주어진 라이프사이클 내에서 관찰할 수 있는 데이터 홀더 클래스 입니다.
    // 항상 최신 버전의 데이터를 보관/캐싱하십시오. 이 경우 능동 관찰자에게 알림
    // 데이터가 변경됨 데이터베이스의 모든 내용을 입수하고 있기 때문에
    // 데이터베이스 내용이 변경될 때마다 알림
    @Query("SELECT * from DBInfo_table ORDER BY rank ASC")
    LiveData<List<DBInfo>> getRank();


    @Insert
    void insert(DBInfo roomDBInfo);

    //@Delete
    @Query("DELETE FROM DBInfo_table")
    void deleteAll();


    // 그 단어가 우리의 주요 열쇠고, 당신은 우리의 주요 열쇠가 될 수 없기 때문에, 우리는 갈등 전략이 필요하지 않다.
    // 동일한 기본 키를 가진 두 항목을 데이터베이스에 추가하십시오. 테이블이 하나 이상 있는 경우
    // 열, @Insert(onConfiguration = OnConflictStrategy)를 사용할 수 있다.REPLACE)를 사용하여 행을 업데이트한다.
}
