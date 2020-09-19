package com.westwing.campaignviewer.presentation.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.westwing.campaignviewer.presentation.viewstate.CampaignViewState
import com.westwing.campaignviewer.usecase.GetCampaignsUseCase

class MainViewModel @ViewModelInject constructor(
    private val getCampaignsUseCase: GetCampaignsUseCase
) : ViewModel() {

    fun fetchCampaigns() = getCampaignsUseCase.execute()

    fun observeCampaigns(): LiveData<CampaignViewState> = getCampaignsUseCase.campaignViewStateLiveData

}