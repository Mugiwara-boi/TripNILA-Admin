package com.example.tripnila_admin.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tripnila_admin.R
import com.example.tripnila_admin.common.AdminBottomNavigationBar
import com.example.tripnila_admin.common.AppFilledButton
import com.example.tripnila_admin.common.AppTopBar
import com.example.tripnila_admin.common.AppTopBarWithIcon

@Composable
fun AdminProfileScreen(
    adminId: String = "",
    navController: NavHostController? = null
) {

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val selectedItemIndex by rememberSaveable { mutableIntStateOf(2) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                AppTopBarWithIcon(
                    headerText = "Reports",
                    headerIcon = ImageVector.vectorResource(id = R.drawable.logout),
                    onLogout = {
                        /*TODO*/
                    }
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
            }
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                item {
                    AdminProfile(
                        profilePicture = "",
                        name = "Admin",
                        userName = "admin1"
                    )
                }
                item {
                    AppFilledButton(
                        buttonText = "Edit Profile",
                        onClick = { /*TODO*/ },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        contentFontSize = 16.sp,
                        buttonShape = RoundedCornerShape(10.dp),
                        modifier = Modifier.padding(vertical = verticalPaddingValue).height(40.dp)
                    )
                }
                item {
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = horizontalPaddingValue)
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.Start)

                    )
                }
                item {
                    OptionsRow(
                        icon = R.drawable.notification,
                        rowText = "Notification",
                        onClick = {
                            /*TODO*/
                        },
                        modifier = Modifier
                            .padding(horizontal = horizontalPaddingValue)
                    )
                }
                item {
                    OptionsRow(
                        icon = R.drawable.payment,
                        rowText = "Payment and payouts",
                        onClick = {
                            /*TODO*/
                        },
                        modifier = Modifier
                            .padding(horizontal = horizontalPaddingValue)
                    )
                }
                item {
                    OptionsRow(
                        icon = R.drawable.privacy,
                        rowText = "Privacy",
                        onClick = {
                            /*TODO*/
                        },
                        modifier = Modifier
                            .padding(horizontal = horizontalPaddingValue)
                    )
                }

                item {
                    Text(
                        text = "Legal",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = horizontalPaddingValue)
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.Start)
                    )

                }
                item {
                    OptionsRow(
                        icon = R.drawable.document,
                        rowText = "Update Terms of Service",
                        onClick = {
                            /*TODO*/
                        },
                        modifier = Modifier
                            .padding(horizontal = horizontalPaddingValue)
                    )
                }
                item {
                    OptionsRow(
                        icon = R.drawable.document,
                        rowText = "Update Privacy Policy",
                        onClick = {
                            /*TODO*/
                        },
                        modifier = Modifier
                            .padding(horizontal = horizontalPaddingValue)
                    )
                }
                item {
                    Text(
                        text = "Version 1.0 (Beta)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF999999),
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = horizontalPaddingValue)
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.Start)
                    )
                }
            }
        }
    }
}

@Composable
fun AdminProfile(
    modifier: Modifier = Modifier,
    profilePicture: String,
    name: String,
    userName: String
) {

    val imagePlaceholder = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = if (profilePicture == "") imagePlaceholder else profilePicture,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .size(140.dp)
                .clip(shape = RoundedCornerShape(100))

        )
        Text(
            text = name, //Admin
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "@$userName", //admin1
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF999999)
        )
    }
}

@Composable
fun OptionsRow(
    icon: Int,
    rowText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = rowText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 3.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier.size(34.dp)
            )
        }
    }
    Divider(modifier = Modifier.padding(vertical = 5.dp))
}


@Preview
@Composable
private fun AdminProfilePreview() {

    AdminProfileScreen()
}
