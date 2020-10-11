package com.ts.tawkexam.utils

import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.ts.tawkexam.Outcome
import com.ts.tawkexam.ui.user_list.UserListAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

fun ViewGroup.inflater(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun <T> PublishSubject<T>.toLiveData(compositeDisposable: CompositeDisposable): LiveData<T> {
    val data = MutableLiveData<T>()
    compositeDisposable.add(this.subscribe({ t: T -> data.value = t }))
    return data
}

/**
 * Extension function to push a failed event with an exception to the observing outcome
 * */
fun <T> PublishSubject<Outcome<T>>.failed(e: Throwable) {
    with(this) {
        loading(false)
        onNext(Outcome.failure(e))
    }
}

/**
 * Extension function to push  a success event with data to the observing outcome
 * */
fun <T> PublishSubject<Outcome<T>>.success(t: T) {
    with(this) {
        loading(false)
        onNext(Outcome.success(t))
    }

}
/**
 * Extension function to push the loading status to the observing outcome
 * */
fun <T> PublishSubject<Outcome<T>>.loading(isLoading: Boolean) {
    this.onNext(Outcome.loading(isLoading))


}