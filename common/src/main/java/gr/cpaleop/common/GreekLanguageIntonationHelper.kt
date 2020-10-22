package gr.cpaleop.common

/**
 * Helper object to normalize intonation in Greek language
 */
object GreekLanguageIntonationHelper {

    private val untonnedMapChar = mapOf(
        Pair('ά', 'α'),
        Pair('έ', 'ε'),
        Pair('ή', 'η'),
        Pair('ί', 'ι'),
        Pair('ό', 'ο'),
        Pair('ύ', 'υ'),
        Pair('ώ', 'ω')
    )

    fun normalizeTonnes(greekChar: Char): Char = untonnedMapChar[greekChar] ?: greekChar
}