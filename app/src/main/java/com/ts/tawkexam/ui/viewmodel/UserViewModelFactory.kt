package com.ts.tawkexam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ts.tawkexam.ui.user_list.GetUsersRepository
import com.ts.tawkexam.ui.user_list.data.UserDataContract
import io.reactivex.disposables.CompositeDisposable

class UserViewModelFactory(private val repository: GetUsersRepository, private val compositeDisposable: CompositeDisposable) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(repository, compositeDisposable) as T
    }
}