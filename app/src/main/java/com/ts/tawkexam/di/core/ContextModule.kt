package com.ts.tawkexam.di.core

import android.app.Application
import android.content.Context
import com.ts.tawkexam.AppScheduler
import com.ts.tawkexam.Scheduler
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal abstract class ContextModule {
    @Binds // @Binds, binds the Application instance to Context
    abstract fun bindContext(application: Application?): Context?

}
