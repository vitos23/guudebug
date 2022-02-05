# GuuDebug

Simple debugger for pretty straightforward language Guu.
Guu doesn't really exist.

### About Guu

Guu program consists of a set of procedures.
Each procedure starts with `sub` (subname) and ends with the declaration of another procedure.
Execution of program begins with `sub main`.

The procedure body is a set of instructions each of which is on a separate line.
A line can start with insignificant tabulation and whitespace characters.
Blank lines are ignored. There are no comments in Guu.

There are only three operators:
- `set (varname) (new value)` - setting a new integer value of the variable.
- `call (subname)` - procedure call. Calls can be recursive.
- `print (varname)` - prints the variable value to the console.

Variables in Guu have a global scope.

An example. The program below prints `2`:
```
sub main
set a 1
call foo
print a

sub foo
set a 2
```

### Debugger

The task is to write a step-by-step interpreter for Guu.
When it starts, the debugger should stop at the line with the first instruction of `sub main` and wait for user commands.

The debugger command-line interface is gdb-like.
To debug (or execute) guu program use the following command: `guudebug (filename)`

Debugger commands:
- `i` - step into, the debugger goes inside `call (subname)`
- `o` - step over, the debugger doesn't go inside `call`
- `trace` - prints the execution stack trace with line numbers (beginning with main)
- `var` - prints the values of all declared variables
- `execute` - executes remaining code without debugging
- `help` - prints help information
- `list` - prints the program source code
- `quit`