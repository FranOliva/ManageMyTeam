package es.us.managemyteam.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    companion object {
        private const val DEFAULT_DATE_FORMAT = "dd/MM/yyyy"
        private const val DEFAULT_ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
        private const val DATE_FORMAT_MONTH_COMPLETE_TEXT = "MMMM"
        private const val DATE_FORMAT_DAY_OF_WEEK_TEXT = "EEEE"
        private const val TIME_FORMAT = "HH:mm"
        private const val TIME_FORMAT_SECONDS = "HH:mm:ss"

        fun format(iso: String, format: String = DEFAULT_DATE_FORMAT): String {
            return format(parseIso(iso), format)
        }

        fun format(timestamp: Long, format: String = DEFAULT_DATE_FORMAT): String {
            return format(Date().apply { time = timestamp }, format)
        }

        fun format(calendar: Calendar, format: String = DEFAULT_DATE_FORMAT): String {
            return format(calendar.timeInMillis, format)
        }

        fun format(year: Int, month: Int, day: Int, format: String = DEFAULT_DATE_FORMAT): String {
            return format(Calendar.getInstance().apply {
                this[Calendar.YEAR] = year
                this[Calendar.MONTH] = month
                this[Calendar.DAY_OF_MONTH] = day
            }, format)
        }

        fun format(date: Date, format: String = DEFAULT_DATE_FORMAT): String {
            return SimpleDateFormat(format, Locale.getDefault()).format(date)
        }

        fun parseIso(iso: String, isoFormat: String = DEFAULT_ISO_FORMAT): Date {
            return try {
                SimpleDateFormat(isoFormat, Locale.getDefault()).parse(iso)
            } catch (ex: Exception) {
                ex.printStackTrace()
                Date()
            }
        }

        fun getDayFromMillis(millis: Long): Int {
            return getCalendarFromMillis(millis)[Calendar.DAY_OF_MONTH]
        }

        fun getMonthLabelFromMillis(millis: Long): String {
            return SimpleDateFormat(DATE_FORMAT_MONTH_COMPLETE_TEXT, Locale.getDefault()).format(
                getCalendarFromMillis(millis).time
            )
        }

        fun getTimeFromMillis(millis: Long): String {
            return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(
                getCalendarFromMillis(
                    millis
                ).time
            )
        }

        fun getMonthFromMillis(millis: Long): Int {
            return getCalendarFromMillis(millis)[Calendar.MONTH]
        }

        fun getYearFromMillis(millis: Long): Int {
            return getCalendarFromMillis(millis)[Calendar.YEAR]
        }

        fun getDayOfWeekFromMillis(millis: Long): String {
            return SimpleDateFormat(DATE_FORMAT_DAY_OF_WEEK_TEXT, Locale.getDefault()).format(
                getCalendarFromMillis(millis).time
            ).capitalize()
        }

        fun getTimeMillis(year: Int, month: Int, dayOfMonth: Int): Long {
            return Calendar.getInstance().apply {
                this[Calendar.YEAR] = year
                this[Calendar.MONTH] = month
                this[Calendar.DAY_OF_MONTH] = dayOfMonth
            }.timeInMillis
        }

        fun getCurrentDate(): Long {
            return Date().time
        }

        private fun getCalendarFromMillis(millis: Long): Calendar {
            return Calendar.getInstance().apply {
                timeInMillis = millis
            }
        }

        fun formatTimeWithoutSeconds(timeWithSeconds: String): String {
            val formatWithSeconds = SimpleDateFormat(TIME_FORMAT_SECONDS, Locale.getDefault())
            val formatWithoutSeconds = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
            return formatWithoutSeconds.format(formatWithSeconds.parse(timeWithSeconds))
        }

    }

}