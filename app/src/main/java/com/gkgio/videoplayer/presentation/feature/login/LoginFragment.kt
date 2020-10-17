package com.gkgio.videoplayer.presentation.feature.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.gkgio.videoplayer.R
import com.gkgio.videoplayer.di.AppInjector
import com.gkgio.videoplayer.presentation.DialogUtils
import com.gkgio.videoplayer.presentation.base.BaseFragment
import com.gkgio.videoplayer.presentation.ext.createViewModel

class LoginFragment : BaseFragment<LoginViewModel>(R.layout.fragment_login) {

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.loginViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.errorEvent.observe(viewLifecycleOwner, Observer {
            DialogUtils.showError(view, it)
        })
    }
}