package tech.ixor.kotlox.examples.astprinter

import tech.ixor.kotlox.AstPrinter
import tech.ixor.kotlox.ast.Expr
import tech.ixor.kotlox.token.Token
import tech.ixor.kotlox.token.TokenType

/**
 * Builds the book's sample tree for `-123 * (45.67)` by hand and prints it.
 */
@Suppress("MagicNumber") // The literals are the sample program itself.
fun main() {
    val expression = Expr.Binary(
        Expr.Unary(
            Token(TokenType.MINUS, "-", null, 1),
            Expr.Literal(123),
        ),
        Token(TokenType.STAR, "*", null, 1),
        Expr.Grouping(
            Expr.Literal(45.67),
        ),
    )

    println(AstPrinter().print(expression))
}
