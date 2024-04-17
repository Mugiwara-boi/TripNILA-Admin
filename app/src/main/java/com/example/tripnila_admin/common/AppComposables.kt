package com.example.tripnila_admin.common

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tripnila_admin.R
import com.example.tripnila_admin.data.BottomNavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    headerText: String,
    color: Color = Color(0xFF999999),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: @Composable () -> Unit = {},
){

    //val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

    TopAppBar(
        title = {
            Text(
                text = headerText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = navigationIcon,
        scrollBehavior = scrollBehavior,
        modifier = modifier
            .drawWithContent {
                drawContent()
                drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2f
                )
            }

    )

}

data class ShimmerAnimationData(
    private val isLightMode: Boolean
) {
    fun getColours(): List<Color> {
        return if (isLightMode) {
            val color = Color.White

            listOf(
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 1.0f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0.3f),
            )
        } else {
            val color = Color.Black

            listOf(
                color.copy(alpha = 0.0f),
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.0f),
            )
        }
    }
}


@Composable
fun LoadingScreen(
    isLoadingCompleted: Boolean,
    isLightModeActive: Boolean,
) {
    Box(
        modifier = Modifier
            .background(color = Color.LightGray.copy(.8f))
            .fillMaxSize()
            .shimmerLoadingAnimation(isLoadingCompleted, isLightModeActive)
    )
}

fun Modifier.shimmerLoadingAnimation(
    isLoadingCompleted: Boolean = true, // <-- New parameter for start/stop.
    isLightModeActive: Boolean = true, // <-- New parameter for display modes.
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier {
    if (isLoadingCompleted) { // <-- Step 1.
        return this
    }
    else {
        return composed {

            // Step 2.
            val shimmerColors = ShimmerAnimationData(isLightMode = isLightModeActive).getColours()

            val transition = rememberInfiniteTransition(label = "")

            val translateAnimation = transition.animateFloat(
                initialValue = 0f,
                targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = durationMillis,
                        easing = LinearEasing,
                    ),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "Shimmer loading animation",
            )

            this.background(
                brush = Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
                    end = Offset(x = translateAnimation.value, y = angleOfAxisY),
                ),
            )
        }
    }
}

@Composable
fun AppDropDownFilter(options: List<String>, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(options.firstOrNull() ?: "") }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = selectedText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
        )
        IconButton(
            modifier = Modifier.size(20.dp),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
        ) {
            options.forEach { label ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    colors = MenuDefaults.itemColors(
                        textColor = Color(0xFF6B6B6B)
                    ),
                    onClick = {
                        selectedText = label
                        expanded = false
                    }
                )
            }
        }
    }

}
@Composable
fun ChartDropDownFilter(
    options: List<String>,
    onItemSelected: (String) -> Unit, // Callback function to handle selection changes
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf(options.firstOrNull() ?: "") }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = selectedYear,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
        )
        IconButton(
            modifier = Modifier.size(20.dp),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
        ) {
            options.forEach { label ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    colors = MenuDefaults.itemColors(
                        textColor = Color(0xFF6B6B6B)
                    ),
                    onClick = {
                        selectedYear = label
                        expanded = false
                        onItemSelected(label) // Call the callback function
                    }
                )
            }
        }
    }
}

@Composable
fun AppOutlinedButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    containerColor: Color = Color.White,
    buttonShape: RoundedCornerShape = RoundedCornerShape(10.dp),
    borderStroke: BorderStroke = BorderStroke(1.dp, Color(0xFFF9A664)),
    contentPadding: PaddingValues = PaddingValues(10.dp),
    contentFontSize: TextUnit = 16.sp,
    contentFontWeight: FontWeight = FontWeight.Medium,
    contentColor: Color = Color(0xFFF9A664),
    onClick: () -> Unit,
    enableButton: Boolean = true
){

    OutlinedButton(
        onClick = onClick,
        border = if (enableButton) borderStroke else BorderStroke(1.dp, contentColor.copy(alpha = 0.3f)),
        shape = buttonShape,
        colors = ButtonDefaults.outlinedButtonColors(containerColor),
        contentPadding = contentPadding,
        enabled = enableButton,
        modifier = modifier
    ) {
        Text(
            text = buttonText,
            fontSize = contentFontSize,
            fontWeight = contentFontWeight,
            color = if (enableButton) contentColor else contentColor.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun AppFilledButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    buttonShape: RoundedCornerShape = RoundedCornerShape(10.dp),
    onClick: () -> Unit,
    containerColor: Color = Color(0xFFF9A664),
    contentPadding: PaddingValues = PaddingValues(10.dp),
    contentFontSize: TextUnit = 16.sp,
    contentFontWeight: FontWeight = FontWeight.Medium,
    contentColor: Color = Color.White,
    isLoading: Boolean = false,
    strokeWidth: Dp = 3.dp,
    enabled: Boolean = true,
    circularProgressIndicatorSize: Dp = 20.dp
){

    Button(
        onClick = onClick,
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor.copy(0.3f)
        ),
        contentPadding = contentPadding,
        enabled = enabled,
        modifier = modifier
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = strokeWidth,
                modifier = Modifier.size(circularProgressIndicatorSize)
            )
        } else {
            Text(
                text = buttonText,
                fontSize = contentFontSize,
                fontWeight = contentFontWeight,
                color = contentColor
            )
        }

    }

}


