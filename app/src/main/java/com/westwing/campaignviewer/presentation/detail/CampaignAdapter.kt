package com.westwing.campaignviewer.presentation.detail

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.westwing.campaignviewer.R
import com.westwing.campaignviewer.presentation.view.createCircularProgressDrawable
import com.westwing.campaignviewer.utility.inflateWithoutAttach
import com.westwing.domain.CampaignModel
import kotlinx.android.synthetic.main.campaign_fragment_item.view.*

class CampaignAdapter(
    private val context: Context
) : RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder>() {

    private val items: MutableList<CampaignModel> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder =
        CampaignViewHolder(parent.inflateWithoutAttach(R.layout.campaign_fragment_item))

    override fun onBindViewHolder(holder: CampaignViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun update(campaignModelList: List<CampaignModel>) {
        items.clear()
        items.addAll(campaignModelList)
        notifyDataSetChanged()
    }

    inner class CampaignViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(campaignModel: CampaignModel) {
            itemView.apply {
                campaignTitleTextView.text = campaignModel.title
                campaignDescriptionTextView.text = campaignModel.description
                Glide.with(this@CampaignAdapter.context)
                    .load(campaignModel.imageUrl)
                    .placeholder(createCircularProgressDrawable(context.applicationContext))
                    .error(R.drawable.ic_no_network)
                    .into(campaignImageView)
            }
        }
    }
}