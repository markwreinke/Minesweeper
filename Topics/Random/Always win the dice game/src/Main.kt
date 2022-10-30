import kotlin.random.Random

fun createDiceGameRandomizer(n: Int): Int {
    var beat = false
    var currentSeed: Int = 0

    while (!beat) {
        val randGen = Random(currentSeed)
        var houseRoll: Int = 0
        var friendRoll: Int = 0

        repeat(n) {
            friendRoll += randGen.nextInt(1, 7)
        }
        repeat(n) {
            houseRoll += randGen.nextInt(1, 7)
        }
        if (houseRoll > friendRoll) {
            beat = true
        } else {
            currentSeed++
        }
    }

    return currentSeed
}
