package com.westwing.campaignviewer.repository.campaign

import com.westwing.campaignviewer.schedulers.SchedulersProvider
import com.westwing.domain.CampaignModel
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.processors.BehaviorProcessor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface CampaignRepository {

    fun fetchCampaigns()

    fun campaignsStream(): Flowable<CampaignRepositoryModel>
}

class CampaignRepositoryImpl @Inject constructor(
    private val schedulersProvider: SchedulersProvider
) : CampaignRepository {

    private val campaignsSubject: BehaviorProcessor<CampaignRepositoryModel> =
        BehaviorProcessor.createDefault<CampaignRepositoryModel>(CampaignRepositoryModel.NotPresent)


    override fun fetchCampaigns() {
        Single.timer(1000L, TimeUnit.MILLISECONDS).subscribe(Consumer {
            campaignsSubject.onNext(
                CampaignRepositoryModel.Data(
                    listOf(
                        CampaignModel("xD", "my long campaign title", "someinvalidurl"),
                        CampaignModel("xDD", "my second long campaign title", "someinvalidurl"),
                        CampaignModel("xDDD", "my third long campaign title", "someinvalidurl")
                    )
                )
            )
        })
    }

    override fun campaignsStream(): Flowable<CampaignRepositoryModel> =
        campaignsSubject.onBackpressureLatest()
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
}
