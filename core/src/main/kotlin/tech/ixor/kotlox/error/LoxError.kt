package tech.ixor.kotlox.error

/**
 * A compile-time error found while processing Lox source.
 *
 * @property line one-based source line on which the error occurred
 * @property where location detail inside the line, e.g. ` at end`; empty when there is none
 * @property message description of the error
 */
public data class LoxError(
    val line: Int,
    val where: String,
    val message: String,
) {
    /**
     * Formats the error the way the book reports it: `[line 1] Error: Unexpected character.`
     */
    override fun toString(): String = "[line $line] Error$where: $message"
}
