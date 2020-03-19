package com.kl.findix

import android.app.Activity
import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.kl.findix.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class Application: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.factory().create(this).inject(this)
        MobileAds.initialize(this, getString(R.string.admob_app_id))
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
}