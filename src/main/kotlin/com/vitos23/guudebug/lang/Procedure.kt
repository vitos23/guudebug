package com.vitos23.guudebug.lang

open class Procedure(line: Int, val name: String, val body: List<Instruction>) : Statement(line) {

    constructor(signature: ProcSignature, body: List<Instruction>) : this(signature.line, signature.name, body)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Procedure

        return line == other.line && name == other.name && body == other.body
    }

    override fun toString(): String {
        val res = StringBuilder("$line: sub $name\n")
        for (statement in body) {
            res.append("\t$statement\n")
        }
        return res.toString()
    }
}