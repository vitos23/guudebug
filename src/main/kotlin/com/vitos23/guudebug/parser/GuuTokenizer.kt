package com.vitos23.guudebug.parser

import kotlin.NoSuchElementException

internal open class GuuTokenizer(private val source: String) : Tokenizer {

    private var cachedToken: Token? = null
    private var pos = 0

    override fun hasNext(): Boolean {
        updateToken()
        return cachedToken != null
    }

    override fun nextToken(): Token {
        updateToken()
        if (cachedToken == null) {
            throw NoSuchElementException("There are no more tokens")
        }
        val token = cachedToken!!
        cachedToken = null
        return token
    }

    private fun updateToken() {
        if (cachedToken == null) {
            cachedToken = readNextToken()
        }
    }

    private fun readNextToken(): Token? {
        if (isEndOfLine()) {
            return Token(TokenType.EOL)
        }

        skipWhitespaces()

        if (pos > source.length) {
            return null
        }
        if (pos == source.length) {
            pos++
            return Token(TokenType.EOF)
        }
        if (isEndOfLine()) {
            return Token(TokenType.EOL)
        }

        val start = pos
        while (pos < source.length && !source[pos].isWhitespace()) {
            pos++
        }
        return Token(str = source.substring(start, pos))
    }

    private fun isEndOfLine(): Boolean {
        val cachedPos = pos
        if (pos < source.length && source[pos] == '\r') {
            pos++
            if (pos < source.length && source[pos] == '\n') {
                pos++
            }
            return true
        }
        if (pos < source.length && source[pos] == '\n') {
            pos++
            return true
        }
        pos = cachedPos
        return false
    }

    private fun skipWhitespaces() {
        while (pos < source.length && source[pos].isWhitespace() && !(source[pos] == '\r' || source[pos] == '\n')) {
            pos++
        }
    }
}