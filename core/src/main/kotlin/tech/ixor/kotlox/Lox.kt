package tech.ixor.kotlox

import tech.ixor.kotlox.error.LoxError
import tech.ixor.kotlox.token.Token

/**
 * Entry point for running Lox programs in an embedding application.
 *
 * Errors are collected instead of printed, so the host decides how to display them.
 * An instance keeps its errors until [resetError] is called, which makes it suitable
 * for both running a whole file and a REPL session.
 */
public class Lox {
    private val _errors = mutableListOf<LoxError>()

    /** Errors reported since the last [resetError], in the order they were found. */
    public val errors: List<LoxError> get() = _errors

    /** Whether an error occurred since the last [resetError]. */
    public val hadError: Boolean get() = _errors.isNotEmpty()

    //> Error Handling
    private fun report(line: Int, where: String, message: String) {
        _errors.add(LoxError(line, where, message))
    }

    /**
     * Reports an error at [line] with the given [message].
     */
    internal fun error(line: Int, message: String) {
        report(line, "", message)
    }

    /**
     * Clears the reported errors, e.g. between REPL lines.
     */
    public fun resetError() {
        _errors.clear()
    }
    //< Error Handling

    /**
     * Runs the given Lox source code.
     *
     * Scanning continues past errors so that as many as possible are reported at once;
     * check [hadError] and [errors] afterwards.
     *
     * @param source the Lox source code to be run
     * @return the scanned tokens, including the terminating EOF token
     */
    public fun run(source: String): List<Token> = Scanner(source, this).scanTokens()
}
