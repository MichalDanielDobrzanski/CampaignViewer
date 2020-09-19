package com.westwing.campaignviewer.presentation.main

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.westwing.campaignviewer.R
import com.westwing.campaignviewer.utility.inflateWithoutAttach
import com.westwing.domain.CampaignModel
import kotlinx.android.synthetic.main.main_campaign_item.view.*

class MainCampaignAdapter(
    private val onCampaignClickAtPosition: (campaignTitle: String) -> Unit
) : RecyclerView.Adapter<MainCampaignAdapter.MainCampaignViewHolder>() {

    private val items: MutableList<CampaignModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCampaignViewHolder =
        MainCampaignViewHolder(
            parent.inflateWithoutAttach(R.layout.main_campaign_item)
        )

    override fun onBindViewHolder(holder: MainCampaignViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun update(campaignModelList: List<CampaignModel>) {
        items.clear()
        items.addAll(campaignModelList)
        notifyDataSetChanged()
    }

    inner class MainCampaignViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(campaignModel: CampaignModel) {
            itemView.apply {
                campaignNameTextView.text = campaignModel.title
                setOnClickListener {
                    onCampaignClickAtPosition(campaignModel.title)
                }
                Glide.with(this.context)
                    .load("https://moodle.htwchur.ch/pluginfile.php/124614/mod_page/content/4/example.jpg")
                    .placeholder(R.drawable.ic_loader)
                    .into(campaignImageView)
            }
        }
    }
}