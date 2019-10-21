package com.fundroid.routinesc.data

import androidx.room.*

@Dao
interface RoutineDao {
    @Insert
    fun createRoutine(routine: Routine)

    @Query("SELECT * FROM Routine")
    fun getRoutines(): List<Routine>

    @Query("SELECT * FROM Routine WHERE id= :id")
    fun getRoutineById(id: Int): Routine

}