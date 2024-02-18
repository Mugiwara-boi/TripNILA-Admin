package com.example.tripnila_admin.repositories

import com.google.firebase.firestore.FirebaseFirestore

class repository {
    val db = FirebaseFirestore.getInstance()
    private val touristCollection = db.collection("tourist")
    private val hostCollection = db.collection("host")
    private val touristPreferencesCollection = db.collection("tourist_preference")
    private val staycationCollection = db.collection("staycation")
    private val staycationAvailabilityCollection = db.collection("staycation_availability")
    private val staycationBookingCollection = db.collection("staycation_booking")
    private val reviewCollection = db.collection("review")
    private val businessCollection = db.collection("business")
    private val tourCollection = db.collection("tour")
}