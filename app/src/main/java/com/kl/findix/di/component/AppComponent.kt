package com.kl.findix.di.component

import com.kl.findix.Application
import com.kl.findix.di.module.BuildersModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(
    modules = [
        AndroidInjectionModule::class,
        BuildersModule::class
    ]
)
interface AppComponent: AndroidInjector<Application> {

    override fun inject(app: Application)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            app: Application
        ): AppComponent
    }
}