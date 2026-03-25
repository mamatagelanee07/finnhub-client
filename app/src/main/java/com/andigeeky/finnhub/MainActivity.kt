package com.andigeeky.finnhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.andigeeky.finnhub.ipoCalendar.ui.IPOCalendarScreen
import com.andigeeky.finnhub.ui.theme.FinnhubclientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinnhubclientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IPOCalendarScreen()
                }
            }
        }
    }
}