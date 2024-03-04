package com.example.tripnila_admin.screens

import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.example.tripnila_admin.common.AppDropDownFilter
import com.example.tripnila_admin.common.AppFilledButton
import com.example.tripnila_admin.common.AppOutlinedButton
import com.example.tripnila_admin.common.AppRatingBar
import com.example.tripnila_admin.common.AppTopBar
import com.example.tripnila_admin.viewmodels.AdminReports
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportsScreen(
    adminId: String = "",
    adminReports: AdminReports,
    navController: NavHostController? = null
) {

    LaunchedEffect(adminId){
        adminReports.fetchPendingVerifications()
    }

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp


    val pendingVerifications by adminReports.pendingVerifications.collectAsState()
    val scope = rememberCoroutineScope()

    val selectedItemIndex by rememberSaveable { mutableIntStateOf(1) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

    Log.d("AdminReportsScreen", "Pending verifications size: ${pendingVerifications.size}")


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
                        scope = scope
                    )

                }

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
    scope: CoroutineScope
) {

    val context = LocalContext.current

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
                    modifier = Modifier.padding(horizontal = 3.dp).wrapContentWidth(Alignment.Start).padding(top = 10.dp)
                )
                Text(
                    text = reportDateTime, //"2022-09-27 18:00:00",
                    color = Color(0xff999999),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f).wrapContentWidth(Alignment.Start).padding(top = 10.dp)
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

   /* UserVerificationCard(
        userImage = "",
        userFullName = "Denice Lucio",
        userUsername = "denlucio",
        reportDateTime = "2022-09-27 18:00:00",
        firstValidIdType = "POSTAL ID",
        firstValidIdUrl = "https://firebasestorage.googleapis.com/v0/b/tripnila-20a77.appspot.com/o/1709488149228_1000009707?alt=media&token=fc19b530-c234-4141-90e4-c6e15d3374b1",
        secondValidIdType = "SCHOOL ID",
        secondValidIdUrl = "https://firebasestorage.googleapis.com/v0/b/tripnila-20a77.appspot.com/o/1709488149228_1000009707?alt=media&token=fc19b530-c234-4141-90e4-c6e15d3374b1",
        // modifier = Modifier.padding(vertical = verticalPaddingValue)
    )*/
}