package com.westwing.campaignviewer.repository.campaign

import com.westwing.domain.CampaignModel

sealed class CampaignRepositoryModel {
    data class Data(val campaignModelList: List<CampaignModel>) : CampaignRepositoryModel()

    object NotPresent : CampaignRepositoryModel()
}