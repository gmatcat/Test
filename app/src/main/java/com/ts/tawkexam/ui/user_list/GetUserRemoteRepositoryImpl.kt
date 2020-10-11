package com.ts.tawkexam.ui.user_list

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.ts.tawkexam.data_source.local.UserDatabase
import com.ts.tawkexam.data_source.model.User
import com.ts.tawkexam.ui.viewmodel.UserListRemoteMediator
import io.reactivex.Flowable

class GetUserRemoteRepositoryImpl(
    private val userDb: UserDatabase,
    private val remoteMediator: UserListRemoteMediator
): GetUsersRepository {

    override fun getAllUsers(): Flowable<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true),
            remoteMediator = remoteMediator,
            pagingSourceFactory = {
                userDb.userDao().getAllUsers()
            }
        ).flowable
    }
}