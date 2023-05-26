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

class App : Application() {

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
                            prefs = androidContext().getSharedPreferences(
                                "APP_SHARED_PREFS",
                                Context.MODE_PRIVATE
                            )
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

        val fibJson = applicationContext.assets.open("fibonacci.json").reader().use(
            InputStreamReader::readText
        )
        val bubbleSortJson = applicationContext.assets.open("bubble_sort.json").reader().use(
            InputStreamReader::readText
        )
        val cumshotJson = applicationContext.assets.open("oral_cum_shot.json").reader().use(
            InputStreamReader::readText
        )

        val jsonList: List<Pair<String, String>> =
            listOf(
                Pair(fibJson, "Fibonacci"),
                Pair(bubbleSortJson, "BubbleSort"),
                Pair(cumshotJson, "Oral cum shot")
            )

        val arrayType = object : TypeToken<List<List<BlockImpl>>>() {}.type

        val gson = GsonBuilder()
            .registerTypeAdapter(BlockImpl::class.java, BlockInstanceCreator())
            .create()

        jsonList.forEach {
            val functionList: List<List<BlockImpl>> = gson.fromJson(it.first, arrayType)

            val saveModel = SaveModel(
                functionsList = functionList,
                name = UUID.randomUUID().toString(),
                date = Date().time,
                realName = it.second
            )

            repository.createSave(saveModel)
        }
    }
}