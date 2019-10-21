package com.fundroid.routinesc

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.facebook.stetho.Stetho
import com.fundroid.routinesc.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime

class RoutineApplication : Application() {

    val scope = CoroutineScope(Dispatchers.IO)

    companion object {
        lateinit var database: RoutineDatabase
        lateinit var prefs: RoutineSharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("lsc", "RoutineApplication onCreate")
        Stetho.initializeWithDefaults(this)
        database = Room.databaseBuilder(this, RoutineDatabase::class.java, "ROUTINE_DB").build()
        prefs = RoutineSharedPreferences(applicationContext)

        if (!prefs.isInitialized) {
            dataInitialize()
            prefs.isInitialized = true
        }

    }

    fun dataInitialize() {
        scope.launch {
            database.routineDao().createRoutine(
                Routine(
                    "너두 나두 다이어트",
                    "너두 할 수 있어~\n나두 한다.\n다이어트",
                    "한달에 5킬로 성공보장 루틴",
                    "매일 반복하는 습관루틴과 미리 정해놓은 식단으로 다이어트 하",
                    RoutineType.DIET.name
                )
            )
            database.routineDao().createRoutine(
                Routine(
                    "수지의 백수일과",
                    "너두 수지처럼\n당당한 집순이 루틴\n따라하기",
                    "스케쥴 없는 수지처럼 하루 따라하기.",
                    "스케쥴 없는 날, 백수가 된 수지 !@#$%",
                    RoutineType.STAR.name
                )
            )
            database.routineDao().createRoutine(
                Routine(
                    "100억 상단 텍스트",
                    "100억 자산가되기",
                    "100억 하단 텍스트",
                    "WEALTHY ...!@#$#%",
                    RoutineType.RICH.name
                )
            )
            database.routineDao().createRoutine(
                Routine(
                    "상단 텍스트",
                    "FUNDROID 스터디모임",
                    "하단 텍스트",
                    "테스트 루틴입니다.",
                    RoutineType.TEST.name
                )
            )

            //알람 - 너두 나두 다이어트
            database.alarmDao()
                .createAlarm(Alarm(LocalTime.now(), "헤더용 더미", true, RoutineType.DIET.name, 1))
            database.alarmDao().createAlarm(
                Alarm(
                    LocalTime.of(11, 0),
                    "다이어트 알람 1",
                    true,
                    RoutineType.DIET.name,
                    1
                )
            )
            database.alarmDao().createAlarm(
                Alarm(
                    LocalTime.of(12, 0),
                    "다이어트 알람 2",
                    true,
                    RoutineType.DIET.name,
                    1
                )
            )
            database.alarmDao().createAlarm(
                Alarm(
                    LocalTime.of(13, 0),
                    "다이어트 알람 3",
                    true,
                    RoutineType.DIET.name,
                    1
                )
            )

            //노티 - 너두 나두 다이어트
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(11, 10),
                    "이불에서 나와라~",
                    RoutineType.DIET.name,
                    1
                )
            )
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(12, 10),
                    "이불에서 나와라~",
                    RoutineType.DIET.name,
                    1
                )
            )
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(13, 10),
                    "이불에서 나와라~",
                    RoutineType.DIET.name,
                    1
                )
            )

            //알람 - 수지
            database.alarmDao()
                .createAlarm(Alarm(LocalTime.now(), "헤더용 더미", true, RoutineType.STAR.name, 2))
            database.alarmDao().createAlarm(
                Alarm(
                    LocalTime.of(11, 0),
                    "일어나, 이쁜 수지처럼~\n기지개를 켜고",
                    true,
                    RoutineType.STAR.name,
                    2
                )
            )
            database.alarmDao().createAlarm(
                Alarm(
                    LocalTime.of(12, 0),
                    "트레이더 분이 방문하는 수지\n넌 그냥 홈트(영상으로) 해~",
                    true,
                    RoutineType.STAR.name,
                    2
                )
            )
            database.alarmDao().createAlarm(
                Alarm(
                    LocalTime.of(13, 0),
                    "아침 겸 점심으로\n식사하기",
                    true,
                    RoutineType.STAR.name,
                    2
                )
            )
            database.alarmDao().createAlarm(
                Alarm(
                    LocalTime.of(15, 0),
                    "연예인 수지도 TV보며\n뒹굴 뒹굴~",
                    true,
                    RoutineType.STAR.name,
                    2
                )
            )
            database.alarmDao().createAlarm(
                Alarm(
                    LocalTime.of(16, 0),
                    "튜터와 함께 작사, 작곡하는\n수지, 너의 관심은 뭐야?",
                    true,
                    RoutineType.STAR.name,
                    2
                )
            )
            database.alarmDao().createAlarm(
                Alarm(
                    LocalTime.of(20, 0),
                    "벌써~ 저녁시간이야\n간단한 식사하기",
                    true,
                    RoutineType.STAR.name,
                    2
                )
            )
            database.alarmDao().createAlarm(
                Alarm(
                    LocalTime.of(21, 0),
                    "다시 뒹굴 뒹굴~\n소화시키며 TV 시청",
                    true,
                    RoutineType.STAR.name,
                    2
                )
            )
            database.alarmDao().createAlarm(
                Alarm(
                    LocalTime.of(0, 0),
                    "이불 밖은 위험해~\n다시 안전한 잠자리로~\n자~ 자 푹!!!",
                    true,
                    RoutineType.STAR.name,
                    2
                )
            )

            //노티 - 수지
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(11, 10),
                    "이불에서 나와라~",
                    RoutineType.STAR.name,
                    2
                )
            )
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(11, 11),
                    "이불에서 나와라~2",
                    RoutineType.STAR.name,
                    2
                )
            )
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(11, 12),
                    "이불에서 나와라~3",
                    RoutineType.STAR.name,
                    2
                )
            )

            //알람 - 100억
            database.alarmDao()
                .createAlarm(Alarm(LocalTime.now(), "헤더용 더미", true, RoutineType.RICH.name, 3))
            database.alarmDao()
                .createAlarm(Alarm(LocalTime.of(11, 0), "100억 1", true, RoutineType.RICH.name, 3))
            database.alarmDao()
                .createAlarm(Alarm(LocalTime.of(12, 0), "100억 2", true, RoutineType.RICH.name, 3))
            database.alarmDao()
                .createAlarm(Alarm(LocalTime.of(13, 0), "100억 3", true, RoutineType.RICH.name, 3))

            //노티 - 100억
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(11, 10),
                    "이불에서 나와라~",
                    RoutineType.RICH.name,
                    3
                )
            )
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(12, 10),
                    "이불에서 나와라~",
                    RoutineType.RICH.name,
                    3
                )
            )
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(13, 10),
                    "이불에서 나와라~",
                    RoutineType.RICH.name,
                    3
                )
            )


            //알람 - FUNDROID
            database.alarmDao()
                .createAlarm(Alarm(LocalTime.now(), "헤더용 더미", true, RoutineType.TEST.name, 4))
            database.alarmDao().createAlarm(
                Alarm(
                    LocalTime.of(19, 0),
                    "스터디 시작시간입니다.",
                    true,
                    RoutineType.TEST.name,
                    4
                )
            )

            //노티 - FUNDROID
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(19, 0),
                    "스터디 시작시간입니다.",
                    RoutineType.TEST.name,
                    4
                )
            )
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(19, 30),
                    "!@#에 대하여 $%^하는 시간입니다.",
                    RoutineType.TEST.name,
                    4
                )
            )
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(20, 0),
                    "XXX에 대하여 XXX합시다.",
                    RoutineType.TEST.name,
                    4
                )
            )
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(20, 30),
                    "",
                    RoutineType.TEST.name,
                    4
                )
            )
            database.notificationDao().createNotification(
                Notification(
                    LocalTime.of(20, 50),
                    "스터디 종료 10분전입니다.\n마무리하세요.",
                    RoutineType.TEST.name,
                    4
                )
            )
        }

    }
}