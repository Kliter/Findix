package com.kl.findix.di.module.profile

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kl.findix.presentation.profile.ProfileBottomSheetDialog
import dagger.Binds
import dagger.Module

@Module
abstract class ProfileBottomSheetDialogModule {

    @Binds
    abstract fun bindsFragment(fragment: ProfileBottomSheetDialog): BottomSheetDialogFragment

}