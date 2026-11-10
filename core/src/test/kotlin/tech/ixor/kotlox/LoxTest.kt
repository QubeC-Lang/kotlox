package tech.ixor.kotlox

import tech.ixor.kotlox.error.LoxError
import tech.ixor.kotlox.token.TokenType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoxTest {
    @Test
    fun `run scans the source`() {
        assertEquals(listOf(TokenType.PRINT, TokenType.NIL, TokenType.EOF), Lox().run("print nil").map { it.type })
    }

    @Test
    fun `reset error clears reported errors`() {
        val lox = Lox()
        lox.run("@")
        assertTrue(lox.hadError)

        lox.resetError()

        assertFalse(lox.hadError)
        assertEquals(emptyList(), lox.errors)
    }

    @Test
    fun `errors are formatted like the book`() {
        assertEquals("[line 3] Error: Unexpected character.", LoxError(3, "", "Unexpected character.").toString())
        assertEquals("[line 1] Error at end: Expect ';'.", LoxError(1, " at end", "Expect ';'.").toString())
    }
}
