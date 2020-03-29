package com.kl.findix.di.module

import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.kl.findix.R
import com.kl.findix.presentation.MainActivity
import dagger.Module
import dagger.Provides

@Module
class NavModule {

    @Provides
    fun provideNavController(mainActivity: MainActivity): NavController {
        return Navigation.findNavController(mainActivity, R.id.nav_host_fragment)
    }
}