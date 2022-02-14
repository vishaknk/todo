package com.sample.todo.database

import androidx.room.*
import com.sample.todo.database.entity.Todo

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    @Update()
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("Delete from todo_table")
    suspend fun deleteAllTodo()

    @Query("Select * from todo_table order by id ASC")
    fun getAllTodo(): List<Todo>
}