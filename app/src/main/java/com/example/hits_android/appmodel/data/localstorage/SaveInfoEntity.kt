package com.example.hits_android.appmodel.data.localstorage

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "save_table")
data class SaveInfoEntity (
    @PrimaryKey
    val name: UUID = UUID.randomUUID(),
    val date: Long
)