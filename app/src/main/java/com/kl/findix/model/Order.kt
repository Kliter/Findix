package com.kl.findix.model

import java.util.*

data class Order(
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val photoUrl: String = "",
    var timeStamp: Date? = null
)