package com.westwing.campaignviewer.presentation.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.westwing.campaignviewer.R
import com.westwing.campaignviewer.presentation.viewstate.CampaignViewState
import com.westwing.domain.CampaignModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_content_view.*
import kotlinx.android.synthetic.main.main_error_view.*
import kotlinx.android.synthetic.main.main_fragment_layout.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var campaignViewState: CampaignViewState? = null

    private lateinit var viewModel: MainViewModel

    private val mainCampaignAdapter: MainCampaignAdapter by lazy {
        MainCampaignAdapter(requireActivity()) {
            mainNavigator.goToDetailScreenFromCampaign(it)
        }
    }

    @Inject
    lateinit var mainNavigator: MainNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment_layout, container, false)

    override fun onConfigurationChanged(newConfig: Configuration) {
        campaignViewState
            ?.takeIf { it is CampaignViewState.Content }
            ?.let { adjustLayoutToOrientation(newConfig.orientation) }
        super.onConfigurationChanged(newConfig)
    }

    private fun adjustLayoutToOrientation(orientation: Int) = when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            campaignListRecyclerView.layoutManager =
                GridLayoutManager(requireContext(), COLUMN_COUNT_IN_LANDSCAPE)
        }
        Configuration.ORIENTATION_PORTRAIT -> {
            campaignListRecyclerView.layoutManager =
                LinearLayoutManager(requireContext())
        }
        else -> {
            // no-op
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.observeCampaigns().observe(viewLifecycleOwner, Observer {
            renderState(it)
            campaignViewState = it
        })
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false);
    }

    private fun renderState(viewState: CampaignViewState) = when (viewState) {
        CampaignViewState.Loading -> {
            renderLoading()
        }
        is CampaignViewState.Content -> {
            renderContent(viewState.campaignModelList)
        }
        CampaignViewState.Error -> {
            renderError()
        }
    }

    private fun renderLoading() {
        contentViewAnimator.displayedChild = LOADING_INDEX
    }

    private fun renderContent(campaignModelList: List<CampaignModel>) {
        contentViewAnimator.displayedChild = CONTENT_INDEX
        campaignListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = mainCampaignAdapter
        }
        adjustLayoutToOrientation(resources.configuration.orientation)
        mainCampaignAdapter.update(campaignModelList)
    }

    private fun renderError() {
        contentViewAnimator.displayedChild = ERROR_INDEX
        retryButton.setOnClickListener {
            renderLoading()
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
private const val COLUMN_COUNT_IN_LANDSCAPE = 2