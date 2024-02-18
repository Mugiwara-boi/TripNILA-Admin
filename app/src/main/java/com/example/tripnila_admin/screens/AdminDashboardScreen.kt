package com.example.tripnila_admin.screens

import android.graphics.Typeface
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.twotone.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.tripnila_admin.common.AppDropDownFilter
import com.example.tripnila_admin.common.AppTopBar
import com.example.tripnila_admin.R
import com.example.tripnila_admin.common.AdminBottomNavigationBar
import com.example.tripnila_admin.common.SeeAllButton
import com.example.tripnila_admin.data.TableRow
import com.example.tripnila_admin.viewmodels.AdminDashboard
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import java.text.NumberFormat
import java.util.Locale

import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.tripnila_admin.common.ChartDropDownFilter
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    adminId: String = "",
    navController: NavHostController? = null,
    dashboardViewModel : AdminDashboard
) {

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    // FOR BOTTOM NAVIGATION BAR
    val selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    // FOR SEARCH BAR
    var search by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    //--------------------------------------

    // FOR TOP APP BAR
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())
    //--------------------------------------

    // FOR TABLE
    val sampleTableData = listOf(
        TableRow("1", "Withdrawal", "@jdelacruz", "2024-02-10 09:30 AM"),
        TableRow("2", "Payment", "@placeholder", "2024-02-10 10:45 AM"),
        TableRow("3", "Add listing", "@placeholder", "2024-02-10 12:15 PM"),
        TableRow("4", "Comment", "@placeholder", "2024-02-10 02:00 PM"),
        TableRow("5", "Payment", "@placeholder", "2024-02-10 10:45 AM"),
        TableRow("6", "Add listing", "@placeholder", "2024-02-10 12:15 PM"),
        TableRow("7", "Comment", "@placeholder", "2024-02-10 02:00 PM")
    )
    //--------------------------------------

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            topBar = {
                AppTopBar(
                    headerText = "Dashboard",
                    color = Color.White,
                    scrollBehavior = scrollBehavior
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

                item {
                    LazyVerticalGrid(
                        userScrollEnabled = false,
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .padding(horizontalPaddingValue, verticalPaddingValue)
                            .fillMaxWidth()
                            .height(120.dp)
                    ) {
                        items(6) { index ->
                            val cardText = when (index) {
                                0 -> "Users"
                                1 -> "Staycation Listings"
                                2 -> "Bookings"
                                3 -> "Hosts"
                                4 -> "Tours"
                                5 -> "Businesses"
                                else -> "Default Card Text"
                            }


                            val countLabel = when (index) {
                                0 -> "tourist"
                                1 -> "staycation"
                                2 -> "staycation_booking"
                                3 -> "host"
                                4 -> "tour"
                                5 -> "business"
                                else -> ""
                            }
                            val totalCount = remember { mutableStateOf(0) }

                            LaunchedEffect(key1 = countLabel) {
                                val count = dashboardViewModel.getTotalCount(countLabel)
                                totalCount.value = count
                            }

                            val color = when (index) {
                                0 -> Color(0xffF9A664)
                                1 -> Color(0xff9ED93D)
                                2 -> Color(0xffF97664)
                                3 -> Color(0xff8B64F9)
                                4 -> Color(0xff56DFD7)
                                5 -> Color(0xffE177E3)
                                else -> Color.Gray
                            }

                            DashboardInfoCard(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                cardText = cardText,
                                count = totalCount.value,
                                color = color
                            )
                        }
                    }
                }

                item {
                    SalesChart(
                        modifier = Modifier.padding(horizontalPaddingValue,verticalPaddingValue),
                        viewModel = AdminDashboard()
                    )
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontalPaddingValue, verticalPaddingValue)
                    ) {
                        Text(
                            text = "Stats",
                            color = Color(0xff333333),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                        )
                        AppDropDownFilter(
                            options = listOf("Daily", "Weekly", "Monthly", "Yearly"),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 3.dp)
                        )
                        val totalProfitState = remember { mutableStateOf(0.0) }
                        val itemCountState = remember { mutableStateOf(0) }
                        val profitPercentageState = remember { mutableStateOf(0.0) }
                        LaunchedEffect(Unit) {
                            val (totalProfit, itemCount) = dashboardViewModel.getTodayProfitAndItemCount()
                            val profitPercentage = dashboardViewModel.getPercentageDifference()
                            totalProfitState.value = totalProfit
                            itemCountState.value = itemCount
                            profitPercentageState.value = profitPercentage
                        }
                        ProfitCard(
                            lastUpdateTime = "12:23 PM",
                            percentage = profitPercentageState.value,
                            profitAmount = totalProfitState.value.toInt(),
                            bookingCount = itemCountState.value,
                            modifier = Modifier.padding(bottom = verticalPaddingValue)
                        )
                        RevenueCard(
                            revenueAmount = 84370,
                            lastUpdateTime = "12:23 PM",
                            percentage = 7.3,
                            modifier = Modifier.padding(bottom = verticalPaddingValue)
                        )
                        VerifiedUsersCard(
                            verifiedUsersCount = 1821,
                            totalUsersCount = 2123,
                            lastUpdateTime = "12:23 PM",

                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontalPaddingValue, verticalPaddingValue)
                    ) {
                        Text(
                            text = "Recent",
                            color = Color(0xff333333),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                        )
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 5.dp)
                        ) {

                            CustomSearchView(
                                search = search,
                                onValueChange = { inputText ->
                                    search = inputText
                                },
                                onSearch = {
                                    /*TODO*/
                                },
                                focusManager = focusManager,
                                trailingIcon = {
                                    if (search.isNotEmpty()) {
                                        Icon(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clickable {
                                                    search = ""

                                                },
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Close icon",
                                        )
                                    }
                                },
                                searchFieldWidth = 120.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            AppDropDownFilter(
                                options = listOf("Relevance", "...", "...", "..."),
                                modifier = Modifier.offset(y = 3.dp)
                            )
                        }

                        TableCard(modifier = Modifier, tableData =  sampleTableData)
                    }

                }


            }

        }
    }
}

