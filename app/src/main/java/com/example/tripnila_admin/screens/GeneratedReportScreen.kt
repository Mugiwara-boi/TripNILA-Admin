package com.example.tripnila_admin.screens

import android.annotation.SuppressLint
import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tripnila_admin.common.AppFilledButton
import com.example.tripnila_admin.common.AppTopBar
import com.example.tripnila_admin.common.LoadingScreen
import com.example.tripnila_admin.viewmodels.AdminReports
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratedReportScreen(
    adminReports: AdminReports,
    reportType: String,
    onNavToBack: () -> Unit
) {

    val context = LocalContext.current
    var webView: WebView? = null

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

    val staycationData by adminReports.staycationDataMap.collectAsState()
    val tourData by adminReports.tourDataMap.collectAsState()

//    val staycationData = listOf(
//        mapOf("column1" to "", "column2" to "Data 2", "column3" to "Data 3", "column4" to "Data 2", "column5" to "Data 2", "column6" to "Data 2", "column7" to "Data 5"),
//        mapOf("column1" to "", "column2" to "Data 5", "column3" to "Data 6", "column4" to "Data 2", "column5" to "Data 2", "column6" to "Data 2", "column7" to "Data 2"),
//        mapOf("column1" to "", "column2" to "Data 8", "column3" to "Data 9", "column4" to "Data 2", "column5" to "Data 2", "column6" to "Data 2", "column7" to "Data 2")
//    )


  //  val tourData = emptyList<Map<String,String>>()
//    val tourData = listOf(
//        mapOf("column1" to "Data 1", "column2" to "Data 2", "column3" to "Data 3", "column4" to "Data 2", "column5" to "Data 2", "column6" to "Data 2", "column7" to "Data 5"),
//        mapOf("column1" to "Data 4", "column2" to "Data 5", "column3" to "Data 6", "column4" to "Data 2", "column5" to "Data 2", "column6" to "Data 2", "column7" to "Data 2"),
//        mapOf("column1" to "Data 7", "column2" to "Data 8", "column3" to "Data 9", "column4" to "Data 2", "column5" to "Data 2", "column6" to "Data 2", "column7" to "Data 2")
//    )

    val period by adminReports.selectedPeriod.collectAsState()
    val month by adminReports.selectedMonth.collectAsState()
    val year by adminReports.selectedYear.collectAsState()
    val startMonth by adminReports.selectedStartMonth.collectAsState()
    val endMonth by adminReports.selectedEndMonth.collectAsState()
    val dateRange by adminReports.dateRange.collectAsState()

    val staycationTotalCollectedCommission by adminReports.staycationTotalCollectedCommission.collectAsState()
    val staycationTotalPendingCommission by adminReports.staycationTotalPendingCommission.collectAsState()
    val staycationTotalGrossSale by adminReports.staycationTotalGrossSale.collectAsState()

    val tourTotalCollectedCommission by adminReports.tourTotalCollectedCommission.collectAsState()
    val tourTotalPendingCommission by adminReports.tourTotalPendingCommission.collectAsState()
    val tourTotalGrossSale by adminReports.tourTotalGrossSale.collectAsState()

    val reportHeader = when(reportType) {
        "salesReport" -> "$period Sales Report"
        else -> {"Unregistered Report Type"}
    }

    val dateHeader = when(period) {
        "Monthly" -> "$month $year"
        "Bi-yearly" -> "$startMonth - $endMonth $year"
        "Yearly" -> "Year $year"
        else -> "Unknown Error"
    }

    val totalGrossSales = "₱ %.2f".format(staycationTotalGrossSale.plus(tourTotalGrossSale))
    val totalCollectedCommission = "₱ %.2f".format(staycationTotalCollectedCommission.plus(tourTotalCollectedCommission))
    val totalPendingCommission = "₱ %.2f".format(staycationTotalPendingCommission.plus(tourTotalPendingCommission))

//    val totalGrossSales = "₱ ${staycationTotalGrossSale.plus(tourTotalGrossSale)}"
//    val totalCollectedCommission = "₱ ${staycationTotalCollectedCommission.plus(tourTotalCollectedCommission)}"
//    val totalPendingCommission = "₱ ${staycationTotalPendingCommission.plus(tourTotalPendingCommission)}"


//    val dateRange = when(period) {
//        "Monthly" -> adminReports.getDateRangeForMonth()
//        "Bi-yearly" -> adminReports.getDateRangeForMonths()
//        "Yearly" -> adminReports.getDateRangeForYear()
//        else -> "Unregistered Report Type"
//    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Scaffold(
            topBar = {
                AppTopBar(
                    headerText = "Reports",
                    scrollBehavior = scrollBehavior,
                    color = Color.White,
                    navigationIcon = {
                        IconButton(onClick = { onNavToBack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {

                AppFilledButton(
                    modifier = Modifier
                        .padding(horizontalPaddingValue, verticalPaddingValue)
                        .align(Alignment.End),
                    buttonText = "Export",
                    onClick = {
                        exportAsPdf(reportHeader, webView = webView, context = context)
                    }
                )


                AndroidView(
                    modifier = Modifier.padding(horizontalPaddingValue, verticalPaddingValue),
                    factory = { context ->
                        WebView(context)
                            .apply {
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = true
                                loadDataWithBaseURL(null,
                                    getHtmlContent(staycationData, tourData, reportHeader,
                                        dateHeader, dateRange, totalGrossSales, totalCollectedCommission,
                                        totalPendingCommission
                                    ),
//                                    getHtmlContent(staycationData, tourData, reportHeader,
//                                        dateHeader, dateRange),
                                    "text/html",
                                    "UTF-8",
                                    null)
                            }
                    },
                ) { view ->
                    webView = view
                    view.webViewClient = WebViewClient()
                    view.settings.javaScriptEnabled = true
                    view.loadDataWithBaseURL(null,
                        getHtmlContent(staycationData, tourData, reportHeader,
                            dateHeader, dateRange, totalGrossSales, totalCollectedCommission,
                            totalPendingCommission
                        ),
                        "text/html",
                        "UTF-8",
                        null)
                }

            }
        }


    }
}

private fun getHtmlContent(
    staycationData: List<Map<String, String>>,
    tourData: List<Map<String, String>>,
    reportHeader: String,
    dateHeader: String,
    dateRange: String,
    totalGrossSales: String,
    totalCollectedCommission: String,
    totalPendingCommission: String
): String {

    return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Report</title>
            <style>
                
        
                span {
                    width: 100%;
                }
        
                #address-date-range {
                    display: flex;
                    justify-content: space-between;
                }
        
                #date-range {
                    text-align: end;
                }
        
                #report-id, #report-date {
                    text-align: center;
                }
        
                #table-container {
                    overflow-x: auto; /* Enable horizontal scrolling */
                }
        
                table {
                    border-collapse: collapse;
                    width: 100%;
                }
        
                th, td {
                    border: 1px solid #000000;
                    text-align: left;
                    padding: 8px;
                }
        
                th {
                    background-color: #f2f2f2;
                }
        
                .signature-line {
                    text-align: center; /* Align content in the middle */
                }
        
                .signature-line p {
                    margin: 0; /* Remove default margin */
                }
        
                .signature-line p:first-child {
                    width: 300px; /* Adjust width as needed */
                    border-top: 1px solid black; /* Add border to create the line */
                }
        
            </style>
        </head>
        <body>
            <h1 id="report-id">$reportHeader</h1>
            <h3 id="report-date">$dateHeader</h3>
        
            <h2>TripNILA</h2>
            
            <div id="contact">
                <div id="address-date-range">
                    <span>PUP Sta. Mesa, Manila, Philippines</span> 
                    <span id="date-range">$dateRange</span>
                </div>
                <p>(+63)123456789</p>
                <p>tripnila@tripnila.com</p>
            </div>
            
        
            <div id="table-container">
                <table>
                    <thead>
                        <tr>
                            <th></th>
                            <th>Title</th>
                            <th>Host Name</th>
                            <th>Number of Bookings</th>
                            <th>Gross Booking Sales</th>
                            <th>Collected Commission</th>
                            <th>Pending Commission</th>
                        </tr>
                    </thead>
                    <tbody id="table-body">
                        <!-- Table rows will be dynamically added here -->
                    </tbody>
                </table>
            </div>
            
        
            
            <br><br><br>
            <div class="signature-line">
                <p>Signed by</p>
            </div>
        
            <script>
                // Sample data for the table
                var staycationData = ${staycationData.joinToString(separator = ",", prefix = "[", postfix = "]") {
                    it.entries.joinToString(separator = ",", prefix = "{", postfix = "}") { (key, value) ->
                        """"$key":"$value""""
                    }
                }}
                
                var tourData = ${tourData.joinToString(separator = ",", prefix = "[", postfix = "]") {
                    it.entries.joinToString(separator = ",", prefix = "{", postfix = "}") { (key, value) ->
                        """"$key":"$value""""
                    }
                }}
                
                var totalGrossSales = "$totalGrossSales";
                var totalCollectedCommission = "$totalCollectedCommission";
                var totalPendingCommission = "$totalPendingCommission";
                  
                function populateTable() {
                    var tableBody = document.getElementById("table-body");
                    tableBody.innerHTML = ""; // Clear existing rows
        
                     // Create a new row for "Staycation" above the other rows
                    var staycationRow = document.createElement("tr");
        
                    // Loop to create 7 columns and empty their content except for the first column
                    for (var i = 0; i < 7; i++) {
                        var cell = document.createElement("td");
                        if (i === 0) {
                            var boldText = document.createElement("strong");
                            boldText.textContent = "Staycation";
                            cell.appendChild(boldText);
                        }
                        staycationRow.appendChild(cell);
                    }
                    tableBody.appendChild(staycationRow);
        
                    staycationData.forEach(function(item) {
                        var row = document.createElement("tr");
                        var isFirstColumn = true; // Flag to track if it's the first column
                        Object.values(item).forEach(function(value) {
                            var cell = document.createElement("td");
                            if (isFirstColumn) {
                                // Skip populating data in the first column
                                isFirstColumn = false; // Reset the flag for the next row
                            } else {
                                cell.textContent = value;
                            }
                            row.appendChild(cell);
                        });
                        tableBody.appendChild(row);
                    });
        
                    var tourRow = document.createElement("tr");
        
                    // Loop to create 7 columns and empty their content except for the first column
                    for (var i = 0; i < 7; i++) {
                        var cell = document.createElement("td");
                        if (i === 0) {
                            var boldText = document.createElement("strong");
                            boldText.textContent = "Tour";
                            cell.appendChild(boldText);
                        }
                        tourRow.appendChild(cell);
                    }
                    tableBody.appendChild(tourRow);
        
                    tourData.forEach(function(item) {
                        var row = document.createElement("tr");
                        var isFirstColumn = true; // Flag to track if it's the first column
                        Object.values(item).forEach(function(value) {
                            var cell = document.createElement("td");
                            if (isFirstColumn) {
                                // Skip populating data in the first column
                                isFirstColumn = false; // Reset the flag for the next row
                            } else {
                                cell.textContent = value;
                            }
                            row.appendChild(cell);
                        });
                        tableBody.appendChild(row);
                    });
        
                    var totalRow = document.createElement("tr");
        
                    // Loop to create 7 columns and empty their content except for the first column
                    for (var i = 0; i < 7; i++) {
                        var cell = document.createElement("td");
                        if (i === 0) {
                            var boldText = document.createElement("strong");
                            boldText.textContent = "Total";
                            cell.appendChild(boldText);
                        } else if (i === 4) {
                            var boldText = document.createElement("strong");
                            boldText.textContent = totalGrossSales;
                            cell.appendChild(boldText);
                        } else if (i === 5) {
                            var boldText = document.createElement("strong");
                            boldText.textContent = totalCollectedCommission;
                            cell.appendChild(boldText);
                        } else if (i === 6) {
                            var boldText = document.createElement("strong");
                            boldText.textContent = totalPendingCommission;
                            cell.appendChild(boldText);
                        }
                        totalRow.appendChild(cell);
                    }
                    tableBody.appendChild(totalRow);
                }
        
                // Call the function to populate the table when the page loads
                window.onload = populateTable;
            </script>
        </body>
        </html>
    """
}

fun exportAsPdf(fileName: String, webView: WebView?, context: Context) {

    val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val fileNameWithDateTime = "$fileName ${sdf.format(Date())}"

    if (webView != null) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter =
            webView.createPrintDocumentAdapter(fileNameWithDateTime)
        val printAttributes = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .build()
        printManager.print(fileNameWithDateTime,
            printAdapter,
            printAttributes)
    }
}

@Preview
@Composable
private fun GeneratedReportScreenPreview() {
   // GeneratedReportScreen()
}