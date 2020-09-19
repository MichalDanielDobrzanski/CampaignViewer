package com.westwing.campaignviewer.presentation.detail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.westwing.campaignviewer.R
import com.westwing.campaignviewer.utility.inflateWithoutAttach
import com.westwing.domain.CampaignModel

class CampaignAdapter(
) : RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder>() {

    private val items: MutableList<CampaignModel> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder =
        CampaignViewHolder(parent.inflateWithoutAttach(R.layout.campaign_fragment_item))

    override fun onBindViewHolder(holder: CampaignViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setCurrentCampaign(campaignTitle: String) {
    }

    fun update(campaignModelList: List<CampaignModel>) {
        items.clear()
        items.addAll(campaignModelList)
        notifyDataSetChanged()
    }

    inner class CampaignViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(campaignModel: CampaignModel) {

        }
    }
}