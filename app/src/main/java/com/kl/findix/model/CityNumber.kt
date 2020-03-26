package com.kl.findix.model

import androidx.annotation.Keep
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

@Keep
class CityNumber : BaseObservable() { // Spinnerで選択中のテキスト取得するためのクラス

    @Bindable
    var number: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.cityNumber)
        }

}