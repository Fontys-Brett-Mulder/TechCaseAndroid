package com.brettfontys.techcase

import NotificationHelper
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.brettfontys.techcase.ui.theme.TechCaseTheme
import android.Manifest
import android.os.Handler
import android.os.Looper
import androidx.compose.material3.Button
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var notificationHelper: NotificationHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationHelper = NotificationHelper(this)

        setContent {
            TechCaseTheme {
                // This function is being used as state, when this var gets updated, the UI gets updated as well
                var sendingStatus by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Tech Case",
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "This tech case has the following cases inside it: May I interrupt you? & What's your color?",
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = { askNotificationPermission() }) {
                        Text(text = "Ask for permission")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = { startSendingNotifications(); sendingStatus = true }) {
                        Text(text = "Start Sending Messages")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = { stopSendingNotifications(); sendingStatus = false }) {
                        Text(text = "Stop Sending Messages")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    if (sendingStatus){
                        Text(text = "Sending messages every 10 seconds...")
                    }else{
                        Text(text = "Sending nothing...")
                    }
                }
            }
        }
    }

    /**
     * Creating a runnable object with a delay of 10 seconds
     */
    private val runnable = object : Runnable {
        override fun run() {
            notificationHelper.sendNotification()
            handler.postDelayed(this, 10000)
        }
    }

    /**
     * Start the Sending Notifications every 10 seconds
     */
    fun startSendingNotifications() {
        handler.post(runnable)
    }

    /**
     * Stop sending notifications
     */
    fun stopSendingNotifications() {
        handler.removeCallbacks(runnable)
    }

    /**
     * The launcher for asking permission for notifications
     */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    /**
     * Function to request the users permission to send notifications
     * When the function is being allowed once, the app wil remember it
     */
    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TechCaseTheme {

    }
}