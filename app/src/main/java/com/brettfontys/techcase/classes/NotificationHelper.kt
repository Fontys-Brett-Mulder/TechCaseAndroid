import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat


class NotificationHelper(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val notificationChannelId = "MyNotificationChannel"

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                "Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    /**
     * Send notifications message
     */
    fun sendNotification() {
        val notificationBuilder = NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
            .setContentTitle("Hello, may I interrupt you?")
            .setContentText("You have wasted your time opening this message :)")
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }
}