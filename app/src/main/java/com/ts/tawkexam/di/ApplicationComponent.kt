package com.ts.tawkexam.di

import android.app.Application
import android.content.Context
import com.ts.tawkexam.ApplicationClass
import com.ts.tawkexam.di.core.ContextModule
import com.ts.tawkexam.di.core.CoreModule
import com.ts.tawkexam.di.core.NetworkModule
import com.ts.tawkexam.di.core.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Component(
    modules = [AndroidInjectionModule::class,
        NetworkModule::class,
        ContextModule::class,
        RepositoryModule::class,
        BindingModule::class,
        UserListApiModule::class,
        CoreModule::class]
)
interface ApplicationComponent : AndroidInjector<ApplicationClass> {
    @Component.Builder
    interface Builder {


        @BindsInstance
        fun application(application: Application): ApplicationComponent.Builder

        fun build(): ApplicationComponent
    }
}