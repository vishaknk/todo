package com.sample.todo.view.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.sample.todo.database.entity.Todo
import com.sample.todo.receiver.AlarmReceiver
import com.sample.todo.utils.TypefaceSpan
import com.sample.todo.view.fragment.AddNewTodoFragment
import com.sample.todo.view.fragment.HomeTodoListFragment
import com.sample.todo.view.fragment.TodoLoginFragment
import com.sample.todo.view.fragment.UpdateTodoFragment
import todo.R
import todo.databinding.ActivityBaseHomeBinding
import java.text.SimpleDateFormat
import java.util.*


class BaseHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseHomeBinding
    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_base_home)

        val window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.p_dark)
        }
        setUpActionBar()
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
        navController = Navigation.findNavController(this, R.id.myFragment)
    }

    fun setUpActionBar(text: String = "Home") {
        //change actionbar font
        val s = SpannableString(text)
        s.setSpan(
            TypefaceSpan(this, "AveriaSansLibre-Bold.ttf"),
            0,
            s.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.toolbar.title = s

    }

    override fun onBackPressed() {
        val navHost = supportFragmentManager.primaryNavigationFragment
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.fragments.get(0)?.let { fragment ->
                if (fragment is HomeTodoListFragment) {
                    finish()
                } else if (fragment is AddNewTodoFragment) {
                    navController?.navigate(R.id.homeTodoListFragment)
                } else if (fragment is TodoLoginFragment) {
                    finish()
                } else if (fragment is UpdateTodoFragment) {
                    navController?.navigate(R.id.homeTodoListFragment)
                } else {
                    // empty
                }
            }
        }
    }

}