package com.ts.tawkexam.ui.user_list

import com.ts.tawkexam.data_source.model.User

interface UserListCallback {
    fun onClickUser(id: User, inverted: Boolean) {

    }

}
