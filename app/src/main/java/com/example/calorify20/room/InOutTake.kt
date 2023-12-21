package com.example.calorify20.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_table")
data class InOutTake (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id : Int = 0,
    @ColumnInfo(name = "type")
    val type : String,
    @ColumnInfo(name = "occasion")
    val occasion : String,
    @ColumnInfo(name = "item")
    val item : String,
    @ColumnInfo(name = "time")
    val time : String,
    @ColumnInfo(name = "calory")
    val calory : Int,
    @ColumnInfo(name = "uid")
    val uid : String
)