@Composable
fun DashboardInfoCard(modifier: Modifier = Modifier, cardText: String, count: Int, color: Color) {

    val formattedNumber = NumberFormat.getNumberInstance(Locale.US)
        .format(count)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {
            Text(
                text = cardText,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = formattedNumber,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

        }

    }
}
@Composable
fun SalesChart(
    modifier: Modifier = Modifier,
    viewModel : AdminDashboard
) {
    var year by remember { mutableStateOf(viewModel.selectedYear.value) }

    // Observe changes in aggregated sales data based on the selected year
    val aggregatedData by viewModel.aggregatedSalesData.observeAsState(initial = emptyList())

    // Fetch sales data whenever the year changes
    LaunchedEffect(key1 = year) {
        viewModel.getSales(year)
    }
    val monthNames = mapOf(
        2 to "January",
        3 to "February",
        4 to "March",
        5 to "April",
        6 to "May",
        7 to "June",
        8 to "July",
        9 to "August",
        10 to "September",
        11 to "October",
        12 to "November",
        13 to "December"
    )

    // Prepare chart data
    val chartEntries = remember(aggregatedData) {
        aggregatedData.map { entry ->
            Pair(entry.month.toFloat(), entry.totalAmount.toFloat())
        }
    }

    // Pass the entries to the chart model
    val chartEntryModel = entryModelOf(*chartEntries.toTypedArray())
    val horizontalAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        val monthNumber = value.toInt()+1 // Assuming month numbers start from 1
        monthNames[monthNumber] ?: ""
    }

    //val chartEntries = aggregatedData.map { entry -> Entry(entry.month.toFloat(), entry.totalAmount.toFloat()) }
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp, 5.dp)
        ) {
            Text(
                text = "Sales for Year ${viewModel.selectedYear.value}",
                color = Color(0xfff9a664),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            ChartDropDownFilter(
                options = listOf("2024", "2023"),
                onItemSelected = { year ->
                    viewModel.setSelectedYear(year.toInt()) // Update the selected year in ViewModel
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            )

        }
        ProvideChartStyle(
            chartStyle = m3ChartStyle(
                axisLabelColor = Color.Black,
                axisGuidelineColor = Color(0xff999999),
                axisLineColor = Color.Transparent,
                entityColors = listOf(
                    Color(0xfff9a664),
                    Color(0xff9FFFB4)
                ),
                // elevationOverlayColor = Color.Transparent
            )
        ) {
            Chart(
                chart = lineChart(),
                model = chartEntryModel,
                startAxis = rememberStartAxis(
                    title = "Top Values",
                    tickLength = 0.dp,
                    valueFormatter = { value, _ -> value.toInt().toString()

                    }, itemPlacer = AxisItemPlacer.Vertical.default(
                        maxItemCount = 6
                    )
                ),
                bottomAxis = rememberBottomAxis(
                    title = "Count of Values",
                    tickLength = 0.dp,
                    valueFormatter = horizontalAxisValueFormatter
                ),
                // legend = horizontalLegend(items = , iconSize = , iconPadding = ),
                modifier = Modifier.padding(start = 5.dp, end = 20.dp, bottom = 5.dp),
            )
        }
    }
}

