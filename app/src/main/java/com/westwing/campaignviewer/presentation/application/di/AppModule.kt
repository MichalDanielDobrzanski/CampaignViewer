package com.westwing.campaignviewer.presentation.application.di

import com.google.gson.Gson
import com.westwing.campaignviewer.repository.campaign.CampaignRepository
import com.westwing.campaignviewer.repository.campaign.CampaignRepositoryImpl
import com.westwing.campaignviewer.schedulers.AndroidSchedulersProvider
import com.westwing.campaignviewer.usecase.GetCampaignsUseCase
import com.westwing.campaignviewer.webservice.WestwingCampaignWebservice
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCampaignRepositoryImpl(
        westwingCampaignWebservice: WestwingCampaignWebservice,
        schedulersProvider: AndroidSchedulersProvider
    ): CampaignRepositoryImpl =
        CampaignRepositoryImpl(westwingCampaignWebservice, schedulersProvider)

    @Provides
    @Singleton
    fun provideAndroidSchedulersProvider(): AndroidSchedulersProvider = AndroidSchedulersProvider()

    @Provides
    @Singleton
    fun provideGetCampaignsUseCase(campaignRepository: CampaignRepository) =
        GetCampaignsUseCase(campaignRepository)

    @Provides
    @Singleton
    fun providesGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideWestwingCampaignWebservice(gson: Gson): WestwingCampaignWebservice =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://static.westwing.de/")
            .build()
            .create(WestwingCampaignWebservice::class.java)

}