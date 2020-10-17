package com.gkgio.videoplayer.presentation.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

inline fun <reified VM : ViewModel> Fragment.createViewModel(crossinline viewModelProducer: () -> VM): VM {
    val factory = object : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>) = viewModelProducer() as VM
    }
    val viewModelProvider = ViewModelProvider(this, factory)
    return viewModelProvider[VM::class.java]
}

inline fun <reified T : ViewModel> AppCompatActivity.createViewModel(crossinline factory: () -> T): T =
    T::class.java.let { classT ->
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass == classT) {
                    @Suppress("UNCHECKED_CAST")
                    return factory() as T
                }
                throw IllegalArgumentException("Unexpected argument: $modelClass")
            }
        }).get(classT)
    }