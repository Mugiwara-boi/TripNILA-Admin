package com.example.tripnila_admin.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tripnila_admin.R
import com.example.tripnila_admin.common.AdminBottomNavigationBar
import com.example.tripnila_admin.common.AppFilledButton
import com.example.tripnila_admin.common.AppOutlinedButton
import com.example.tripnila_admin.common.AppRatingBar
import com.example.tripnila_admin.common.AppTopBar
import com.example.tripnila_admin.common.LoadingScreen
import com.example.tripnila_admin.viewmodels.AdminReports
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportsScreen(
    adminId: String = "",
    adminReports: AdminReports,
    navController: NavHostController? = null,
    onNavToGeneratedReport: (String) -> Unit
) {

    val isFetchingStaycationBookings by adminReports.isFetchingStaycationBookings.collectAsState()
    val isStaycationBookingsFetched by adminReports.isStaycationBookingsFetched.collectAsState()
    val isFetchingTourBookings by adminReports.isFetchingTourBookings.collectAsState()
    val isTourBookingsFetched by adminReports.isTourBookingsFetched.collectAsState()

    LaunchedEffect(adminId){
        adminReports.fetchPendingVerifications()
      //  adminReports.fetchStaycationBookings()


//        val staycationBookingsDeferred = async { adminReports.fetchStaycationBookings() }
//        val tourBookingsDeferred = async { adminReports.fetchTourBookings() }
//
//        staycationBookingsDeferred.await()
//        tourBookingsDeferred.await()



    }




    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val pendingVerifications by adminReports.pendingVerifications.collectAsState()
    val isFetchingPendingVerifications by adminReports.isFetchingPendingVerifications.collectAsState()
    val selectedPeriod by adminReports.selectedPeriod.collectAsState()
    val selectedMonth by adminReports.selectedMonth.collectAsState()
    val selectedYear by adminReports.selectedYear.collectAsState()
    val selectedStartMonth by adminReports.selectedStartMonth.collectAsState()
    val selectedEndMonth by adminReports.selectedEndMonth.collectAsState()
//    val isGeneratingExcel by adminReports.isGeneratingExcel.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var generateExcelClicked by remember { mutableStateOf(false) }

    val selectedItemIndex by rememberSaveable { mutableIntStateOf(1) }


    val selectionTab = listOf(
        "Monthly", "Bi-yearly", "Yearly"
    )

    val monthOptions = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )

    val biyearlyOptions = listOf(
        "January - June", "July - December"
    )

    val yearOptions = (2023..LocalDate.now().year).toList().reversed()


    var expandMonth by remember { mutableStateOf(false) }
  //  var selectedText by remember { mutableStateOf(options.firstOrNull() ?: "") }
    val monthIcon = if (expandMonth)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var expandMonthRange by remember { mutableStateOf(false) }
    //  var selectedText by remember { mutableStateOf(options.firstOrNull() ?: "") }
    val monthRangeIcon = if (expandMonthRange)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var expandYear by remember { mutableStateOf(false) }
    //  var selectedText by remember { mutableStateOf(options.firstOrNull() ?: "") }
    val yearIcon = if (expandYear)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

    Log.d("AdminReportsScreen", "Pending verifications size: ${pendingVerifications.size}")

    var showDialog by remember {
        mutableStateOf(false)
    }
//
//    LaunchedEffect(generateExcelClicked) {
//        if (generateExcelClicked) {
//            adminReports.generateExcelFile(context)
//            generateExcelClicked = false
//        }
//    }

