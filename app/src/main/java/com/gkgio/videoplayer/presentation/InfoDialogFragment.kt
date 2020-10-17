package com.gkgio.videoplayer.presentation

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.gkgio.videoplayer.R
import com.gkgio.videoplayer.presentation.ext.setDebounceOnClickListener
import kotlinx.android.synthetic.main.view_dialog.*

class InfoDialogFragment : DialogFragment() {

    companion object {
        val TAG = InfoDialogFragment::class.java.simpleName

        const val ARG_PARENT_FRAGMENT_TAG = "PARENT_FRAGMENT_TAG"
        const val ARG_TITLE: String = "TITLE"
        const val ARG_BODY: String = "BODY"
        const val ARG_BUTTON_RIGHT_TEXT = "BUTTON_RIGHT_TEXT"
        const val ARG_BUTTON_LEFT_TEXT = "BUTTON_LEFT_TEXT"
        const val ARG_ICON_RES_ID = "ICON_RES_ID"

        fun getInstance(
            fragmentTag: String,
            fragmentManager: FragmentManager,
            body: String,
            buttonRightText: String,
            iconRes: Int = 0,
            title: String? = null,
            buttonLeftText: String? = null
        ): InfoDialogFragment {
            var dialogFragment =
                fragmentManager.findFragmentByTag(TAG) as? InfoDialogFragment
            if (dialogFragment == null) {
                dialogFragment = InfoDialogFragment()
            }

            dialogFragment.arguments = Bundle().apply {
                putString(ARG_PARENT_FRAGMENT_TAG, fragmentTag)
                putString(ARG_TITLE, title)
                putString(ARG_BODY, body)
                putString(ARG_BUTTON_RIGHT_TEXT, buttonRightText)
                putString(ARG_BUTTON_LEFT_TEXT, buttonLeftText)
                putInt(ARG_ICON_RES_ID, iconRes)
            }
            return dialogFragment
        }
    }

    private lateinit var parentFragmentTag: String
    private var title: String? = null
    private lateinit var body: String
    private lateinit var buttonRightText: String
    private var buttonLeftText: String? = null
    private var iconRes: Int = 0

    private var listener: ClickDialogCallBack? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is ClickDialogCallBack) {
            listener = parentFragment as ClickDialogCallBack
        } else if (context is ClickDialogCallBack) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            parentFragmentTag = it.getString(ARG_PARENT_FRAGMENT_TAG, "")
            title = it.getString(ARG_TITLE)
            body = it.getString(ARG_BODY, "")
            buttonRightText = it.getString(ARG_BUTTON_RIGHT_TEXT, "")
            buttonLeftText = it.getString(ARG_BUTTON_LEFT_TEXT)
            iconRes = it.getInt(ARG_ICON_RES_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.view_dialog, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        titleTv.isVisible = title != null
        titleTv.text = title
        bodyTv.text = body
        leftBtnText.text = buttonLeftText
        leftBtnText.isVisible = buttonLeftText != null
        rightBtn.text = buttonRightText
        if (iconRes != 0) {
            icon.isVisible = true
            icon.setImageDrawable(ContextCompat.getDrawable(view.context, iconRes))
        } else {
            icon.isVisible = false
        }

        leftBtnText.setDebounceOnClickListener {
            listener?.onLeftButtonClick(parentFragmentTag)
            dismiss()
        }

        rightBtn.setDebounceOnClickListener {
            listener?.onRightButtonClick(parentFragmentTag)
            dismiss()
        }
    }
}

interface ClickDialogCallBack {
    fun onLeftButtonClick(fragmentTag: String) {/* empty */
    }

    fun onRightButtonClick(fragmentTag: String) {/* empty */
    }
}