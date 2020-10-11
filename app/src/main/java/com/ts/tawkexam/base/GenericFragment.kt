package com.ts.tawkexam.base

import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class GenericFragment : DaggerFragment() {

    private lateinit var internetChecker: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Used ReactiveNetwork to check network connectivity
        internetChecker = ReactiveNetwork
            .observeNetworkConnectivity(activity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { connectivity: Connectivity ->
                if (connectivity.state() == NetworkInfo.State.CONNECTED) {
                    onInternetConnect()
                } else {
                    onInternetDisconnect()
                }
            }
    }

    abstract fun onInternetConnect()
    abstract fun onInternetDisconnect()

    override fun onDestroy() {
        super.onDestroy()
        internetChecker.dispose()
    }

}