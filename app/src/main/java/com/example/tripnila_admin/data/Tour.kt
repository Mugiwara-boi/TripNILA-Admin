package com.example.tripnila_admin.data

data class Tour(
    val tourId: String = "",
    val tourContact: String = "",
    val tourDescription: String = "",
    val tourDuration: String = "",
    val tourEmail: String = "",
    val tourFacebook: String = "",
    val tourInstagram: String = "",
    val tourLanguage: String = "",
    val tourLat: Double = 0.0,
    val tourLng: Double = 0.0,
    val tourLocation: String = "",
    val tourPrice: Double = 0.0,
    val tourTitle: String = "",
    val tourType: String = "",
    val hostId: String = ""
)
