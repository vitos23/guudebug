package com.vitos23.guudebug.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TokenTest {
    @Test
    fun testEquals() {
        val token1 = Token(TokenType.EOF, "1")
        val token2 = Token(TokenType.EOF, "2")
        assertEquals(token1, token2)

        val token3 = Token(TokenType.EOL, "1")
        val token4 = Token(TokenType.EOL, "2")
        assertEquals(token3, token4)

        assertNotEquals(token1, token3)

        val token5 = Token(str = "sub")
        val token6 = Token(str = "sub")
        val token7 = Token(str = "print")
        assertEquals(token5, token6)
        assertNotEquals(token6, token7)
    }
}