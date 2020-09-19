package com.westwing.campaignviewer.presentation.detail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import kotlin.math.max


@AndroidEntryPoint
class CampaignFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private val campaignAdapter: CampaignAdapter by lazy {
        CampaignAdapter(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.campaign_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        campaignViewPager.adapter = campaignAdapter

        toolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            inflateMenu(R.menu.campaign_fragment_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.callSupportMenuItem -> {
                        dialCustomerSupport()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun dialCustomerSupport() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED -> {
                startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.fromParts("tel", WESTWING_CUSTOMER_SUPPORT_NUMBER, null)
                    )
                )
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) -> {
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)

            }
            else -> {
                CampaignMissingCallPermissionDialog().show(
                    requireActivity().supportFragmentManager,
                    CampaignMissingCallPermissionDialog::class.java.simpleName
                )
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.observeCampaigns().observe(viewLifecycleOwner, Observer {
            renderState(it)
        })
    }

    private fun renderState(viewState: CampaignViewState) = when (viewState) {
        is CampaignViewState.Content -> {
            renderContent(viewState.campaignModelList)
            campaignViewPager.currentItem =
                extractCurrentCampaignIndexByTitle(viewState.campaignModelList)
        }
        else -> {
            // no-op
        }
    }

    private fun extractCurrentCampaignIndexByTitle(campaignModelList: List<CampaignModel>): Int =
        arguments?.getString(DETAIL_SCREEN_CAMPAIGN_KEY)
            ?.let { title ->
                max(FALLBACK_INDEX, campaignModelList.indexOfFirst { it.title == title })
            } ?: FALLBACK_INDEX


    private fun renderContent(campaignModelList: List<CampaignModel>) {
        campaignAdapter.update(campaignModelList)
    }

    companion object {
        fun newInstance() = CampaignFragment()
    }
}

private const val FALLBACK_INDEX = 0

private const val WESTWING_CUSTOMER_SUPPORT_NUMBER = "+49 89 412 072 72"
private const val REQUEST_PHONE_CALL = 1