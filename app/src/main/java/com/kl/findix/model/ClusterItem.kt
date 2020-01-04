package com.kl.findix.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class ClusterItem(
    var mPosition: LatLng = LatLng(0.0, 0.0),
    var mainText: String = "", // Titleだとエラー出る
    var subText: String = "" // Mainに対してSubにした
) : ClusterItem {

    override fun getSnippet(): String {
        return subText
    }

    override fun getTitle(): String {
        return mainText
    }

    override fun getPosition(): LatLng {
        return mPosition
    }
}