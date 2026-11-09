package tech.ixor.kotlox

import kotlin.test.Test
import kotlin.test.assertEquals

class LoxTest {
    @Test
    fun `foo returns bar`() {
        assertEquals("bar", Lox().foo())
    }
}
