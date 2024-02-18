package com.example.tripnila_admin.viewmodels
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar

class AdminDashboard : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // LiveData to hold the count
    private val _totalCount = MutableLiveData<Int>()
    val totalCount: LiveData<Int>
        get() = _totalCount
    //val collectionRef = db.collection("tourist")

    // Function to fetch the total count

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

    data class MonthTotal(val month: Int, val totalAmount: Double)

    fun aggregateSalesByMonth(salesData: List<DocumentSnapshot>): List<MonthTotal> {
        val salesByMonth = mutableMapOf<Int, Double>()

        // Iterate through the sales data to aggregate by month
        for (document in salesData) {
            val date = document.getDate("bookingDate")
            val amount = document.getDouble("totalAmount")

            // Extract the month from the date
            val calendar = Calendar.getInstance()
            calendar.time = date

            val month = calendar.get(Calendar.MONTH)

            // Aggregate the amount for the corresponding month
            val currentTotal = salesByMonth.getOrDefault(month, 0.0)
            salesByMonth[month] = currentTotal + (amount ?: 0.0)
        }

        // Convert the map to a list of MonthTotal objects
        val monthTotalList = mutableListOf<MonthTotal>()
        for ((month, totalAmount) in salesByMonth) {
            monthTotalList.add(MonthTotal(month, totalAmount))
        }

        return monthTotalList
    }
    private val _aggregatedSalesData = MutableLiveData<List<MonthTotal>>()
    val aggregatedSalesData: LiveData<List<MonthTotal>> = _aggregatedSalesData
    fun getSales(){
        val salesCollection = db.collection("staycation_booking")

        salesCollection.get().addOnSuccessListener { documents ->
            val salesData = documents.filter { document ->
                document.contains("bookingDate") &&
                document.contains("totalAmount") &&
                document.getString("bookingStatus") == "Completed"
            }
            val aggregatedData = aggregateSalesByMonth(salesData)
            _aggregatedSalesData.value = aggregatedData
            // Now aggregatedData contains a list of MonthTotal objects with the month and total amount
        }
    }
}