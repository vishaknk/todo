package com.sample.todo.viewModel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.sample.todo.database.entity.Todo
import com.sample.todo.model.authentication.TodoLoginResponseModel
import com.sample.todo.receiver.AlarmReceiver
import com.sample.todo.repository.TodoRepository
import java.text.SimpleDateFormat
import java.util.*

class ToDoViewModel(val todoRepository: TodoRepository, val context: Context) : ViewModel() {

    fun getAllTodo(): LiveData<List<Todo>> {
        return todoRepository.getAllTodo()
    }

    fun insertTodo(todoItem: Todo) {
        todoRepository.insertTodo(todoItem)
        setAlarm(context, todoItem)
    }

    fun deleteTodo(todoItem: Todo): LiveData<Boolean> {
        Log.e("Tag", "delete")
        cancelAlarm(context, todoItem)
        return todoRepository.deleteTodo(todoItem)
    }

    fun updateTodo(updatedTodoItem: Todo): LiveData<Boolean> {
        Log.e("Tag", "update")
        setAlarm(context, updatedTodoItem)
        return todoRepository.updateTodo(updatedTodoItem)
    }

    fun authenticateUser(email: String, password: String): LiveData<TodoLoginResponseModel> {
        return todoRepository.authenticateUser(email, password)
    }

    private fun setAlarm(context: Context, todoItem: Todo) {
        val timeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val todoTime = todoItem.date.plus(" ").plus(todoItem.time+":00")
        Log.e("Tag", todoTime.toString())
        val formatted = timeFormat.parse(todoTime)
        val cal = Calendar.getInstance()
        cal.time = formatted
        Log.e("Tag", formatted.toString())
        Log.e("Tag", cal.get(Calendar.HOUR_OF_DAY).toString())
        Log.e("Tag", cal.get(Calendar.MINUTE).toString())

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("todo", Gson().toJson(todoItem))
        val pendingIntent = PendingIntent.getBroadcast(context, todoItem.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = cal.get(Calendar.HOUR_OF_DAY)
        calendar[Calendar.MINUTE] = cal.get(Calendar.MINUTE)
        calendar[Calendar.SECOND] = 0

        //alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
        if (todoItem.type == 1) {
            //daily
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } else {
            //weekly
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent
            )
        }
    }

    private fun cancelAlarm(context: Context, todoItem: Todo) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("todo", Gson().toJson(todoItem))
        val pendingIntent =
            PendingIntent.getBroadcast(context, todoItem.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

}