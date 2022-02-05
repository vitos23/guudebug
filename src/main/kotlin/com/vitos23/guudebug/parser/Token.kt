package com.vitos23.guudebug.parser

class Token(val type: TokenType = TokenType.ORDINARY, val str: String = "") {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        return type == other.type && (type == TokenType.EOL || type == TokenType.EOF || str == other.str)
    }

    override fun toString(): String = "Token \"$str\" of type $type"

}