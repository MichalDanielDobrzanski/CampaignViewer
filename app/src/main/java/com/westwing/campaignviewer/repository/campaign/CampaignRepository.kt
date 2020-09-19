package com.westwing.campaignviewer.repository.campaign

import com.westwing.campaignviewer.schedulers.SchedulersProvider
import com.westwing.campaignviewer.webservice.WestwingCampaignWebservice
import com.westwing.campaignviewer.webservice.models.CampaignDataModel
import com.westwing.domain.CampaignModel
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.processors.BehaviorProcessor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface CampaignRepository {

    fun fetchCampaigns()

    fun campaignsStream(): Flowable<CampaignRepositoryModel>
}

class CampaignRepositoryImpl @Inject constructor(
    private val westwingCampaignWebservice: WestwingCampaignWebservice,
    private val schedulersProvider: SchedulersProvider
) : CampaignRepository {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val campaignsSubject: BehaviorProcessor<CampaignRepositoryModel> =
        BehaviorProcessor.createDefault<CampaignRepositoryModel>(CampaignRepositoryModel.NotPresent)


    override fun fetchCampaigns() {
        compositeDisposable.add(
            westwingCampaignWebservice.fetchCampaigns()
                .map<CampaignRepositoryModel> { toDataCampaignRepositoryModel(it) }
                .onErrorReturnItem(CampaignRepositoryModel.Error)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
                .subscribe(Consumer { campaignsSubject.onNext(it) })

        )
    }

    private fun toDataCampaignRepositoryModel(campaignDataModel: CampaignDataModel): CampaignRepositoryModel.Data =
        CampaignRepositoryModel.Data(
            campaignDataModel.metadata.data.map {
                CampaignModel(it.name, it.description, it.image.url)
            }
        )

    override fun campaignsStream(): Flowable<CampaignRepositoryModel> =
        campaignsSubject.onBackpressureLatest()
            .observeOn(schedulersProvider.ui())
}
