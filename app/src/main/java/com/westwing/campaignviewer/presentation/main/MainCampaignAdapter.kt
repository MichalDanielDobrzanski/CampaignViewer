package com.westwing.campaignviewer.presentation.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.westwing.campaignviewer.R
import com.westwing.campaignviewer.utility.inflateWithoutAttach
import com.westwing.domain.CampaignModel

class MainCampaignAdapter(
    private val onCampaignClickAtPosition: (campaignTitle: String) -> Unit
) : RecyclerView.Adapter<MainCampaignAdapter.MainCampaignViewHolder>() {

    private val items: MutableList<CampaignModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCampaignViewHolder =
        MainCampaignViewHolder(
            parent.inflateWithoutAttach(R.layout.main_campaign_item),
            onCampaignClickAtPosition
        )

    override fun onBindViewHolder(holder: MainCampaignViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = items.size

    inner class MainCampaignViewHolder(
        view: View,
        onCampaignClickAtPosition: (campaignTitle: String) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            itemView.apply {
                // TODO: item clicks + layout
            }
        }
    }
}