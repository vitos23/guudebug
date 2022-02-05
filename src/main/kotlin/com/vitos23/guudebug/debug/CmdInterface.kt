package com.vitos23.guudebug.debug

import com.vitos23.guudebug.lang.Statement
import com.vitos23.guudebug.parser.GuuParser
import com.vitos23.guudebug.parser.ParseException
import com.vitos23.guudebug.runtime.GuuRuntimeException
import com.vitos23.guudebug.runtime.GuuVM
import java.io.File
import java.io.IOException

class CmdInterface(args: Array<String>) {

    private val source: String
    private val program: List<Statement>
    private val debugger: Debugger

    init {
        checkArgs(args)

        source = readFile(args[0])

        try {
            program = GuuParser(source).parse()
        } catch (e: ParseException) {
            println("The source code is incorrect: ${e.message}")
            throw e
        }

        try {
            val vm = GuuVM(program)
            debugger = GuuDebugger(vm)
        } catch (e: GuuRuntimeException) {
            println("An error occurred while initializing virtual machine: ${e.message}")
            throw e
        }
    }

    fun run() {
        printIntro()

        try {
            while (!debugger.finished()) {
                printPrefix()

                when (val cmd = getCmd()) {
                    "help" -> printHelp()
                    "list" -> printSources()
                    "i" -> debugger.stepInto()
                    "o" -> debugger.stepOver()
                    "trace" -> printStackTrace()
                    "var" -> printVars()
                    "execute" -> debugger.executeAll()
                    "quit" -> break
                    else -> wrongCommand(cmd)
                }
            }
        } catch (e: GuuRuntimeException) {
            println("An error occurred while executing program: ${e.message}")
            throw e
        }

        printOutro()
    }

    private fun printIntro() {
        println("Guu debugger has started. Type \"help\" to get additional information.")
    }

    private fun printOutro() {
        println("Execution is finished.")
    }

    private fun printPrefix() {
        print("(Line ${debugger.getNextLine()}) -> ")
    }

    private fun getCmd(): String = readln()

    private fun printHelp() {
        val text = """
            Available commands:
            "help" - prints help information
            "list" - prints the program source code
            "i" - step into
            "o" - step over
            "execute" - executes remaining code without debugging
            "trace" - prints the execution stack trace with line numbers
            "var" - prints the values of all declared variables
            "quit" - quits the debugger
        """.trimIndent()

        println(text)
    }

    private fun printSources() {
        val lines = source.split("\r\n", "\r", "\n")
        val lastIndex = lines.indexOfLast { it.isNotEmpty() }
        val lineNumLength = ceilLog10(lines.size)
        for (i in 0 until lastIndex + 1) {
            val line = (i + 1).toString().padEnd(lineNumLength)
            println("$line: ${lines[i]}")
        }
    }

    private fun printStackTrace() {
        val stack = debugger.getStackTrace().asList()
        val indexLength = ceilLog10(stack.size)
        for (i in stack.indices) {
            val index = (i + 1).toString().padEnd(indexLength)
            println("$index: Procedure \"${stack[i].procedure.name}\" at line ${stack[i].line}")
        }
    }

    private fun printVars() {
        val vars = debugger.getVariables()
        val indexLength = ceilLog10(vars.size)
        for (i in vars.indices) {
            val index = (i + 1).toString().padEnd(indexLength)
            println("$index: ${vars[i].name} = ${vars[i].value}")
        }
    }

    private fun wrongCommand(cmd: String) {
        println("Unknown command \"$cmd\"")
    }

    private fun ceilLog10(a: Int): Int {
        var res = 0
        var pow = 1
        while (pow < a) {
            res++
            pow *= 10
        }
        return res
    }

    private fun checkArgs(args: Array<String>) {
        if (args.size != 1) {
            val msg = "Expected exactly one argument - path to the guu program source code file."
            println(msg)
            throw CmdInterfaceException(msg)
        }
    }

    private fun readFile(path: String): String {
        try {
            return File(path).readText()
        } catch (e: IOException) {
            println("An error occurred while reading file $path: ${e.message}")
            throw CmdInterfaceException(e.message ?: "")
        }
    }
}