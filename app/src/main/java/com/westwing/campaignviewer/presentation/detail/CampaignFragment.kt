package com.westwing.campaignviewer.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.westwing.campaignviewer.R
import com.westwing.campaignviewer.presentation.main.MainNavigator.Companion.DETAIL_SCREEN_CAMPAIGN_KEY
import com.westwing.campaignviewer.presentation.main.MainViewModel
import com.westwing.campaignviewer.presentation.viewstate.CampaignViewState
import com.westwing.domain.CampaignModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.campaign_fragment_layout.*

@AndroidEntryPoint
class CampaignFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private val campaignAdapter: CampaignAdapter = CampaignAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.campaign_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        campaignViewPager.adapter = campaignAdapter
        arguments?.getString(DETAIL_SCREEN_CAMPAIGN_KEY)?.let { campaignTitle ->
            campaignAdapter.setCurrentCampaign(campaignTitle)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.observeCampaigns().observe(viewLifecycleOwner, Observer {
            renderState(it)
        })
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true);
    }

    private fun renderState(viewState: CampaignViewState) = when (viewState) {
        CampaignViewState.Loading -> {

        }
        is CampaignViewState.Content -> {
            renderContent(viewState.campaignModelList)
        }
        CampaignViewState.Error -> {

        }
    }

    private fun renderContent(campaignModelList: List<CampaignModel>) {
        campaignAdapter.update(campaignModelList)
    }

    companion object {
        fun newInstance() = CampaignFragment()
    }
}