@Composable
fun SeeAllButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    containerColor: Color = Color.White,
    buttonShape: RoundedCornerShape = RoundedCornerShape(10.dp),
    borderStroke: BorderStroke = BorderStroke(1.dp, Color(0xFFF9A664)),
    contentPadding: PaddingValues = PaddingValues(10.dp),
    contentFontSize: TextUnit = 16.sp,
    contentFontWeight: FontWeight = FontWeight.Medium,
    contentColor: Color = Color(0xFFF9A664),
    onClick: () -> Unit,
    enableButton: Boolean = true
){

    OutlinedButton(
        onClick = onClick,
        border = if (enableButton) borderStroke else BorderStroke(1.dp, contentColor.copy(alpha = 0.3f)),
        shape = buttonShape,
        colors = ButtonDefaults.outlinedButtonColors(containerColor),
        contentPadding = contentPadding,
        enabled = enableButton,
        modifier = modifier
    ) {
        Text(
            text = buttonText,
            fontSize = contentFontSize,
            fontWeight = contentFontWeight,
            color = if (enableButton) contentColor else contentColor.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun AppRatingBar(
    modifier: Modifier = Modifier,
    initialRating: Int
) {
    val ratingState by remember {
        mutableIntStateOf(initialRating)
    }

    Row(
        modifier = modifier,//.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.star_rating),
                contentDescription = "star",
                modifier = Modifier
                    .padding(2.dp)
                    .width(10.dp)
                    .height(10.dp),
                tint = if (i <= ratingState) Color(0xFFF9A664) else Color(0xFFA2ADB1)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBarWithIcon(headerText: String, onLogout: () -> Unit, headerIcon: ImageVector, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(
                text = headerText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
        actions = {
            IconButton(onClick = { onLogout() }) {
                Icon(
                    imageVector = headerIcon,
                    contentDescription = "",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        modifier = modifier

    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBottomNavigationBar(
    adminId: String = "",
    selectedItemIndex: Int,
    navController: NavHostController
) {
    val items = listOf(
        BottomNavigationItem(
            title = "Dashboard",
            selectedIcon = R.drawable.home_filled,
            unselectedIcon = R.drawable.home_outline,
            hasNews = false

            ),
        BottomNavigationItem(
            title = "Reports",
            selectedIcon = R.drawable.dashboard_filled,
            unselectedIcon = R.drawable.dashboard_outlined,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = R.drawable.account_filled,
            unselectedIcon = R.drawable.account_outlined,
            hasNews = false
        )
    )


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 10.dp
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == item.title } == true,
                onClick = {

                    val route = when (index) {
                        0 -> "Dashboard"
                        1 -> "Reports"
                        2 -> "Profile"
                        else -> "Dashboard"
                    }
                                    //"$route/$adminId"
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                    Log.d("Navigation", "Navigating to ${item.title} with adminId: $adminId")

                },
                icon = {
                    BadgedBox(
                        badge = {
                            if(item.badgeCount != null) {
                                Badge {
                                    Text(text = item.badgeCount.toString())
                                }
                            } else if(item.hasNews) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            painter = if (index == selectedItemIndex) {
                                painterResource(id = item.selectedIcon)
                            } else painterResource(id = item.unselectedIcon),
                            contentDescription = item.title,
                            tint = if (index == selectedItemIndex) Color(0xFFF9A664) else Color.Gray,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        }
    }
}



@Preview
@Composable
private fun AppComposablePreview() {
  //  AdminBottomNavigationBar(selectedItemIndex = 2, navController = rememberNavController())


}
