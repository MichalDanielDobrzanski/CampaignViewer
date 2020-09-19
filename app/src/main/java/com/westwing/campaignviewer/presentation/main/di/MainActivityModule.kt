package com.westwing.campaignviewer.presentation.main.di

import androidx.fragment.app.FragmentActivity
import com.westwing.campaignviewer.presentation.main.MainNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {

    @Provides
    @ActivityScoped
    fun provideMainNavigator(fragmentActivity: FragmentActivity): MainNavigator =
        MainNavigator(fragmentActivity)
}