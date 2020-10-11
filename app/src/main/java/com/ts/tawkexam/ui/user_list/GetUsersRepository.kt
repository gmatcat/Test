package com.ts.tawkexam.ui.user_list

import androidx.paging.PagingData
import com.ts.tawkexam.data_source.model.User
import io.reactivex.Flowable

interface GetUsersRepository {
    fun getAllUsers(): Flowable<PagingData<User>>
}