//    LaunchedEffect(
//        isStaycationBookingsFetched,
//        isTourBookingsFetched
//    ) {
//        if (isStaycationBookingsFetched && isTourBookingsFetched) {
//            onNavToGeneratedReport("salesReport")
//            showDialog = false
//            adminReports.resetFetchStatus()
//        }
//    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (isFetchingPendingVerifications) {
            LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
        } else {
            Scaffold(
                topBar = {
                    AppTopBar(
                        headerText = "Reports",
                        scrollBehavior = scrollBehavior,
                        color = Color.White
                    )
                },
                bottomBar = {
                    if (navController != null) {
                        AdminBottomNavigationBar(
                            adminId = adminId,
                            navController = navController,
                            selectedItemIndex = selectedItemIndex
                        )
                    }
                },
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {

//                    item {
//
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontalPaddingValue, verticalPaddingValue)
//                        ) {
//                            Text(
//                                text = "Generate Reports",
//                                color = Color(0xff333333),
//                                fontSize = 18.sp,
//                                fontWeight = FontWeight.Medium,
//                                modifier = Modifier.padding(bottom = 10.dp)
//                            )
//                            AppFilledButton(
//                                buttonText = "Generate Sales Report",
//                                onClick = {
//                                    showDialog = true
//                                   // onNavToGeneratedReport()
//                                }
//                            )
//
////                            Row(
////                                verticalAlignment = Alignment.CenterVertically,
////                                modifier = Modifier.align(Alignment.End),
////                            ) {
////                                Text(
////                                    text = selectedReportFilter,
////                                    fontSize = 12.sp,
////                                    fontWeight = FontWeight.Medium,
////                                )
////                                IconButton(
////                                    modifier = Modifier.size(24.dp),
////                                    onClick = { expanded = true }
////                                ) {
////                                    Icon(
////                                        imageVector = icon,
////                                        contentDescription = "",
////                                    )
////                                }
////                                DropdownMenu(
////                                    expanded = expanded,
////                                    onDismissRequest = { expanded = false },
////                                    modifier = Modifier
////                                        .background(Color.White)
////                                ) {
////                                    options.forEach { label ->
////                                        DropdownMenuItem(
////                                            text = {
////                                                Text(
////                                                    text = label,
////                                                    fontSize = 12.sp
////                                                )
////                                            },
////                                            colors = MenuDefaults.itemColors(
////                                                textColor = Color(0xFF6B6B6B)
////                                            ),
////                                            onClick = {
////                                                adminReports.setSelectedFilter(label)
////                                                expanded = false
////                                            },
////                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
////                                        )
////                                    }
////                                }
////                            }
////                            AppFilledButton(
////                                buttonText = if (selectedReportFilter == "All") "Generate $selectedReportFilter Report" else "Generate $selectedReportFilter's Report",
////                                isLoading = isGeneratingExcel,
////                                onClick = {
////                                    generateExcelClicked = true
////                                },
////                                modifier = Modifier.align(Alignment.CenterHorizontally)
////                            )
//                        }
//                    }


                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontalPaddingValue, verticalPaddingValue)
                        ) {
                            Text(
                                text = "User Verification",
                                color = Color(0xff333333),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                            )

                        }
                    }

                    items(pendingVerifications) { verification ->
                        UserVerificationCard(
                            userFullName = verification.userFullName,
                            userUsername = verification.userUsername,
                            userImage = verification.userImage,
                            reportDateTime = verification.reportDateTime.toString(),
                            firstValidIdType = verification.firstValidIdType,
                            firstValidIdUrl = verification.firstValidIdUrl,
                            secondValidIdType = verification.secondValidIdType,
                            secondValidIdUrl = verification.secondValidIdUrl,
                            verificationId = verification.verificationId,
                            adminReports = adminReports,
                            scope = scope,
                            context = context,
                            modifier = Modifier.padding(horizontalPaddingValue, verticalPaddingValue)
                        )

                    }

                }
            }


            if (showDialog) { //showOpeningDialog &&
                AlertDialog(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(size = 12.dp)
                        ),
                    onDismissRequest = { showDialog = false }
                ) {
                    Column(
                        modifier = Modifier
//                            .background(
//                                color = Color.LightGray.copy(alpha = 0.3f)
//                            )
                            .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 12.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Generate Sales Reports",
                            color = Color(0xff333333),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                        )

                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = "Select time period",
                                color = Color(0xff333333),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = 12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                selectionTab.forEach { tabLabel ->
                                    SelectionTab(
                                        tabLabel = tabLabel,
                                        isSelected = tabLabel == selectedPeriod,
                                        onTabSelected = {
                                            adminReports.setSelectedPeriod(tabLabel)
                                        },
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                if (selectedPeriod == "Monthly") {
                                    Column {
                                        Text(
                                            text = "Select Month",
                                            color = Color(0xff333333),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(
                                                vertical = 8.dp,
                                                horizontal = 12.dp
                                            )
                                        ) {
                                            Text(
                                                text = selectedMonth,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                            )
                                            IconButton(
                                                modifier = Modifier.size(24.dp),
                                                onClick = { expandMonth = true }
                                            ) {
                                                Icon(
                                                    imageVector = monthIcon,
                                                    contentDescription = "",
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = expandMonth,
                                                onDismissRequest = { expandMonth = false },
                                                modifier = Modifier
                                                    .background(Color.White)
                                            ) {
                                                monthOptions.forEach { label ->
                                                    DropdownMenuItem(
                                                        text = {
                                                            Text(
                                                                text = label,
                                                                fontSize = 12.sp
                                                            )
                                                        },
                                                        colors = MenuDefaults.itemColors(
                                                            textColor = Color(0xFF6B6B6B)
                                                        ),
                                                        onClick = {
                                                            adminReports.setSelectedMonth(label)
                                                            expandMonth = false
                                                        },
                                                        contentPadding = PaddingValues(
                                                            horizontal = 10.dp,
                                                            vertical = 0.dp
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } else if (selectedPeriod == "Bi-yearly") {
                                    Column {
                                        Text(
                                            text = "Select Month Range",
                                            color = Color(0xff333333),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(
                                                vertical = 8.dp,
                                                horizontal = 12.dp
                                            )
                                        ) {
                                            Text(
                                                text = "$selectedStartMonth - $selectedEndMonth",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                            )
                                            IconButton(
                                                modifier = Modifier.size(24.dp),
                                                onClick = { expandMonthRange = true }
                                            ) {
                                                Icon(
                                                    imageVector = monthRangeIcon,
                                                    contentDescription = "",
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = expandMonthRange,
                                                onDismissRequest = { expandMonthRange = false },
                                                modifier = Modifier
                                                    .background(Color.White)
                                            ) {
                                                biyearlyOptions.forEachIndexed { index, label ->
                                                    DropdownMenuItem(
                                                        text = {
                                                            Text(
                                                                text = label,
                                                                fontSize = 12.sp
                                                            )
                                                        },
                                                        colors = MenuDefaults.itemColors(
                                                            textColor = Color(0xFF6B6B6B)
                                                        ),
                                                        onClick = {
                                                            adminReports.setSelectedMonthRange(index)
                                                            expandMonthRange = false
                                                        },
                                                        contentPadding = PaddingValues(
                                                            horizontal = 10.dp,
                                                            vertical = 0.dp
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Column {
                                    Text(
                                        text = "Select Year",
                                        color = Color(0xff333333),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(
                                            vertical = 8.dp,
                                            horizontal = 12.dp
                                        )
                                    ) {
                                        Text(
                                            text = selectedYear.toString(),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                        )
                                        IconButton(
                                            modifier = Modifier.size(24.dp),
                                            onClick = { expandYear = true }
                                        ) {
                                            Icon(
                                                imageVector = yearIcon,
                                                contentDescription = "",
                                            )
                                        }
                                        DropdownMenu(
                                            expanded = expandYear,
                                            onDismissRequest = { expandYear = false },
                                            modifier = Modifier
                                                .background(Color.White)
                                        ) {
                                            yearOptions.forEach { label ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Text(
                                                            text = label.toString(),
                                                            fontSize = 12.sp
                                                        )
                                                    },
                                                    colors = MenuDefaults.itemColors(
                                                        textColor = Color(0xFF6B6B6B)
                                                    ),
                                                    onClick = {
                                                        adminReports.setSelectedYear(label.toString())
                                                        expandYear = false
                                                    },
                                                    contentPadding = PaddingValues(
                                                        horizontal = 10.dp,
                                                        vertical = 0.dp
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            AppOutlinedButton(
                                buttonText = "Cancel",
                                onClick = { showDialog = false }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            AppFilledButton(
                                buttonText = "Confirm",
                                isLoading = isFetchingStaycationBookings || isFetchingTourBookings,
                                onClick = {
                                    scope.launch {
                                        val staycationBookingsDeferred = async { adminReports.fetchStaycationBookings() }
                                        val tourBookingsDeferred = async { adminReports.fetchTourBookings() }

                                        staycationBookingsDeferred.await()
                                        tourBookingsDeferred.await()

                                    }
                                },
                            )
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun SelectionTab(
    tabLabel: String,
    isSelected: Boolean,
    onTabSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onTabSelected,
        border = BorderStroke(width = 1.dp, Color(0xFFF9A664)),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFFF9A664) else Color.White
        ),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        modifier = modifier.height(22.dp)
    ) {
        Text(
            text = tabLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFFF9A664),
        )
    }
}


@Composable
fun UserVerificationCard(
    modifier: Modifier = Modifier,
    userFullName: String,
    userUsername: String,
    userImage: String,
    reportDateTime: String,
    firstValidIdType: String,
    firstValidIdUrl: String,
    secondValidIdType: String,
    secondValidIdUrl: String,
    verificationId: String,
    adminReports: AdminReports,
    scope: CoroutineScope,
    context: Context
) {



    val imagePlaceholder = "https://www.unfe.org/wp-content/uploads/2019/04/SM-placeholder.png"

    val outputFormat = SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.getDefault())

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = if (userImage == "") imagePlaceholder else userImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(shape = RoundedCornerShape(100))

                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.65f)
                        .padding(start = 5.dp)
                ) {
                    Text(
                        text = userFullName, //"Denice Lucio",
                        color = Color(0xff333333),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "@$userUsername", //"denlucio",
                        color = Color(0xff666666),
                        fontSize = 8.sp
                    )
                }
                Surface(
                    shape = RoundedCornerShape(16.dp), // Adjust the corner radius as needed
                    color = Color(0xFFF9A664),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = "For Approval",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }



            }
            Divider(
                thickness = 0.5.dp,
                color = Color(0xff999999),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {

                item {
                    Text(
                        text = firstValidIdType, //"Denice Lucio",
                        color = Color(0xff333333),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)

                    )

                    Box(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .height(100.dp)

                    ) {
                        AsyncImage(
                            model = firstValidIdUrl,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }


                }


                item {
                    Text(
                        text = secondValidIdType, //"Denice Lucio",
                        color = Color(0xff333333),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    )
                    Box(modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .height(100.dp)) {
                        AsyncImage(
                            model = secondValidIdUrl,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }


                }


            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.recent_clock),
                    contentDescription = null,
                    tint = Color(0xff999999),
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .wrapContentWidth(Alignment.Start)
                        .padding(top = 10.dp)
                )
                Text(
                    text = reportDateTime, //"2022-09-27 18:00:00",
                    color = Color(0xff999999),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.Start)
                        .padding(top = 10.dp)
                )
                AppOutlinedButton(
                    buttonText = "Deny",
                    onClick = {
                        scope.launch {
                            adminReports.denyPendingVerifications(verificationId, context)
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    contentFontSize = 12.sp,
                    buttonShape = RoundedCornerShape(5.dp),
                    modifier = Modifier.height(30.dp)//.width(55.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                AppFilledButton(
                    buttonText = "Approve",
                    onClick = {
                        scope.launch {
                            adminReports.approvePendingVerifications(verificationId, context)
                        }

                    },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    contentFontSize = 12.sp,
                    buttonShape = RoundedCornerShape(5.dp),
                    modifier = Modifier.height(30.dp)
                )
            }
        }
    }
}




@Composable
fun ServiceReportCard(
    modifier: Modifier = Modifier,
    serviceName: String,
    serviceLocation: String,
    serviceImage: String,
    reportDateTime: String,
    reporterImage: String,
    reporterFullName: String,
    reporterUsername: String,
    reportType: String,
    report: String
) {

    val imagePlaceholder = "https://www.unfe.org/wp-content/uploads/2019/04/SM-placeholder.png"

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = if (serviceImage == "") imagePlaceholder else serviceImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(shape = RoundedCornerShape(5.dp))

                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.65f)
                        .padding(start = 5.dp)
                ) {
                    Text(
                        text = serviceName, //"Quiapo Staycation with pool by Jun",
                        color = Color(0xff333333),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = serviceLocation, //"Quiapo, Manila",
                        color = Color(0xff666666),
                        fontSize = 8.sp
                    )
                }
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.recent_clock),
                    contentDescription = null,
                    tint = Color(0xff999999),
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
                Text(
                    text = reportDateTime, //"2022-09-27 18:00:00",
                    color = Color(0xff999999),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

            }
            Divider(
                thickness = 0.5.dp,
                color = Color(0xff999999),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = if (serviceImage == "") imagePlaceholder else reporterImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(27.dp)
                        .clip(shape = RoundedCornerShape(27.dp))
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                ) {
                    Row {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xff333333),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                ) { append("$reporterFullName ") } //Maya Cruz
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xff999999),
                                        fontSize = 6.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                ) { append("(") }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xff999999),
                                        fontSize = 6.sp
                                    )
                                ) { append("@$reporterUsername)") } //mcruz
                            }
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.warning),
                            contentDescription = null,
                            tint = Color(0xffF97664),
                            modifier = Modifier
                                .padding(start = 6.dp, end = 3.dp)
                                .offset(y = 1.dp)
                        )
                        Text(
                            text = reportType, // "Inappropriate words used",
                            color = Color(0xfff97664),
                            fontSize = 8.sp
                        )


                    }
                    Text(
                        text = report,
                        color = Color(0xff666666),
                        fontSize = 8.sp,
                        modifier = Modifier.fillMaxWidth() //.7f
                    )
                }

            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                AppOutlinedButton(
                    buttonText = "Delete",
                    onClick = { /*TODO*/ },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    contentFontSize = 10.sp,
                    buttonShape = RoundedCornerShape(5.dp),
                    modifier = Modifier.height(25.dp)//.width(55.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                AppFilledButton(
                    buttonText = "Take action",
                    onClick = { /*TODO*/ },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    contentFontSize = 10.sp,
                    buttonShape = RoundedCornerShape(5.dp),
                    modifier = Modifier.height(25.dp)
                )
            }
        }
    }
}

