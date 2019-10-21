package com.fundroid.routinesc.data

import androidx.room.*

@Dao
interface NotificationDao {
    @Insert
    fun createNotification(notification: Notification)

}