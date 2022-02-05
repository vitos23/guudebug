package com.vitos23.guudebug.parser

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.fail

internal class GuuTokenizerTest {

    @Test
    fun test0() {
        val source = "  \r\n "
        val tokenizer = GuuTokenizer(source)
        assertEquals(Token(TokenType.EOL), tokenizer.nextToken())
        assertEquals(Token(TokenType.EOF), tokenizer.nextToken())
        assertFalse(tokenizer.hasNext())
    }

    @Test
    fun test1() {
        val source = "  sub  main\n set a 1\r set a 2  \r\n print a  \n\n  "
        val tokenizer = GuuTokenizer(source)

        assertTrue(tokenizer.hasNext())
        assertEquals(Token(str = "sub"), tokenizer.nextToken())
        assertEquals(Token(str = "main"), tokenizer.nextToken())
        assertEquals(Token(TokenType.EOL), tokenizer.nextToken())

        assertTrue(tokenizer.hasNext())
        assertEquals(Token(str = "set"), tokenizer.nextToken())
        assertEquals(Token(str = "a"), tokenizer.nextToken())
        assertEquals(Token(str = "1"), tokenizer.nextToken())
        assertEquals(Token(TokenType.EOL), tokenizer.nextToken())

        assertEquals(Token(str = "set"), tokenizer.nextToken())
        assertEquals(Token(str = "a"), tokenizer.nextToken())
        assertEquals(Token(str = "2"), tokenizer.nextToken())
        assertEquals(Token(TokenType.EOL), tokenizer.nextToken())

        assertEquals(Token(str = "print"), tokenizer.nextToken())
        assertEquals(Token(str = "a"), tokenizer.nextToken())
        assertEquals(Token(TokenType.EOL), tokenizer.nextToken())
        assertEquals(Token(TokenType.EOL), tokenizer.nextToken())

        assertTrue(tokenizer.hasNext())
        assertEquals(Token(TokenType.EOF), tokenizer.nextToken())
        assertFalse(tokenizer.hasNext())
        expectError(tokenizer)
        expectError(tokenizer)
        assertFalse(tokenizer.hasNext())

    }

    private fun expectError(tokenizer: GuuTokenizer) {
        try {
            tokenizer.nextToken()
            fail("Expected NoSuchElementException!")
        } catch (e: NoSuchElementException) {
            println("An expected error: ${e.message}")
        }
    }

    @Test
    fun test2() {
        val source = "  a b \n c  "
        val tokenizer = GuuTokenizer(source)

        assertTrue(tokenizer.hasNext())
        assertEquals(Token(str = "a"), tokenizer.nextToken())

        val tokens = tokenizer.getTokens()
        val expected = listOf(Token(str = "b"), Token(TokenType.EOL), Token(str = "c"), Token(TokenType.EOF))
        assertEquals(expected, tokens)

        assertFalse(tokenizer.hasNext())
    }

}