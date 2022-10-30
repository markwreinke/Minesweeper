fun main() {
    val size = readln().toInt()
    val mulList = mutableListOf<Int>()
    repeat(size) {
        mulList.add(readln().toInt())
    }

    val (p, m) = readln().split(" ").map(String::toInt)

    var pBool = false
    var mBool = false

    for(num in mulList) {
        if(num == p) {
            pBool = true
        }
        if(num == m) {
            mBool  = true
        }
    }



    if(pBool && mBool) {
        println("YES")
    } else {
        println("NO")
    }
}
