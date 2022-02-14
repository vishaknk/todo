package com.sample.todo.app

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.facebook.stetho.Stetho
import com.sample.todo.database.AppDatabase
import com.sample.todo.repository.TodoRepository
import com.sample.todo.retrofit.api.ApiInterface
import com.sample.todo.retrofit.base.RetrofitNetwork
import com.sample.todo.retrofit.interceptor.NetworkConnectionInterceptor
import com.sample.todo.utils.common.ViewModelFactory
import com.sample.todo.viewModel.ToDoViewModel
import com.sof.retail.viewModel.common.bindViewModel
import org.kodein.di.Kodein
import org.kodein.di.Kodein.Companion.lazy
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber
import timber.log.Timber.DebugTree
import todo.BuildConfig

class AppController : Application(), KodeinAware {

    companion object {
        lateinit var instance: AppController
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Stetho.initializeWithDefaults(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    override val kodein = lazy {
        import(androidXModule(this@AppController))
        bind<Kodein>() with singleton { kodein }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind<ApiInterface>() with singleton { RetrofitNetwork.getApiInterface(instance()) }
        bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein.direct) }
        //====================================================================================================
        //View Model
        //====================================================================================================
        bindViewModel<ToDoViewModel>() with provider { ToDoViewModel(instance(), instance()) }
        //====================================================================================================
        //Repository
        //====================================================================================================
        bind() from singleton { TodoRepository(instance(), instance()) }
    }
}