package com.westwing.campaignviewer.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.westwing.campaignviewer.R
import com.westwing.campaignviewer.presentation.viewstate.CampaignViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_content_view.*
import kotlinx.android.synthetic.main.main_error_view.*
import kotlinx.android.synthetic.main.main_fragment_layout.*

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var campaignViewState: CampaignViewState? = null

    private lateinit var viewModel: MainViewModel

    private val mainCampaignsAdapter: MainCam

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment_layout, container, false)

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
        contentViewAnimator.displayedChild = LOADING_INDEX
    }

    private fun renderContent() {
        contentViewAnimator.displayedChild = CONTENT_INDEX
        campaignListRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mainCampaignsAdapter
        }
        mainCampaignsAdapter.update(campaignModelList)
    }

    private fun renderError() {
        contentViewAnimator.displayedChild = ERROR_INDEX
        retryButton.setOnClickListener {
            viewModel.fetchCampaigns()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}

private const val LOADING_INDEX = 0
private const val CONTENT_INDEX = 1
private const val ERROR_INDEX = 2