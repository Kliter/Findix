package com.kl.findix.di.component

import com.kl.findix.Application
import com.kl.findix.di.module.AppModule
import com.kl.findix.di.module.BuildersModule
import com.kl.findix.di.module.ServiceModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        BuildersModule::class,
        AppModule::class,
        ServiceModule::class
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