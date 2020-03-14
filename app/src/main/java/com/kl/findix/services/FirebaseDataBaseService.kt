package com.kl.findix.services

import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.Order
import com.kl.findix.model.User
import com.kl.findix.model.UserLocation

interface FirebaseDataBaseService {
    suspend fun fetchOwnProfileInfo(
        firebaseUser: FirebaseUser,
        fetchOwnProfileInfoListener: (User) -> Unit
    )

    suspend fun updateProfileInfo(firebaseUser: FirebaseUser, user: User, profilePhotoUrl: String)
    suspend fun fetchUserLocation(
        firebaseUser: FirebaseUser,
        fetchUserLocationListener: (UserLocation) -> Unit
    )

    suspend fun updateUserLocation(firebaseUser: FirebaseUser, userLocation: UserLocation)
    suspend fun createOrder(
        firebaseUser: FirebaseUser,
        order: Order,
        createOrderListener: (String) -> Unit
    )

    suspend fun fetchLast15Orders(fetchLast15OrdersListener: (List<Order>) -> Unit)
    suspend fun fetchQueriedCityOrders(
        city: String,
        fetchQueriedCityOrdersListener: (List<Order>) -> Unit
    )

    suspend fun fetchOrderDetail(orderId: String, fetchOrderDetailListener: (Order) -> Unit)
    suspend fun fetchUserInfo(userId: String, fetchUserInfoListener: (User) -> Unit)
    suspend fun fetchOwnOrders(
        userId: String,
        lastOrder: Order? = null,
        fetchOwnOrdersListener: (List<Order>) -> Unit
    )
    suspend fun deleteOrder(
        orderId: String,
        deleteOrderListener: () -> Unit
    )
}