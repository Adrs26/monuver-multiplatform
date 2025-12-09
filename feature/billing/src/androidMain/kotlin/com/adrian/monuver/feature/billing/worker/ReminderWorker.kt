package com.adrian.monuver.feature.billing.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.adrian.monuver.core.data.datastore.MonuverPreferences
import com.adrian.monuver.core.domain.usecase.GetUnpaidBillsUseCase
import com.adrian.monuver.feature.billing.R
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class ReminderWorker(
    context: Context,
    params: WorkerParameters,
    private val preferences: MonuverPreferences,
    private val getUnpaidBillsUseCase: GetUnpaidBillsUseCase
//    private val notificationNavigator: NotificationNavigator,
) : CoroutineWorker(context, params) {

    @OptIn(ExperimentalTime::class)
    override suspend fun doWork(): Result {
        val reminderDaysBeforeDue = preferences.reminderDaysBeforeDue.first()
        val isReminderBeforeDueDayEnabled = preferences.isReminderBeforeDueDayEnabled.first()
        val isReminderForDueBillEnabled = preferences.isReminderForDueBillEnabled.first()

        val bills = getUnpaidBillsUseCase()

        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date

        bills.forEach { bill ->
            val dueDate = LocalDate.parse(bill.dueDate)
            val daysDiff = today.daysUntil(dueDate).toLong()

            val isReminderDayBeforeDue = daysDiff == reminderDaysBeforeDue.toLong()
            val isReminderRangeBeforeDue =
                isReminderBeforeDueDayEnabled && (daysDiff in 1..reminderDaysBeforeDue)

            when {
                isReminderDayBeforeDue || isReminderRangeBeforeDue -> {
                    showNotification(
                        title = applicationContext.getString(R.string.do_not_forget_pay_your_bill),
                        message = applicationContext.getString(
                            R.string.n_days_before_due,
                            daysDiff,
                            bill.title
                        ),
                        notificationId = bill.id.toInt()
                    )
                }

                daysDiff == 0L && isReminderForDueBillEnabled -> {
                    showNotification(
                        title = applicationContext.getString(R.string.do_not_forget_pay_your_bill),
                        message = applicationContext.getString(R.string.due_days, bill.title),
                        notificationId = bill.id.toInt()
                    )
                }

                daysDiff < 0 && isReminderForDueBillEnabled -> {
                    showNotification(
                        title = applicationContext.getString(R.string.do_not_forget_pay_your_bill),
                        message = applicationContext.getString(
                            R.string.n_days_after_due,
                            bill.title,
                            abs(daysDiff)
                        ),
                        notificationId = bill.id.toInt()
                    )
                }
            }
        }

        return Result.success()
    }

    private fun showNotification(title: String, message: String, notificationId: Int) {
        val channelId = "reminder_channel"
        val context = applicationContext

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminder Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

//        val intent = notificationNavigator.openMainActivity(context)
//
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            notificationId.hashCode(),
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_overview)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(notificationId, notification)
        }
    }
}

internal fun startReminderWorker(context: Context) {
    val workManager = WorkManager.getInstance(context)
    val request = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS).build()

    workManager.enqueueUniquePeriodicWork(
        "bill_reminder",
        ExistingPeriodicWorkPolicy.KEEP,
        request
    )
}