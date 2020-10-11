package com.ts.tawkexam.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ts.tawkexam.data_source.model.User
@Database(entities = [User::class], version = 1,
    exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}