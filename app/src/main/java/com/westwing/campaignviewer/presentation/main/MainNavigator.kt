package com.westwing.campaignviewer.presentation.main

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.westwing.campaignviewer.R
import com.westwing.campaignviewer.presentation.detail.CampaignFragment

// TODO: use android Navigation framework
class MainNavigator(private val fragmentActivity: FragmentActivity) {

    fun goToDetailScreenFromCampaign(campaignTitle: String) {
        fragmentActivity.supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.container, CampaignFragment.newInstance()
                    .apply {
                        arguments = Bundle().apply {
                            putString(DETAIL_SCREEN_CAMPAIGN_KEY, campaignTitle)
                        }
                    })
            .commit()
    }

    companion object {
        const val DETAIL_SCREEN_CAMPAIGN_KEY = "DETAIL_SCREEN_CAMPAIGN_KEY"
    }
}