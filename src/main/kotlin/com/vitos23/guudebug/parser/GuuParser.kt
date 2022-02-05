package com.vitos23.guudebug.parser

import com.vitos23.guudebug.lang.Instruction
import com.vitos23.guudebug.lang.ProcSignature
import com.vitos23.guudebug.lang.Procedure
import com.vitos23.guudebug.lang.Statement
import com.vitos23.guudebug.lang.instructions.Set
import com.vitos23.guudebug.lang.instructions.Call
import com.vitos23.guudebug.lang.instructions.Print

class GuuParser(source: String) : BaseParser(GuuTokenizer(source)) {

    fun parse(): List<Statement> {
        val statements = ArrayList<Statement>()

        while (true) {
            val statement = parseNextStatement() ?: break
            statements.add(statement)
        }

        return assembleStatements(statements)
    }

    private fun assembleStatements(statements: List<Statement>): List<Statement> {
        val program = ArrayList<Statement>()

        var i = 0
        while (i < statements.size) {
            if (statements[i] is ProcSignature) {
                val procSignature = statements[i] as ProcSignature
                val procBody = ArrayList<Instruction>()
                while (i + 1 < statements.size && statements[i + 1] is Instruction) {
                    procBody.add(statements[++i] as Instruction)
                }

                /*// Check that the procedure has non-empty body
                if (procBody.isEmpty()) {
                    error("Procedure ${procSignature.name} doesn't have the body", procSignature.line)
                }*/

                program.add(Procedure(procSignature, procBody))
            } else {
                program.add(statements[i])
            }
            i++
        }

        // Check that program is not empty
        if (program.isEmpty()) {
            error("The source must be non-empty")
        }

        // Check that all instructions are in procedures
        if (program[0] !is Procedure) {
            error("Instruction ${program[0]} cannot be outside of a procedure", program[0].line)
        }

        checkProcNames(program as List<Procedure>)

        return program
    }

    private fun parseNextStatement(): Statement? {
        var token: Token
        do {
            val tk = getToken()
            token = tk ?: return null
        } while (token.type == TokenType.EOL || token.type == TokenType.EOF)

        return when (token.str) {
            "sub" -> sub()
            "print" -> print()
            "call" -> call()
            "set" -> set()
            else -> error("Unexpected token $token")
        }
    }

    private fun sub(): ProcSignature {
        val procName = getOrdinaryToken()
        expectEolOrEof()
        return ProcSignature(line, procName.str)
    }

    private fun print(): Print {
        val varName = getOrdinaryToken()
        expectEolOrEof()
        return Print(line, varName.str)
    }

    private fun call(): Call {
        val procName = getOrdinaryToken()
        expectEolOrEof()
        return Call(line, procName.str)
    }

    private fun set(): Set {
        val varName = getOrdinaryToken()
        val value = convertToInt(getOrdinaryToken().str)
        expectEolOrEof()
        return Set(line, varName.str, value)
    }

    private fun getOrdinaryToken(): Token {
        val token = getToken()
        if (token == null || token.type != TokenType.ORDINARY) {
            error("Expected token of type ORDINARY but found $token")
        }
        return token
    }

    private fun convertToInt(str: String): Int {
        try {
            return str.toInt()
        } catch (e: NumberFormatException) {
            error("Expected number but found $str")
        }
    }

    private fun checkProcNames(program: List<Procedure>) {
        val names = HashSet<String>()

        for (statement in program) {
            if (statement.name in names) {
                error("Procedure ${statement.name} was already declared", statement.line)
            } else {
                names.add(statement.name)
            }
        }
    }
}