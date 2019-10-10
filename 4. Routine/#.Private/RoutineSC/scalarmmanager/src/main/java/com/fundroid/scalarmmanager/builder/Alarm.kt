package com.fundroid.scalarmmanager.builder

import android.content.Context
import com.fundroid.scalarmmanager.enums.AlarmType
import com.fundroid.scalarmmanager.interfaces.IAlarmListener

internal class Alarm(var context: Context?, var id: String?, var time: Long, var alarmType: AlarmType?, var alarmListener: IAlarmListener?)
