package com.example.calorify20.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface InOutTakeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertActivity(activity : InOutTake)
    @Update
    fun updateActiviy(activity: InOutTake)
    @Delete
    fun deleteActivity(activity: InOutTake)
    @get:Query("SELECT * FROM activity_table ORDER BY id ASC")
    val allActivity: LiveData<List<InOutTake>>
    @Query("SELECT * FROM activity_table WHERE uid = :userUid ORDER BY id ASC")
    fun getActivtiyByCurrentUser(userUid : String): LiveData<List<InOutTake>>
    @Query("SELECT * FROM activity_table WHERE uid = :userUid ORDER BY id DESC")
    fun getRecentActivtiyByCurrentUser(userUid : String): LiveData<List<InOutTake>>
    @Query("DELETE FROM activity_table")
    fun clearActivity()
    @Query("SELECT COALESCE(SUM(calory), 0) FROM activity_table WHERE uid = :userId AND type = :activityType")
    fun getCaloryByType(userId: String, activityType: String): LiveData<Int>

}