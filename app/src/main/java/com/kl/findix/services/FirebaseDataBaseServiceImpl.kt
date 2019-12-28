package com.kl.findix.services

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.kl.findix.model.User
import com.kl.findix.model.UserLocation
import com.kl.findix.util.getProfileStorageUrl
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

    override suspend fun updateProfileInfo(
        firebaseUser: FirebaseUser,
        user: User,
        profilePhotoUrl: String
    ) {
        database.collection("User")
            .document(firebaseUser.uid)
            .set(
                user.apply {
                    this.profilePhotoUrl = getProfileStorageUrl(firebaseUser.uid)
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
}