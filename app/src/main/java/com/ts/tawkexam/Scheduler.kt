package com.ts.tawkexam

import io.reactivex.Scheduler

/**
 *  Interface to mock different threads during testing.
* */
interface Scheduler {
    fun mainThread():Scheduler
    fun io():Scheduler
}