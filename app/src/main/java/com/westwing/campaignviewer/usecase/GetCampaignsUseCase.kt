package com.westwing.campaignviewer.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.westwing.campaignviewer.presentation.viewstate.CampaignViewState
import com.westwing.campaignviewer.repository.campaign.CampaignRepository
import com.westwing.campaignviewer.repository.campaign.CampaignRepositoryModel
import io.reactivex.Flowable
import javax.inject.Inject

class GetCampaignsUseCase @Inject constructor(
    private val campaignRepository: CampaignRepository
) {

    private val campaignViewStateStream: Flowable<CampaignViewState>
        get() = campaignRepository.campaignsStream()
            .map { model ->
                return@map when (model) {
                    is CampaignRepositoryModel.Data -> CampaignViewState.Content(model.campaignModelList)
                    CampaignRepositoryModel.NotPresent -> CampaignViewState.Loading
                }
            }
            .onErrorReturnItem(CampaignViewState.Error)


    val campaignViewStateLiveData: LiveData<CampaignViewState>
        get() = LiveDataReactiveStreams.fromPublisher(campaignViewStateStream)


    fun execute() {
        campaignRepository.fetchCampaigns()
    }
}