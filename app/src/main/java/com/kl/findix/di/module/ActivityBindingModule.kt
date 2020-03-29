package com.kl.findix.di.module

import com.kl.findix.di.ActivityScoped
import com.kl.findix.presentation.MainActivity
import com.kl.findix.presentation.MainModule
import com.kl.findix.presentation.createorder.CreateOrderModule
import com.kl.findix.presentation.login.LoginModule
import com.kl.findix.presentation.map.MapsModule
import com.kl.findix.presentation.order.OrderModule
import com.kl.findix.presentation.orderdetail.OrderDetailModule
import com.kl.findix.presentation.profile.ProfileModule
import com.kl.findix.presentation.profiledetail.ProfileDetailModule
import com.kl.findix.presentation.profileedit.ProfileEditModule
import com.kl.findix.presentation.signup.SignUpModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module
 * ActivityBindingModule is on, in our case that will be [AppComponent]. You never
 * need to tell [AppComponent] that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that [AppComponent] exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the
 * specified modules and be aware of a scope annotation [@ActivityScoped].
 * When Dagger.Android annotation processor runs it will create 2 subcomponents for us.
 */
@Module
@Suppress("UNUSED")
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            // Activity
            MainModule::class,
            // Fragment
            CreateOrderModule::class,
            LoginModule::class,
            MapsModule::class,
            OrderModule::class,
            OrderDetailModule::class,
            ProfileModule::class,
            ProfileDetailModule::class,
            ProfileEditModule::class,
            SignUpModule::class,
            NavModule::class
        ]
    )
    abstract fun mainActivity(): MainActivity
}