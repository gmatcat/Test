package com.ts.tawkexam.di.core

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.ts.tawkexam.Scheduler
import com.ts.tawkexam.data_source.local.UserDatabase
import com.ts.tawkexam.data_source.remote.UserListApi
import com.ts.tawkexam.di.scope.ActivityScope
import com.ts.tawkexam.ui.user_list.GetUserRemoteRepositoryImpl
import com.ts.tawkexam.ui.user_list.GetUsersRepository
import com.ts.tawkexam.ui.user_list.data.UserDataContract
import com.ts.tawkexam.ui.user_list.data.UserLocalData
import com.ts.tawkexam.ui.user_list.data.UserRemoteData
import com.ts.tawkexam.ui.user_list.data.UserRepository
import com.ts.tawkexam.ui.viewmodel.UserListRemoteMediator
import com.ts.tawkexam.ui.viewmodel.UserViewModelFactory
import com.ts.tawkexam.utils.DB_NAME
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class RepositoryModule {

    @Provides
    fun userDb(context: Context): UserDatabase = Room.databaseBuilder(context, UserDatabase::class.java, DB_NAME).build()

    @Provides
    fun userViewModelFactory(repository: GetUsersRepository,compositeDisposable: CompositeDisposable): UserViewModelFactory
            = UserViewModelFactory(repository,compositeDisposable)

    @Provides
    fun userRepo(local: UserDataContract.Local, remote: UserDataContract.Remote, scheduler: Scheduler,
                 compositeDisposable: CompositeDisposable): UserDataContract.Repository
            = UserRepository(local, remote, scheduler, compositeDisposable)

    @Provides
    fun remoteData(api: UserListApi): UserDataContract.Remote = UserRemoteData(api)

    @Provides
    fun localData(userDb: UserDatabase, scheduler: Scheduler): UserDataContract.Local = UserLocalData(userDb, scheduler)

    @Provides
    fun remoteRepository(userDb: UserDatabase, remoteMediator: UserListRemoteMediator): GetUsersRepository = GetUserRemoteRepositoryImpl(userDb, remoteMediator)

    @Provides
    fun remoteMediator(userDb: UserDatabase, api: UserListApi): UserListRemoteMediator = UserListRemoteMediator(userDb, api)

    @Provides
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()
}