package com.ts.tawkexam.di.core

import com.ts.tawkexam.AppScheduler
import com.ts.tawkexam.Scheduler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule {

    @Provides
    fun scheduler(): Scheduler {
        return AppScheduler()
    }
}