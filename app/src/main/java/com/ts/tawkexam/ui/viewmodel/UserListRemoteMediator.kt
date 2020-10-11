package com.ts.tawkexam.ui.viewmodel

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.ts.tawkexam.data_source.local.UserDatabase
import com.ts.tawkexam.data_source.model.User
import com.ts.tawkexam.data_source.remote.UserListApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class UserListRemoteMediator(
    private val userDb: UserDatabase,
    private val userListApi: UserListApi
) : RxRemoteMediator<Int, User>() {

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, User>
    ): Single<MediatorResult> {
        Timber.e("loadType $loadType")
        Timber.e("state " + state.pages)
        return Single.just(loadType)
            .subscribeOn(Schedulers.io())
            .map {
                when (it) {
                    LoadType.REFRESH -> 0
                    LoadType.PREPEND -> {
                        0
                    }
                    LoadType.APPEND -> {
                        val id = getRemoteKeyForLastItem(state)
                        id ?: 0
                    }
                }
            }.flatMap { id ->
                if (id == -1) {
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                } else {
                    userListApi.queryUsers(
                        since = id
                    ).flattenAsObservable { it }
                        .flatMapSingle { getUserObservable(it) }
                        .toList()
                        .map { insertToDb(loadType, it) }
                        .map<MediatorResult> {
                            MediatorResult.Success(endOfPaginationReached = false)
                        }
                        .onErrorReturn {
                            Timber.e("ERROR 1 " + it.localizedMessage)
                            MediatorResult.Error(it)
                        }
                }
            }
            .onErrorReturn {
                Timber.e("ERROR 2 " + it.localizedMessage)
                MediatorResult.Error(it)

            }
    }


    private fun getUserObservable(it: User): Single<User> {
        return userListApi.getUser(it.login)
    }


    private fun insertToDb(loadType: LoadType, data: List<User>): List<User> {
        userDb.beginTransaction()

        try {
            if (loadType == LoadType.REFRESH) {
                userDb.userDao().deleteUsers()
            }

            userDb.userDao().upsertAll(data)
            userDb.setTransactionSuccessful()
        } finally {
            userDb.endTransaction()
        }
        return data
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, User>): Int? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { user ->
                userDb.userDao().getLastId()
            }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, User>): Int? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { user ->
                userDb.userDao().getFirstId()
            }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.login?.let { login ->
                userDb.userDao().getFirstId()
            }
        }
    }

    companion object {
        const val INVALID_ID = -1
    }
}