@Composable
fun ProfitCard(
    modifier: Modifier = Modifier,
    profitAmount: Int,
    bookingCount: Int,
    lastUpdateTime: String,
    percentage: Double
) {

    val increase = Color(0xff5CE37A)
    val decrease = Color(0xffF24E1E)
    val formattedNumber = NumberFormat.getNumberInstance(Locale.US)
        .format(profitAmount)


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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Text(
                    text = "Today’s Profit",
                    color = Color(0xff333333),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.recent_clock),
                    contentDescription = null,
                    tint = Color(0xff999999),
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
                Text(
                    text = "Updated: $lastUpdateTime",
                    color = Color(0xff999999),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(23.dp)
                        .offset(x = 7.dp, y = (-4).dp)
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(23.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                Text(
                    text = "₱$formattedNumber",
                    color = Color(0xfff9a664),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (percentage > 0) ImageVector.vectorResource(R.drawable.stats_up) else ImageVector.vectorResource(R.drawable.stats_down),
                    contentDescription = null,
                    tint = if (percentage > 0) increase else decrease,
                    modifier = Modifier
                        .padding(end = 3.dp)
                        .offset(y = 5.dp)
                )
                Text(
                    text = if (percentage > 0) "+$percentage%" else "$percentage%",
                    color = if (percentage > 0) increase else decrease,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(y = 5.dp)
                )
            }
            Text(
                text = "Raised from $bookingCount bookings",
                color = Color(0xff666666),
                fontSize = 8.sp,

                )

        }
    }
}


@Composable
fun RevenueCard(
    modifier: Modifier = Modifier,
    revenueAmount: Int,
    lastUpdateTime: String,
    percentage: Double
) {

    val increase = Color(0xff5CE37A)
    val decrease = Color(0xffF24E1E)
    val formattedNumber = NumberFormat.getNumberInstance(Locale.US)
        .format(revenueAmount)


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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Text(
                    text = "Total Revenue",
                    color = Color(0xff333333),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.recent_clock),
                    contentDescription = null,
                    tint = Color(0xff999999),
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
                Text(
                    text = "Updated: $lastUpdateTime",
                    color = Color(0xff999999),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(23.dp)
                        .offset(x = 7.dp, y = (-4).dp)
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(23.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                Text(
                    text = "₱$formattedNumber",
                    color = Color(0xfff9a664),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (percentage > 0) ImageVector.vectorResource(R.drawable.stats_up) else ImageVector.vectorResource(R.drawable.stats_down),
                    contentDescription = null,
                    tint = if (percentage > 0) increase else decrease,
                    modifier = Modifier
                        .padding(end = 3.dp)
                )
                Text(
                    text = if (percentage > 0) "+$percentage%" else "$percentage%",
                    color = if (percentage > 0) increase else decrease,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

        }
    }
}

@Composable
fun VerifiedUsersCard(
    modifier: Modifier = Modifier,
    verifiedUsersCount: Int,
    totalUsersCount: Int,
    lastUpdateTime: String,
) {

    val verifiedUsers = verifiedUsersCount.toFloat()
    val totalUsers = totalUsersCount.toFloat()

    val verifiedPercentage = (verifiedUsers / totalUsers) * 100
    val unverifiedPercentage = 100 - verifiedPercentage

    val donutChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("Verified", verifiedPercentage, Color(0xFFF9A664)),
            PieChartData.Slice("Unverified", unverifiedPercentage, Color(0xFFF97664)),
        ),
        plotType = PlotType.Donut
    )

    val donutChartConfig = PieChartConfig(
        backgroundColor = Color.White,
        strokeWidth = 3f,
        activeSliceAlpha = .9f,
        isEllipsizeEnabled = true,
        isAnimationEnable = false,
        chartPadding = 25,
        labelVisible = false,
        labelType = PieChartConfig.LabelType.PERCENTAGE,
        labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
        labelFontSize = 42.sp,
        labelColor = Color.Black,
        showSliceLabels = false,
        //isClickOnSliceEnabled = false,
    )

    val selectedSliceValue by remember { mutableFloatStateOf(verifiedPercentage) }
    val formattedNumber = NumberFormat.getNumberInstance(Locale.US)


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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Text(
                    text = "Verified Users",
                    color = Color(0xff333333),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.recent_clock),
                    contentDescription = null,
                    tint = Color(0xff999999),
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
                Text(
                    text = "Updated: $lastUpdateTime",
                    color = Color(0xff999999),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                //UNCOMMENT KUNG KAILANGAN YUNG BUTTON
//                IconButton(
//                    onClick = { /*TODO*/ },
//                    modifier = Modifier
//                        .size(23.dp)
//                        .offset(x = 7.dp, y = (-4).dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.TwoTone.KeyboardArrowRight,
//                        contentDescription = null,
//                        modifier = Modifier.size(23.dp)
//                    )
//                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, start = 15.dp, end = 15.dp)
            ) {
                PieChart(
                    pieChartData = donutChartData,
                    pieChartConfig = donutChartConfig,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(200.dp)
                )
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterEnd)
                ) {
                    Text(
                        text = formattedNumber.format(verifiedUsersCount),
                        color = Color(0xfff9a664),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.offset((-35).dp)
                    )
                    Text(
                        text = "from ${formattedNumber.format(totalUsersCount)} total users",
                        color = Color(0xff666666),
                        fontSize = 8.sp
                    )
                }

                Text(
                    text = "Verified users: ${String.format("%.1f", selectedSliceValue)}%",
                    color = Color(0xfff9a664),
                    textAlign = TextAlign.Center,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 70.dp)
                        .width(60.dp)
                )
            }
        }
    }
}


