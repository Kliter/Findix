package com.kl.findix.di.component

import com.kl.findix.Application
import com.kl.findix.di.module.ActivityBindingModule
import com.kl.findix.di.module.AppModule
import com.kl.findix.di.module.DelegateModule
import com.kl.findix.di.module.ServiceModule
import com.kl.findix.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        ViewModelModule::class,
        AppModule::class,
        ServiceModule::class,
        DelegateModule::class
    ]
)
interface AppComponent : AndroidInjector<Application> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}