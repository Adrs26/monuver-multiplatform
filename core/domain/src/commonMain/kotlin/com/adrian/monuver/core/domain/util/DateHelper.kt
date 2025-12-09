package com.adrian.monuver.core.domain.util

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object DateHelper {
    fun formatToReadable(inputDate: String): String {
        return try {
            val date = LocalDate.parse(inputDate)
            val monthName = date.month.number.toMonthNameId()
            "${date.day} $monthName ${date.year}"
        } catch (_: Exception) {
            inputDate
        }
    }

    @OptIn(ExperimentalTime::class)
    fun formatToLocalDate(timeMillis: Long): LocalDate {
        val instant = Instant.fromEpochMilliseconds(timeMillis)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return localDate
    }

    fun formatToShortDate(inputDate: String): String {
        val parsedDate = LocalDate.parse(inputDate)
        val monthNames = listOf(
            "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
            "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
        )
        val day = parsedDate.day
        val month = monthNames[parsedDate.month.number - 1]
        val year = parsedDate.year

        return "$day $month $year"
    }

    fun getMonthAndYear(inputDate: String): Pair<Int, Int> {
        return try {
            val date = LocalDate.parse(inputDate)
            Pair(date.month.number, date.year)
        } catch (_: Exception) {
            Pair(0, 0)
        }
    }

    fun getDateRangeForWeek(week: Int, month: Int, year: Int): Pair<LocalDate, LocalDate> {
        val firstDay = LocalDate(year, month, 1)
        val nextMonth = firstDay.plus(DatePeriod(months = 1))
        val lastDayOfMonth = nextMonth.minus(DatePeriod(days = 1))
        val daysInMonth = lastDayOfMonth.day

        val startDay = ((week - 1) * 7 + 1).coerceIn(1, daysInMonth)
        val endDay = (startDay + 6).coerceAtMost(daysInMonth)

        return LocalDate(year, month, startDay) to LocalDate(year, month, endDay)
    }

    fun getCurrentWeekNumber(dayOfMonth: Int): Int {
        return when (dayOfMonth) {
            in 1..7 -> 1
            in 8..14 -> 2
            in 15..21 -> 3
            in 22..28 -> 4
            else -> 5
        }
    }

    fun getWeekOptions(month: Int, year: Int): List<Int> {
        val daysInMonth = daysInMonth(month, year)

        val weekOptions = mutableListOf(1, 2, 3, 4)
        if (daysInMonth > 28) weekOptions.add(5)

        return weekOptions
    }

    fun formatToWeekString(weekNumber: Int): String {
        return when (weekNumber) {
            1 -> "Minggu ke-1"
            2 -> "Minggu ke-2"
            3 -> "Minggu ke-3"
            4 -> "Minggu ke-4"
            5 -> "Minggu ke-5"
            else -> ""
        }
    }

    fun formatToBarChartDate(inputDate: String): String {
        val date = LocalDate.parse(inputDate)
        val day = date.day.toString().padStart(2, '0')
        val month = date.month.number.toString().padStart(2, '0')

        return "$day/$month"
    }

    @OptIn(ExperimentalTime::class)
    fun getFirstDayOfCurrentMonth(): String {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date

        return LocalDate(today.year, today.month, 1).toString()
    }

    @OptIn(ExperimentalTime::class)
    fun getLastDayOfCurrentMonth(): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        val nextMonth = if (today.month.number == 12) {
            LocalDate(today.year + 1, 1, 1)
        } else {
            LocalDate(today.year, today.month.number + 1, 1)
        }

        val lastDay = nextMonth.minus(1, DateTimeUnit.DAY)
        return lastDay.toString()
    }


    @OptIn(ExperimentalTime::class)
    fun getFirstDayOfCurrentWeek(): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val dayOfWeekIndex = today.dayOfWeek.ordinal
        val firstDay = today.minus(dayOfWeekIndex, DateTimeUnit.DAY)
        return firstDay.toString()
    }

    @OptIn(ExperimentalTime::class)
    fun getLastDayOfCurrentWeek(): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val dayOfWeekIndex = today.dayOfWeek.ordinal
        val lastDay = today.plus(6 - dayOfWeekIndex, DateTimeUnit.DAY)
        return lastDay.toString()
    }

    private fun Int.toMonthNameId(): String {
        return when (this) {
            1 -> "Januari"
            2 -> "Februari"
            3 -> "Maret"
            4 -> "April"
            5 -> "Mei"
            6 -> "Juni"
            7 -> "Juli"
            8 -> "Agustus"
            9 -> "September"
            10 -> "Oktober"
            11 -> "November"
            12 -> "Desember"
            else -> ""
        }
    }

    private fun daysInMonth(month: Int, year: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (year.isLeap()) 29 else 28
            else -> 30
        }
    }

    private fun Int.isLeap(): Boolean {
        return (this % 4 == 0 && this % 100 != 0) || (this % 400 == 0)
    }
}