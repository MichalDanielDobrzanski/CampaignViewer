package com.westwing.campaignviewer.presentation.main

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.westwing.campaignviewer.R
import com.westwing.campaignviewer.presentation.view.createCircularProgressDrawable
import com.westwing.campaignviewer.utility.inflateWithoutAttach
import com.westwing.domain.CampaignModel
import kotlinx.android.synthetic.main.main_campaign_item.view.*


class MainCampaignAdapter(
    private val context: Context,
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
                Glide.with(this@MainCampaignAdapter.context)
                    .load(campaignModel.imageUrl)
                    .placeholder(createCircularProgressDrawable(context.applicationContext))
                    .error(R.drawable.ic_no_network)
                    .into(campaignImageView)
            }
        }
    }
}