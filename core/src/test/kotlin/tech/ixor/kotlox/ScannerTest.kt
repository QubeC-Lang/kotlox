package tech.ixor.kotlox

import tech.ixor.kotlox.error.LoxError
import tech.ixor.kotlox.token.Token
import tech.ixor.kotlox.token.TokenType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ScannerTest {
    private fun scan(source: String, lox: Lox = Lox()): List<Token> = Scanner(source, lox).scanTokens()

    private fun types(source: String) = scan(source).map { it.type }

    @Test
    fun `scans keywords literals and operators`() {
        val tokens = scan("var answer = 40.5 + 1.5; print answer;")

        assertEquals(
            listOf(
                TokenType.VAR, TokenType.IDENTIFIER, TokenType.EQUAL, TokenType.NUMBER,
                TokenType.PLUS, TokenType.NUMBER, TokenType.SEMICOLON, TokenType.PRINT,
                TokenType.IDENTIFIER, TokenType.SEMICOLON, TokenType.EOF,
            ),
            tokens.map { it.type },
        )
        assertEquals(40.5, tokens[3].literal)
        assertEquals("answer", tokens[1].lexeme)
    }

    @Test
    fun `scans every single and two character token`() {
        assertEquals(
            listOf(
                TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN, TokenType.LEFT_BRACE, TokenType.RIGHT_BRACE,
                TokenType.COMMA, TokenType.DOT, TokenType.MINUS, TokenType.PLUS, TokenType.SEMICOLON,
                TokenType.SLASH, TokenType.STAR,
                TokenType.BANG, TokenType.BANG_EQUAL, TokenType.EQUAL, TokenType.EQUAL_EQUAL,
                TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL,
                TokenType.EOF,
            ),
            types("(){},.-+;/* ! != = == > >= < <="),
        )
    }

    @Test
    fun `scans every reserved word`() {
        assertEquals(
            listOf(
                TokenType.AND, TokenType.CLASS, TokenType.ELSE, TokenType.FALSE, TokenType.FOR,
                TokenType.FUN, TokenType.IF, TokenType.NIL, TokenType.OR, TokenType.PRINT,
                TokenType.RETURN, TokenType.SUPER, TokenType.THIS, TokenType.TRUE, TokenType.VAR,
                TokenType.WHILE, TokenType.IDENTIFIER, TokenType.EOF,
            ),
            types(
                """
                and class else false for fun if nil or print
                return super this true var while identifier_name
                """.trimIndent(),
            ),
        )
    }

    @Test
    fun `does not treat a trailing dot as part of a number`() {
        val tokens = scan("123.")

        assertEquals(listOf(TokenType.NUMBER, TokenType.DOT, TokenType.EOF), tokens.map { it.type })
        assertEquals(123.0, tokens[0].literal)
    }

    @Test
    fun `ignores comments and tracks lines`() {
        val tokens = scan("// ignored\nprint 1;\nprint 2;")

        assertEquals(listOf(2, 2, 2, 3, 3, 3, 3), tokens.map { it.line })
        assertEquals(TokenType.EOF, tokens.last().type)
    }

    @Test
    fun `scans multi-line strings without their quotes`() {
        val tokens = scan("\"first\nsecond\"")

        assertEquals(TokenType.STRING, tokens[0].type)
        assertEquals("first\nsecond", tokens[0].literal)
        assertEquals("\"first\nsecond\"", tokens[0].lexeme)
        assertEquals(2, tokens[0].line)
    }

    @Test
    fun `empty source produces only EOF`() {
        val tokens = scan("")

        assertEquals(1, tokens.size)
        assertEquals(TokenType.EOF, tokens.first().type)
        assertNull(tokens.first().literal)
    }

    @Test
    fun `valid source reports no errors`() {
        val lox = Lox()
        scan("print 1;", lox)

        assertFalse(lox.hadError)
    }

    @Test
    fun `reports unexpected character and keeps scanning`() {
        val lox = Lox()
        val tokens = scan("\n@ print", lox)

        assertTrue(lox.hadError)
        assertEquals(listOf(LoxError(2, "", "Unexpected character.")), lox.errors)
        assertEquals(listOf(TokenType.PRINT, TokenType.EOF), tokens.map { it.type })
    }

    @Test
    fun `reports every error in one pass`() {
        val lox = Lox()
        scan("@\n#", lox)

        assertEquals(listOf(1, 2), lox.errors.map { it.line })
    }

    @Test
    fun `reports unterminated string at its final line`() {
        val lox = Lox()
        val tokens = scan("\"first\nsecond", lox)

        assertEquals(listOf(LoxError(2, "", "Unterminated string.")), lox.errors)
        assertEquals(listOf(TokenType.EOF), tokens.map { it.type })
    }
}
