package com.ts.tawkexam.ui.user_list.data

import android.annotation.SuppressLint
import com.ts.tawkexam.Outcome
import com.ts.tawkexam.Scheduler
import com.ts.tawkexam.data_source.model.User
import com.ts.tawkexam.utils.success
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class UserRepository(
    private val local: UserDataContract.Local,
    private val remote: UserDataContract.Remote,
    private val scheduler: Scheduler,
    private val compositeDisposable: CompositeDisposable
) : UserDataContract.Repository {


    override val userFetchOutcome: PublishSubject<Outcome<User>> =
        PublishSubject.create<Outcome<User>>()

    override val userListFetchOutcome: PublishSubject<Outcome<List<User>>> =
        PublishSubject.create<Outcome<List<User>>>()


    override fun fetchUsersInLocal() {
//        val fetch = local.getUsersInLocal()
//            .subscribeOn(scheduler.io())
//            .observeOn(scheduler.mainThread())
//            .subscribe({ users ->
//                when (users.size) {
//                    0 -> {
//                        fetchUsersInRemote()
//                    }
//                    else -> {
//                        userListFetchOutcome.success(users)
//                    }
//                }
//            }, { error ->
//                handleError(error)
//            })
//
//        compositeDisposable.add(fetch)


    }

    override fun fetchUsersInRemote() {
        val fetch =
            remote.getUsersInRemote()
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.mainThread())
                .subscribe({ users ->
                    Observable.fromIterable(users)
                        .subscribeOn(scheduler.io())
                        .map { user -> fetchUserByIdInRemoteSync(user) }
                        .subscribe()
                }, { error ->
                    handleError(error)
                })

        compositeDisposable.add(fetch)
    }

    //All network calls must be queued and limited to 1 request at a time.
    //Combined with Dispatcher
    //dispatcher.maxRequests = 1
    //dispatcher.maxRequestsPerHost = 1
    @SuppressLint("CheckResult")
    private fun fetchUserByIdInRemoteSync(user: User) {
        Timber.e("queue fetchUserByIdInRemoteSync " + user.login)

        //Block
        val userGet = remote.getUserByIdInRemote(user.login).blockingGet()
        Observable.fromIterable(mutableListOf(userGet))
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.mainThread())
            .subscribe {
                it?.let {
                    Timber.e("post fetchUserByIdInRemote " + it.login)
                    //Pass back user data to list and notify
                    userFetchOutcome.success(it)
                    //Add User in Room DB
                    local.addUser(it)
                }
            }

    }

    private fun fetchUserByIdInRemoteAsync(user: User) {
        Timber.e("queue fetchUserByIdInRemoteAsync " + user.login)
        val fetch = remote.getUserByIdInRemote(user.login)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.mainThread())
            .subscribe({ userX ->
                Timber.e("post fetchUserByIdInRemote " + user.login)
                userFetchOutcome.success(userX)
                local.addUser(userX)
            }, { error ->
                handleError(error)
            })
        compositeDisposable.add(fetch)
    }

    override fun fetchUsersInLocalWithKeyword(searchText: String) {
        compositeDisposable.clear()
        val fetchUsersInLocalWithKeyword = local.getUsersInLocalWithKeyword(searchText)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.mainThread())
            .subscribe({ users ->
                when (users.size) {
                    0 -> {
                    }
                    else -> {
                        Timber.e("Returned users $users")
                        userListFetchOutcome.success(users)
                    }
                }
            }, { error ->
                handleError(error)
            })
        compositeDisposable.add(fetchUsersInLocalWithKeyword)
    }

    override fun updateNoteInLocalWithId(id: Int, text: String) {
        local.updateNoteInLocal(id, text)
    }


    override fun handleError(error: Throwable) {
        Timber.e("ERROR $error")
    }

}



