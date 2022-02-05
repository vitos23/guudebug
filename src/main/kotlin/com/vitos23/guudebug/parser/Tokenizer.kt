package com.vitos23.guudebug.parser

interface Tokenizer {

    fun hasNext(): Boolean

    fun nextToken(): Token

    fun getTokens(): List<Token> {
        val res = ArrayList<Token>()
        while (hasNext()) {
            res.add(nextToken())
        }
        return res
    }

}