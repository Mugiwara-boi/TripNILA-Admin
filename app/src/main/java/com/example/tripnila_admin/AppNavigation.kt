package com.example.tripnila_admin

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.tripnila_admin.screens.AdminDashboardScreen
import com.example.tripnila_admin.screens.AdminProfileScreen
import com.example.tripnila_admin.screens.AdminReportsScreen
import com.example.tripnila_admin.screens.GeneratedReportScreen
import com.example.tripnila_admin.viewmodels.AdminDashboard
import com.example.tripnila_admin.viewmodels.AdminReports

enum class LoginRoutes {

}

enum class AdminRoutes {
    Dashboard,
    Reports,
    Profile,
    GeneratedReport
}

enum class NestedRoutes {
    Admin
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    adminReports: AdminReports
) {
    NavHost(navController = navController, startDestination = NestedRoutes.Admin.name) {
        adminGraph(
            navController = navController, adminReports
        )
    }
}


fun NavGraphBuilder.adminGraph(
    navController: NavHostController,
    adminReports: AdminReports
) {
    navigation(
        startDestination = AdminRoutes.Dashboard.name,
        route = NestedRoutes.Admin.name
    ) {
        composable(
            route = AdminRoutes.Dashboard.name, //  + "/{adminId}"
            arguments = listOf(navArgument("adminId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            AdminDashboardScreen(
                adminId = it.arguments?.getString("adminId") ?: "",
                navController = navController,
                dashboardViewModel = AdminDashboard()
            )
        }
        composable(
            route = AdminRoutes.Reports.name,
            arguments = listOf(navArgument("adminId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            AdminReportsScreen(
                adminId = it.arguments?.getString("adminId") ?: "",
                navController = navController,
                adminReports = adminReports,
                onNavToGeneratedReport = { reportType ->
                    navigateToGeneratedReportScreen(navController, reportType)
                }
            )
        }
        composable(
            route = AdminRoutes.Profile.name,
            arguments = listOf(navArgument("adminId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            AdminProfileScreen(
                adminId = it.arguments?.getString("adminId") ?: "",
                navController = navController
            )
        }
        composable(
            route = AdminRoutes.GeneratedReport.name + "/{reportType}",
            arguments = listOf(navArgument("reportType") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            GeneratedReportScreen(
                adminReports = adminReports,
                reportType = it.arguments?.getString("reportType") ?: "",
                onNavToBack = {
                    onNavToBack(navController)
                }
            )
        }
    }
}

private fun onNavToBack(navController: NavHostController) {
    navController.navigate(AdminRoutes.Reports.name) {
        launchSingleTop = true
    }
}

private fun navigateToGeneratedReportScreen(navController: NavHostController, reportType: String) {
    navController.navigate("${AdminRoutes.GeneratedReport.name}/$reportType") {
        launchSingleTop = true
    }
}

//fun NavGraphBuilder.adminGraph(
//    navController: NavHostController
//) {
//    navigation(
//        startDestination = AdminRoutes.Dashboard.name,
//        route = NestedRoutes.Admin.name
//    ) {
//        composable(
//            route = AdminRoutes.Dashboard.name + "/{adminId}",
//            arguments = listOf(navArgument("adminId") {
//                type = NavType.StringType
//                defaultValue = ""
//            })
//        ) {
//            AdminDashboardScreen(
//                adminId = it.arguments?.getString("adminId") ?: "",
//                navController = navController
//            )
//        }
//        composable(
//            route = AdminRoutes.Reports.name + "/{adminId}",
//            arguments = listOf(navArgument("adminId") {
//                type = NavType.StringType
//                defaultValue = ""
//            })
//        ) {
//            AdminReportsScreen(
//                adminId = it.arguments?.getString("adminId") ?: "",
//                navController = navController
//            )
//        }
//    }
//}