@Composable
fun ReviewReportCard(
    modifier: Modifier = Modifier,
    serviceName: String,
    serviceLocation: String,
    serviceImage: String,
    reportDateTime: String,
    reviewerImage: String,
    reviewerFullName: String,
    reviewerUsername: String,
    reportType: String,
    reviewDateTime: String,
    review: String
) {

    val imagePlaceholder = "https://www.unfe.org/wp-content/uploads/2019/04/SM-placeholder.png"

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = if (serviceImage == "") imagePlaceholder else serviceImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(shape = RoundedCornerShape(5.dp))

                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.65f)
                        .padding(start = 5.dp)
                ) {
                    Text(
                        text = serviceName, //"Quiapo Staycation with pool by Jun",
                        color = Color(0xff333333),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = serviceLocation, //"Quiapo, Manila",
                        color = Color(0xff666666),
                        fontSize = 8.sp
                    )
                }
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.recent_clock),
                    contentDescription = null,
                    tint = Color(0xff999999),
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
                Text(
                    text = reportDateTime, //"2022-09-27 18:00:00",
                    color = Color(0xff999999),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

            }
            Divider(
                thickness = 0.5.dp,
                color = Color(0xff999999),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = if (serviceImage == "") imagePlaceholder else reviewerImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(27.dp)
                        .clip(shape = RoundedCornerShape(27.dp))
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xff333333),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                ) { append("$reviewerFullName ") } //Maya Cruz
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xff999999),
                                        fontSize = 6.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                ) { append("(") }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xff999999),
                                        fontSize = 6.sp
                                    )
                                ) { append("@$reviewerUsername)") } //mcruz
                            }
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.warning),
                            contentDescription = null,
                            tint = Color(0xffF97664),
                            modifier = Modifier.padding(start = 6.dp, end = 3.dp)
                        )
                        Text(
                            text = reportType, // "Inappropriate words used",
                            color = Color(0xfff97664),
                            fontSize = 8.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Box {
                            AppRatingBar(
                                initialRating = 3
                            )
                        }



                    }
                    Text(
                        text = reviewDateTime,//"04 April 2023  ",
                        color = Color(0xff999999),
                        fontSize = 6.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = review,
                        color = Color(0xff666666),
                        fontSize = 8.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                AppOutlinedButton(
                    buttonText = "Delete",
                    onClick = { /*TODO*/ },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    contentFontSize = 10.sp,
                    buttonShape = RoundedCornerShape(5.dp),
                    modifier = Modifier.height(25.dp)//.width(55.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                AppFilledButton(
                    buttonText = "Take action",
                    onClick = { /*TODO*/ },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    contentFontSize = 10.sp,
                    buttonShape = RoundedCornerShape(5.dp),
                    modifier = Modifier.height(25.dp)
                )
            }
        }
    }
}


