package com.example.tripnila_admin.data

import java.util.Date

data class TouristVerification(
    val verificationId: String,
    val userFullName: String,
    val userUsername: String,
    val userImage: String,
    val reportDateTime: Date,
    val firstValidIdType: String,
    val firstValidIdUrl: String,
    val secondValidIdType: String,
    val secondValidIdUrl: String,
    val verificationStatus: String,
    val touristId: String
)
