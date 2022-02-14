package com.sample.todo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "todo_table")
data class Todo(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "desc")
    var description: String,
    @ColumnInfo(name = "time")
    var time: String,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "type")
    var type: Int,    //daily = 1 and weekly = 2
    @ColumnInfo(name = "checked")
    var isChecked: Boolean = false,
) : Serializable {

    fun isSelected(): Boolean {
        return isChecked
    }

    fun setSelected(selected: Boolean) {
        isChecked = selected
    }
}