@Composable
fun CustomSearchView(
    search: String,
    trailingIcon: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    focusManager: FocusManager,
    searchFieldWidth: Dp = 200.dp,
) {

    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = if (isFocused) Color.White else Color(0xffdfdfdf))
    ) {
        BasicTextField(
            singleLine = true,
            value = search,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 9.sp, color = Color(0xff999999)),
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (isFocused) Color(0XFFF9A664) else Color(0XFFDFDFDF),
                    shape = CircleShape
                )
                .width(searchFieldWidth)
                .padding(vertical = 5.dp)
                .onFocusChanged { isFocused = it.isFocused },

            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                    Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search icon",
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        if (search.isEmpty()) {
                            Text(
                                text = "Search",
                                color = Color(0XFF999999),
                                fontSize = 9.sp
                            )
                        }
                        innerTextField()
                    }
                    trailingIcon()
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                    focusManager.clearFocus()
                }
            )
        )
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    fontWeight: FontWeight = FontWeight.Medium,
    fontSize: TextUnit = 10.sp,
    fontColor: Color = Color(0xFF666666),
    weight: Float
) {

    Text(
        text = text,
        fontWeight = fontWeight,
        fontSize = fontSize,
        color = fontColor,
        modifier = Modifier
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Color(0xff999999),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
            .weight(weight)
            .padding(4.dp)
    )

}


@Composable
fun TableCard(modifier: Modifier = Modifier, tableData: List<TableRow>) {

    val column1Weight = .12f
    val column2Weight = .28f
    val column3Weight = .25f
    val column4Weight = .35f

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(155.dp)
                .padding(10.dp),
            userScrollEnabled = false
        ) {
            // Header
            item {
                Row {
                    TableCell(text = "ID", weight = column1Weight, fontSize = 12.sp, fontColor = Color(0xFF333333))
                    TableCell(text = "Transaction", weight = column2Weight, fontSize = 12.sp, fontColor = Color(0xFF333333))
                    TableCell(text = "Username", weight = column3Weight, fontSize = 12.sp, fontColor = Color(0xFF333333))
                    TableCell(text = "Date & Time", weight = column4Weight, fontSize = 12.sp, fontColor = Color(0xFF333333))
                }
            }
            // Rows
            items(tableData) { rowData ->
                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = rowData.id, weight = column1Weight)
                    TableCell(text = rowData.transaction, weight = column2Weight)
                    TableCell(text = rowData.username, weight = column3Weight)
                    TableCell(text = rowData.dateTime, weight = column4Weight)
                }
            }
        }

        SeeAllButton(
            buttonText = "See all transactions",
            buttonShape = RoundedCornerShape(10.dp),
            borderStroke = BorderStroke(1.dp, Color(0xff999999)),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
            contentFontSize  = 12.sp,
            contentFontWeight = FontWeight.Medium,
            contentColor = Color(0xff999999),
            onClick = {
                /*TODO*/
            },
            modifier = Modifier
                .padding(start = 50.dp, end = 50.dp, bottom = 10.dp)
                .fillMaxWidth()
                .height(30.dp)
        )
    }

}


@Preview
@Composable
private fun SearchbarPreview() {

    var search by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    CustomSearchView(
        search = search,
        onValueChange = {
            search = it
        },
        onSearch = {
            /*TODO*/
        },
        focusManager = focusManager,
        trailingIcon = {
            if (search.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .size(12.dp)
                        .clickable {
                            search = ""

                        },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon",
                )
            }
        }
    )

}



@Preview
@Composable
private fun AdminDashboardPreview() {

    AdminDashboardScreen(dashboardViewModel = AdminDashboard())

//    val sampleTableData = listOf(
//        Triple("Withdrawal", "@jdelacruz", "2024-02-10 09:30 AM"),
//        Triple("Payment", "@placeholder", "2024-02-10 10:45 AM"),
//        Triple("Add listing", "@placeholder", "2024-02-10 12:15 PM"),
//        Triple("Comment", "@placeholder", "2024-02-10 02:00 PM")
//    )
//
//    TableCard(modifier = Modifier, tableData =  sampleTableData)

}