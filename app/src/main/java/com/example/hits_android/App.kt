package com.example.hits_android

import android.app.Application
import android.content.Context
import com.example.hits_android.appmodel.data.filestorage.SaveFileStorage
import com.example.hits_android.appmodel.data.localstorage.SaveRoomStorage
import com.example.hits_android.appmodel.data.model.SaveModel
import com.example.hits_android.appmodel.data.repository.AppRepositoryImpl
import com.example.hits_android.appmodel.data.repository.SaveRepository
import com.example.hits_android.blocks.BlockImpl
import com.example.hits_android.blocks.BlockInstanceCreator
import com.example.hits_android.model.SavesViewModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import java.io.InputStreamReader
import java.util.Date
import java.util.UUID

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

                    factory {
                        AppRepositoryImpl(
                            prefs = androidContext().getSharedPreferences("APP_SHARED_PREFS", Context.MODE_PRIVATE)
                        )
                    }

                }
            )
        }
        val appRepository: AppRepositoryImpl by inject()

        if (appRepository.isFirstLaunch()) {
            CoroutineScope(Dispatchers.IO).launch {
                initDb()
            }
        }
    }

    private suspend fun initDb() {
        val repository: SaveRepository by inject()

        val initDataJson = applicationContext.assets.open("fibonacci.json").reader().use(
            InputStreamReader::readText
        )

        val arrayType = object : TypeToken<List<List<BlockImpl>>>() {}.type

        val gson = GsonBuilder()
            .registerTypeAdapter(BlockImpl::class.java, BlockInstanceCreator())
            .create()

        val functionList: List<List<BlockImpl>> = gson.fromJson(initDataJson, arrayType)

        val saveModel = SaveModel(
            functionsList = functionList,
            name = UUID.randomUUID().toString(),
            date = Date().time,
            realName = "Fibonacci"
        )

        repository.createSave(saveModel)
    }
}