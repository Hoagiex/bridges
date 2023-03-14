package practice.util

import java.time.LocalTime

object LocalTimes {

    fun LocalTime.isBetween(
        start: LocalTime,
        end: LocalTime
    ) = !isBefore(start) && !isAfter(end)
}
