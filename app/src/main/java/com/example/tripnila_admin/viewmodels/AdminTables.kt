package com.example.tripnila_admin.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tripnila_admin.data.Review
import com.example.tripnila_admin.data.ServicePerformance
import com.example.tripnila_admin.data.Staycation
import com.example.tripnila_admin.data.StaycationBooking
import com.example.tripnila_admin.data.Tour
import com.example.tripnila_admin.data.TourBooking
import com.example.tripnila_admin.data.Tourist
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

class AdminTables : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val staycationCollection = "staycation"
    private val staycationBookingsCollection = "staycation_booking"
    private val tourCollection = "tour"
    private val tourBookingsCollection = "tour_booking"
    private val serviceViewsCollection = "service_view"
    private val reviewCollection = "review"

    private val _staycationsPerformance = MutableStateFlow<List<ServicePerformance>>(emptyList())
    val staycationsPerformance = _staycationsPerformance.asStateFlow()

    private val _toursPerformance = MutableStateFlow<List<ServicePerformance>>(emptyList())
    val toursPerformance = _toursPerformance.asStateFlow()

    private val _staycationPerformanceMap = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val staycationPerformanceMap = _staycationPerformanceMap.asStateFlow()

    private val _tourPerformanceMap = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val tourPerformanceMap = _tourPerformanceMap.asStateFlow()

    private val _isFetchingStaycationsPerformance = MutableStateFlow(false)
    val isFetchingStaycationsPerformance = _isFetchingStaycationsPerformance.asStateFlow()

    private val _isFetchingToursPerformance = MutableStateFlow(false)
    val isFetchingToursPerformance = _isFetchingToursPerformance.asStateFlow()

    private val _isStaycationPerformanceFetched = MutableStateFlow(false)
    val isStaycationPerformanceFetched = _isStaycationPerformanceFetched.asStateFlow()

    private val _isTourPerformanceFetched = MutableStateFlow(false)
    val isTourPerformanceFetched = _isTourPerformanceFetched.asStateFlow()

    private val _selectedSort = MutableStateFlow("Highest Booking Count")
    val selectedSort = _selectedSort.asStateFlow()

    private val _selectedMonthPerformance = MutableStateFlow(getCurrentMonthName())
    val selectedMonthPerformance = _selectedMonthPerformance.asStateFlow()

    private val _selectedYearPerformance = MutableStateFlow(getCurrentYear())
    val selectedYearPerformance = _selectedYearPerformance.asStateFlow()

    fun setSelectedMonthPerformance(filter: String) {
        _selectedMonthPerformance.value = filter
    }


    fun setSelectedYearPerformance(filter: String) {
        _selectedYearPerformance.value = filter.toInt()

    }

    private fun getCurrentMonthName(): String {
        val currentMonth = LocalDate.now().month
        return currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    private fun getCurrentYear(): Int {
        return LocalDate.now().year
    }

    fun setSortBy(sort: String) {

        _selectedSort.value = sort
        when (_selectedSort.value) {
            "Highest Booking Count" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedByDescending { it["totalBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedByDescending { it["totalBookings"].toString().toInt() }
            }
            "Lowest Booking Count" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedBy { it["totalBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedBy { it["totalBookings"].toString().toInt() }
            }
            "Highest Completed Bookings" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedByDescending { it["completedBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedByDescending { it["completedBookings"].toString().toInt() }
            }
            "Lowest Completed Bookings" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedBy { it["completedBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedBy { it["completedBookings"].toString().toInt() }
            }
            "Highest Cancelled Bookings" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedByDescending { it["cancelledBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedByDescending { it["cancelledBookings"].toString().toInt() }
            }
            "Lowest Cancelled Bookings" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedBy { it["cancelledBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedBy { it["cancelledBookings"].toString().toInt() }
            }
            "Highest Pending Bookings" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedByDescending { it["pendingBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedByDescending { it["pendingBookings"].toString().toInt() }
            }
            "Lowest Pending Bookings" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedBy { it["pendingBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedBy { it["pendingBookings"].toString().toInt() }
            }
            "Highest Views" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedByDescending { it["views"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedByDescending { it["views"].toString().toInt() }
            }
            "Lowest Views" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedBy { it["views"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedBy { it["views"].toString().toInt() }
            }
            "Highest Rating" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedByDescending { it["averageRating"].toString().toDouble() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedByDescending { it["averageRating"].toString().toDouble() }
            }
            "Lowest Rating" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedBy { it["averageRating"].toString().toDouble() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedBy { it["averageRating"].toString().toDouble() }
            }
        }
    }

    suspend fun fetchStaycationsPerformance(): List<ServicePerformance> {

        _isFetchingStaycationsPerformance.value = true

       // val (startTimestamp, endTimestamp) = parseDateRange()

        val servicePerformanceList = mutableListOf<ServicePerformance>()

        try {
            val query = db.collection(staycationBookingsCollection)
                    .orderBy("bookingDate", Query.Direction.ASCENDING)

            val querySnapshot = query.get().await()

            for (document in querySnapshot.documents) {
                val staycationBookingId = document.id
                val staycationId = document.getString("staycationId") ?: ""
                val bookingDate = document.getTimestamp("bookingDate")?.toDate() ?: Date()
                val bookingStatus = document.getString("bookingStatus") ?: ""

                val staycation = fetchStaycationDocumentById(staycationId)
                val views = fetchServiceViews(staycationId, "Staycation")
                val review = fetchReviewData(staycationBookingId, "Staycation")
                val host = fetchTouristData(staycation.hostId.substring(5))

                val staycationPerformance = ServicePerformance(
                    id = staycationId,
                    title = staycation.staycationTitle,
                    hostName = "${host.firstName} ${host.lastName}",
                    bookingStatus = bookingStatus,
                    views = views,
                    rating = review?.reviewRating ?: 0,
                    bookingDate = bookingDate
//                    totalBookings = totalBookings++ ,
//                    completedBookings = if (bookingStatus == "Completed") completedBookings++ else completedBookings,
//                    pendingBookings = if (bookingStatus == "Pending" || bookingStatus == "Ongoing" ) pendingBookings++ else pendingBookings,
//                    cancelledBookings = if (bookingStatus == "Cancelled") cancelledBookings++ else cancelledBookings,
//                    views,
//                    averageRating
                )


                servicePerformanceList.add(staycationPerformance)
            }

            _staycationsPerformance.value = servicePerformanceList
            groupPerformanceById(servicePerformanceList, "Staycation")
            Log.d("Staycation Performance", servicePerformanceList.toString())
        //    generateMapFromTourBookingReports()

        } catch (e: Exception) {
            Log.e("Staycation", "Error fetching staycation performance: ${e.message}")
        } finally {
            _isFetchingStaycationsPerformance.value = false
            _isStaycationPerformanceFetched.value = true
        }

        return servicePerformanceList
    }

    suspend fun fetchToursPerformance(): List<ServicePerformance> {

        _isFetchingToursPerformance.value = true

        // val (startTimestamp, endTimestamp) = parseDateRange()

        val servicePerformanceList = mutableListOf<ServicePerformance>()

        try {
            val query = db.collection(tourBookingsCollection)
                .orderBy("bookingDate", Query.Direction.ASCENDING)

            val querySnapshot = query.get().await()

            for (document in querySnapshot.documents) {
                val tourBookingId = document.id
                val tourId = document.getString("tourId") ?: ""
                val bookingDate = document.getTimestamp("bookingDate")?.toDate() ?: Date()
                val bookingStatus = document.getString("bookingStatus") ?: ""

                val tour = fetchTourDocumentById(tourId)
                val views = fetchServiceViews(tourId, "Tour")
                val review = fetchReviewData(tourBookingId, "Tour")
                val host = fetchTouristData(tour.hostId.substring(5))

                val tourPerformance = ServicePerformance(
                    id = tourId,
                    title = tour.tourTitle,
                    hostName = "${host.firstName} ${host.lastName}",
                    bookingStatus = bookingStatus,
                    views = views,
                    rating = review?.reviewRating ?: 0,
                    bookingDate = bookingDate
                )

                servicePerformanceList.add(tourPerformance)
            }

            _toursPerformance.value = servicePerformanceList
            groupPerformanceById(servicePerformanceList, "Tour")
            //    generateMapFromTourBookingReports()
            Log.d("Tour Performance", servicePerformanceList.toString())

        } catch (e: Exception) {
            Log.e("Tour", "Error fetching tour performance: ${e.message}")
        } finally {
            _isFetchingToursPerformance.value = false
            _isTourPerformanceFetched.value = true
        }

        return servicePerformanceList
    }

    private fun groupPerformanceById(servicePerformanceList: List<ServicePerformance>, serviceType: String) {

        val groupedById = servicePerformanceList.groupBy { it.id }

        val listOfMap = groupedById.map { (serviceId, servicesPerformance) ->

            val title = servicesPerformance.first().title
            val host = servicesPerformance.first().hostName

            val totalBookings = servicesPerformance.size
            val completedBookings = servicesPerformance.filter { it.bookingStatus == "Completed" }.size
            val pendingBookings = servicesPerformance.filter { it.bookingStatus == "Pending" || it.bookingStatus == "Ongoing" }.size
            val cancelledBookings = servicesPerformance.filter { it.bookingStatus == "Cancelled" }.size
            val views = servicesPerformance.sumOf { it.views }

            val totalRating = servicePerformanceList.sumOf { it.rating }
            val ratingSize = servicesPerformance.filter { it.rating != 0 }.size
            val averageRating = if (ratingSize > 0) totalRating.toDouble() / ratingSize else 0.0


            mapOf(
                "id" to serviceId,
                "title" to title,
                "hostName" to host,
                "totalBookings" to totalBookings.toString(),
                "completedBookings" to completedBookings.toString(),
                "pendingBookings" to pendingBookings.toString(),
                "cancelledBookings" to cancelledBookings.toString(),
                "views" to views.toString(),
                "averageRating" to averageRating.toString(),
                //  "bookingDate" to "EMPTY"
            )
        }

        val sortedListOfMap = listOfMap.sortedByDescending { it["totalBookings"].toString().toInt() }

        sortedListOfMap.forEach { map ->
            Log.d("$serviceType Performance Map", map.toString())
        }

        if (serviceType == "Staycation") {
            _staycationPerformanceMap.value = sortedListOfMap
        } else {
            _tourPerformanceMap.value = sortedListOfMap
        }

    }


    private suspend fun fetchTourDocumentById(documentId: String): Tour {

        val tourRef = db.collection(tourCollection).document(documentId)

        return try {

            val documentSnapshot = tourRef.get().await()

            if (documentSnapshot.exists()) {
                val hostId = documentSnapshot.getString("hostId") ?: ""
                val tourTitle = documentSnapshot.getString("tourTitle") ?: ""

                Tour(
                    tourId = documentId,
                    tourTitle = tourTitle,
                    hostId = hostId,
                )
            } else {
                Tour()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Tour()
        }
    }

    private suspend fun fetchStaycationDocumentById(documentId: String): Staycation {

        val staycationRef = db.collection(staycationCollection).document(documentId)

        return try {
            val documentSnapshot = staycationRef.get().await()

            if (documentSnapshot.exists()) {
                val hostId = documentSnapshot.getString("hostId") ?: ""
                val staycationTitle = documentSnapshot.getString("staycationTitle") ?: ""

                Staycation(
                    hostId = hostId,
                    staycationTitle = staycationTitle,
                    staycationId = documentId,
                )
            } else {
                Staycation()
            }


        } catch (e: Exception) {
            Log.e("StaycationRepository", "Error fetching staycation document: ${e.message}")
            Staycation()
        }
    }

    private suspend fun fetchTouristData(touristId: String): Tourist {
        val documentRef = db.collection("tourist").document(touristId)
        return try {
            val documentSnapshot = documentRef.get().await()

            if (documentSnapshot.exists()) {
                val firstName = documentSnapshot.getString("fullName.firstName") ?: ""
                val middleName = documentSnapshot.getString("fullName.middleName") ?: ""
                val lastName = documentSnapshot.getString("fullName.lastName") ?: ""
                val username = documentSnapshot.getString("username") ?: ""

                Tourist(firstName, middleName, lastName, username)
            } else {
                Tourist()
            }
        } catch (e: Exception) {
            Log.e("TouristVerificationViewModel", "Error fetching tourist data: ${e.message}")
            Tourist()
        }
    }

    private suspend fun fetchServiceViews(serviceID: String, serviceType: String): Int {
        return try {
            val documentSnapshot = db.collection(serviceViewsCollection)
                .whereEqualTo("serviceId", serviceID)
                .whereEqualTo("serviceType", serviceType)
                .get()
                .await()

            val viewCount = documentSnapshot.documents.firstOrNull()?.getLong("viewCount") ?: 0
            viewCount.toInt()
        } catch (e: Exception) {
            0
        }
    }

    private suspend fun fetchReviewData(bookingId: String, serviceType: String): Review? {
        return try {
            val querySnapshot = db.collection(reviewCollection)
                .whereEqualTo("bookingId", bookingId)
                .whereEqualTo("serviceType", serviceType)
                .get()
                .await()

            querySnapshot.documents.firstOrNull()?.let { document ->

                val reviewDate = document.getTimestamp("reviewDate")?.toDate() ?: Date()
                val reviewRating = document.getDouble("reviewRating")?.toInt() ?: 0

                Review(
                    bookingId = bookingId,
                    reviewDate = reviewDate,
                    reviewRating = reviewRating
                )
            }
        } catch (e: Exception) {
            null
        }
    }



}