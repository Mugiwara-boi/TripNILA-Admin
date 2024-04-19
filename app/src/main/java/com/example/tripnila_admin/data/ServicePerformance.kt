package com.example.tripnila_admin.data

import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ServicePerformance(
    val id: String,
    val title: String,
    val hostName: String,
    val bookingStatus: String,
//    val totalBookings: Int,
//    val completedBookings: Int,
//    val pendingBookings: Int,
//    val cancelledBookings: Int,
    val views: List<View>,
    val rating: Int,
    val bookingDate: Date
//    val averageRating: Double
)

data class View(
    val id: String,
    val month: Int,
    val views: Int
) {
    val date: Date = Calendar.getInstance().apply {
        set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
        set(Calendar.MONTH, month - 1) // Calendar months are zero-based
        set(Calendar.DAY_OF_MONTH, 1) // Set to the first day of the month
    }.time

    fun getMonthName(): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
    }
}

