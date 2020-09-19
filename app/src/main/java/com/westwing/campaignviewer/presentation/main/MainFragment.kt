package com.westwing.campaignviewer.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.westwing.campaignviewer.R
import com.westwing.campaignviewer.presentation.viewstate.CampaignViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var campaignViewState: CampaignViewState? = null

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.observeCampaigns().observe(viewLifecycleOwner, Observer {
            Log.d("Me", "viewstate: $it")
            renderState(it)
            campaignViewState = it
        })
    }

    private fun renderState(viewState: CampaignViewState) = when (viewState) {
        CampaignViewState.Loading -> {
            renderLoading()
        }
        is CampaignViewState.Content -> {
            renderContent()
        }
        CampaignViewState.Error -> {
            renderError()
        }
    }

    private fun renderLoading() {

    }

    private fun renderContent() {

    }

    private fun renderError() {

    }

    companion object {
        fun newInstance() = MainFragment()
    }
}