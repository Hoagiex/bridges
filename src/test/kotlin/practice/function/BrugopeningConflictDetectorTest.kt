package practice.function

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import practice.dto.BrugopeningConflictDto
import practice.dto.BrugopeningDto
import java.time.LocalTime.of

class BrugopeningConflictDetectorTest {

    private val sut = BrugopeningConflictDetector()

    @Nested
    inner class Collision {

        @Nested
        inner class Opening {

            @Test
            fun `GIVEN - opening equals AND sluiting before - EXPECT - collision`() {
                test(
                    BrugopeningDto("opening", of(12, 20, 30), of(12, 20, 35)),
                    BrugopeningDto("opening", of(12, 20, 30), of(12, 21, 20))
                )
            }

            @Test
            fun `GIVEN - opening in between AND sluiting in between - EXPECT - collision`() {
                test(
                    BrugopeningDto("opening", of(12, 20, 40), of(12, 20, 45)),
                    BrugopeningDto("opening", of(12, 20, 41), of(12, 20, 42))
                )
            }

            @Test
            fun `GIVEN - opening in between AND sluiting equals - EXPECT - collision`() {
                test(
                    BrugopeningDto("opening", of(12, 20, 41), of(12, 20, 45)),
                    BrugopeningDto("opening", of(12, 20, 40), of(12, 20, 45))
                )
            }

            @Test
            fun `GIVEN - opening in between AND sluiting after - EXPECT - collision`() {
                test(
                    BrugopeningDto("opening", of(12, 20, 41), of(12, 20, 48)),
                    BrugopeningDto("opening", of(12, 20, 40), of(12, 20, 45))
                )
            }

            @Test
            fun `GIVEN - opening equals sluiting - EXPECT - collision`() {
                test(
                    BrugopeningDto("opening", of(12, 20, 20), of(12, 20, 22)),
                    BrugopeningDto("opening", of(12, 19, 10), of(12, 20, 20))
                )
            }
        }

        @Nested
        inner class Sluiting {

            @Test
            fun `GIVEN - opening before AND sluiting equals opening - EXPECT - collision`() {
                test(
                    BrugopeningDto("sluiting", of(11, 20, 30), of(11, 20, 35)),
                    BrugopeningDto("sluiting", of(11, 20, 35), of(11, 21, 15))
                )
            }

            @Test
            fun `GIVEN - opening before AND sluiting in between - EXPECT - collision`() {
                test(
                    BrugopeningDto("sluiting", of(11, 30, 30), of(11, 30, 38)),
                    BrugopeningDto("sluiting", of(11, 30, 36), of(11, 30, 40))
                )
            }

            @Test
            fun `GIVEN - opening before AND sluiting equals sluiting - EXPECT - collision`() {
                test(
                    BrugopeningDto("sluiting", of(11, 30, 20), of(11, 30, 54)),
                    BrugopeningDto("sluiting", of(11, 29, 10), of(11, 30, 54))
                )
            }

            @Test
            fun `GIVEN - opening before AND sluiting after sluiting - EXPECT - collision`() {
                test(
                    BrugopeningDto("sluiting", of(11, 29, 10), of(11, 30, 58)),
                    BrugopeningDto("sluiting", of(11, 30, 20), of(11, 30, 54))
                )
            }
        }

        private fun test(
            left: BrugopeningDto,
            right: BrugopeningDto,
        ) {
            val result = sut.execute(listOf(left, right))

            assertThat(result).isNotNull()
            assertThat(result).hasSize(1)

            result[0].also {
                assertThat(it.brugnaam).isEqualTo(it.eerste.brugnaam)
                assertThat(it.brugnaam).isEqualTo(it.tweede.brugnaam)
                assertThat(it.eerste).isEqualTo(left)
                assertThat(it.tweede).isEqualTo(right)
            }
        }

        @Nested
        inner class MultiCollision {

            @Test
            fun `GIVEN - triple overlap - EXPECT - collision`() {
                val input = listOf(
                    BrugopeningDto("triple", of(11, 29, 10), of(11, 30, 58)),
                    BrugopeningDto("triple", of(11, 30, 20), of(11, 30, 54)),
                    BrugopeningDto("triple", of(11, 30, 22), of(11, 30, 54))
                )

                val result = sut.execute(input)

                assertThat(result).isNotNull()
                assertThat(result).hasSize(3)

                assertThat(result).containsAll(
                    BrugopeningConflictDto("triple", input[0], input[1]),
                    BrugopeningConflictDto("triple", input[0], input[2]),
                    BrugopeningConflictDto("triple", input[1], input[2])
                )
            }

            @Test
            fun `GIVEN - quint overlap - EXPECT - collision`() {
                val input = listOf(
                    BrugopeningDto("quint", of(11, 29, 10), of(11, 30, 58)),
                    BrugopeningDto("quint", of(11, 30, 20), of(11, 30, 54)),
                    BrugopeningDto("quint", of(11, 30, 22), of(11, 30, 54)),
                    BrugopeningDto("quint", of(11, 30, 17), of(11, 30, 33)),
                    BrugopeningDto("quint", of(11, 29, 10), of(11, 31, 54))
                )

                val result = sut.execute(input)

                assertThat(result).isNotNull()
                assertThat(result).hasSize(10)

                assertThat(result).containsAll(
                    BrugopeningConflictDto("quint", input[0], input[1]),
                    BrugopeningConflictDto("quint", input[0], input[2]),
                    BrugopeningConflictDto("quint", input[0], input[3]),
                    BrugopeningConflictDto("quint", input[0], input[4]),
                    BrugopeningConflictDto("quint", input[1], input[2]),
                    BrugopeningConflictDto("quint", input[1], input[3]),
                    BrugopeningConflictDto("quint", input[1], input[4]),
                    BrugopeningConflictDto("quint", input[2], input[3]),
                    BrugopeningConflictDto("quint", input[2], input[4]),
                    BrugopeningConflictDto("quint", input[3], input[4])
                )
            }
        }
    }

    @Nested
    inner class NoCollision {

        @Test
        fun `GIVEN - different basename with overlapping time - EXPECT - no collisions`() {
            test(
                listOf(
                    BrugopeningDto("a", of(11, 20, 20), of(12, 20, 22)),
                    BrugopeningDto("b", of(11, 20, 20), of(12, 20, 21))
                )
            )
        }

        @Test
        fun `GIVEN - same basename but no overlapping time - EXPECT - no collisions`() {
            test(
                listOf(
                    BrugopeningDto("a", of(11, 20, 20), of(12, 20, 20)),
                    BrugopeningDto("a", of(10, 20, 20), of(11, 10, 10))
                )
            )
        }

        @Test
        fun `GIVEN - complex mixture - EXPECT - no collisions`() {
            test(
                listOf(
                    BrugopeningDto("a", of(10, 20, 20), of(10, 21, 20)),
                    BrugopeningDto("c", of(10, 20, 20), of(10, 21, 20)),
                    BrugopeningDto("b", of(10, 20, 20), of(10, 21, 20)),
                    BrugopeningDto("c", of(10, 23, 20), of(10, 26, 20)),
                    BrugopeningDto("a", of(10, 22, 20), of(10, 23, 30))
                )
            )
        }

        private fun test(
            input: List<BrugopeningDto>
        ) {
            val result = sut.execute(input)

            assertThat(result).isNotNull()
            assertThat(result).isEmpty()
        }
    }
}
