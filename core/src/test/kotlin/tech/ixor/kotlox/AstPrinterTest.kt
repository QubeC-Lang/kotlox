package tech.ixor.kotlox

import tech.ixor.kotlox.ast.Expr
import tech.ixor.kotlox.token.Token
import tech.ixor.kotlox.token.TokenType
import kotlin.test.Test
import kotlin.test.assertEquals

class AstPrinterTest {
    @Test
    fun `prints the book's example expression`() {
        // -123 * (45.67)
        val expression = Expr.Binary(
            Expr.Unary(
                Token(TokenType.MINUS, "-", null, 1),
                Expr.Literal(123),
            ),
            Token(TokenType.STAR, "*", null, 1),
            Expr.Grouping(Expr.Literal(45.67)),
        )

        assertEquals("(* (- 123) (group 45.67))", AstPrinter().print(expression))
    }

    @Test
    fun `prints nil strings and booleans`() {
        val printer = AstPrinter()

        assertEquals("nil", printer.print(Expr.Literal(null)))
        assertEquals("hello", printer.print(Expr.Literal("hello")))
        assertEquals("true", printer.print(Expr.Literal(true)))
    }
}
