package com.example.tripnila_admin.data

import java.util.Date

data class TourBooking(
    val host: Tourist,
    val tourist: Tourist,
    val tour: Tour,
    val noOfGuests: Int,
    val startTime: String,
    val endTime: String,
    val tourDate: String,
    val totalAmount: Double,
    val commission: Double,
    val bookingStatus: String,
    val bookingDate: Date
)



