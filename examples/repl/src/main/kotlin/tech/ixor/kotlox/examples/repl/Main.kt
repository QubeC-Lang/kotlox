package tech.ixor.kotlox.examples.repl

import tech.ixor.kotlox.Lox
import java.io.File
import kotlin.system.exitProcess

private const val EX_USAGE = 64
private const val EX_DATAERR = 65

/**
 * Runs [source] with [lox], printing the scanned tokens and any errors.
 */
private fun run(lox: Lox, source: String) {
    lox.run(source).forEach(::println)
    lox.errors.forEach(System.err::println)
}

private fun runFile(path: String) {
    val lox = Lox()
    run(lox, File(path).readText())
    // Indicate an error in the exit code.
    if (lox.hadError) exitProcess(EX_DATAERR)
}

private fun runPrompt() {
    val lox = Lox()
    while (true) {
        print("> ")
        val line = readlnOrNull() ?: break
        run(lox, line)
        lox.resetError()
    }
}

fun main(args: Array<String>) {
    when {
        args.size > 1 -> {
            println("Usage: kotlox [script]")
            exitProcess(EX_USAGE)
        }
        args.size == 1 -> runFile(args[0])
        else -> runPrompt()
    }
}
