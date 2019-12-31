package com.kl.findix.services

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.kl.findix.model.Order
import com.kl.findix.model.User
import com.kl.findix.model.UserLocation
import com.kl.findix.util.getStorageProfileIconPath
import java.util.*
import javax.inject.Inject

class FirebaseDataBaseServiceImpl @Inject constructor(
    private val database: FirebaseFirestore
): FirebaseDataBaseService {

    override suspend fun fetchProfileInfo(firebaseUser: FirebaseUser, fetchProfileInfoListener: (User) -> Unit) {
        database.collection("User").document(firebaseUser.uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.toObject(User::class.java)?.let { user ->
                    fetchProfileInfoListener.invoke(user)
                }
            }
        }
    }

    suspend fun fetchNearOrder(
        latitude: Long,
        longitude: Long,
        fetchNearOrderListener: (List<Order>) -> Unit
    ) {
        database.collection("Order")
            .whereGreaterThan("latitude", latitude - 0.005)
            .whereLessThan("latitude", latitude + 0.05)
            .whereGreaterThan("longitude", longitude - 0.005)
            .whereLessThan("longitude", longitude + 0.05)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.toObjects(Order::class.java)?.let { orders ->
                        fetchNearOrderListener.invoke(orders)
                    }
                }
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
                    this.profilePhotoUrl = getStorageProfileIconPath(firebaseUser.uid)
                }
            )
    }

    override suspend fun fetchUserLocation(firebaseUser: FirebaseUser, fetchUserLocationListener: (UserLocation) -> Unit) {
        database.collection("UserLocation").document(firebaseUser.uid).get().addOnCompleteListener { task ->
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

    override suspend fun fetchNearOrder(latLng: LatLng) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun fetchLast15Orders(fetchLast15OrdersListener: (List<Order>) -> Unit) {
        database.collection("Order").orderBy("timeStamp").limit(15).get()
            .addOnSuccessListener { result ->
                result?.toObjects(Order::class.java)?.let { orders ->
                    fetchLast15OrdersListener.invoke(orders)
                }
            }
    }

    override suspend fun createOrder(
        firebaseUser: FirebaseUser,
        order: Order,
        createOrderListener: () -> Unit
    ) {
        database.collection("Order")
            .document()
            .set(order.apply {
                this.timeStamp = Date()
            })
            .addOnSuccessListener {
                createOrderListener.invoke()
            }
    }
}