package com.example.translatorapp.textforms

private const val lowerCaseCharacters = "\uD835\uDDEE\uD835\uDDEF\uD835\uDDF0\uD835\uDDF1\uD835" +
        "\uDDF2\uD835\uDDF3\uD835\uDDF4\uD835\uDDF5\uD835\uDDF6\uD835\uDDF7\uD835\uDDF8\uD835" +
        "\uDDF9\uD835\uDDFA\uD835\uDDFB\uD835\uDDFC\uD835\uDDFD\uD835\uDDFE\uD835\uDDFF\uD835" +
        "\uDE00\uD835\uDE01\uD835\uDE02\uD835\uDE03\uD835\uDE04\uD835\uDE05\uD835\uDE06\uD835\uDE07"

private const val upperCaseCharacters = "\uD835\uDDD4\uD835\uDDD5\uD835\uDDD6\uD835\uDDD7\uD835" +
        "\uDDD8\uD835\uDDD9\uD835\uDDDA\uD835\uDDDB\uD835\uDDDC\uD835\uDDDD\uD835\uDDDE\uD835" +
        "\uDDDF\uD835\uDDE0\uD835\uDDE1\uD835\uDDE2\uD835\uDDE3\uD835\uDDE4\uD835\uDDE5\uD835" +
        "\uDDE6\uD835\uDDE7\uD835\uDDE8\uD835\uDDE9\uD835\uDDEA\uD835\uDDEB\uD835\uDDEC\uD835\uDDED"

private const val numberCharacters = "\uD835\uDFEC\uD835\uDFED\uD835\uDFEE\uD835\uDFEF\uD835" +
        "\uDFF0\uD835\uDFF1\uD835\uDFF2\uD835\uDFF3\uD835\uDFF4\uD835\uDFF5"

private const val ascii_a = 'a'.code.toByte().toInt()
private const val ascii_A = 'A'.code.toByte().toInt()
private const val ascii_0 = '0'.code.toByte().toInt()
private const val encodingLength = "\uD835".length

private fun rangeOfEncodingInString(characterIndex:Int):IntRange{
    val start = characterIndex*encodingLength*2
    val end = start+encodingLength*2-1
    return IntRange(start = start, endInclusive = end)
}

fun getBoldCharacter(char: Char):String{
    val asciiVal = char.code.toByte().toInt()
    val charIndex:Int
    val res = if(char.isLetter()){
        if(char.isLowerCase()){
            charIndex = asciiVal - ascii_a
            lowerCaseCharacters.substring(rangeOfEncodingInString(charIndex))

        }else{
            charIndex = asciiVal - ascii_A
            upperCaseCharacters.substring(rangeOfEncodingInString(charIndex))
        }
    }else{
        charIndex = asciiVal - ascii_0
        numberCharacters.substring(rangeOfEncodingInString(charIndex))
    }
    return res
}

