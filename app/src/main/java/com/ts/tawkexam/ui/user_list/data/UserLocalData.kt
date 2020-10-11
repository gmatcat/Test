package com.ts.tawkexam.ui.user_list.data

import android.annotation.SuppressLint
import android.util.Log
import com.ts.tawkexam.Outcome
import com.ts.tawkexam.Scheduler
import com.ts.tawkexam.data_source.local.UserDatabase
import com.ts.tawkexam.data_source.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber

class UserLocalData(private val userDb: UserDatabase, private val scheduler: Scheduler) :
    UserDataContract.Local {

//    override fun getUsersInLocal(): Single<List<User>> {
////        return userDb.userDao().getAllUsers()
//        return Single.create(listOf<User>()))
//    }

    override fun addUsers(users: List<User>) {
        Completable.fromAction {
            userDb.userDao().upsertAll(users)
        }.subscribeOn(scheduler.io())
            .subscribe {
                Log.e("Complete", " Add Users")
            }
    }

    override fun addUser(user: User) {
//        Timber.e("addUser $user.name")
        Completable.fromAction {
            userDb.userDao().upsert(user)
        }.subscribeOn(scheduler.io())
            .subscribe {
                Log.e("Complete", " Add User")
            }
    }

    override fun getUsersInLocalWithKeyword(searchText: String): Single<List<User>> {
        Timber.e("getUsersInLocalWithKeyword $searchText")
        val keyword = "%$searchText%"
        return userDb.userDao().searchUserByKeyword(keyword)

    }

    override fun updateNoteInLocal(id: Int, note: String) {
//        Completable.fromAction {
//            userDb.userDao().updateNoteInDb(id, note)
//        }.subscribeOn(scheduler.io())
//            .subscribe {
//                Log.e("Complete", " Update Post")
//            }
    }

//    override fun getCommentsForPost(postId: Int): Flowable<List<Comment>> {
//        return postDb.commentDao().getForPost(postId)
//    }
//
//    override fun saveComments(comments: List<Comment>) {
//        Completable.fromAction {
//            postDb.commentDao().upsertAll(comments)
//        }
//            .performOnBack(scheduler)
//            .subscribe()
//    }
}