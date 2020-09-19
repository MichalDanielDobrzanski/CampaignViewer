package com.westwing.campaignviewer.presentation.detail

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.westwing.campaignviewer.R

class CampaignMissingCallPermissionDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .apply {
                    setMessage(R.string.need_call_permission_rationale)
                        .setPositiveButton(
                            R.string.cancel_text
                        ) { dialog, _ -> dialog.dismiss() }
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}