package com.westwing.campaignviewer.presentation.application.di

import com.westwing.campaignviewer.repository.campaign.CampaignRepositoryImpl
import com.westwing.campaignviewer.schedulers.AndroidSchedulersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCampaignRepositoryImpl(schedulersProvider: AndroidSchedulersProvider): CampaignRepositoryImpl =
        CampaignRepositoryImpl(schedulersProvider)

    @Provides
    @Singleton
    fun provideAndroidSchedulersProvider(): AndroidSchedulersProvider = AndroidSchedulersProvider()
}