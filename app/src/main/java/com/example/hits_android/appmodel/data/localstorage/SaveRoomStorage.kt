package com.example.hits_android.appmodel.data.localstorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SaveInfoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SaveRoomStorage : RoomDatabase() {
    abstract val dao: SaveRoomDao

    companion object {
        private var instance: SaveRoomStorage? = null
        fun getInstance(context: Context): SaveRoomStorage {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context = context,
                        klass = SaveRoomStorage::class.java,
                        name = "saves_database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}