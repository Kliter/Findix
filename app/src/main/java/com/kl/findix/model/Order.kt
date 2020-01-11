package com.kl.findix.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import java.util.*

class Order(): BaseObservable() {

    constructor(
        userId: String? = "",
        title: String? = "",
        description: String? = "",
        city: String? = "",
        userLocation: UserLocation? = null,
        photoUrl: String? = "",
        shouldRegisterLocation: Boolean = false,
        timeStamp: Date? = null
    ): this() {
        this.userId = userId
        this.title = title
        this.description = description
        this.city = city
        this.userLocation = userLocation
        this.photoUrl = photoUrl
        this.shouldRegisterLocation = shouldRegisterLocation
        this.timeStamp = timeStamp
    }

    var orderId: String = ""

    @Bindable
    var userId: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.userId)
        }

    @Bindable
    var title: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @Bindable
    var description: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.description)
        }

    @Bindable
    var city: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.city)
        }

    @Bindable
    var userLocation: UserLocation? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.userLocation)
        }

    @Bindable
    var photoUrl: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.photoUrl)
        }

    @Bindable
    var shouldRegisterLocation: Boolean? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.shouldRegisterLocation)
        }

    @Bindable
    var timeStamp: Date? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.timeStamp)
        }
}