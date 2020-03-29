package com.kl.findix.services

import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.Order
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.User
import com.kl.findix.model.UserLocation

interface FirebaseDataBaseService {
    suspend fun fetchOwnProfileInfo(firebaseUser: FirebaseUser): ServiceResult<User>
    suspend fun updateProfileInfo(firebaseUser: FirebaseUser, user: User, profilePhotoUrl: String): ServiceResult<Unit>
    suspend fun fetchUserLocation(firebaseUser: FirebaseUser, fetchUserLocationListener: (UserLocation) -> Unit)
    suspend fun updateUserLocation(firebaseUser: FirebaseUser, userLocation: UserLocation): ServiceResult<Unit>
    suspend fun createOrder(firebaseUser: FirebaseUser, order: Order): ServiceResult<String>
    suspend fun fetchLast15Orders(): ServiceResult<List<Order>>
    suspend fun fetchQueriedCityOrders(city: String): ServiceResult<List<Order>>
    suspend fun fetchOrderDetail(orderId: String): ServiceResult<Order?>
    suspend fun fetchUserInfo(userId: String): ServiceResult<User?>
    suspend fun fetchOwnOrders(
        userId: String,
        lastOrder: Order? = null
    ): ServiceResult<List<Order>>
    suspend fun deleteOrder(orderId: String): ServiceResult<Unit>
}