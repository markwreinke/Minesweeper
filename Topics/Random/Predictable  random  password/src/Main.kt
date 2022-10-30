import kotlin.random.Random

fun generatePredictablePassword(seed: Int): String {
    var randomPassword = ""
    // write your code here
    val randomGen = Random(seed)
    for(i in 0..9) {
        randomPassword += randomGen.nextInt(33, 127).toChar()
    }
	return randomPassword
}
