package com.example.tripnila_admin

import androidx.compose.runtime.Composable
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

enum class LoginRoutes {

}

enum class AdminRoutes {
    Dashboard,
    Reports,
    Profile
}

enum class NestedRoutes {
    Admin
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = NestedRoutes.Admin.name) {
        adminGraph(
            navController = navController
        )
    }
}


fun NavGraphBuilder.adminGraph(
    navController: NavHostController
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
                navController = navController
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
                navController = navController
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