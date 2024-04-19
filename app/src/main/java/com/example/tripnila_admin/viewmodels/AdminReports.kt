package com.example.tripnila_admin.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.tripnila_admin.data.Staycation
import com.example.tripnila_admin.data.StaycationBooking
import com.example.tripnila_admin.data.Tour
import com.example.tripnila_admin.data.TourBooking
import com.example.tripnila_admin.data.Tourist
import com.example.tripnila_admin.data.TouristVerification
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.*
import java.time.format.TextStyle
import java.util.Calendar

class AdminReports : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _pendingVerifications = MutableStateFlow<List<TouristVerification>>(emptyList())
    val pendingVerifications = _pendingVerifications.asStateFlow()

    private val _staycationBookingReports = MutableStateFlow<List<StaycationBooking>>(emptyList())
    val staycationBookingReports = _staycationBookingReports.asStateFlow()

    private val _tourBookingReports = MutableStateFlow<List<TourBooking>>(emptyList())
    val tourBookingReports = _tourBookingReports.asStateFlow()

    private val _staycationDataMap = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val staycationDataMap = _staycationDataMap.asStateFlow()

    private val _tourDataMap = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val tourDataMap = _tourDataMap.asStateFlow()

    private val _staycationTotalCollectedCommission = MutableStateFlow<Double>(0.0)
    val staycationTotalCollectedCommission = _staycationTotalCollectedCommission.asStateFlow()

    private val _staycationTotalPendingCommission = MutableStateFlow<Double>(0.0)
    val staycationTotalPendingCommission = _staycationTotalPendingCommission.asStateFlow()

    private val _staycationTotalGrossSale = MutableStateFlow<Double>(0.0)
    val staycationTotalGrossSale = _staycationTotalGrossSale.asStateFlow()

    private val _tourTotalCollectedCommission = MutableStateFlow<Double>(0.0)
    val tourTotalCollectedCommission = _tourTotalCollectedCommission.asStateFlow()

    private val _tourTotalPendingCommission = MutableStateFlow<Double>(0.0)
    val tourTotalPendingCommission = _tourTotalPendingCommission.asStateFlow()

    private val _tourTotalGrossSale = MutableStateFlow<Double>(0.0)
    val tourTotalGrossSale = _tourTotalGrossSale.asStateFlow()


    private val _isFetchingStaycationBookings = MutableStateFlow(false)
    val isFetchingStaycationBookings = _isFetchingStaycationBookings.asStateFlow()

    private val _isFetchingTourBookings = MutableStateFlow(false)
    val isFetchingTourBookings = _isFetchingTourBookings.asStateFlow()


    private val _isStaycationBookingsFetched = MutableStateFlow(false)
    val isStaycationBookingsFetched = _isStaycationBookingsFetched.asStateFlow()

    private val _isTourBookingsFetched = MutableStateFlow(false)
    val isTourBookingsFetched = _isTourBookingsFetched.asStateFlow()



    private val _isFetchingPendingVerifications = MutableStateFlow(false)
    val isFetchingPendingVerifications = _isFetchingPendingVerifications.asStateFlow()

    private val _isGeneratingExcel = MutableStateFlow(false)
    val isGeneratingExcel = _isGeneratingExcel.asStateFlow()


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

    private fun generateMapFromStaycationBookingReports() {

        val currencySymbol = "₱"

        val groupedByStaycationId = _staycationBookingReports.value.groupBy { it.staycation.staycationId }

        val listOfMap = groupedByStaycationId.map { (staycationId, bookings) ->
            val staycation = bookings.first().staycation
            val host = bookings.first().host

            val numberOfBookings = bookings.size
            val grossBookingSales = bookings.sumOf { it.totalAmount }
            val totalCollectedCommission = bookings.filter { it.bookingStatus == "Completed" }.sumOf { it.commission }
            val totalPendingCommission = bookings.filter { it.bookingStatus != "Completed" }.sumOf { it.commission }

            mapOf(
                "staycationId" to staycationId,
                "staycationName" to staycation.staycationTitle,
                "hostName" to "${host.firstName} ${host.lastName}" ,
                "numberOfBookings" to numberOfBookings.toString(),
//                "grossBookingSales" to grossBookingSales.toString(),
//                "collectedCommission" to totalCollectedCommission.toString(),
//                "pendingCommission" to totalPendingCommission.toString(),
                "grossBookingSales" to "$currencySymbol %.2f".format(grossBookingSales),
                "collectedCommission" to "$currencySymbol %.2f".format(totalCollectedCommission),
                "pendingCommission" to "$currencySymbol %.2f".format(totalPendingCommission)
            )
        }

        listOfMap.forEach { map ->
            Log.d("Staycation Booking Map", map.toString())
        }

        val totalSumOfCollectedCommission = listOfMap.sumOf {
            it["collectedCommission"]!!.substring(currencySymbol.length).trim().toDouble()
        }
        Log.d("Total Collected Commission", "Total Sum of Collected Commission: $totalSumOfCollectedCommission")

        val totalSumOfPendingCommission = listOfMap.sumOf {
            it["pendingCommission"]!!.substring(currencySymbol.length).trim().toDouble()
        }
        Log.d("Total Pending Commission", "Total Sum of Pending Commission: $totalSumOfPendingCommission")

        val totalSumOfGrossSale = listOfMap.sumOf {
            it["grossBookingSales"]!!.substring(currencySymbol.length).trim().toDouble()
        }
        Log.d("Total Gross Sales", "Total Sum of Gross Sales: $totalSumOfGrossSale")

//        val totalSumOfCollectedCommission = listOfMap.sumOf { it["collectedCommission"]!!.toDouble() }
//        Log.d("Total Collected Commission", "Total Sum of Collected Commission: $totalSumOfCollectedCommission")
//
//        val totalSumOfPendingCommission = listOfMap.sumOf { it["pendingCommission"]!!.toDouble() }
//        Log.d("Total Pending Commission", "Total Sum of Pending Commission: $totalSumOfPendingCommission")
//
//        val totalSumOfGrossSale = listOfMap.sumOf { it["grossBookingSales"]!!.toDouble() }
//        Log.d("Total Gross Sales", "Total Sum of Gross Sales: $totalSumOfGrossSale")

        _staycationDataMap.value = listOfMap
        _staycationTotalCollectedCommission.value = totalSumOfCollectedCommission
        _staycationTotalPendingCommission.value = totalSumOfPendingCommission
        _staycationTotalGrossSale.value = totalSumOfGrossSale





//        val totalGrossSalesByStaycationId = groupedByStaycationId.mapValues { (_, bookings) ->
//            bookings.sumOf { it.totalAmount }
//        }
//
//        val totalCollectedCommissionByStaycationId = groupedByStaycationId.mapValues { (_, bookings) ->
//            bookings.filter { it.bookingStatus == "Completed" }.sumOf { it.commission }
//        }
//
//        val totalPendingCommissionByStaycationId = groupedByStaycationId.mapValues { (_, bookings) ->
//            bookings.filter { it.bookingStatus != "Completed" }.sumOf { it.commission }
//        }
//
//        totalGrossSalesByStaycationId.forEach { (staycationId, totalGrossSales) ->
//            Log.d("Gross Sales from Staycation", "Staycation ID: $staycationId, Total Gross Sales: $totalGrossSales")
//        }
//
//        totalCollectedCommissionByStaycationId.forEach { (staycationId, totalCollectedCommission) ->
//            Log.d("Commission from Staycation", "Staycation ID: $staycationId, Total Collected Commission: $totalCollectedCommission")
//        }
//
//        totalPendingCommissionByStaycationId.forEach { (staycationId, totalPendingCommission) ->
//            Log.d("Commission from Staycation", "Staycation ID: $staycationId, Total Pending Commission: $totalPendingCommission")
//        }
////

    }

    private fun generateMapFromTourBookingReports() {

        val currencySymbol = "₱"

        val groupedByTourId = _tourBookingReports.value.groupBy { it.tour.tourId }

        val listOfMap = groupedByTourId.map { (tourId, bookings) ->
            val tour = bookings.first().tour
            val host = bookings.first().host

            val numberOfBookings = bookings.size
            val grossBookingSales = bookings.sumOf { it.totalAmount }
            val totalCollectedCommission = bookings.filter { it.bookingStatus == "Completed" }.sumOf { it.commission }
            val totalPendingCommission = bookings.filter { it.bookingStatus != "Completed" }.sumOf { it.commission }

            mapOf(
                "tourId" to tourId,
                "tourName" to tour.tourTitle,
                "hostName" to "${host.firstName} ${host.lastName}" ,
                "numberOfBookings" to numberOfBookings.toString(),
//                "grossBookingSales" to grossBookingSales.toString(),
//                "collectedCommission" to totalCollectedCommission.toString(),
//                "pendingCommission" to totalPendingCommission.toString(),
                "grossBookingSales" to "$currencySymbol %.2f".format(grossBookingSales),
                "collectedCommission" to "$currencySymbol %.2f".format(totalCollectedCommission),
                "pendingCommission" to "$currencySymbol %.2f".format(totalPendingCommission)
            )
        }

        listOfMap.forEach { map ->
            Log.d("Tour Booking Map", map.toString())
        }

        val totalSumOfCollectedCommission = listOfMap.sumOf {
            it["collectedCommission"]!!.substring(currencySymbol.length).trim().toDouble()
        }
        Log.d("Total Collected Commission", "Total Sum of Collected Commission: $totalSumOfCollectedCommission")

        val totalSumOfPendingCommission = listOfMap.sumOf {
            it["pendingCommission"]!!.substring(currencySymbol.length).trim().toDouble()
        }
        Log.d("Total Pending Commission", "Total Sum of Pending Commission: $totalSumOfPendingCommission")

        val totalSumOfGrossSale = listOfMap.sumOf {
            it["grossBookingSales"]!!.substring(currencySymbol.length).trim().toDouble()
        }
        Log.d("Total Gross Sales", "Total Sum of Gross Sales: $totalSumOfGrossSale")
//
//        val totalSumOfCollectedCommission = listOfMap.sumOf { it["collectedCommission"]!!.toDouble() }
//        Log.d("Total Collected Commission", "Total Sum of Collected Commission: $totalSumOfCollectedCommission")
//
//        val totalSumOfPendingCommission = listOfMap.sumOf { it["pendingCommission"]!!.toDouble() }
//        Log.d("Total Pending Commission", "Total Sum of Pending Commission: $totalSumOfPendingCommission")
//
//        val totalSumOfGrossSale = listOfMap.sumOf { it["grossBookingSales"]!!.toDouble() }
//        Log.d("Total Gross Sales", "Total Sum of Gross Sales: $totalSumOfGrossSale")

        _tourDataMap.value = listOfMap
        _tourTotalCollectedCommission.value = totalSumOfCollectedCommission
        _tourTotalPendingCommission.value = totalSumOfPendingCommission
        _tourTotalGrossSale.value = totalSumOfGrossSale

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

    fun resetFetchStatus() {
        _isStaycationBookingsFetched.value = false
        _isTourBookingsFetched.value = false
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

    fun setSelectedYear(filter: String) {
        _selectedYear.value = filter.toInt()

    }

    private fun getCurrentMonthName(): String {
        val currentMonth = LocalDate.now().month
        return currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    private fun getCurrentYear(): Int {
        return LocalDate.now().year
    }

    private fun getDateRangeForMonth(): String {
        val month = Month.values().find {
            it.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals(_selectedMonth.value, ignoreCase = true)
        }
        return if (month != null) {
            val startDate = LocalDate.of(_selectedYear.value, month, 1)
            val endDate = startDate.plusMonths(1).minusDays(1)
            val formatter = ofPattern("MM/dd/yyyy")
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
            val formatter = ofPattern("MM/dd/yyyy")
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
        val formatter = ofPattern("MM/dd/yyyy")
        val startDateString = startDate.format(formatter)
        val endDateString = endDate.format(formatter)
        return "$startDateString-$endDateString"
    }



    suspend fun approvePendingVerifications(verificationId: String, context: Context) {
        val collectionRef = db.collection("tourist_verification")

        try {
            val docRef = collectionRef.document(verificationId).get().await()

            if (docRef.exists()) {
                collectionRef.document(verificationId).update("verificationStatus", "Approved").await()
                Toast.makeText(context, "Verification Approved", Toast.LENGTH_SHORT).show()
                fetchPendingVerifications()
            } else {
                // Handle case where document doesn't exist
                println("Document $verificationId does not exist.")
            }
        } catch (e: Exception) {
            // Handle exceptions
            println("Error approving verification: ${e.message}")
        }
    }

    suspend fun denyPendingVerifications(verificationId: String, context: Context) {
        val collectionRef = db.collection("tourist_verification")

        try {
            val docRef = collectionRef.document(verificationId).get().await()

            if (docRef.exists()) {
                collectionRef.document(verificationId).update("verificationStatus", "Denied").await()
                Toast.makeText(context, "Verification Denied", Toast.LENGTH_SHORT).show()
                fetchPendingVerifications()
            } else {
                // Handle case where document doesn't exist
                println("Document $verificationId does not exist.")
            }
        } catch (e: Exception) {
            // Handle exceptions
            println("Error approving verification: ${e.message}")
        }
    }

    suspend fun fetchTourBookings(): List<TourBooking> {

        _isFetchingTourBookings.value = true

        val (startTimestamp, endTimestamp) = parseDateRange()

        val tourBookings = mutableListOf<TourBooking>()

        try {
            val query =
                db.collection("tour_booking")
                    .whereGreaterThanOrEqualTo("bookingDate", startTimestamp)
                    .whereLessThanOrEqualTo("bookingDate", endTimestamp)
                    .orderBy("bookingDate", Query.Direction.ASCENDING)


            val querySnapshot = query.get().await()

            for (document in querySnapshot.documents) {
                val bookingDate = document.getTimestamp("bookingDate")?.toDate() ?: Date()
                val bookingStatus = document.getString("bookingStatus") ?: ""
                val commission = document.getDouble("commission") ?: 0.0
                val endTime = document.getString("endTime") ?: ""
                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                val startTime = document.getString("startTime") ?: ""
                val totalAmount = document.getDouble("totalAmount") ?: 0.0
                //    val tourAvailabilityId = document.getString("tourAvailabilityId") ?: ""
                val tourDate = document.getString("tourDate") ?: ""
                val tourId = document.getString("tourId") ?: ""
                val touristId = document.getString("touristId") ?: ""

                val tour = fetchTourDocumentById(tourId)
                val host = fetchTouristData(tour.hostId.substring(5))
                val tourist = fetchTouristData(touristId)

                val tourBooking = TourBooking(
                    host,
                    tourist,
                    tour,
                    noOfGuests,
                    startTime,
                    endTime,
                    tourDate,
                    totalAmount,
                    commission,
                    bookingStatus,
                    bookingDate
                )


                tourBookings.add(tourBooking)

                Log.d("Tour Booking", tourBooking.toString())
            }

            _tourBookingReports.value = tourBookings
            generateMapFromTourBookingReports()

          //  Log.d("Tour Bookings", _tourBookingReports.value.toString())

        } catch (e: Exception) {
            Log.e("Tour", "Error fetching tour bookings: ${e.message}")
        } finally {
            _isFetchingTourBookings.value = false
            _isTourBookingsFetched.value = true
        }

        return tourBookings
    }

    suspend fun fetchStaycationBookings(): List<StaycationBooking> {

        _isFetchingStaycationBookings.value = true

        val (startTimestamp, endTimestamp) = parseDateRange()

        val staycationBookings = mutableListOf<StaycationBooking>()

        try {

            val query = db.collection("staycation_booking")
                .whereGreaterThanOrEqualTo("bookingDate", startTimestamp)
                .whereLessThanOrEqualTo("bookingDate", endTimestamp)
                .orderBy("bookingDate", Query.Direction.ASCENDING)


            val querySnapshot = query.get().await()


            for (document in querySnapshot.documents) {
                val additionalFee = document.getDouble("additionalFee") ?: 0.0
                val bookingDate = document.getTimestamp("bookingDate")?.toDate() ?: Date()
                val bookingStatus = document.getString("bookingStatus") ?: ""
                val checkInDate = document.getTimestamp("checkInDate")?.toDate() ?: Date()
                val checkOutDate = document.getTimestamp("checkOutDate")?.toDate() ?: Date()
                val commission = document.getDouble("commission") ?: 0.0
                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
                val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
                val staycationId = document.getString("staycationId") ?: ""
                val totalAmount = document.getDouble("totalAmount") ?: 0.0
                val touristId = document.getString("touristId") ?: ""



                val staycation = fetchStaycationDocumentById(staycationId)

                //   Log.d("Host Id", staycation.hostId)

                val host = fetchTouristData(staycation.hostId.substring(5))
                val tourist = fetchTouristData(touristId)

                val staycationBooking = StaycationBooking(
                    host,
                    tourist,
                    staycation,
                    checkInDate,
                    checkOutDate,
                    noOfGuests,
                    noOfInfants,
                    noOfPets,
                    additionalFee,
                    totalAmount,
                    commission,
                    bookingStatus,
                    bookingDate
                )
                staycationBookings.add(staycationBooking)

                Log.d("Staycation Booking", staycationBooking.toString())
            }

            _staycationBookingReports.value = staycationBookings
            generateMapFromStaycationBookingReports()

        } catch (e: Exception) {
            Log.e("StaycationRepository", "Error fetching staycation bookings: ${e.message}")
        } finally {
            _isFetchingStaycationBookings.value = false
            _isStaycationBookingsFetched.value = true

            Log.d("Fetching Status", ": ${_isFetchingStaycationBookings.value}")
            Log.d("Fetched Status", ": ${_isStaycationBookingsFetched.value}")
        }

        return staycationBookings
    }

    fun parseDateRange(): Pair<Timestamp, Timestamp> {
        val dateRangeString = _dateRange.value
        val dateRangeParts = dateRangeString.split("-")
        val startDateString = dateRangeParts.firstOrNull()
        val endDateString = dateRangeParts.lastOrNull()

        // val startDate = Timestamp.valueOf(LocalDateTime.parse(startDateString, DateTimeFormatter.ofPattern("MM/dd/yyyy")))

        val startDate = LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        val endDate = LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"))

        val startTimestamp = Timestamp(startDate.atStartOfDay().toEpochSecond(ZoneOffset.ofHours(8)), 0)
        val endTimestamp = Timestamp(endDate.atTime(23, 59, 59).toEpochSecond(ZoneOffset.ofHours(8)), 0)

        Log.d("Start Time Stamp", startTimestamp.toString())
        Log.d("End Time Stamp", endTimestamp.toString())
        Log.d("Start Date", startTimestamp.toDate().toString())
        Log.d("End Date", endTimestamp.toDate().toString())

        return Pair(startTimestamp, endTimestamp)
    }


//    suspend fun fetchStaycationBookings(): List<StaycationBooking> {
//
//        _isFetchingStaycationBookings.value = true
//
//       // val month = _selectedReportFilter.value
//        val month = "All"
//        val staycationBookings = mutableListOf<StaycationBooking>()
//
//        try {
//            val query = if (month != "All") {
//
//                val calendar = Calendar.getInstance()
//                val dateFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
//                calendar.time = dateFormat.parse(month) ?: Date()
//
//                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
//                calendar.set(Calendar.YEAR, currentYear)
//
//                val startDate = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.time
//
//                // Set the calendar to the last day of the current month
//                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
//                calendar.set(Calendar.HOUR_OF_DAY, 23)
//                calendar.set(Calendar.MINUTE, 59)
//                calendar.set(Calendar.SECOND, 59)
//                calendar.set(Calendar.MILLISECOND, 999)
//
//                val endDate = calendar.time
//
//                db.collection("staycation_booking")
//                    .whereGreaterThanOrEqualTo("bookingDate", startDate)
//                    .whereLessThanOrEqualTo("bookingDate", endDate)
//                    .orderBy("bookingDate", Query.Direction.ASCENDING)
//            } else {
//                db.collection("staycation_booking")
//                    .orderBy("bookingDate", Query.Direction.ASCENDING)
//            }
//
//            val querySnapshot = query.get().await()
//
//
//            for (document in querySnapshot.documents) {
//                val additionalFee = document.getDouble("additionalFee") ?: 0.0
//                val bookingDate = document.getTimestamp("bookingDate")?.toDate() ?: Date()
//                val bookingStatus = document.getString("bookingStatus") ?: ""
//                val checkInDate = document.getTimestamp("checkInDate")?.toDate() ?: Date()
//                val checkOutDate = document.getTimestamp("checkOutDate")?.toDate() ?: Date()
//                val commission = document.getDouble("commission") ?: 0.0
//                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
//                val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
//                val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
//                val staycationId = document.getString("staycationId") ?: ""
//                val totalAmount = document.getDouble("totalAmount") ?: 0.0
//                val touristId = document.getString("touristId") ?: ""
//
//
//
//                val staycation = fetchStaycationDocumentById(staycationId)
//
//             //   Log.d("Host Id", staycation.hostId)
//
//                val host = fetchTouristData(staycation.hostId.substring(5))
//                val tourist = fetchTouristData(touristId)
//
//                val staycationBooking = StaycationBooking(
//                    host,
//                    tourist,
//                    staycation,
//                    checkInDate,
//                    checkOutDate,
//                    noOfGuests,
//                    noOfInfants,
//                    noOfPets,
//                    additionalFee,
//                    totalAmount,
//                    commission,
//                    bookingStatus,
//                    bookingDate
//                )
//
//
//                staycationBookings.add(staycationBooking)
//
//                Log.d("Staycation Booking", staycationBooking.toString())
//            }
//
//            _staycationBookingReports.value = staycationBookings
//
//        } catch (e: Exception) {
//            Log.e("StaycationRepository", "Error fetching staycation bookings: ${e.message}")
//        } finally {
//            _isFetchingStaycationBookings.value = false
//            _isStaycationBookingsFetched.value = true
//        }
//
//        return staycationBookings
////    }
//
//    suspend fun fetchTourBookings(): List<TourBooking> {
//
//        _isFetchingTourBookings.value = true
//
//        //  val month = _selectedReportFilter.value
//        val month = "All"
//        val tourBookings = mutableListOf<TourBooking>()
//
//        try {
//            val query = if (month != "All") {
//
//                val calendar = Calendar.getInstance()
//                val dateFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
//                calendar.time = dateFormat.parse(month) ?: Date()
//
//                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
//                calendar.set(Calendar.YEAR, currentYear)
//
//                val startDate = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.time
//
//                // Set the calendar to the last day of the current month
//                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
//                calendar.set(Calendar.HOUR_OF_DAY, 23)
//                calendar.set(Calendar.MINUTE, 59)
//                calendar.set(Calendar.SECOND, 59)
//                calendar.set(Calendar.MILLISECOND, 999)
//
//                val endDate = calendar.time
//
//                db.collection("tour_booking")
//                    .whereGreaterThanOrEqualTo("bookingDate", startDate)
//                    .whereLessThanOrEqualTo("bookingDate", endDate)
//                    .orderBy("bookingDate", Query.Direction.ASCENDING)
//            } else {
//                db.collection("tour_booking")
//                    .orderBy("bookingDate", Query.Direction.ASCENDING)
//            }
//
//            val querySnapshot = query.get().await()
//
//            for (document in querySnapshot.documents) {
//                val bookingDate = document.getTimestamp("bookingDate")?.toDate() ?: Date()
//                val bookingStatus = document.getString("bookingStatus") ?: ""
//                val commission = document.getDouble("commission") ?: 0.0
//                val endTime = document.getString("endTime") ?: ""
//                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
//                val startTime = document.getString("startTime") ?: ""
//                val totalAmount = document.getDouble("totalAmount") ?: 0.0
//                //    val tourAvailabilityId = document.getString("tourAvailabilityId") ?: ""
//                val tourDate = document.getString("tourDate") ?: ""
//                val tourId = document.getString("tourId") ?: ""
//                val touristId = document.getString("touristId") ?: ""
//
//                val tour = fetchTourDocumentById(tourId)
//                val host = fetchTouristData(tour.hostId.substring(5))
//                val tourist = fetchTouristData(touristId)
//
//                val tourBooking = TourBooking(
//                    host,
//                    tourist,
//                    tour,
//                    noOfGuests,
//                    startTime,
//                    endTime,
//                    tourDate,
//                    totalAmount,
//                    commission,
//                    bookingStatus,
//                    bookingDate
//                )
//
//
//                tourBookings.add(tourBooking)
//
//                Log.d("Tour Booking", tourBooking.toString())
//            }
//
//            _tourBookingReports.value = tourBookings
//
//            //  Log.d("Tour Bookings", _tourBookingReports.value.toString())
//
//        } catch (e: Exception) {
//            Log.e("Tour", "Error fetching tour bookings: ${e.message}")
//        } finally {
//            _isFetchingTourBookings.value = false
//            _isTourBookingsFetched.value = true
//        }
//
//        return tourBookings
//    }


    private suspend fun fetchTourDocumentById(documentId: String): Tour {

        val tourRef = db.collection("tour").document(documentId)

        return try {

            val documentSnapshot = tourRef.get().await()

            if (documentSnapshot.exists()) {
                val hostId = documentSnapshot.getString("hostId") ?: ""
                val tourTitle = documentSnapshot.getString("tourTitle") ?: ""
//                val tourContact = documentSnapshot.getString("tourContact") ?: ""
//                val tourDescription = documentSnapshot.getString("tourDescription") ?: ""
//                val tourDuration = documentSnapshot.getString("tourDuration") ?: ""
//                val tourEmail = documentSnapshot.getString("tourEmail") ?: ""
//                val tourFacebook = documentSnapshot.getString("tourFacebook") ?: ""
//                val tourInstagram = documentSnapshot.getString("tourInstagram") ?: ""
//                val tourLanguage = documentSnapshot.getString("tourLanguage") ?: ""
//                val tourLat = documentSnapshot.getDouble("tourLat") ?: 0.0
//                val tourLng = documentSnapshot.getDouble("tourLng") ?: 0.0
//                val tourLocation = documentSnapshot.getString("tourLocation") ?: ""
//                val tourPrice = documentSnapshot.getDouble("tourPrice") ?: 0.0
 //               val tourType = documentSnapshot.getString("tourType") ?: ""


                Tour(
                    tourId = documentId,
                    tourTitle = tourTitle,
                    hostId = hostId,
//                    tourContact = tourContact,
//                    tourDescription = tourDescription,
//                    tourDuration = tourDuration,
//                    tourEmail = tourEmail,
//                    tourFacebook = tourFacebook,
//                    tourInstagram = tourInstagram,
//                    tourLanguage = tourLanguage,
//                    tourLat = tourLat,
//                    tourLng = tourLng,
//                    tourLocation = tourLocation,
//                    tourPrice = tourPrice,
//
//                    tourType = tourType,

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

        val staycationRef = db.collection("staycation").document(documentId)

        return try {
            val documentSnapshot = staycationRef.get().await()

            if (documentSnapshot.exists()) {
                val hostId = documentSnapshot.getString("hostId") ?: ""
                val staycationTitle = documentSnapshot.getString("staycationTitle") ?: ""
//                val noOfGuests = documentSnapshot.getLong("noOfGuests")?.toInt() ?: 0
//                val noOfBedrooms = documentSnapshot.getLong("noOfBedrooms")?.toInt() ?: 0
//                val noOfBeds = documentSnapshot.getLong("noOfBeds")?.toInt() ?: 0
//                val noOfBathrooms = documentSnapshot.getLong("noOfBathrooms")?.toInt() ?: 0
//                val staycationDescription = documentSnapshot.getString("staycationDescription") ?: ""
//                val staycationLocation = documentSnapshot.getString("staycationLocation") ?: ""
//                val staycationLat = documentSnapshot.getDouble("staycationLat") ?: 0.0
//                val staycationLng = documentSnapshot.getDouble("staycationLng") ?: 0.0
//                val staycationPrice = documentSnapshot.getDouble("staycationPrice") ?: 0.0
//                val staycationSpace = documentSnapshot.getString("staycationSpace") ?: ""
//                val staycationType = documentSnapshot.getString("staycationType") ?: ""
//                val hasSecurityCamera = documentSnapshot.getBoolean("hasSecurityCamera") ?: false
//                val hasWeapon = documentSnapshot.getBoolean("hasWeapon") ?: false
//                val hasDangerousAnimal = documentSnapshot.getBoolean("hasDangerousAnimal") ?: false
//                val hasFirstAid = documentSnapshot.getBoolean("hasFirstAid") ?: false
//                val hasFireExit = documentSnapshot.getBoolean("hasFireExit") ?: false
//                val hasFireExtinguisher = documentSnapshot.getBoolean("hasFireExtinguisher") ?: false
//                val maxNoOfGuests = documentSnapshot.getLong("maxNoOfGuests")?.toInt() ?: 0
//                val additionalFeePerGuest = documentSnapshot.getDouble("additionalFeePerGuest") ?: 0.0
//                val noisePolicy = documentSnapshot.getBoolean("noisePolicy") ?: false
//                val allowSmoking = documentSnapshot.getBoolean("allowSmoking") ?: false
//                val allowPets = documentSnapshot.getBoolean("allowPets") ?: false
//                val additionalInfo = documentSnapshot.getString("additionalInfo") ?: ""
//                val noCancel = documentSnapshot.getBoolean("noCancel") ?: false
//                val noReschedule = documentSnapshot.getBoolean("noReschedule") ?: false
//                val phoneNo = documentSnapshot.getString("phoneNo") ?: ""
//                val email = documentSnapshot.getString("email") ?: ""

                Staycation(
                    hostId = hostId,
                    staycationTitle = staycationTitle,
                    staycationId = documentId,
//                    additionalFeePerGuest = additionalFeePerGuest,
//                    additionalInfo = additionalInfo,
//                    allowPets = allowPets,
//                    allowSmoking = allowSmoking,
//                    email = email,
//                    hasDangerousAnimal = hasDangerousAnimal,
//                    hasFireExit = hasFireExit,
//                    hasFireExtinguisher = hasFireExtinguisher,
//                    hasFirstAid = hasFirstAid,
//                    hasSecurityCamera = hasSecurityCamera,
//                    hasWeapon = hasWeapon,
//                    maxNoOfGuests = maxNoOfGuests,
//                    noCancel = noCancel,
//                    noOfBathrooms = noOfBathrooms,
//                    noOfBedrooms = noOfBedrooms,
//                    noOfBeds = noOfBeds,
//                    noOfGuests = noOfGuests,
//                    noReschedule = noReschedule,
//                    noisePolicy = noisePolicy,
//                    phoneNo = phoneNo,
//                    staycationDescription = staycationDescription,
//                    staycationLat = staycationLat,
//                    staycationLng = staycationLng,
//                    staycationLocation = staycationLocation,
//                    staycationPrice = staycationPrice,
//                    staycationSpace = staycationSpace,
//                    staycationType = staycationType
                )
            } else {
                Staycation()
            }


        } catch (e: Exception) {
            Log.e("StaycationRepository", "Error fetching staycation document: ${e.message}")
            Staycation()
        }
    }


    suspend fun fetchPendingVerifications() {

        _isFetchingPendingVerifications.value = true

        val collectionRef = db.collection("tourist_verification")

        try {
            val documents = collectionRef
                .whereEqualTo("verificationStatus", "For Approval")
                .get()
                .await()

            Log.d("TouristVerificationViewModel", "Fetching pending verifications...")
            val touristVerifications = mutableListOf<TouristVerification>()

            for (document in documents) {
                val verificationId = document.id
                val firstValidIdType = document.getString("firstValidIdType") ?: ""
                val firstValidIdUrl = document.getString("firstValidIdUrl") ?: ""
                val secondValidIdType = document.getString("secondValidIdType") ?: ""
                val secondValidIdUrl = document.getString("secondValidIdUrl") ?: ""
                val touristId = document.getString("touristId") ?: ""
                val verificationStatus = document.getString("verificationStatus") ?: ""
                val timestampInDate = document.getTimestamp("timestamp")?.toDate() ?: Date()

                Log.d("secondValidIdType", secondValidIdType)

                Log.d("touristId", touristId)

                val tourist = fetchTouristData(touristId)

                Log.d("Tourist", tourist.toString())

                val touristVerification = TouristVerification(
                    verificationId = verificationId,
                    firstValidIdType = firstValidIdType,
                    firstValidIdUrl = firstValidIdUrl,
                    secondValidIdType = secondValidIdType,
                    secondValidIdUrl = secondValidIdUrl,
                    touristId = touristId,
                    userFullName = "${tourist.firstName} ${tourist.lastName}",
                    userUsername = tourist.userName,
                    userImage = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
                    reportDateTime = timestampInDate,
                    verificationStatus = verificationStatus
                )
                touristVerifications.add(touristVerification)
            }
            _pendingVerifications.value = touristVerifications

            Log.d("Verification", _pendingVerifications.value.toString())
            Log.d("TouristVerificationViewModel", "Fetching pending verifications successful.")
        } catch (e: Exception) {
            Log.e("TouristVerificationViewModel", "Error fetching pending verifications: ${e.message}")
            // Handle failure
        } finally {
            _isFetchingPendingVerifications.value = false
        }
    }

    private suspend fun fetchTouristData(touristId: String): Tourist {
        val documentRef = db.collection("tourist").document(touristId)
        return try {
            val documentSnapshot = documentRef.get().await()
            if (documentSnapshot.exists()) {

/*                val fullName = documentSnapshot.get("fullName") as? Map<*, *>
                val firstName = fullName?.get("firstName") as? String ?: ""
                val middleName = fullName?.get("middleName") as? String ?: ""
                val lastName = fullName?.get("lastName") as? String ?: ""
                val username = documentSnapshot.getString("username") ?: ""*/

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

    suspend fun generateExcelFile(context: Context) {
        withContext(Dispatchers.IO) {

            _isGeneratingExcel.value = true

            try {



               // val selectedFilter = _selectedReportFilter.value
                val selectedFilter = "All"
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val transactionType = when (selectedFilter) {
                    "All" -> "All time transaction"
                    "January" -> "January $currentYear transaction"
                    "February" -> "February $currentYear transaction"
                    "March" -> "March $currentYear transaction"
                    "April" -> "April $currentYear transaction"
                    "May" -> "May $currentYear transaction"
                    "June" -> "June $currentYear transaction"
                    "July" -> "July $currentYear transaction"
                    "August" -> "August $currentYear transaction"
                    "September" -> "September $currentYear transaction"
                    "October" -> "October $currentYear transaction"
                    "November" -> "November $currentYear transaction"
                    "December" -> "December $currentYear transaction"
                    else -> "All time transaction"
                }

                val workbook: Workbook = XSSFWorkbook()
                val sheet: Sheet = workbook.createSheet("Staycation Bookings")


                val staycationBookingsDeferred = async { fetchStaycationBookings() }
                val tourBookingsDeferred = async { fetchTourBookings() }


                val staycationBookings = staycationBookingsDeferred.await()
                val tourBookings = tourBookingsDeferred.await()

                // Calculate total commission
                var totalCommission = 0.0
                for (booking in staycationBookings) {
                    totalCommission += booking.commission
                }

                val dateGeneratedFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
                val formattedDate = dateGeneratedFormat.format(Date())

                val timeGeneratedFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val formattedTime = timeGeneratedFormat.format(Date())

                val headers = arrayOf(
                    "Host Full Name",
                    "Tourist Full Name",
                    "Staycation Title",
                    "Check-in Date",
                    "Check-out Date",
                    "No. of Guests",
                    "No. of Infants",
                    "No. of Pets",
                    "Additional Fee",
                    "Total Amount",
                    "Commission",
                    "Booking Status",
                    "Booking Date"
                )

                val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault())
                val decimalFormat = DecimalFormat("#.##")

                val font = workbook.createFont()
                font.fontHeightInPoints = 16 // Adjust the font size as needed

                val startAlignmentStyle = workbook.createCellStyle()
                startAlignmentStyle.alignment = HorizontalAlignment.LEFT
                startAlignmentStyle.setFont(font)

                // Add title, "TripNila", and date generated cells in the same row
                val titleAndTripNilaRow: Row = sheet.createRow(1)

                // Add title cell
                val titleCell: Cell = titleAndTripNilaRow.createCell(2)
                titleCell.setCellValue("Staycation Bookings")

                // Merge cells for the title row
                val titleCellRange = CellRangeAddress(1, 1, 2, 10)
                sheet.addMergedRegion(titleCellRange)

                val titleCellStyle = workbook.createCellStyle()
                titleCellStyle.alignment = HorizontalAlignment.CENTER
                titleCellStyle.setFont(font)
                titleCell.cellStyle = titleCellStyle


                // Add "TripNila" cell
                val tripNilaCell: Cell = titleAndTripNilaRow.createCell(0)
                tripNilaCell.setCellValue("TripNila")

                // Create cell style for center alignment
                val centerAlignmentStyle = workbook.createCellStyle()
                centerAlignmentStyle.alignment = HorizontalAlignment.CENTER
                centerAlignmentStyle.setFont(font)

                // Apply the center alignment style to the "TripNila" cell
                tripNilaCell.cellStyle = startAlignmentStyle

                titleAndTripNilaRow.heightInPoints = 30f


                // Add date generated cell
                val dateGeneratedCell: Cell = titleAndTripNilaRow.createCell(headers.size - 1)
                dateGeneratedCell.setCellValue(formattedDate)
                dateGeneratedCell.cellStyle = centerAlignmentStyle

                // Create time generated row
                val timeGeneratedRow: Row = sheet.createRow(2)

                // Add time generated cell below date generated cell
                val timeGeneratedCell: Cell = timeGeneratedRow.createCell(headers.size - 1)
                timeGeneratedCell.setCellValue(formattedTime)
                timeGeneratedCell.cellStyle = centerAlignmentStyle

                // Merge cells for the "type" cell
                val mergedRegion = CellRangeAddress(
                    2, // start row
                    2, // end row
                    0, // start column (type cell)
                    1 // end column (type cell)
                )
                sheet.addMergedRegion(mergedRegion)

                val typeCell: Cell = timeGeneratedRow.createCell(0) // Adjust the column index as needed

// Set the content of the new cell
                typeCell.setCellValue(transactionType)
                // Apply center alignment style to the new cell
                typeCell.cellStyle = startAlignmentStyle

                timeGeneratedRow.heightInPoints = 30f

                // Create header row
                val headerRow: Row = sheet.createRow(4)

                // Set header values and cell styles
                for ((index, header) in headers.withIndex()) {
                    val cell = headerRow.createCell(index)
                    cell.setCellValue(header)

                    // Set cell style for alignment
                    val cellStyle = workbook.createCellStyle()
                    cellStyle.alignment = HorizontalAlignment.CENTER
                    cell.cellStyle = cellStyle

                    // Calculate and set column width based on header length
                    val columnWidth = (header.length + 1) * 320 // Adjust the multiplier as needed
                    sheet.setColumnWidth(index, columnWidth)
                }

                val commissionFont = workbook.createFont()
                commissionFont.fontHeightInPoints = 12

                val commissionCellStyle = workbook.createCellStyle()
                commissionCellStyle.setFont(font)
                commissionCellStyle.alignment = HorizontalAlignment.CENTER

                // Create total commission cell
                val totalCommissionRow: Row = sheet.createRow(5 + staycationBookings.size) // Place total commission cell after the last booking row
                val totalCommissionCell: Cell = totalCommissionRow.createCell(10) // Commission column index is 10
                totalCommissionCell.setCellValue(decimalFormat.format(totalCommission))
                totalCommissionCell.cellStyle = commissionCellStyle

                // Create label cell for total commission
                val labelCell: Cell = totalCommissionRow.createCell(8) // Place label cell before the commission value
                labelCell.setCellValue("Total Commission =")
                labelCell.cellStyle = commissionCellStyle

                // Merge cells for the label row
                val labelCellRange = CellRangeAddress(5 + staycationBookings.size, 5 + staycationBookings.size, 8, 9)
                sheet.addMergedRegion(labelCellRange)


                for ((index, booking) in staycationBookings.withIndex()) {
                    val row: Row = sheet.createRow(index + 5)
                    for (i in 0..12) {
                        val cell = row.createCell(i)
                        val cellStyle: CellStyle = sheet.workbook.createCellStyle()
                        cellStyle.alignment = HorizontalAlignment.CENTER
                        cell.cellStyle = cellStyle

                        when (i) {
                            0 -> cell.setCellValue("${booking.host.firstName} ${booking.host.middleName} ${booking.host.lastName}")
                            1 -> cell.setCellValue("${booking.tourist.firstName} ${booking.tourist.middleName} ${booking.tourist.lastName}")
                            2 -> cell.setCellValue(booking.staycation.staycationTitle)
                            3 -> cell.setCellValue(dateFormat.format(booking.checkInDate))
                            4 -> cell.setCellValue(dateFormat.format(booking.checkOutDate))
                            5 -> cell.setCellValue(booking.noOfGuests.toDouble())
                            6 -> cell.setCellValue(booking.noOfInfants.toDouble())
                            7 -> cell.setCellValue(booking.noOfPets.toDouble())
                            8 -> cell.setCellValue(decimalFormat.format(booking.additionalFee))
                            9 -> cell.setCellValue(decimalFormat.format(booking.totalAmount))
                            10 -> cell.setCellValue(decimalFormat.format(booking.commission))
                            11 -> cell.setCellValue(booking.bookingStatus)
                            12 -> cell.setCellValue(dateFormat.format(booking.bookingDate))
                        }
                    }
                }

                val tourSheet: Sheet = workbook.createSheet("Tour Bookings")

                totalCommission = 0.0
                for (booking in tourBookings) {
                    totalCommission += booking.commission
                }

                val tourHeaders = arrayOf(
                    "Host Full Name",
                    "Tourist Full Name",
                    "Tour Title",
                    "Booked Schedule",
                    "No. of Guests",
                    "Total Amount",
                    "Commission",
                    "Booking Status",
                    "Booking Date"
                )

                // Add title, "TripNila", and date generated cells in the same row
                val titleAndDateRow: Row = tourSheet.createRow(1)

                val tourTitleCell: Cell = titleAndDateRow.createCell(2)
                tourTitleCell.setCellValue("Tour Bookings")

                val tourTitleCellRange = CellRangeAddress(1, 1, 2, 7)
                tourSheet.addMergedRegion(tourTitleCellRange)

                tourTitleCell.cellStyle = titleCellStyle

                val tourTripNilaCell: Cell = titleAndDateRow.createCell(0)
                tourTripNilaCell.setCellValue("TripNila")

                tourTripNilaCell.cellStyle = startAlignmentStyle

                titleAndDateRow.heightInPoints = 30f

                val tourDateGeneratedCell: Cell = titleAndDateRow.createCell(tourHeaders.size - 1)
                tourDateGeneratedCell.setCellValue(formattedDate)
                tourDateGeneratedCell.cellStyle = centerAlignmentStyle

                val tourTimeGeneratedRow: Row = tourSheet.createRow(2)

                val tourTimeGeneratedCell: Cell = tourTimeGeneratedRow.createCell(tourHeaders.size - 1)
                tourTimeGeneratedCell.setCellValue(formattedTime)
                tourTimeGeneratedCell.cellStyle = centerAlignmentStyle

                tourSheet.addMergedRegion(mergedRegion)

                val tourTypeCell: Cell = tourTimeGeneratedRow.createCell(0) // Adjust the column index as needed

// Set the content of the new cell
                tourTypeCell.setCellValue(transactionType)
                // Apply center alignment style to the new cell
                tourTypeCell.cellStyle = startAlignmentStyle

                tourTimeGeneratedRow.heightInPoints = 30f

                val tourHeaderRow: Row = tourSheet.createRow(4)

                for ((index, header) in tourHeaders.withIndex()) {
                    val cell = tourHeaderRow.createCell(index)
                    cell.setCellValue(header)

                    // Set cell style for alignment
                    val cellStyle = workbook.createCellStyle()
                    cellStyle.alignment = HorizontalAlignment.CENTER
                    cell.cellStyle = cellStyle

                    // Calculate and set column width based on header length
                    val columnWidth = (header.length + 1) * 320 // Adjust the multiplier as needed
                    tourSheet.setColumnWidth(index, columnWidth)
                }


                // Create total commission cell
                val totalTourCommissionRow: Row = tourSheet.createRow(5 + tourBookings.size) // Place total commission cell after the last booking row
                val totalTourCommissionCell: Cell = totalTourCommissionRow.createCell(6) // Commission column index is 10
                totalTourCommissionCell.setCellValue(decimalFormat.format(totalCommission))
                totalTourCommissionCell.cellStyle = commissionCellStyle

                // Create label cell for total commission
                val tourLabelCell: Cell = totalTourCommissionRow.createCell(4) // Place label cell before the commission value
                tourLabelCell.setCellValue("Total Commission =")
                tourLabelCell.cellStyle = commissionCellStyle

                // Merge cells for the label row
                val tourLabelCellRange = CellRangeAddress(5 + tourBookings.size, 5 + tourBookings.size, 4, 5)
                tourSheet.addMergedRegion(tourLabelCellRange)

                // Iterate through tour bookings and populate rows
                for ((index, booking) in tourBookings.withIndex()) {
                    val row: Row = tourSheet.createRow(index + 5)
                    for (i in tourHeaders.indices) {
                        val cell = row.createCell(i)
                        val cellStyle: CellStyle = tourSheet.workbook.createCellStyle()
                        cellStyle.alignment = HorizontalAlignment.CENTER
                        cell.cellStyle = cellStyle

                        when (i) {
                            0 -> cell.setCellValue("${booking.host.firstName} ${booking.host.middleName} ${booking.host.lastName}")
                            1 -> cell.setCellValue("${booking.tourist.firstName} ${booking.tourist.middleName} ${booking.tourist.lastName}")
                            2 -> cell.setCellValue(booking.tour.tourTitle)
                            3 -> cell.setCellValue("${booking.tourDate} ${booking.startTime} - ${booking.endTime}")
                            4 -> cell.setCellValue(booking.noOfGuests.toDouble())
                            5 -> cell.setCellValue(decimalFormat.format(booking.totalAmount))
                            6 -> cell.setCellValue(decimalFormat.format(booking.commission))
                            7 -> cell.setCellValue(booking.bookingStatus)
                            8 -> cell.setCellValue(dateFormat.format(booking.bookingDate))
                        }
                    }
                }


                val fileName = "transactions_${System.currentTimeMillis()}.xlsx"
                val file = File(context.getExternalFilesDir(null), fileName)

                // Write the workbook to the file
                val fos = FileOutputStream(file)
                workbook.write(fos)
                fos.close()

                // Display toast message
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Report generated successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error generating report file: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                _isGeneratingExcel.value = false
            }
        }

    }



}



/*suspend fun fetchTourBookings(): List<TourBooking> {

    _isFetchingTourBookings.value = true

    val tourBookings = mutableListOf<TourBooking>()

    try {
        val querySnapshot = db.collection("tour_booking")
            .orderBy("bookingDate", Query.Direction.ASCENDING)
            .get()
            .await()

        for (document in querySnapshot.documents) {
            val bookingDate = document.getTimestamp("bookingDate")?.toDate() ?: Date()
            val bookingStatus = document.getString("bookingStatus") ?: ""
            val commission = document.getDouble("commission") ?: 0.0
            val endTime = document.getString("endTime") ?: ""
            val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
            val startTime = document.getString("startTime") ?: ""
            val totalAmount = document.getDouble("totalAmount") ?: 0.0
        //    val tourAvailabilityId = document.getString("tourAvailabilityId") ?: ""
            val tourDate = document.getString("tourDate") ?: ""
            val tourId = document.getString("tourId") ?: ""
            val touristId = document.getString("touristId") ?: ""

            val tour = fetchTourDocumentById(tourId)
            val host = fetchTouristData(tour.hostId.substring(5))
            val tourist = fetchTouristData(touristId)

            val tourBooking = TourBooking(
                host,
                tourist,
                tour,
                noOfGuests,
                startTime,
                endTime,
                tourDate,
                totalAmount,
                commission,
                bookingStatus,
                bookingDate
            )


            tourBookings.add(tourBooking)
        }

        _tourBookingReports.value = tourBookings

        Log.d("Tour Bookings", _tourBookingReports.value.toString())

    } catch (e: Exception) {
        Log.e("Tour", "Error fetching tour bookings: ${e.message}")
    } finally {
        _isFetchingTourBookings.value = false
    }

    return tourBookings
}



suspend fun fetchStaycationBookings(): List<StaycationBooking> {

    _isFetchingStaycationBookings.value = true

    val staycationBookings = mutableListOf<StaycationBooking>()

    try {
        val querySnapshot = db.collection("staycation_booking")
            .orderBy("bookingDate", Query.Direction.ASCENDING)
            .get()
            .await()

        for (document in querySnapshot.documents) {
            val additionalFee = document.getDouble("additionalFee") ?: 0.0
            val bookingDate = document.getTimestamp("bookingDate")?.toDate() ?: Date()
            val bookingStatus = document.getString("bookingStatus") ?: ""
            val checkInDate = document.getTimestamp("checkInDate")?.toDate() ?: Date()
            val checkOutDate = document.getTimestamp("checkOutDate")?.toDate() ?: Date()
            val commission = document.getDouble("commission") ?: 0.0
            val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
            val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
            val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
            val staycationId = document.getString("staycationId") ?: ""
            val totalAmount = document.getDouble("totalAmount") ?: 0.0
            val touristId = document.getString("touristId") ?: ""



            val staycation = fetchStaycationDocumentById(staycationId)

            Log.d("Host Id", staycation.hostId)

            val host = fetchTouristData(staycation.hostId.substring(5))
            val tourist = fetchTouristData(touristId)

            val staycationBooking = StaycationBooking(
                host,
                tourist,
                staycation,
                checkInDate,
                checkOutDate,
                noOfGuests,
                noOfInfants,
                noOfPets,
                additionalFee,
                totalAmount,
                commission,
                bookingStatus,
                bookingDate
            )


            staycationBookings.add(staycationBooking)
        }

        _staycationBookingReports.value = staycationBookings

    } catch (e: Exception) {
        Log.e("StaycationRepository", "Error fetching staycation bookings: ${e.message}")
    } finally {
        _isFetchingStaycationBookings.value = false
    }

    return staycationBookings
}
*/

/*suspend fun generateExcelFile(context: Context) {
    withContext(Dispatchers.IO) {
        try {


            val workbook: Workbook = XSSFWorkbook()
            val sheet: Sheet = workbook.createSheet("Staycation Bookings")

            val bookings = _staycationBookingReports.value

            // Calculate total commission
            var totalCommission = 0.0
            for (booking in bookings) {
                totalCommission += booking.commission
            }

            val headers = arrayOf(
                "Host Full Name",
                "Tourist Full Name",
                "Staycation Title",
                "Check-in Date",
                "Check-out Date",
                "No. of Guests",
                "No. of Infants",
                "No. of Pets",
                "Additional Fee",
                "Total Amount",
                "Commission",
                "Booking Status",
                "Booking Date"
            )

            val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault())
            val decimalFormat = DecimalFormat("#.##")

            val font = workbook.createFont()
            font.fontHeightInPoints = 16 // Adjust the font size as needed

            // Add title, "TripNila", and date generated cells in the same row
            val titleAndTripNilaRow: Row = sheet.createRow(1)

            // Add title cell
            val titleCell: Cell = titleAndTripNilaRow.createCell(2)
            titleCell.setCellValue("Staycation Bookings")

            // Merge cells for the title row
            val titleCellRange = CellRangeAddress(1, 1, 2, 10)
            sheet.addMergedRegion(titleCellRange)

            val titleCellStyle = workbook.createCellStyle()
            titleCellStyle.alignment = HorizontalAlignment.CENTER
            titleCellStyle.setFont(font)
            titleCell.cellStyle = titleCellStyle


            // Add "TripNila" cell
            val tripNilaCell: Cell = titleAndTripNilaRow.createCell(0)
            tripNilaCell.setCellValue("TripNila")

            // Create cell style for center alignment
            val centerAlignmentStyle = workbook.createCellStyle()
            centerAlignmentStyle.alignment = HorizontalAlignment.CENTER
            centerAlignmentStyle.setFont(font)

            // Apply the center alignment style to the "TripNila" cell
            tripNilaCell.cellStyle = centerAlignmentStyle

            titleAndTripNilaRow.heightInPoints = 30f


            // Add date generated cell
            val dateGeneratedCell: Cell = titleAndTripNilaRow.createCell(headers.size - 1)
            dateGeneratedCell.setCellValue(SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(Date()))
            dateGeneratedCell.cellStyle = centerAlignmentStyle

            // Create time generated row
            val timeGeneratedRow: Row = sheet.createRow(2)

            // Add time generated cell below date generated cell
            val timeGeneratedCell: Cell = timeGeneratedRow.createCell(headers.size - 1)
            timeGeneratedCell.setCellValue(SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()))
            timeGeneratedCell.cellStyle = centerAlignmentStyle

            timeGeneratedRow.heightInPoints = 30f

            // Create header row
            val headerRow: Row = sheet.createRow(4)

            // Set header values and cell styles
            for ((index, header) in headers.withIndex()) {
                val cell = headerRow.createCell(index)
                cell.setCellValue(header)

                // Set cell style for alignment
                val cellStyle = workbook.createCellStyle()
                cellStyle.alignment = HorizontalAlignment.CENTER
                cell.cellStyle = cellStyle

                // Calculate and set column width based on header length
                val columnWidth = (header.length + 1) * 320 // Adjust the multiplier as needed
                sheet.setColumnWidth(index, columnWidth)
            }

            val commissionFont = workbook.createFont()
            commissionFont.fontHeightInPoints = 12

            val commissionCellStyle = workbook.createCellStyle()
            commissionCellStyle.setFont(font)
            commissionCellStyle.alignment = HorizontalAlignment.CENTER

            // Create total commission cell
            val totalCommissionRow: Row = sheet.createRow(5 + bookings.size) // Place total commission cell after the last booking row
            val totalCommissionCell: Cell = totalCommissionRow.createCell(10) // Commission column index is 10
            totalCommissionCell.setCellValue(decimalFormat.format(totalCommission))
            totalCommissionCell.cellStyle = commissionCellStyle

            // Create label cell for total commission
            val labelCell: Cell = totalCommissionRow.createCell(8) // Place label cell before the commission value
            labelCell.setCellValue("Total Commission =")
            labelCell.cellStyle = commissionCellStyle

            // Merge cells for the label row
            val labelCellRange = CellRangeAddress(5 + bookings.size, 5 + bookings.size, 8, 9)
            sheet.addMergedRegion(labelCellRange)


            for ((index, booking) in bookings.withIndex()) {
                val row: Row = sheet.createRow(index + 5)
                for (i in 0..12) {
                    val cell = row.createCell(i)
                    val cellStyle: CellStyle = sheet.workbook.createCellStyle()
                    cellStyle.alignment = HorizontalAlignment.CENTER
                    cell.cellStyle = cellStyle

                    when (i) {
                        0 -> cell.setCellValue("${booking.host.firstName} ${booking.host.middleName} ${booking.host.lastName}")
                        1 -> cell.setCellValue("${booking.tourist.firstName} ${booking.tourist.middleName} ${booking.tourist.lastName}")
                        2 -> cell.setCellValue(booking.staycation.staycationTitle)
                        3 -> cell.setCellValue(dateFormat.format(booking.checkInDate))
                        4 -> cell.setCellValue(dateFormat.format(booking.checkOutDate))
                        5 -> cell.setCellValue(booking.noOfGuests.toDouble())
                        6 -> cell.setCellValue(booking.noOfInfants.toDouble())
                        7 -> cell.setCellValue(booking.noOfPets.toDouble())
                        8 -> cell.setCellValue(decimalFormat.format(booking.additionalFee))
                        9 -> cell.setCellValue(decimalFormat.format(booking.totalAmount))
                        10 -> cell.setCellValue(decimalFormat.format(booking.commission))
                        11 -> cell.setCellValue(booking.bookingStatus)
                        12 -> cell.setCellValue(dateFormat.format(booking.bookingDate))
                    }
                }
            }

            *//*for ((index, booking) in bookings.withIndex()) {
                val row: Row = sheet.createRow(index + 5)
                var cellStyle = workbook.createCellStyle()
                cellStyle.alignment = HorizontalAlignment.CENTER

                row.createCell(0).apply {
                    setCellValue("${booking.host.firstName} ${booking.host.middleName} ${booking.host.lastName}")
                }
                row.createCell(1).apply {
                    setCellValue("${booking.tourist.firstName} ${booking.tourist.middleName} ${booking.tourist.lastName}")
                }
                row.createCell(2).apply {
                    setCellValue(booking.staycation.staycationTitle)
                }
                row.createCell(3).apply {
                    setCellValue(dateFormat.format(booking.checkInDate))
                }
                row.createCell(4).apply {
                    setCellValue(dateFormat.format(booking.checkOutDate))
                }
                row.createCell(5).apply {
                    setCellValue(booking.noOfGuests.toDouble())
                }
                row.createCell(6).apply {
                    setCellValue(booking.noOfInfants.toDouble())
                }
                row.createCell(7).apply {
                    setCellValue(booking.noOfPets.toDouble())
                }
                row.createCell(8).apply {
                    setCellValue(decimalFormat.format(booking.additionalFee))
                }
                row.createCell(9).apply {
                    setCellValue(decimalFormat.format(booking.totalAmount))
                }
                row.createCell(10).apply {
                    setCellValue(decimalFormat.format(booking.commission))
                }
                row.createCell(11).apply {
                    setCellValue(booking.bookingStatus)
                }
                row.createCell(12).apply {
                    setCellValue(dateFormat.format(booking.bookingDate))
                }
            }*//*
            *//*                for ((index, booking) in bookings.withIndex()) {
                                val row: Row = sheet.createRow(index + 5)
                                row.createCell(0)
                                    .setCellValue("${booking.host.firstName} ${booking.host.middleName} ${booking.host.lastName}")
                                row.createCell(1)
                                    .setCellValue("${booking.tourist.firstName} ${booking.tourist.middleName} ${booking.tourist.lastName}")
                                row.createCell(2).setCellValue(booking.staycation.staycationTitle)
                                row.createCell(3).setCellValue(dateFormat.format(booking.checkInDate))
                                row.createCell(4).setCellValue(dateFormat.format(booking.checkOutDate))
                                row.createCell(5).setCellValue(booking.noOfGuests.toDouble())
                                row.createCell(6).setCellValue(booking.noOfInfants.toDouble())
                                row.createCell(7).setCellValue(booking.noOfPets.toDouble())
                                row.createCell(8).setCellValue(decimalFormat.format(booking.additionalFee))
                                row.createCell(9).setCellValue(decimalFormat.format(booking.totalAmount))
                                row.createCell(10).setCellValue(decimalFormat.format(booking.commission))
                                row.createCell(11).setCellValue(booking.bookingStatus)
                                row.createCell(12).setCellValue(dateFormat.format(booking.bookingDate))
                            }*//*



            val fileName = "staycation_bookings_${System.currentTimeMillis()}.xlsx"
            val file = File(context.getExternalFilesDir(null), fileName)

            // Write the workbook to the file
            val fos = FileOutputStream(file)
            workbook.write(fos)
            fos.close()

            // Display toast message
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Excel file generated successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error generating Excel file: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}*/



/*    suspend fun generateExcelFile(context: Context, title: String) {
      withContext(Dispatchers.IO) {
          try {
              // Create a new Excel workbook
              val workbook: Workbook = XSSFWorkbook()
              val sheet: Sheet = workbook.createSheet("Pending Verifications")

              // Create title row
              val titleRow: Row = sheet.createRow(0)
              val titleCell: Cell = titleRow.createCell(0)
              titleCell.setCellValue(title)
              val titleCellStyle: CellStyle = workbook.createCellStyle()
              val font: Font = workbook.createFont()
              font.bold = true
              font.fontHeightInPoints = 16.toShort() // Adjust font size if needed
              titleCellStyle.setFont(font)
              titleCell.cellStyle = titleCellStyle
              // Merge cells for the title row
              sheet.addMergedRegion(CellRangeAddress(0, 0, 0, 10))

              // Create header row
              val headerRow: Row = sheet.createRow(1)
              val headers = arrayOf(
                  "Verification ID",
                  "First Valid ID Type",
                  "First Valid ID URL",
                  "Second Valid ID Type",
                  "Second Valid ID URL",
                  "Tourist ID",
                  "User Full Name",
                  "User Username",
                  "User Image",
                  "Report Date Time",
                  "Verification Status"
              )
              for ((index, header) in headers.withIndex()) {
                  headerRow.createCell(index).setCellValue(header)
              }

              // Add data rows
              for ((index, verification) in _pendingVerifications.value.withIndex()) {
                  val row: Row = sheet.createRow(index + 2) // Start data rows after title and header
                  row.createCell(0).setCellValue(verification.verificationId)
                  row.createCell(1).setCellValue(verification.firstValidIdType)
                  row.createCell(2).setCellValue(verification.firstValidIdUrl)
                  row.createCell(3).setCellValue(verification.secondValidIdType)
                  row.createCell(4).setCellValue(verification.secondValidIdUrl)
                  row.createCell(5).setCellValue(verification.touristId)
                  row.createCell(6).setCellValue(verification.userFullName)
                  row.createCell(7).setCellValue(verification.userUsername)
                  row.createCell(8).setCellValue(verification.userImage)
                  row.createCell(9).setCellValue(verification.reportDateTime.toString())
                  row.createCell(10).setCellValue(verification.verificationStatus)
              }

              // Write the workbook to a file
              val file = File(context.getExternalFilesDir(null), "pending_verifications_${System.currentTimeMillis()}.xlsx")
              val fos = FileOutputStream(file)
              workbook.write(fos)
              fos.close()


              Log.d("AdminReports", "Excel file generated successfully at: ${file.absolutePath}")
              withContext(Dispatchers.Main) {
                  Toast.makeText(context, "Excel file generated successfully", Toast.LENGTH_SHORT).show()
              }



          } catch (e: Exception) {
              Log.e("AdminReports", "Error generating Excel file: ${e.message}")
              withContext(Dispatchers.Main) {
                  Toast.makeText(context, "Error generating Excel file: ${e.message}", Toast.LENGTH_SHORT).show()
              }
          }
      }
  }*/




/*suspend fun generateExcelFile(context: Context, title: String) {
        withContext(Dispatchers.IO) {
            try {
                // Create a new Excel workbook
                val workbook: Workbook = XSSFWorkbook()
                val sheet: Sheet = workbook.createSheet("Pending Verifications")

                // Create title row
                val titleRow: Row = sheet.createRow(0)
                val titleCell: Cell = titleRow.createCell(0)
                titleCell.setCellValue(title)
                val titleCellStyle: CellStyle = workbook.createCellStyle()
                val font: Font = workbook.createFont()
                font.bold = true
                font.fontHeightInPoints = 16.toShort() // Adjust font size if needed
                titleCellStyle.setFont(font)
                titleCell.cellStyle = titleCellStyle
                // Merge cells for the title row
                sheet.addMergedRegion(CellRangeAddress(0, 0, 0, 10))

                // Create header row
                val headerRow: Row = sheet.createRow(1)
                val headers = arrayOf(
                    "Verification ID",
                    "First Valid ID Type",
                    "First Valid ID URL",
                    "Second Valid ID Type",
                    "Second Valid ID URL",
                    "Tourist ID",
                    "User Full Name",
                    "User Username",
                    "User Image",
                    "Report Date Time",
                    "Verification Status"
                )
                for ((index, header) in headers.withIndex()) {
                    headerRow.createCell(index).setCellValue(header)
                }

                // Add data rows
                for ((index, verification) in _pendingVerifications.value.withIndex()) {
                    val row: Row = sheet.createRow(index + 2) // Start data rows after title and header
                    row.createCell(0).setCellValue(verification.verificationId)
                    row.createCell(1).setCellValue(verification.firstValidIdType)
                    row.createCell(2).setCellValue(verification.firstValidIdUrl)
                    row.createCell(3).setCellValue(verification.secondValidIdType)
                    row.createCell(4).setCellValue(verification.secondValidIdUrl)
                    row.createCell(5).setCellValue(verification.touristId)
                    row.createCell(6).setCellValue(verification.userFullName)
                    row.createCell(7).setCellValue(verification.userUsername)
                    row.createCell(8).setCellValue(verification.userImage)
                    row.createCell(9).setCellValue(verification.reportDateTime.toString())
                    row.createCell(10).setCellValue(verification.verificationStatus)
                }

                // Write the workbook to a file
                val file = File(context.getExternalFilesDir(null), "pending_verifications_${System.currentTimeMillis()}.xlsx")
                val fos = FileOutputStream(file)
                workbook.write(fos)
                fos.close()


                Log.d("AdminReports", "Excel file generated successfully at: ${file.absolutePath}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Excel file generated successfully", Toast.LENGTH_SHORT).show()
                }



            } catch (e: Exception) {
                Log.e("AdminReports", "Error generating Excel file: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error generating Excel file: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
*/

/*03 - 07 - 2024
suspend fun generateExcelFile(context: Context) {
    withContext(Dispatchers.IO) {
        try {
            // Create a new Excel workbook
            val workbook: Workbook = XSSFWorkbook()
            val sheet: Sheet = workbook.createSheet("Pending Verifications")

            // Create header row
            val headerRow: Row = sheet.createRow(0)
            val headers = arrayOf(
                "Verification ID",
                "First Valid ID Type",
                "First Valid ID URL",
                "Second Valid ID Type",
                "Second Valid ID URL",
                "Tourist ID",
                "User Full Name",
                "User Username",
                "User Image",
                "Report Date Time",
                "Verification Status"
            )
            for ((index, header) in headers.withIndex()) {
                headerRow.createCell(index).setCellValue(header)
            }

            // Add data rows
            for ((index, verification) in _pendingVerifications.value.withIndex()) {
                val row: Row = sheet.createRow(index + 1)
                row.createCell(0).setCellValue(verification.verificationId)
                row.createCell(1).setCellValue(verification.firstValidIdType)
                row.createCell(2).setCellValue(verification.firstValidIdUrl)
                row.createCell(3).setCellValue(verification.secondValidIdType)
                row.createCell(4).setCellValue(verification.secondValidIdUrl)
                row.createCell(5).setCellValue(verification.touristId)
                row.createCell(6).setCellValue(verification.userFullName)
                row.createCell(7).setCellValue(verification.userUsername)
                row.createCell(8).setCellValue(verification.userImage)
                row.createCell(9).setCellValue(verification.reportDateTime.toString())
                row.createCell(10).setCellValue(verification.verificationStatus)
            }

            // Write the workbook to a file
            val file = File(context.getExternalFilesDir(null), "pending_verifications_${System.currentTimeMillis()}.xlsx")
            val fos = FileOutputStream(file)
            workbook.write(fos)
            fos.close()

            Log.d("AdminReports", "Excel file generated successfully at: ${file.absolutePath}")

        } catch (e: Exception) {
            Log.e("AdminReports", "Error generating Excel file: ${e.message}")
        }
    }
}
*/



/* suspend fun generateExcelFile(context: Context, title: String) {
     withContext(Dispatchers.IO) {
         try {
             // Create a new Excel workbook
             val workbook: Workbook = XSSFWorkbook()
             val sheet: Sheet = workbook.createSheet("Staycation Bookings")

             // Create title row
             val titleRow: Row = sheet.createRow(0)
             val titleCell: Cell = titleRow.createCell(0)
             titleCell.setCellValue(title)
             val titleCellStyle: CellStyle = workbook.createCellStyle()
             val font: Font = workbook.createFont()
             font.bold = true
             font.fontHeightInPoints = 16.toShort() // Adjust font size if needed
             titleCellStyle.setFont(font)
             titleCell.cellStyle = titleCellStyle
             // Merge cells for the title row
             sheet.addMergedRegion(CellRangeAddress(0, 0, 0, 10))

             // Create header row
             val headerRow: Row = sheet.createRow(1)
             val headers = arrayOf(
                 "Verification ID",
                 "First Valid ID Type",
                 "First Valid ID URL",
                 "Second Valid ID Type",
                 "Second Valid ID URL",
                 "Tourist ID",
                 "User Full Name",
                 "User Username",
                 "User Image",
                 "Report Date Time",
                 "Verification Status"
             )
             for ((index, header) in headers.withIndex()) {
                 headerRow.createCell(index).setCellValue(header)
             }

             // Add data rows
             for ((index, verification) in _pendingVerifications.value.withIndex()) {
                 val row: Row = sheet.createRow(index + 2) // Start data rows after title and header
                 row.createCell(0).setCellValue(verification.verificationId)
                 row.createCell(1).setCellValue(verification.firstValidIdType)
                 row.createCell(2).setCellValue(verification.firstValidIdUrl)
                 row.createCell(3).setCellValue(verification.secondValidIdType)
                 row.createCell(4).setCellValue(verification.secondValidIdUrl)
                 row.createCell(5).setCellValue(verification.touristId)
                 row.createCell(6).setCellValue(verification.userFullName)
                 row.createCell(7).setCellValue(verification.userUsername)
                 row.createCell(8).setCellValue(verification.userImage)
                 row.createCell(9).setCellValue(verification.reportDateTime.toString())
                 row.createCell(10).setCellValue(verification.verificationStatus)
             }

             // Write the workbook to a file
             val file = File(context.getExternalFilesDir(null), "pending_verifications_${System.currentTimeMillis()}.xlsx")
             val fos = FileOutputStream(file)
             workbook.write(fos)
             fos.close()


             Log.d("AdminReports", "Excel file generated successfully at: ${file.absolutePath}")
             withContext(Dispatchers.Main) {
                 Toast.makeText(context, "Excel file generated successfully", Toast.LENGTH_SHORT).show()
             }



         } catch (e: Exception) {
             Log.e("AdminReports", "Error generating Excel file: ${e.message}")
             withContext(Dispatchers.Main) {
                 Toast.makeText(context, "Error generating Excel file: ${e.message}", Toast.LENGTH_SHORT).show()
             }
         }
     }
 }
*/
