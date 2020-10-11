package com.ts.tawkexam.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.ts.tawkexam.Outcome
import com.ts.tawkexam.data_source.model.User
import com.ts.tawkexam.ui.user_list.GetUsersRepository
import com.ts.tawkexam.ui.user_list.data.UserDataContract
import com.ts.tawkexam.utils.toLiveData
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class UserViewModel(
    private val repo: GetUsersRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    fun getAllUsers(): Flowable<PagingData<User>> {
        return repo
            .getAllUsers()
            .cachedIn(viewModelScope)
    }
//
//    val userOutcome: LiveData<Outcome<User>> by lazy {
//        //Convert publish subject to livedata
//        repo.userFetchOutcome.toLiveData(compositeDisposable)
//    }
//
//    val userListOutcome: LiveData<Outcome<List<User>>> by lazy {
//        //Convert publish subject to livedata
//        repo.userListFetchOutcome.toLiveData(compositeDisposable)
//    }
//
//    //
//    fun getUsers() {
//        repo.fetchUsersInLocal()
//    }
//
//
//    fun searchUserWithKeyword(searchText: String) {
//        repo.fetchUsersInLocalWithKeyword(searchText)
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        compositeDisposable.clear()
//    }
//
//    fun updateNote(id: Int, text: String) {
//        repo.updateNoteInLocalWithId(id, text)
//    }
}