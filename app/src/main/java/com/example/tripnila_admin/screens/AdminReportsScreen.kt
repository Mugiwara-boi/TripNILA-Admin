package com.example.tripnila_admin.screens

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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportsScreen(
    adminId: String = "",
    navController: NavHostController? = null
) {

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val selectedItemIndex by rememberSaveable { mutableIntStateOf(1) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

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
                            text = "Staycation",
                            color = Color(0xff333333),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                        )
                        AppDropDownFilter(
                            options = listOf("Recent", "...", "...", "..."),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 3.dp)
                        )
                        ServiceReportCard(
                            serviceImage = "",
                            reporterImage = "",
                            serviceName = "Quiapo Staycation with pool by Jun",
                            serviceLocation = "Quiapo, Manila",
                            reportDateTime = "2022-09-27 18:00:00",
                            reporterFullName = "Maya Cruz",
                            reporterUsername = "mcruz",
                            reportType = "Scamming",
                            report = "This is a scam. we checked the location and there’s no building on it.",
                           // modifier = Modifier.padding(vertical = verticalPaddingValue)
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
                            text = "Tour",
                            color = Color(0xff333333),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                        )
                        AppDropDownFilter(
                            options = listOf("Recent", "...", "...", "..."),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 3.dp)
                        )
                        ServiceReportCard(
                            serviceImage = "",
                            reporterImage = "",
                            serviceName = "Quiapo Tour by Janella",
                            serviceLocation = "Quiapo, Manila",
                            reportDateTime = "2022-09-27 18:00:00",
                            reporterFullName = "Maya Cruz",
                            reporterUsername = "mcruz",
                            reportType = "Scamming",
                            report = "This is a scam. we checked the location and there’s no building on it.",
                            // modifier = Modifier.padding(vertical = verticalPaddingValue)
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
                            text = "Business",
                            color = Color(0xff333333),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                        )
                        AppDropDownFilter(
                            options = listOf("Recent", "...", "...", "..."),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 3.dp)
                        )
                        ServiceReportCard(
                            serviceImage = "",
                            reporterImage = "",
                            serviceName = "Sta. Cruz Park",
                            serviceLocation = "Quiapo, Manila",
                            reportDateTime = "2022-09-27 18:00:00",
                            reporterFullName = "Maya Cruz",
                            reporterUsername = "mcruz",
                            reportType = "Scamming",
                            report = "This is a scam. we checked the location and there’s no building on it.",
                            // modifier = Modifier.padding(vertical = verticalPaddingValue)
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
                            text = "Reviews",
                            color = Color(0xff333333),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                        )
                        AppDropDownFilter(
                            options = listOf("Recent", "...", "...", "..."),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 3.dp)
                        )
                        ReviewReportCard(
                            serviceImage = "",
                            reviewerImage = "",
                            serviceName = "Quiapo Tour by Janella",
                            serviceLocation = "Quiapo, Manila",
                            reportDateTime = "2022-09-27 18:00:00",
                            reviewerFullName = "Maya Cruz",
                            reviewerUsername = "mcruz",
                            reportType = "Inappropriate words used",
                            review = "You fuckers are the worst!!",
                            reviewDateTime = "2022-09-25 18:00:00",
                            // modifier = Modifier.padding(vertical = verticalPaddingValue)
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
                            text = "Users",
                            color = Color(0xff333333),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                        )
                        AppDropDownFilter(
                            options = listOf("Recent", "...", "...", "..."),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 3.dp)
                        )
                        UserReportCard(
                            userImage = "",
                            reporterImage = "",
                            userFullName = "Denice Lucio",
                            userUsername = "denlucio",
                            reportDateTime = "2022-09-27 18:00:00",
                            reporterFullName = "Maya Cruz",
                            reporterUsername = "mcruz",
                            reportType = "Scamming",
                            report = "This person is a known scammer, please remove her",
                            // modifier = Modifier.padding(vertical = verticalPaddingValue)
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
                            text = "Feedback",
                            color = Color(0xff333333),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                        )
                        AppDropDownFilter(
                            options = listOf("Recent", "...", "...", "..."),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 3.dp)
                        )
                        FeedbackCard(
                            userImage = "",
                            userFullName = "Chino Abat",
                            userUsername = "abatchi",
                            feedbackDateTime = "2022-09-27 18:00:00",
                            feedback = "Your application is very good, but I have a couple of suggestions that you could add since I found a couple of things....",
                            // modifier = Modifier.padding(vertical = verticalPaddingValue)
                        )
                    }
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
                            modifier = Modifier.padding(start = 6.dp, end = 3.dp).offset(y = 1.dp)
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
fun UserReportCard(
    modifier: Modifier = Modifier,
    userFullName: String,
    userUsername: String,
    userImage: String,
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
                    model = if (reporterImage == "") imagePlaceholder else reporterImage,
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
                            modifier = Modifier.padding(start = 6.dp, end = 3.dp).offset(y = 1.dp)
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

    AdminReportsScreen()
}