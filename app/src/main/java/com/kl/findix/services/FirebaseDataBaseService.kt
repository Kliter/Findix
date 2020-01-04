package com.kl.findix.services

import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.Order
import com.kl.findix.model.User
import com.kl.findix.model.UserLocation

interface FirebaseDataBaseService {
    suspend fun fetchProfileInfo(
        firebaseUser: FirebaseUser,
        fetchProfileInfoListener: (User) -> Unit
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
        createOrderListener: () -> Unit
    )

    suspend fun fetchLast15Orders(fetchLast15OrdersListener: (List<Order>) -> Unit)
    suspend fun fetchQueriedCityOrders(city: String, fetchQueriedCityOrdersListener: (List<Order>) -> Unit)
}