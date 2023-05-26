package com.example.hits_android

import android.app.Application
import com.example.hits_android.appmodel.data.filestorage.SaveFileStorage
import com.example.hits_android.appmodel.data.localstorage.SaveRoomStorage
import com.example.hits_android.appmodel.data.repository.SaveRepository
import com.example.hits_android.model.SavesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {

            androidContext(this@App)
            androidLogger(Level.ERROR)

            modules(
                modules = module {
                    viewModel {
                        SavesViewModel(
                            saveRepository = get()
                        )
                    }
                    factory {
                        SaveRepository(
                            fileStorage = get(),
                            database = SaveRoomStorage.getInstance(androidContext()),
                            context = androidContext()
                        )
                    }
                    factory {
                        SaveFileStorage(
                            context = androidContext()
                        )
                    }

                }
            )
        }
    }

}