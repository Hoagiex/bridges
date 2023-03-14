package practice.function

import practice.dto.BrugopeningConflictDto
import practice.dto.BrugopeningDto
import practice.util.LocalTimes.isBetween

class BrugopeningConflictDetector {

    fun execute(
        openingen: List<BrugopeningDto>
    ): List<BrugopeningConflictDto> {
        return openingen
            .asSequence()
            .groupBy(BrugopeningDto::brugnaam)
            .map { it.value }
            .filter { it.size > 1 }
            .map(::pairOverlapElements)
            .flatten()
            .map(::toConflict)
            .toList()
    }

    private fun pairOverlapElements(
        brugGroup: List<BrugopeningDto>
    ) = brugGroup.mapIndexed { index, root ->
        brugGroup.slice(index + 1 until brugGroup.size)
            .map { Pair(root, it) }
            .filter { haveTimeOverlap(it.first, it.second) }
    }.flatten()

    private fun haveTimeOverlap(
        first: BrugopeningDto,
        second: BrugopeningDto,
    ) = first.openingsTijd.isBetween(second.openingsTijd, second.sluitingsTijd) ||
        first.sluitingsTijd.isBetween(second.openingsTijd, second.sluitingsTijd) ||
        second.openingsTijd.isBetween(first.openingsTijd, first.sluitingsTijd) ||
        second.sluitingsTijd.isBetween(first.openingsTijd, first.sluitingsTijd)


    private fun toConflict(
        conflict: Pair<BrugopeningDto, BrugopeningDto>
    ) = BrugopeningConflictDto(
        brugnaam = conflict.first.brugnaam,
        eerste = conflict.first,
        tweede = conflict.second
    )
}
