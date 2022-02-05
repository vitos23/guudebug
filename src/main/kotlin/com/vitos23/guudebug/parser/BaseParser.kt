package com.vitos23.guudebug.parser

abstract class BaseParser(private val tokenizer: Tokenizer) {
    private var cachedToken: Token? = null

    protected var line = 1
        private set

    protected fun getToken(): Token? {
        updateToken()
        val token = cachedToken
        if (token != null && token.type == TokenType.EOL) {
            line++
        }
        cachedToken = null
        return token
    }

    private fun updateToken() {
        if (cachedToken == null) {
            cachedToken = readNextToken()
        }
    }

    private fun readNextToken(): Token? {
        if (!tokenizer.hasNext()) {
            return null
        }
        return tokenizer.nextToken()
    }

    protected fun expectEolOrEof() {
        updateToken()
        if (cachedToken?.type != TokenType.EOL && cachedToken?.type != TokenType.EOF) {
            error("Expected EOL or EOF token but found $cachedToken")
        }
    }

    protected fun error(message: String = "", line:Int = this.line) : Nothing {
        throw ParseException("An error occurred while parsing line $line: $message")
    }
}