@Composable
fun FeedbackCard(
    modifier: Modifier = Modifier,
    userFullName: String,
    userUsername: String,
    userImage: String,
    feedbackDateTime: String,
    feedback: String
) {

    val imagePlaceholder = "https://www.unfe.org/wp-content/uploads/2019/04/SM-placeholder.png"

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = if (userImage == "") imagePlaceholder else userImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(shape = RoundedCornerShape(100))

                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.65f)
                        .padding(start = 5.dp)
                ) {
                    Text(
                        text = userFullName, //"Denice Lucio",
                        color = Color(0xff333333),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "@$userUsername", //"denlucio",
                        color = Color(0xff666666),
                        fontSize = 8.sp
                    )
                }
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.recent_clock),
                    contentDescription = null,
                    tint = Color(0xff999999),
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
                Text(
                    text = feedbackDateTime, //"2022-09-27 18:00:00",
                    color = Color(0xff999999),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

            }
            Divider(
                thickness = 0.5.dp,
                color = Color(0xff999999),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                ) {
                    Text(
                        text = feedback,
                        color = Color(0xff666666),
                        fontSize = 8.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                AppOutlinedButton(
                    buttonText = "Delete",
                    onClick = { /*TODO*/ },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    contentFontSize = 10.sp,
                    buttonShape = RoundedCornerShape(5.dp),
                    modifier = Modifier.height(25.dp)//.width(55.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                AppFilledButton(
                    buttonText = "Reply",
                    onClick = { /*TODO*/ },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    contentFontSize = 10.sp,
                    buttonShape = RoundedCornerShape(5.dp),
                    modifier = Modifier.height(25.dp)
                )
            }
        }
    }
}



@Preview
@Composable
private fun ReportScreenPreview() {


    val month = "January"

    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
    calendar.time = dateFormat.parse(month) ?: Date()
    val startDate = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.time
    val endDate = calendar.apply { set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) }.time

    println(startDate)
    println(endDate)
}