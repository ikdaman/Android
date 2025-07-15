package project.side.ikdaman.feature.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.main.MainActivity
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "알람 수신됨: ${intent.action}")
        showNotification(context)
    }

    private fun showNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 알림 채널 생성 (Android 8.0 이상 필수)
        val channel = NotificationChannel(
            CHANNEL_ID,
            "정기 알림",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "월요일과 금요일에 발송되는 정기 알림입니다"
        }
        notificationManager.createNotificationChannel(channel)

        // 현재 요일과 시간에 맞는 메시지 가져오기
        val message = getNotificationMessage()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 생성
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_small_notification)
            .setColor(context.getColor(android.R.color.black))
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun getNotificationMessage(): String {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        // 시간대 구분
        val timeSlot = when (hourOfDay) {
            in 6..10 -> TimeSlot.MORNING
            in 11..15 -> TimeSlot.AFTERNOON
            in 16..20 -> TimeSlot.EVENING
            else -> TimeSlot.NIGHT
        }

        // 요일 구분
        val day = when (dayOfWeek) {
            Calendar.MONDAY -> Day.MONDAY
            Calendar.FRIDAY -> Day.FRIDAY
            else -> Day.MONDAY // 기본값
        }

        return getMessageForDayAndTime(day, timeSlot)
    }

    private fun getMessageForDayAndTime(day: Day, timeSlot: TimeSlot): String {
        return when (day) {
            Day.MONDAY -> when (timeSlot) {
                TimeSlot.MORNING -> "✏️ 월요일 아침이에요. 지난번 읽던 책으로 기분 좋은 한 주를 시작해보세요."
                TimeSlot.AFTERNOON -> "📖 점심시간의 작은 여유, 읽던 책 중 한 권을 이어서 읽어보는 건 어때요? 생각이 환기될지도 몰라요."
                TimeSlot.EVENING -> "☁️ 오후의 고요함 속에서 책 한 권 펼쳐보세요. 오늘은 잠시 다른 책으로 마음을 전환해보는 건 어떨까요?"
                TimeSlot.NIGHT -> "🌙 하루의 끝, 조용한 밤이에요. 여러 권 중 어떤 책이 지금 가장 끌리시나요? 오늘의 마음에 맞는 책을 골라보세요."
            }
            Day.FRIDAY -> when (timeSlot) {
                TimeSlot.MORNING -> "🌞 금요일 아침이에요. 주말을 앞두고 기분 좋은 책의 한 페이지로 시작해보세요."
                TimeSlot.AFTERNOON -> "🍵 나른한 금요일 오후, 잠시 쉬어가며 책 속으로 여행을 떠나보세요. 평소 아껴둔 그 책, 지금이 딱이에요."
                TimeSlot.EVENING -> "🌆 이번 주도 수고 많으셨어요. 오늘은 지난번에 멈춰두었던 책을 다시 펼쳐보는 건 어떠세요?"
                TimeSlot.NIGHT -> "🌌 편안한 금요일 밤이에요. 이번 주 읽었던 책들을 천천히 정리해보고, 아직 덜 읽은 책도 놓치지 마세요."
            }
        }
    }

    enum class Day {
        MONDAY, FRIDAY
    }

    enum class TimeSlot {
        MORNING,    // 06~10시
        AFTERNOON,  // 11~15시
        EVENING,    // 16~20시
        NIGHT       // 21~05시
    }

    companion object {
        private const val CHANNEL_ID = "regular_notification_channel"
        private const val NOTIFICATION_ID = 1001
    }
}