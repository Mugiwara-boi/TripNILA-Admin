package com.example.tripnila_admin.viewmodels
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class AdminDashboard : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _selectedYear = mutableStateOf(2024)
    val selectedYear: State<Int> = _selectedYear

    private val _aggregatedSalesData = MutableLiveData<List<MonthTotal>>()
    val aggregatedSalesData: LiveData<List<MonthTotal>> = _aggregatedSalesData
    val totalProfitForYearLiveData = MutableLiveData<Double>()

    private val _verifiedUser = MutableStateFlow(0)
    val verifiedUser = _verifiedUser.asStateFlow()

    private val _allUser = MutableStateFlow(0)
    val allUser = _allUser.asStateFlow()

    fun setVerifieduser(count: Int){
        _verifiedUser.value = count
    }

    fun setAlluser(count: Int){
        _allUser.value = count
    }

    fun setSelectedYear(year: Int) {
        _selectedYear.value = year
        getSales(year)
    }


    suspend fun getTotalCount(collectionPath: String): Int {
        return withContext(Dispatchers.IO) {
            try {
                // Perform the query to get all documents in the collection
                val querySnapshot: QuerySnapshot = db.collection(collectionPath).get().await()
                // Return the count of documents
                querySnapshot.size()
            } catch (e: Exception) {
                // Handle any exceptions here
                // For simplicity, you can print the error message
                e.printStackTrace()
                // Return a default value or handle the error as per your application logic
                -1
            }
        }
    }

    data class TodayProfitAndItemCount(val totalProfit: Double, val itemCount: Int)

    suspend fun getTodayProfitAndItemCount(): TodayProfitAndItemCount {
        val todayCalendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDayTimestamp = Timestamp(todayCalendar.time)

        return try {
            val querySnapshot = db.collection("staycation_booking")
                .whereGreaterThanOrEqualTo("bookingDate", startOfDayTimestamp)
                .get()
                .await()

            val totalProfit = querySnapshot.documents.sumOf { document ->
                document.getDouble("commission") ?: 0.0
            }

            TodayProfitAndItemCount(totalProfit, querySnapshot.size())
        } catch (e: Exception) {
            // Handle errors here
            TodayProfitAndItemCount(0.0, 0)
        }
    }

    suspend fun getProfitForDate(date: Calendar): Double {
        val timestamp = Timestamp(date.time)
        return try {
            val querySnapshot = db.collection("staycation_booking")
                .whereEqualTo("bookingDate", timestamp)
                .get()
                .await()

            querySnapshot.documents.sumOf { document ->
                document.getDouble("commission") ?: 0.0
            }
        } catch (e: Exception) {
            // Handle errors here
            0.0
        }
    }

    suspend fun getPercentageDifference(): Double {
        val todayCalendar = Calendar.getInstance()
        val yesterdayCalendar = Calendar.getInstance().apply {
            add(Calendar.DATE, -1) // Set to yesterday
        }

        val todayProfit = getProfitForDate(todayCalendar)
        val yesterdayProfit = getProfitForDate(yesterdayCalendar)

        val difference = todayProfit - yesterdayProfit

        // Avoid division by zero
        return if (yesterdayProfit != 0.0) {
            (difference / yesterdayProfit) * 100
        } else {
            // Handle case when yesterday's profit is zero
            difference
        }
    }

    data class MonthTotal(val month: Int, val totalAmount: Double)

    fun aggregateSalesByMonth(salesData: List<DocumentSnapshot>, year: Int): List<MonthTotal> {
        val salesByMonth = mutableMapOf<Int, Double>()

        // Initialize salesByMonth with zero values for all months of the year
        for (month in 1..12) {
            salesByMonth[month] = 0.0
        }

        // Iterate through the sales data to aggregate by month
        for (document in salesData) {
            val date = document.getDate("bookingDate")
            val amount = document.getDouble("commission")

            // Extract the month and year from the date
            val calendar = Calendar.getInstance()
            calendar.time = date

            val month = calendar.get(Calendar.MONTH) + 1 // Adjust for 0-based index
            val docYear = calendar.get(Calendar.YEAR)

            if (docYear == year) {
                // Aggregate the amount for the corresponding month
                val currentTotal = salesByMonth[month] ?: 0.0
                salesByMonth[month] = currentTotal + (amount ?: 0.0)
            }
        }

        // Convert the map to a list of MonthTotal objects
        val monthTotalList = mutableListOf<MonthTotal>()
        for ((month, totalAmount) in salesByMonth) {
            monthTotalList.add(MonthTotal(month, totalAmount))
        }

        return monthTotalList
    }

    fun getSales(year : Int) {
        val salesCollection = db.collection("staycation_booking")

        salesCollection.get().addOnSuccessListener { documents ->
            val salesData = documents.filter { document ->
                document.contains("bookingDate") &&
                        document.contains("commission") &&
                        document.getString("bookingStatus") == "Completed"
            }
            val aggregatedData = aggregateSalesByMonth(salesData, year)
            _aggregatedSalesData.value = aggregatedData
            // Now aggregatedData contains a list of MonthTotal objects with the month and total amount
        }
    }
    fun getProfitForYear(year: Int) {
        val salesCollection = db.collection("staycation_booking")

        salesCollection.get().addOnSuccessListener { documents ->
            val salesData = documents.filter { document ->
                document.contains("bookingDate") &&
                        document.contains("commission") &&
                        document.getString("bookingStatus") == "Completed"
            }
            val totalProfitForYear = calculateProfitForYear(salesData, year)
            totalProfitForYearLiveData.postValue(totalProfitForYear) // Assuming totalProfitForYearLiveData is a MutableLiveData<Double>
        }.addOnFailureListener { exception ->
            // Handle failure
            exception.printStackTrace()
        }
    }

    private fun calculateProfitForYear(salesData: List<DocumentSnapshot>, year: Int): Double {
        val totalProfitForYear = mutableMapOf<Int, Double>()

        // Initialize total profit for each month of the year
        for (month in 1..12) {
            totalProfitForYear[month] = 0.0
        }

        // Iterate through the sales data to aggregate by month
        for (document in salesData) {
            val date = document.getDate("bookingDate")
            val amount = document.getDouble("commission")

            // Extract the month and year from the date
            val calendar = Calendar.getInstance()
            calendar.time = date

            val month = calendar.get(Calendar.MONTH) + 1 // Adjust for 0-based index
            val docYear = calendar.get(Calendar.YEAR)

            if (docYear == year) {
                // Aggregate the amount for the corresponding month
                val currentTotal = totalProfitForYear[month] ?: 0.0
                totalProfitForYear[month] = currentTotal + (amount ?: 0.0)
            }
        }

        // Calculate the total profit for the year
        val totalProfit = totalProfitForYear.values.sum()

        return totalProfit
    }

    suspend fun getVerifiedTouristCount(): Int {
        try {
            val querySnapshot = db.collection("tourist_verification")
                .whereEqualTo("verificationStatus", "Approved")
                .get()
                .await()

            return querySnapshot.size()
        } catch (e: Exception) {
            // Handle any errors
            e.printStackTrace()
        }
        return 0 // Return 0 if an error occurs
    }

}