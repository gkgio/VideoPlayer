package com.gkgio.videoplayer.presentation.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.gkgio.videoplayer.di.AppInjector
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<VM : BaseViewModel>(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    protected lateinit var viewModel: VM
    protected var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        AppInjector.appComponent.inject(this as BaseFragment<BaseViewModel>)

        viewModel = provideViewModel()
    }

    protected fun showDialog(dialog: DialogFragment, tag: String) {
        if (isDetached || childFragmentManager.findFragmentByTag(tag)?.isAdded == true) {
            return
        }

        dialog.show(childFragmentManager, tag)
    }

    abstract fun provideViewModel(): VM

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}