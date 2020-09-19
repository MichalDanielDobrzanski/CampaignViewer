package com.westwing.campaignviewer.presentation.application.di

import com.westwing.campaignviewer.repository.campaign.CampaignRepository
import com.westwing.campaignviewer.repository.campaign.CampaignRepositoryImpl
import com.westwing.campaignviewer.schedulers.AndroidSchedulersProvider
import com.westwing.campaignviewer.schedulers.SchedulersProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppBindingModule {

    @Binds
    abstract fun bindCampaignRepository(campaignRepositoryImpl: CampaignRepositoryImpl): CampaignRepository

    @Binds
    abstract fun bindSchedulersProvider(androidSchedulersProvider: AndroidSchedulersProvider): SchedulersProvider
}