package com.example.tripnila_admin.data

import java.util.Date

data class StaycationBooking(
    val host: Tourist,
    val tourist: Tourist,
    val staycation: Staycation,
    val checkInDate: Date,
    val checkOutDate: Date,
    val noOfGuests: Int,
    val noOfInfants: Int,
    val noOfPets: Int,
    val additionalFee: Double,
    val totalAmount: Double,
    val commission: Double,
    val bookingStatus: String,
    val bookingDate: Date
)
