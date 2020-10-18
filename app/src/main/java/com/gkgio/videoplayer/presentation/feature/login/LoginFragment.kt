package com.gkgio.videoplayer.presentation.feature.login

import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.gkgio.videoplayer.R
import com.gkgio.videoplayer.di.AppInjector
import com.gkgio.videoplayer.presentation.DialogUtils
import com.gkgio.videoplayer.presentation.base.BaseFragment
import com.gkgio.videoplayer.presentation.ext.*
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment<LoginViewModel>(R.layout.fragment_login) {
    companion object {
        private const val ACCEPTED_SYMBOLS = "1234567890+-() "
        private const val PHONE_FORMAT = "+7 ([000]) [000]-[00]-[00]"
        private const val RU_CODE = "+7"
    }

    var inputPhone: String? = null

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.loginViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initPhoneView()

        viewModel.errorEvent.observe(viewLifecycleOwner, {
            DialogUtils.showError(view, it)
        })

        viewModel.pushErrorEvent.observe(viewLifecycleOwner, {
            DialogUtils.showDialog(
                "dssd",
                childFragmentManager,
                "Для работы приложение требуется добавление Google account в телефоне, это можно сделать через настройки или в приложении Play Market",
                "Понятно"
            )
        })

        viewModel.progress.observe(viewLifecycleOwner, {
            progressView.isVisible = it
        })

        sendDataBtn.setDebounceOnClickListener {
            val isCarNumberValid = validateEditText(carNumberEditText)
            if (isCarNumberValid && inputPhone!=null)  {
                viewModel.login(inputPhone!!, carNumberEditText.text.toString())
            }
        }
    }

    private fun initPhoneView() {
        phoneEditText.inputType = InputType.TYPE_CLASS_NUMBER
        phoneEditText.keyListener = DigitsKeyListener.getInstance(ACCEPTED_SYMBOLS)

        MaskedTextChangedListener.installOn(
            phoneEditText,
            PHONE_FORMAT,
            listOf(PHONE_FORMAT),
            AffinityCalculationStrategy.WHOLE_STRING,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {
                    if (maskFilled) {
                        sendDataBtn.backgroundTintList =
                            ContextCompat.getColorStateList(requireContext(), R.color.btn_color)
                        sendDataBtn.isClickable = true
                        inputPhone = RU_CODE + extractedValue
                    } else {
                        sendDataBtn.backgroundTintList =
                            ContextCompat.getColorStateList(requireContext(), R.color.btn_color_40)
                        sendDataBtn.isClickable = false
                        inputPhone = null
                    }
                }
            }
        )

        phoneEditText.requestFocus()
        openKeyBoard()
    }

    override fun onStop() {
        super.onStop()
        closeKeyboard(phoneEditText)
    }

    private fun validateEditText(editText: EditText): Boolean = with(editText) {
        getNotBlankTextOrNull()?.let {
            setDrawableResRightIcon(null)
            return true
        }
        setDrawableResRightIcon(R.drawable.ic_edit_error)
        return false
    }

    private fun EditText.getNotBlankTextOrNull(): String? =
        if (!text.toString().isBlank()) text.toString() else null
}