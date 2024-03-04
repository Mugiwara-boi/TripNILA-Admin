package com.example.tripnila_admin.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila_admin.data.Tourist
import com.example.tripnila_admin.data.TouristVerification
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

class AdminReports : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _pendingVerifications = MutableStateFlow<List<TouristVerification>>(emptyList())
    val pendingVerifications = _pendingVerifications.asStateFlow()

    suspend fun approvePendingVerifications(verificationId: String, context: Context) {
        val collectionRef = db.collection("tourist_verification")

        try {
            val docRef = collectionRef.document(verificationId).get().await()

            if (docRef.exists()) {
                collectionRef.document(verificationId).update("verificationStatus", "Approved").await()
                Toast.makeText(context, "Verification Approved", Toast.LENGTH_SHORT).show()
                fetchPendingVerifications()
            } else {
                // Handle case where document doesn't exist
                println("Document $verificationId does not exist.")
            }
        } catch (e: Exception) {
            // Handle exceptions
            println("Error approving verification: ${e.message}")
        }
    }

    suspend fun denyPendingVerifications(verificationId: String, context: Context) {
        val collectionRef = db.collection("tourist_verification")

        try {
            val docRef = collectionRef.document(verificationId).get().await()

            if (docRef.exists()) {
                collectionRef.document(verificationId).update("verificationStatus", "Denied").await()
                Toast.makeText(context, "Verification Denied", Toast.LENGTH_SHORT).show()
                fetchPendingVerifications()
            } else {
                // Handle case where document doesn't exist
                println("Document $verificationId does not exist.")
            }
        } catch (e: Exception) {
            // Handle exceptions
            println("Error approving verification: ${e.message}")
        }
    }


    suspend fun fetchPendingVerifications() {
        val collectionRef = db.collection("tourist_verification")

        try {
            val documents = collectionRef
                .whereEqualTo("verificationStatus", "For Approval")
                .get()
                .await()

            Log.d("TouristVerificationViewModel", "Fetching pending verifications...")
            val touristVerifications = mutableListOf<TouristVerification>()

            for (document in documents) {
                val verificationId = document.id
                val firstValidIdType = document.getString("firstValidIdType") ?: ""
                val firstValidIdUrl = document.getString("firstValidIdUrl") ?: ""
                val secondValidIdType = document.getString("secondValidIdType") ?: ""
                val secondValidIdUrl = document.getString("secondValidIdUrl") ?: ""
                val touristId = document.getString("touristId") ?: ""
                val verificationStatus = document.getString("verificationStatus") ?: ""
                val timestampInDate = document.getTimestamp("timestamp")?.toDate() ?: Date()

                Log.d("secondValidIdType", secondValidIdType)

                Log.d("touristId", touristId)

                val tourist = fetchTouristData(touristId)

                Log.d("Tourist", tourist.toString())

                val touristVerification = tourist?.let {
                    TouristVerification(
                        verificationId = verificationId,
                        firstValidIdType = firstValidIdType,
                        firstValidIdUrl = firstValidIdUrl,
                        secondValidIdType = secondValidIdType,
                        secondValidIdUrl = secondValidIdUrl,
                        touristId = touristId,
                        userFullName = "${tourist.firstName} ${tourist.lastName}",
                        userUsername = tourist.userName,
                        userImage = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
                        reportDateTime = timestampInDate,
                        verificationStatus = verificationStatus
                    )
                }
                if (touristVerification != null) {
                    touristVerifications.add(touristVerification)
                }
            }
            _pendingVerifications.value = touristVerifications

            Log.d("Verification", _pendingVerifications.value.toString())
            Log.d("TouristVerificationViewModel", "Fetching pending verifications successful.")
        } catch (e: Exception) {
            Log.e("TouristVerificationViewModel", "Error fetching pending verifications: ${e.message}")
            // Handle failure
        }
    }

    private suspend fun fetchTouristData(touristId: String): Tourist? {
        val documentRef = db.collection("tourist").document(touristId)
        return try {
            val documentSnapshot = documentRef.get().await()
            if (documentSnapshot.exists()) {

                val fullName = documentSnapshot.get("fullName") as? Map<*, *>
                val firstName = fullName?.get("firstName") as? String ?: ""
                val lastName = fullName?.get("lastName") as? String ?: ""
                val username = documentSnapshot.getString("username") ?: ""



                Tourist(firstName, lastName, username)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("TouristVerificationViewModel", "Error fetching tourist data: ${e.message}")
            null
        }
    }


}