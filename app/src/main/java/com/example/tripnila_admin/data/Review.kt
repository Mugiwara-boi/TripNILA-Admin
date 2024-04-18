package com.example.tripnila_admin.data

import java.util.Date

data class Review(
    val bookingId: String,
    val reviewDate: Date,
    val reviewRating: Int,
)
