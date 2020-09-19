package com.westwing.campaignviewer.presentation.viewstate

import com.westwing.domain.CampaignModel

sealed class CampaignViewState {

    object Loading: CampaignViewState()

    data class Content(val campaignModelList: List<CampaignModel>) : CampaignViewState()

    object Error: CampaignViewState()
}