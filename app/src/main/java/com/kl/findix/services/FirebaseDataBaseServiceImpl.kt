package com.kl.findix.services

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kl.findix.model.Order
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.User
import com.kl.findix.model.UserLocation
import com.kl.findix.util.FindixError
import com.kl.findix.util.extension.getStorageProfileIconPath
import java.net.SocketTimeoutException
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseDataBaseServiceImpl @Inject constructor(
    private val database: FirebaseFirestore
) : FirebaseDataBaseService {

    override suspend fun fetchOwnProfileInfo(firebaseUser: FirebaseUser) =
        suspendCoroutine<ServiceResult<User>> { continuation ->
            try {
                database.collection("User")
                    .document(firebaseUser.uid)
                    .get()
                    .addOnSuccessListener {
                        val user = it.toObject(User::class.java) ?: User()
                        continuation.resume(ServiceResult.Success(user))
                    }
                    .addOnFailureListener {
                        when (it) {
                            is SocketTimeoutException -> {
                                continuation.resume(ServiceResult.Failure(FindixError.NetworkError()))
                            }
                            else -> {
                                continuation.resume(ServiceResult.Failure(FindixError.UndefinedError()))
                            }
                        }
                    }
            } catch (e: Exception) {
                continuation.resume(ServiceResult.Failure(e))
            }
        }

    override suspend fun updateProfileInfo(
        firebaseUser: FirebaseUser,
        user: User,
        profilePhotoUrl: String
    ) {
        database.collection("User")
            .document(firebaseUser.uid)
            .set(
                user.apply {
                    this.profilePhotoUrl =
                        getStorageProfileIconPath(
                            firebaseUser.uid
                        )
                }
            )
    }

    override suspend fun fetchUserLocation(
        firebaseUser: FirebaseUser,
        fetchUserLocationListener: (UserLocation) -> Unit
    ) {
        database.collection("UserLocation").document(firebaseUser.uid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.toObject(UserLocation::class.java)?.let { userLocation ->
                        fetchUserLocationListener.invoke(userLocation)
                    }
                }
            }
    }

    override suspend fun updateUserLocation(
        firebaseUser: FirebaseUser,
        userLocation: UserLocation
    ) {
        database.collection("UserLocation").document(firebaseUser.uid).set(userLocation)
    }

    override suspend fun fetchLast15Orders(fetchLast15OrdersListener: (List<Order>) -> Unit) {
        database.collection("Order")
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .limit(15)
            .get()
            .addOnSuccessListener { results ->
                val orders: List<Order> = results.map { result ->
                    result.toObject(Order::class.java).apply {
                        orderId = result.id
                    }
                }
                fetchLast15OrdersListener.invoke(orders)
            }
    }

    override suspend fun createOrder(
        firebaseUser: FirebaseUser,
        order: Order,
        createOrderListener: (String) -> Unit
    ) {
        database.collection("Order")
            .add(order.apply {
                this.userId = firebaseUser.uid
                this.timeStamp = Date()
            })
            .addOnSuccessListener {
                createOrderListener.invoke(it.id)
            }
    }

    override suspend fun fetchQueriedCityOrders(
        city: String,
        fetchQueriedCityOrdersListener: (List<Order>) -> Unit
    ) {
        database.collection("Order")
            .whereEqualTo("city", city)
            .get()
            .addOnSuccessListener { results ->
                val orders: List<Order> = results.map { result ->
                    result.toObject(Order::class.java).apply {
                        orderId = result.id
                    }
                }
                fetchQueriedCityOrdersListener.invoke(orders)
            }
    }

    override suspend fun fetchOrderDetail(
        orderId: String,
        fetchOrderDetailListener: (Order) -> Unit
    ) {
        database.collection("Order")
            .document(orderId)
            .get()
            .addOnSuccessListener { result ->
                val order = result.toObject(Order::class.java).also {
                    it?.orderId = result.id
                }
                order?.let {
                    fetchOrderDetailListener.invoke(order)
                }
            }
    }

    override suspend fun fetchUserInfo(userId: String, fetchUserInfoListener: (User) -> Unit) {
        database.collection("User")
            .document(userId)
            .get()
            .addOnSuccessListener { result ->
                val user = result.toObject(User::class.java)
                user?.let {
                    fetchUserInfoListener.invoke(it)
                }
            }
    }

    override suspend fun fetchOwnOrders(userId: String, lastOrder: Order?) =
        suspendCoroutine<ServiceResult<List<Order>>> { continuation ->
            try {
                // lastOrder受け取った時だけそこから10件取得するためにstartAfter設定する。
                val query = database.collection("Order")
                if (lastOrder != null) {
                    query.startAfter(lastOrder)
                }

                query.whereEqualTo("userId", userId)
                    .orderBy("timeStamp", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .addOnSuccessListener { results ->
                        val orders: List<Order> = results.map { result ->
                            result.toObject(Order::class.java).apply {
                                orderId = result.id
                            }
                        }
                        continuation.resume(ServiceResult.Success(orders))
                    }
                    .addOnFailureListener {
                        continuation.resume(ServiceResult.Failure(it))
                    }
            } catch (e: Exception) {
                continuation.resume(ServiceResult.Failure(e))
            }
        }

    override suspend fun deleteOrder(orderId: String) =
        suspendCoroutine<ServiceResult<Unit>> { continuation ->
            try {
                database.collection("Order")
                    .document(orderId)
                    .delete()
                    .addOnSuccessListener { result ->
                        continuation.resume(ServiceResult.Success(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(ServiceResult.Failure(it))
                    }
            } catch (e: Exception) {
                continuation.resume(ServiceResult.Failure(e))
            }
        }
}