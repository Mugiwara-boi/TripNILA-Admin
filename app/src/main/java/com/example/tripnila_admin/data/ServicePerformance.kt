package com.example.tripnila_admin.data

import java.util.Date

data class ServicePerformance(
    val id: String,
    val title: String,
    val hostName: String,
    val bookingStatus: String,
//    val totalBookings: Int,
//    val completedBookings: Int,
//    val pendingBookings: Int,
//    val cancelledBookings: Int,
    val views: Int,
    val rating: Int,
    val bookingDate: Date
//    val averageRating: Double
)
