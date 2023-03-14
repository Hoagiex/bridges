package practice.dto

import java.time.LocalTime

data class BrugopeningDto(
    val brugnaam: String,
    val openingsTijd: LocalTime,
    val sluitingsTijd: LocalTime
) {

    override fun toString(): String {
        return "BrugopeningDto(brugnaam='$brugnaam', openingsTijd=$openingsTijd, sluitingsTijd=$sluitingsTijd)"
    }
}
