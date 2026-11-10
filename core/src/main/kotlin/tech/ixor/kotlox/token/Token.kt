package tech.ixor.kotlox.token

/**
 * One lexical unit of Lox source.
 *
 * Tokens retain the original lexeme and one-based line number. Literal tokens also
 * carry their decoded runtime value: a [Double] for numbers, a [String] for strings,
 * and `null` for tokens without a literal value.
 *
 * @property type syntactic category recognized by the scanner
 * @property lexeme exact source text occupied by this token
 * @property literal parsed literal value, or `null` when the token has none
 * @property line one-based source line on which the token ends
 */
public data class Token(
    val type: TokenType,
    val lexeme: String,
    val literal: Any?,
    val line: Int,
) {
    /**
     * Formats the token as its type, lexeme, and literal value for diagnostics.
     */
    override fun toString(): String = "$type $lexeme $literal"
}
