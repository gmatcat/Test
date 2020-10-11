package com.ts.tawkexam.ui.user_list.data

import androidx.paging.PagedList
import androidx.paging.PagingData
import com.ts.tawkexam.Outcome
import com.ts.tawkexam.data_source.model.User
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class UserDataContract {
    interface Repository {
        fun fetchUsersInLocal()
        val userFetchOutcome: PublishSubject<Outcome<User>>
        val userListFetchOutcome: PublishSubject<Outcome<List<User>>>
        fun fetchUsersInRemote()

        fun handleError(error: Throwable)
        fun fetchUsersInLocalWithKeyword(searchText: String)
        fun updateNoteInLocalWithId(id: Int, text: String)
    }

    interface Local {
//        fun getUsersInLocal(): Single<List<User>>
        fun addUsers(users: List<User>)
        fun addUser(user: User)
        fun getUsersInLocalWithKeyword(searchText: String): Single<List<User>>
        fun updateNoteInLocal(id: Int, note: String)
    }

    interface Remote {
        fun getUsersInRemote(): Single<List<User>>
        fun getUserByIdInRemote(name: String?): Single<User>
    }
}