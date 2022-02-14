package com.sample.todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sample.todo.database.entity.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    //Define all DAO class here
    abstract fun getTodoDAO(): TodoDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "${"todo_db"}"
        )
            .build()
    }
}