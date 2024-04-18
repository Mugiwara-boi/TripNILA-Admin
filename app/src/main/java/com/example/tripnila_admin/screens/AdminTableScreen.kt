package com.example.tripnila_admin.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tripnila_admin.common.AdminBottomNavigationBar
import com.example.tripnila_admin.common.AppDropDownFilter
import com.example.tripnila_admin.common.AppTopBar
import com.example.tripnila_admin.common.LoadingScreen
import com.example.tripnila_admin.data.TableRow
import com.example.tripnila_admin.viewmodels.AdminTables
import kotlinx.coroutines.async
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTableScreen(
    adminId: String = "",
    navController: NavHostController? = null,
    adminTable: AdminTables
) {

    LaunchedEffect(adminId) {
        val tourPerformanceDeferred = async { adminTable.fetchToursPerformance() }
        val staycationPerformanceDeferred = async { adminTable.fetchStaycationsPerformance() }

        tourPerformanceDeferred.await()
        staycationPerformanceDeferred.await()

    }

    val staycationsPerformanceData by adminTable.staycationPerformanceMap.collectAsState()
    val toursPerformanceData by adminTable.tourPerformanceMap.collectAsState()
    val selectedSort by adminTable.selectedSort.collectAsState()
    val selectedMonthPerformance by adminTable.selectedMonthPerformance.collectAsState()
    val selectedYearPerformance by adminTable.selectedYearPerformance.collectAsState()

    val selectedItemIndex by rememberSaveable { mutableIntStateOf(2) }

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

    val tableHeader = listOf(
        "", "Title", "Host Name", "Number of Bookings", "Completed Bookings",
        "Pending Bookings", "Cancelled Bookings", "Total Views", "Average Rating"
    )

    val mapKey = listOf(
        "id",
        "title",
        "hostName",
        "totalBookings" ,
        "completedBookings",
        "pendingBookings",
        "cancelledBookings",
        "views",
        "averageRating",
    )

    val monthOptions = listOf(
        "All", "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )

    val yearOptions = (2023..LocalDate.now().year).toList().reversed()

    val sortOptions = listOf(
        "Highest Booking Count",
        "Lowest Booking Count",
        "Highest Completed Bookings",
        "Lowest Completed Bookings",
        "Highest Cancelled Bookings",
        "Lowest Cancelled Bookings",
        "Highest Pending Bookings",
        "Lowest Pending Bookings",
        "Highest Views",
        "Lowest Views",
        "Highest Rating",
        "Lowest Rating"
    )

    var expandMonthPerformance by remember {
        mutableStateOf(false)
    }

    val monthPerformanceIcon = if (expandMonthPerformance)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var expandYearPerformance by remember {
        mutableStateOf(false)
    }

    val yearPerformanceIcon = if (expandYearPerformance)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var expandSort by remember {
        mutableStateOf(false)
    }

    val sortIcon = if (expandSort)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    headerText = "Tables",
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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontalPaddingValue, verticalPaddingValue)
                    ) {

                        Text(
                            text = "Staycation & Tours",
                            color = Color(0xff333333),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 3.dp,
                                    start = 12.dp,
                                    end = 12.dp
                                )
                        ) {

                            Text(
                                text = "Filter By: $selectedMonthPerformance",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                            )
                            IconButton(
                                modifier = Modifier.size(24.dp),
                                onClick = { expandMonthPerformance = true }
                            ) {
                                Icon(
                                    imageVector = monthPerformanceIcon,
                                    contentDescription = "",
                                )
                            }
                            DropdownMenu(
                                expanded = expandMonthPerformance,
                                onDismissRequest = { expandMonthPerformance = false },
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
                                            adminTable.setSelectedMonthPerformance(label)
                                            expandMonthPerformance = false
                                        },
                                        contentPadding = PaddingValues(
                                            horizontal = 10.dp,
                                            vertical = 0.dp
                                        )
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = selectedYearPerformance.toString(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                            )
                            IconButton(
                                modifier = Modifier.size(24.dp),
                                onClick = { expandYearPerformance = true }
                            ) {
                                Icon(
                                    imageVector = yearPerformanceIcon,
                                    contentDescription = "",
                                )
                            }
                            DropdownMenu(
                                expanded = expandYearPerformance,
                                onDismissRequest = { expandYearPerformance = false },
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
                                            adminTable.setSelectedYearPerformance(label.toString())
                                            expandYearPerformance = false
                                        },
                                        contentPadding = PaddingValues(
                                            horizontal = 10.dp,
                                            vertical = 0.dp
                                        )
                                    )
                                }

                            }
                        }



                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 8.dp,
                                    start = 12.dp,
                                    end = 12.dp
                                )
                        ) {
                            Text(
                                text = "Sort By: $selectedSort",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                            )
                            IconButton(
                                modifier = Modifier.size(24.dp),
                                onClick = { expandSort = true }
                            ) {
                                Icon(
                                    imageVector = sortIcon,
                                    contentDescription = "",
                                )
                            }
                            DropdownMenu(
                                expanded = expandSort,
                                onDismissRequest = { expandSort = false },
                                modifier = Modifier
                                    .background(Color.White)
                            ) {
                                sortOptions.forEach { label ->
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
                                            adminTable.setSortBy(label)
                                            expandSort = false
                                        },
                                        contentPadding = PaddingValues(
                                            horizontal = 10.dp,
                                            vertical = 0.dp
                                        )
                                    )
                                }
                            }
                        }

                        if (staycationsPerformanceData.isEmpty() || toursPerformanceData.isEmpty()) {

                            Box(modifier = Modifier
                                .fillMaxWidth(.96f)
                                .align(Alignment.CenterHorizontally)
                                .height(500.dp)
                            ) {
                                LoadingScreen(
                                    isLoadingCompleted = false,
                                    isLightModeActive = true
                                )
                            }

                        } else {
                            Log.d("Row Size", "${(staycationsPerformanceData.size + 2 + toursPerformanceData.size + 3)}")

                            Table(
                                modifier = Modifier
                                    .fillMaxWidth(.96f)
                                    .align(Alignment.CenterHorizontally)
                                    .height(500.dp),
                                columnCount = tableHeader.size,
                                rowCount = (staycationsPerformanceData.size + 2 + toursPerformanceData.size + 1),
                                cellContent = { columnIndex, rowIndex ->

                                    val isHeader = rowIndex == 0
                                    val cellText =
                                        if (isHeader) {
                                            tableHeader[columnIndex]
                                        }
                                        else if (rowIndex == 1) {
                                            if (columnIndex == 0) {
                                                "Staycation"
                                            } else {
                                                ""

                                            }
                                        }                       //18 //CHECKED
                                        else if ((rowIndex == staycationsPerformanceData.size + 2)) {
                                            if (columnIndex == 0) {
                                                "Tour"
                                            } else {
                                                ""
                                            }
                                        }
                                        else if (columnIndex == 0) {
                                            ""
                                        }           //17            //18        //CHECKED
                                        else if (rowIndex < (staycationsPerformanceData.size + 2)) {
                                            staycationsPerformanceData[rowIndex - 2][mapKey[columnIndex]] ?: ""
                                        }          // 19        //18   16= staycationsPerformanceData.size   2 = HEADER + "STAYCATION ROW" //CHECKED
                                        else if (rowIndex > (staycationsPerformanceData.size + 2)) {

                                            //19   16= staycationsPerformanceData.size   2 = HEADER + "STAYCATION ROW" 1 = "TOUR" ROW
                                            toursPerformanceData[rowIndex - (staycationsPerformanceData.size + 2 + 1)][mapKey[columnIndex]] ?: ""
                                        }
                                        else {
                                       //     staycationsPerformanceData[rowIndex - 2][mapKey[columnIndex]] ?: ""

                                                                //19        //19   16= staycationsPerformanceData.size   2 = HEADER + "STAYCATION ROW" 1 = "TOUR" ROW
                                            ""
                                                                //[rowIndex - (staycationsPerformanceData.size + 2 + 1)]
                                        }

                                    // "Column: $columnIndex; Row: $rowIndex"



                                    Text(
                                        text = cellText,
                                        fontWeight = if (isHeader || cellText == "Staycation" || cellText == "Tour") FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier
                                            .wrapContentSize(Alignment.Center)
                                            .padding(all = 4.dp)

                                    )

                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Table(
    modifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    columnCount: Int,
    rowCount: Int,
    beforeRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    afterRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    cellContent: @Composable (columnIndex: Int, rowIndex: Int) -> Unit
) {
    val columnWidths = remember { mutableStateMapOf<Int, Int>() }

    Box(
        modifier = modifier
            //.simpleVerticalScrollbar(verticalLazyListState)
            .then(Modifier.horizontalScroll(horizontalScrollState)
             //   .simpleHorizontalScrollbar(horizontalScrollState)
            )
    ) {
        LazyColumn(state = verticalLazyListState) {
            items(rowCount) { rowIndex ->
                Column {
                    beforeRow?.invoke(rowIndex)

                    Row(modifier = rowModifier) {
                        (0 until columnCount).forEach { columnIndex ->
                            Box(
                                modifier = Modifier
                                    .border(width = 1.dp, color = Color.Black)
                                    .background(if (rowIndex == 0) Color.LightGray else Color.Transparent)
                                    .layout { measurable, constraints ->
                                        val placeable = measurable.measure(constraints)

                                        val existingWidth = columnWidths[columnIndex] ?: 0
                                        val maxWidth = maxOf(existingWidth, placeable.width)

                                        if (maxWidth > existingWidth) {
                                            columnWidths[columnIndex] = maxWidth
                                        }

                                        layout(width = maxWidth, height = placeable.height) {
                                            placeable.placeRelative(0, 0)
                                        }
                                    }

                            ) {
                                cellContent(columnIndex, rowIndex)
                            }
                        }
                    }

                    afterRow?.invoke(rowIndex)
                }
            }
        }

    }
}

//@Composable
//fun Table(
//    modifier: Modifier = Modifier,
//    rowModifier: Modifier = Modifier,
//    verticalLazyListState: LazyListState = rememberLazyListState(),
//    horizontalScrollState: ScrollState = rememberScrollState(),
//    columnCount: Int,
//    rowCount: Int,
//    beforeRow: (@Composable (rowIndex: Int) -> Unit)? = null,
//    afterRow: (@Composable (rowIndex: Int) -> Unit)? = null,
//    cellContent: @Composable (columnIndex: Int, rowIndex: Int) -> Unit
//) {
//    val columnWidths = remember { mutableStateMapOf<Int, Int>() }
//
//    Box(modifier = modifier.simpleVerticalScrollbar(verticalLazyListState).then(Modifier.horizontalScroll(horizontalScrollState).simpleHorizontalScrollbar(horizontalScrollState))) {
//        LazyColumn(state = verticalLazyListState) {
//            items(rowCount) { rowIndex ->
//                Column {
//                    beforeRow?.invoke(rowIndex)
//
//                    Row(modifier = rowModifier) {
//                        (0 until columnCount).forEach { columnIndex ->
//                            Box(
//                                modifier = Modifier
//                                    .border(width = 1.dp, color = Color.Black)
//                                    .background(if (rowIndex == 0) Color.LightGray else Color.Transparent)
//                                    .layout { measurable, constraints ->
//                                        val placeable = measurable.measure(constraints)
//
//                                        val existingWidth = columnWidths[columnIndex] ?: 0
//                                        val maxWidth = maxOf(existingWidth, placeable.width)
//
//                                        if (maxWidth > existingWidth) {
//                                            columnWidths[columnIndex] = maxWidth
//                                        }
//
//                                        layout(width = maxWidth, height = placeable.height) {
//                                            placeable.placeRelative(0, 0)
//                                        }
//                                    }
//
//                            ) {
//                                cellContent(columnIndex, rowIndex)
//                            }
//                        }
//                    }
//
//                    afterRow?.invoke(rowIndex)
//                }
//            }
//        }
//
//    }
//}

@Composable
fun Modifier.simpleVerticalScrollbar(
    state: LazyListState,
    width: Dp = 8.dp
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )

    return drawWithContent {
        drawContent()

        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

        // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
        if (needDrawScrollbar && firstVisibleElementIndex != null) {
            val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
            val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
            val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

            drawRect(
                color = Color.Red,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha
            )
        }
    }
}

@Composable
fun Modifier.simpleHorizontalScrollbar(
    state: ScrollState,
    width: Dp = 8.dp
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )


    return drawWithContent {
        drawContent()

        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

        // Draw scrollbar if scrolling or if the animation is still running
        if (needDrawScrollbar) {
            val contentHeight = state.maxValue - state.value
            val totalWidth = this.size.width
            val visibleWidth = totalWidth - width.toPx() // Adjusted width to exclude scrollbar width
            val scrollbarWidth = visibleWidth * (visibleWidth / (visibleWidth + contentHeight))
            val scrollbarOffsetX = state.value * (visibleWidth - scrollbarWidth) / contentHeight

            drawRect(
                color = Color.Red,
                topLeft = Offset(scrollbarOffsetX, this.size.height - width.toPx()),
                size = Size(scrollbarWidth, width.toPx()),
                alpha = alpha
            )

        }
    }

//    return drawWithContent {
//        drawContent()
//
//        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f
//
//        // Draw scrollbar if scrolling or if the animation is still running
//        if (needDrawScrollbar) {
//            val contentHeight = state.maxValue - state.value
//            val scrollbarWidth = this.size.width * (this.size.width / (this.size.width + contentHeight))
//            val scrollbarOffsetX = state.value * (this.size.width - scrollbarWidth) / contentHeight
//
//            drawRect(
//                color = Color.Red,
//                topLeft = Offset(scrollbarOffsetX, this.size.height - width.toPx()),
//                size = Size(scrollbarWidth, width.toPx()),
//                alpha = alpha
//            )
//
//        }
//    }
}


//@Composable
//fun Modifier.simpleHorizontalScrollbar(
//    state: LazyListState,
//    height: Dp = 8.dp
//): Modifier {
//    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
//    val duration = if (state.isScrollInProgress) 150 else 500
//
//    val alpha by animateFloatAsState(
//        targetValue = targetAlpha,
//        animationSpec = tween(durationMillis = duration)
//    )
//
//    return drawWithContent {
//        drawContent()
//
//        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
//        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f
//
//        // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
//        if (needDrawScrollbar && firstVisibleElementIndex != null) {
//            val elementWidth = this.size.width / state.layoutInfo.totalItemsCount
//            val scrollbarOffsetX = firstVisibleElementIndex * elementWidth
//            val scrollbarWidth = state.layoutInfo.visibleItemsInfo.size * elementWidth
//
//            drawRect(
//                color = Color.Red,
//                topLeft = Offset(scrollbarOffsetX, this.size.height - height.toPx()),
//                size = Size(scrollbarWidth, height.toPx()),
//                alpha = alpha
//            )
//        }
//    }
//}


