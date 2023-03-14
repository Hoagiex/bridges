package practice.dto

data class BrugopeningConflictDto(
    val brugnaam: String,
    val eerste: BrugopeningDto,
    val tweede: BrugopeningDto
) {

    override fun toString(): String {
        return "BrugopeningConflictDto(brugnaam='$brugnaam', eerste=$eerste, tweede=$tweede)"
    }
}
