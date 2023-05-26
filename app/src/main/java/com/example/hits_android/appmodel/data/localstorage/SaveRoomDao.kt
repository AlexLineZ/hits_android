package com.example.hits_android.appmodel.data.localstorage

import androidx.room.*
import java.util.UUID

@Dao
interface SaveRoomDao {
    @Query("SELECT * FROM save_table WHERE name = :name")
    suspend fun getSave(name: UUID) : SaveInfoEntity
    @Query("SELECT * FROM save_table")
    suspend fun getAllSaves() : List<SaveInfoEntity>
    @Insert
    suspend fun createSave(save: SaveInfoEntity)
    @Delete
    suspend fun deleteSave(save: SaveInfoEntity)
    @Update
    suspend fun updateSave(save: SaveInfoEntity)
}