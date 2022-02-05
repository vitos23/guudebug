package com.vitos23.guudebug.runtime

import com.vitos23.guudebug.lang.Procedure
import com.vitos23.guudebug.lang.Statement

open class GuuVM(
    prog: List<Statement>,
    val heapSizeLimit: Int = 4096,
    val callStackLimit: Int = 4096,
    private val ioInterface: IOInterface = StandardIO()
) : Environment, Executor {

    private val callStack = CallStack()
    private val vars = HashMap<String, Int>()
    private val procedures = HashMap<String, Procedure>()

    private var instrIndex = 0
    private lateinit var curProcedure: Procedure

    // Current version of Guu guarantees that program is a set of procedures
    private val program: List<Procedure> = prog as List<Procedure>

    init {
        init()
    }

    private fun init() {
        // Load procedures in the environment (vm)
        for (proc in program) {
            loadProcedure(proc)
        }

        // Find main procedure
        if ("main" !in procedures) {
            throw GuuRuntimeException("The program must contain procedure `main`")
        }
        curProcedure = procedures["main"]!!
    }

    override fun print(s: String) {
        ioInterface.print(s)
    }

    override fun println(s: String) {
        ioInterface.println(s)
    }

    override fun setVar(varName: String, value: Int) {
        vars[varName] = value
        if (vars.size > heapSizeLimit) {
            throw GuuRuntimeException("Heap size limit ($heapSizeLimit) exceeded")
        }
    }

    override fun getVar(varName: String): Int {
        return vars[varName] ?: throwError(getNextLine(), "Variable $varName is not declared")
    }

    override fun getVariables(): List<Variable> {
        val res = ArrayList<Variable>()
        vars.forEach { (name, value) -> res.add(Variable(name, value)) }
        return res
    }

    override fun runProcedure(procName: String) {
        if (procName !in procedures) {
            throwError(getNextLine(), "Procedure $procName doesn't exist")
        }

        val proc = procedures[procName]!!
        if (proc.body.isNotEmpty()) {
            callStack.push(CallFrame(instrIndex, curProcedure, curProcedure.body[instrIndex].line))
            instrIndex = 0
            curProcedure = proc
        }

        if (callStack.size() > callStackLimit) {
            throw GuuRuntimeException("Call stack limit ($callStackLimit) exceeded")
        }
    }

    override fun getCallStack(): CallStack {
        val res = callStack.copy()
        if (hasNext()) {
            res.push(CallFrame(instrIndex, curProcedure, getNextLine()))
        }
        return res
    }

    override fun hasNext(): Boolean = instrIndex < curProcedure.body.size

    override fun executeNext() {
        if (!hasNext()) {
            throw GuuRuntimeException("Execution is finished")
        }

        val callStackSize = callStack.size()
        curProcedure.body[instrIndex].execute(this)

        // No procedures were run
        if (callStackSize == callStack.size()) {
            instrIndex++
        }

        while (instrIndex == curProcedure.body.size && !callStack.isEmpty()) {
            instrIndex = callStack.peek().instructionIndex + 1
            curProcedure = callStack.pop().procedure
        }
    }

    override fun executeNextUnit() {
        val initialSize = callStack.size()
        do {
            executeNext()
        } while (callStack.size() > initialSize)
    }

    override fun getNextLine(): Int = curProcedure.body.getOrNull(instrIndex)?.line ?: -1

    override fun getEnv(): Environment = this

    private fun loadProcedure(proc: Procedure) {
        procedures[proc.name] = proc
    }

    private fun throwError(line: Int, message: String = ""): Nothing {
        throw GuuRuntimeException("An error occurred while executing line $line: $message")
    }
}