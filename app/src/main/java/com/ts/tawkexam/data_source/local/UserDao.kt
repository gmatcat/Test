package com.ts.tawkexam.data_source.local

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.*
import com.ts.tawkexam.Outcome
import com.ts.tawkexam.data_source.model.User
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUsers(): PagingSource<Int, User>

    @Query("SELECT * FROM user WHERE name LIKE :name")
    fun findByName(name: String): List<User>

    @Query("SELECT * FROM user WHERE name LIKE :id")
    fun findById(id: Int): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(user: User)

    @Query("SELECT * FROM user WHERE name LIKE :keyword OR note LIKE :keyword")
    fun searchUserByKeyword(keyword: String): Single<List<User>>

    @Query("SELECT id FROM user ORDER BY id DESC LIMIT 1")
    fun getLastId(): Int

    @Query("SELECT id FROM user ORDER BY id ASC LIMIT 1")
    fun getFirstId(): Int

    @Query("DELETE FROM user")
    fun deleteUsers()


}