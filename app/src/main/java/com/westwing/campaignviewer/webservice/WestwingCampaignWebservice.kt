package com.westwing.campaignviewer.webservice

import com.westwing.campaignviewer.webservice.models.CampaignDataModel
import io.reactivex.Single
import retrofit2.http.GET

interface WestwingCampaignWebservice {

    @GET("cms/test/data.json")
    fun fetchCampaigns(): Single<CampaignDataModel>
}
