package com.muhammadwahyudin.kadefootballapp.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    protected val compDisp by lazy { CompositeDisposable() }

    override fun onCleared() {
        compDisp.clear()
        super.onCleared()
    }
}