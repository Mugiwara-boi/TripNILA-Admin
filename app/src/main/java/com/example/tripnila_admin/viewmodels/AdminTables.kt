package com.example.tripnila_admin.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tripnila_admin.data.Review
import com.example.tripnila_admin.data.ServicePerformance
import com.example.tripnila_admin.data.Staycation
import com.example.tripnila_admin.data.Tour
import com.example.tripnila_admin.data.Tourist
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.Month
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
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

    private val _staycationPerformanceMapForHTML = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val staycationPerformanceMapForHTML = _staycationPerformanceMapForHTML.asStateFlow()

    private val _tourPerformanceMapForHTML = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val tourPerformanceMapForHTML = _tourPerformanceMapForHTML.asStateFlow()

    private val _isFetchingStaycationsPerformance = MutableStateFlow(false)
    val isFetchingStaycationsPerformance = _isFetchingStaycationsPerformance.asStateFlow()

    private val _isFetchingToursPerformance = MutableStateFlow(false)
    val isFetchingToursPerformance = _isFetchingToursPerformance.asStateFlow()

    private val _isStaycationPerformanceFetched = MutableStateFlow(false)
    val isStaycationPerformanceFetched = _isStaycationPerformanceFetched.asStateFlow()

    private val _isTourPerformanceFetched = MutableStateFlow(false)
    val isTourPerformanceFetched = _isTourPerformanceFetched.asStateFlow()

    private val _isGeneratingReport = MutableStateFlow(false)
    val isGeneratingReport = _isGeneratingReport.asStateFlow()


    private val _selectedSort = MutableStateFlow("Highest Booking Count")
    val selectedSort = _selectedSort.asStateFlow()

    private val _selectedMonthPerformance = MutableStateFlow("All")
    val selectedMonthPerformance = _selectedMonthPerformance.asStateFlow()

    private val _selectedYearPerformance = MutableStateFlow("All")
    val selectedYearPerformance = _selectedYearPerformance.asStateFlow()

    // GENERATE REPORT

    private val _selectedPeriod = MutableStateFlow("Monthly")
    val selectedPeriod = _selectedPeriod.asStateFlow()

    private val _selectedMonth = MutableStateFlow(getCurrentMonthName())
    val selectedMonth =_selectedMonth.asStateFlow()

    private val _selectedStartMonth = MutableStateFlow("January")
    val selectedStartMonth =_selectedStartMonth.asStateFlow()

    private val _selectedEndMonth = MutableStateFlow("June")
    val selectedEndMonth =_selectedEndMonth.asStateFlow()

    private val _selectedYear = MutableStateFlow(getCurrentYear())
    val selectedYear =_selectedYear.asStateFlow()

    private val _dateRange = MutableStateFlow(getDateRangeForMonth())
    val dateRange = _dateRange.asStateFlow()

    private val _isReportGenerated = MutableStateFlow(false)
    val isReportGenerated = _isReportGenerated.asStateFlow()

    fun resetReportGenerated() {
        _isReportGenerated.value = false
    }

    fun setSelectedPeriod(period: String) {
        _selectedPeriod.value = period
        _dateRange.value = when(_selectedPeriod.value) {
            "Monthly" -> getDateRangeForMonth()
            "Bi-yearly" -> getDateRangeForMonths()
            "Yearly" -> getDateRangeForYear()
            else -> "Unregistered Report Type"
        }

        Log.d("Date Range", _dateRange.value)

    }

    fun setSelectedMonth(filter: String) {
        _selectedMonth.value = filter

    }

    fun setSelectedYear(filter: String) {
        _selectedYear.value = filter.toInt()

    }


    fun setSelectedMonthRange(index: Int) {
        if (index == 0) {
            _selectedStartMonth.value = "January"
            _selectedEndMonth.value = "June"
        } else {
            _selectedStartMonth.value = "July"
            _selectedEndMonth.value = "December"
        }
    }

    private fun getDateRangeForMonth(): String {
        val month = Month.values().find {
            it.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals(_selectedMonth.value, ignoreCase = true)
        }
        return if (month != null) {
            val startDate = LocalDate.of(_selectedYear.value, month, 1)
            val endDate = startDate.plusMonths(1).minusDays(1)
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            val startDateString = startDate.format(formatter)
            val endDateString = endDate.format(formatter)
            "$startDateString-$endDateString"
        } else {
            ""
        }
    }

    private fun getDateRangeForMonths(): String {
        val startMonth = Month.values().find {
            it.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals(_selectedStartMonth.value, ignoreCase = true)
        }
        val endMonth = Month.values().find {
            it.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals(_selectedEndMonth.value, ignoreCase = true)
        }

        return if (startMonth != null && endMonth != null) {
            val startDate = LocalDate.of(_selectedYear.value, startMonth, 1)
            val endDate = LocalDate.of(_selectedYear.value, endMonth, 1).plusMonths(1).minusDays(1)
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            val startDateString = startDate.format(formatter)
            val endDateString = endDate.format(formatter)
            "$startDateString-$endDateString"
        } else {
            ""
        }
    }

    private fun getDateRangeForYear(): String {
        val startDate = LocalDate.of(_selectedYear.value, 1, 1)
        val endDate = LocalDate.of(_selectedYear.value, 12, 31)
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val startDateString = startDate.format(formatter)
        val endDateString = endDate.format(formatter)
        return "$startDateString-$endDateString"
    }

    fun generateReport() {
     //   _selectedYearPerformance.value = filter

        _isGeneratingReport.value = true

        val (startDate, endDate) = parseDateRange(_dateRange.value)

        val tempTourPerformance = _toursPerformance.value.filter {
            it.bookingDate in startDate..endDate
        }

        val tempStaycationPerformance = _staycationsPerformance.value.filter {
            it.bookingDate in startDate..endDate
        }

        groupPerformanceById(tempTourPerformance, "Tour")
        groupPerformanceById(tempStaycationPerformance, "Staycation")

        _isGeneratingReport.value = false
        _isReportGenerated.value = true

    }



    fun setSelectedMonthPerformance(filter: String) {
        _selectedMonthPerformance.value = filter

        val (startDate, endDate) = parseDateRange(getDateRange())

        val tempTourPerformance = _toursPerformance.value.filter {
            it.bookingDate in startDate..endDate
        }

        val tempStaycationPerformance = _staycationsPerformance.value.filter {
            it.bookingDate in startDate..endDate
        }

        groupPerformanceById(tempTourPerformance, "Tour")
        groupPerformanceById(tempStaycationPerformance, "Staycation")

    }


    fun setSelectedYearPerformance(filter: String) {
        _selectedYearPerformance.value = filter

        val (startDate, endDate) = parseDateRange(getDateRange())

        val tempTourPerformance = _toursPerformance.value.filter {
            it.bookingDate in startDate..endDate
        }

        val tempStaycationPerformance = _staycationsPerformance.value.filter {
            it.bookingDate in startDate..endDate
        }

        groupPerformanceById(tempTourPerformance, "Tour")
        groupPerformanceById(tempStaycationPerformance, "Staycation")

    }

    private fun getDateRange(): String {

        val monthStartName = if (_selectedMonthPerformance.value == "All") "January" else _selectedMonthPerformance.value
        val monthEndName = if (_selectedMonthPerformance.value == "All") "December" else _selectedMonthPerformance.value

        val monthStart = Month.values().find { it.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals(monthStartName, ignoreCase = true) }
        val monthEnd = Month.values().find { it.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals(monthEndName, ignoreCase = true) }

        val yearStart = if (_selectedYearPerformance.value == "All") 2023 else _selectedYearPerformance.value.toInt()
        val yearEnd = if (_selectedYearPerformance.value == "All") getCurrentYear() else _selectedYearPerformance.value.toInt()

        return if (monthStart != null) {
            val startDate = LocalDate.of(yearStart, monthStart, 1)
            val endDate =
                if (_selectedYearPerformance.value == "All") LocalDate.of(yearEnd, getCurrentMonth(), getCurrentDay())
                else if (_selectedMonthPerformance.value == "All") LocalDate.of(yearEnd, monthEnd, 31)
                else startDate.plusMonths(1).minusDays(1)

            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            val startDateString = startDate.format(formatter)
            val endDateString = endDate.format(formatter)

            Log.d("Start-End", "$startDateString-$endDateString")

            "$startDateString-$endDateString"
        } else {
            ""
        }
    }

