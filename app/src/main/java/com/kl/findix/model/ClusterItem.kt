package com.kl.findix.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class ClusterItem(
    val latLng: LatLng = LatLng(0.0, 0.0),
    val mainText: String = "", // Titleだとエラー出る
    val subText: String = "" // Mainに対してSubにした
) : ClusterItem {

    override fun getSnippet(): String {
        return subText
    }

    override fun getTitle(): String {
        return mainText
    }

    override fun getPosition(): LatLng {
        return latLng
    }

}