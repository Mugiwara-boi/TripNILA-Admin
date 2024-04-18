package com.example.tripnila_admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila_admin.ui.theme.TripNILAAdminTheme
import com.example.tripnila_admin.viewmodels.AdminReports
import com.example.tripnila_admin.viewmodels.AdminTables

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripNILAAdminTheme {

                Navigation(
                    adminReports = viewModel(modelClass = AdminReports::class.java),
                    adminTables = viewModel(modelClass = AdminTables::class.java)
                )

            }
        }
    }
}