//    private fun getDateRangeForYear(): String {
//
//        val year = if (_selectedYearPerformance.value == "All") 2023 else _selectedYearPerformance.value.toInt()
//
//        val startDate = LocalDate.of(year, 1, 1)
//        val endDate = LocalDate.of(year, 12, 31)
//        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
//        val startDateString = startDate.format(formatter)
//        val endDateString = endDate.format(formatter)
//        return "$startDateString-$endDateString"
//    }

    private fun parseDateRange(dateRange: String): Pair<Date, Date> {
        val dateRangeParts = dateRange.split("-")
        val startDateString = dateRangeParts.firstOrNull()
        val endDateString = dateRangeParts.lastOrNull()

        val startDate = LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        val endDate = LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"))

        val startTimestamp =
            Timestamp(startDate.atStartOfDay().toEpochSecond(ZoneOffset.ofHours(8)), 0)
        val endTimestamp =
            Timestamp(endDate.atTime(23, 59, 59).toEpochSecond(ZoneOffset.ofHours(8)), 0)

        return Pair(startTimestamp.toDate(), endTimestamp.toDate())
    }

    private fun getCurrentYear(): Int {
        return LocalDate.now().year
    }

    private fun getCurrentMonth(): Int {
        return LocalDate.now().monthValue
    }

    private fun getCurrentMonthName(): String {
        val currentMonth = LocalDate.now().month
        return currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    private fun getCurrentDay(): Int {
        return LocalDate.now().dayOfMonth
    }


    fun setSortBy(sort: String) {

        _selectedSort.value = sort
        when (_selectedSort.value) {
            "Highest Booking Count" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedByDescending { it["totalBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedByDescending { it["totalBookings"].toString().toInt() }

                _staycationPerformanceMapForHTML.value =
                    _staycationPerformanceMapForHTML.value.sortedByDescending { it["totalBookings"].toString().toInt() }
                _tourPerformanceMapForHTML.value =
                    _tourPerformanceMapForHTML.value.sortedByDescending { it["totalBookings"].toString().toInt() }
            }
            "Lowest Booking Count" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedBy { it["totalBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedBy { it["totalBookings"].toString().toInt() }

                _staycationPerformanceMapForHTML.value =
                    _staycationPerformanceMapForHTML.value.sortedBy { it["totalBookings"].toString().toInt() }
                _tourPerformanceMapForHTML.value =
                    _tourPerformanceMapForHTML.value.sortedBy { it["totalBookings"].toString().toInt() }
            }
            "Highest Completed Bookings" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedByDescending { it["completedBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedByDescending { it["completedBookings"].toString().toInt() }

                _staycationPerformanceMapForHTML.value =
                    _staycationPerformanceMapForHTML.value.sortedByDescending { it["completedBookings"].toString().toInt() }
                _tourPerformanceMapForHTML.value =
                    _tourPerformanceMapForHTML.value.sortedByDescending { it["completedBookings"].toString().toInt() }
            }
            "Lowest Completed Bookings" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedBy { it["completedBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedBy { it["completedBookings"].toString().toInt() }

                _staycationPerformanceMapForHTML.value =
                    _staycationPerformanceMapForHTML.value.sortedBy { it["completedBookings"].toString().toInt() }
                _tourPerformanceMapForHTML.value =
                    _tourPerformanceMapForHTML.value.sortedBy { it["completedBookings"].toString().toInt() }
            }
            "Highest Cancelled Bookings" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedByDescending { it["cancelledBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedByDescending { it["cancelledBookings"].toString().toInt() }

                _staycationPerformanceMapForHTML.value =
                    _staycationPerformanceMapForHTML.value.sortedByDescending { it["cancelledBookings"].toString().toInt() }
                _tourPerformanceMapForHTML.value =
                    _tourPerformanceMapForHTML.value.sortedByDescending { it["cancelledBookings"].toString().toInt() }
            }
            "Lowest Cancelled Bookings" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedBy { it["cancelledBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedBy { it["cancelledBookings"].toString().toInt() }

                _staycationPerformanceMapForHTML.value =
                    _staycationPerformanceMapForHTML.value.sortedBy { it["cancelledBookings"].toString().toInt() }
                _tourPerformanceMapForHTML.value =
                    _tourPerformanceMapForHTML.value.sortedBy { it["cancelledBookings"].toString().toInt() }
            }
            "Highest Pending Bookings" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedByDescending { it["pendingBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedByDescending { it["pendingBookings"].toString().toInt() }

                _staycationPerformanceMapForHTML.value =
                    _staycationPerformanceMapForHTML.value.sortedByDescending { it["pendingBookings"].toString().toInt() }
                _tourPerformanceMapForHTML.value =
                    _tourPerformanceMapForHTML.value.sortedByDescending { it["pendingBookings"].toString().toInt() }
            }
            "Lowest Pending Bookings" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedBy { it["pendingBookings"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedBy { it["pendingBookings"].toString().toInt() }

                _staycationPerformanceMapForHTML.value =
                    _staycationPerformanceMapForHTML.value.sortedBy { it["pendingBookings"].toString().toInt() }
                _tourPerformanceMapForHTML.value =
                    _tourPerformanceMapForHTML.value.sortedBy { it["pendingBookings"].toString().toInt() }
            }
            "Highest Views" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedByDescending { it["views"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedByDescending { it["views"].toString().toInt() }

                _staycationPerformanceMapForHTML.value =
                    _staycationPerformanceMapForHTML.value.sortedByDescending { it["views"].toString().toInt() }
                _tourPerformanceMapForHTML.value =
                    _tourPerformanceMapForHTML.value.sortedByDescending { it["views"].toString().toInt() }
            }
            "Lowest Views" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedBy { it["views"].toString().toInt() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedBy { it["views"].toString().toInt() }

                _staycationPerformanceMapForHTML.value =
                    _staycationPerformanceMapForHTML.value.sortedBy { it["views"].toString().toInt() }
                _tourPerformanceMapForHTML.value =
                    _tourPerformanceMapForHTML.value.sortedBy { it["views"].toString().toInt() }
            }
            "Highest Rating" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedByDescending { it["averageRating"].toString().toDouble() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedByDescending { it["averageRating"].toString().toDouble() }

                _staycationPerformanceMapForHTML.value =
                    _staycationPerformanceMapForHTML.value.sortedByDescending { it["averageRating"].toString().toDouble() }
                _tourPerformanceMapForHTML.value =
                    _tourPerformanceMapForHTML.value.sortedByDescending { it["averageRating"].toString().toDouble() }
            }
            "Lowest Rating" -> {
                _staycationPerformanceMap.value =
                    _staycationPerformanceMap.value.sortedBy { it["averageRating"].toString().toDouble() }
                _tourPerformanceMap.value =
                    _tourPerformanceMap.value.sortedBy { it["averageRating"].toString().toDouble() }

                _staycationPerformanceMapForHTML.value =
                    _staycationPerformanceMapForHTML.value.sortedBy { it["averageRating"].toString().toDouble() }
                _tourPerformanceMapForHTML.value =
                    _tourPerformanceMapForHTML.value.sortedBy { it["averageRating"].toString().toDouble() }
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

                Log.d("Date", bookingDate.toString())
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

      //  val sortedListOfMap = listOfMap.sortedByDescending { it["totalBookings"].toString().toInt() }

        listOfMap.forEach { map ->
            Log.d("$serviceType Performance Map", map.toString())
        }

        if (serviceType == "Staycation") {
            _staycationPerformanceMap.value = listOfMap
            _staycationPerformanceMapForHTML.value = listOfMap
        } else {
            _tourPerformanceMap.value = listOfMap
            _tourPerformanceMapForHTML.value = listOfMap
        }

        setSortBy(_selectedSort.value)

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