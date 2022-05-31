# RPN Calculator

This is a Java project which provides a GUI-based RPN calculator
capable of processing 64-bit IEEE-754 floating point numbers.
**RPN** is short for **Reverse Polish Notation** (see the
[Wikipedia entry](https://en.wikipedia.org/wiki/Reverse_Polish_notation)
for details), also commonly referred to as **Postfix Notation**.
Some examples follow:

Infix | Postfix
----- | -------
2 + 3 | 2 3 +
7 - 9 | 7 9 -
4 - 6 * 2 | 4 6 2 * -
8 / 2 + 9 * 9 | 8 2 / 9 9 * +
3 * (9 - 4) | 3 9 4 - *

## Usage

Because this calculator evaluates RPN expressions, things are
a bit different when compared to traditional infix notation.

Due to the stack-based nature of RPN, the need for parentheses
disappears and operator precedence is nullified as expressions
are solely evaluated from left to right.
Another consequence is that calculation results may take on
more than one value: The entirety of the remaining stack is returned,
no matter how many operands it contains. Each operand is popped off
the stack and prepended to the others, so that the topmost stack value
will appear farthest to the right when output.
Thanks to this feature, incomplete calculations can executed,
allowing for complex expressions to be broken up into smaller pieces.
After evaluating a partial expression, one can resume exactly where
the calculation left off.

In order to evaluate the infix expression `1 + 2 + 3`, either of the
following methods is valid (`⏎` is the enter key):

```verbatim
a)  1 ⏎ 2 ⏎ 3 ⏎ + ⏎ + ⏎
b)  1 2 3 + + ⏎
```

Do note however that variant `b)` yields more accurate results
as no rounding occurs. This is the case when using `a)` due to
the intermediate steps, and the effects are extremely dependent
on the active output precision.

## Precision

All calculations are performed with the full precision a 64-bit double allows for.
The *output* precision however can be adjusted via the `<-` and `->` buttons,
with the result being rounded to the number of digits currently set.
If the stack yields multiple values following a calculation,
the same amount of rounding is applied to all of them.

## Operators

Besides supporting simple unary and binary operators,
the calculator can handle n-ary operators which take
all operands as input that are currently on the stack.
N-ary operators are variadic in nature and can be
applied to any number of arguments starting from 1.

Unary Operator | Example | Result | Description
:-------------:| ------- |:------:| -----------
neg  | 94 neg  | -94 | Negation
abs  | -7 abs  |   7 | Absolute value
pow2 | 8 pow2  |  64 | Raise to power of 2
sqrt | 16 sqrt |   4 | Square root

Binary Operator | Example | Result | Description
:--------------:| ------- |:------:| -----------
\+  | 2 3 +   |  5   | Addition
\-  | 1 4 -   | -3   | Subtraction
\*  | 2 5 \*  | 10   | Multiplication
/   | 3 2 /   |  1.5 | Division
%   | -21 4 % | -1   | Remainder
pow | 2 3 pow |  8   | Raise to power of
<=  | 17 a <= | 17   | Store in register\*

N-ary Operator | Example | Result | Description
:-------------:| ------- |:------:| -----------
sum | 1 2 3 sum   |  6   | Summation
avg | 4 5 avg     |  4.5 | Average
min | 9 -3 4 min  | -3   | Minimum
max | 5 10 2 max  | 10   | Maximum
cnt | 1 3 5 7 cnt |  4   | Count

\*The operand on the right-hand side of a store operation
must be a register since an assignable location is required.
In C or C++ terms, this is also known as an *lvalue*.
See the next section for details concerning registers.

## Registers

So-called *registers* are provided for the sake of saving and loading values,
which is useful for carrying over results from one calculation to the next.
General-purpose registers range from lowercase `a` to `z`, each of which is
initialized to `0.0` on program startup.
Building up on this concept, there exist further registers which are
predefined to specific numerical constants. These are listed in the table below.
Note that these registers are *not* read-only and may be overwritten by any
value of your choosing.

Register | Value
:-------:| -----
PI  | Math.PI
E   | Math.E
MIN | Double.MIN_VALUE
MAX | Double.MAX_VALUE
INF | Double.POSITIVE_INFINITY;
NAN | Double.NaN;

It is important to discern between *rvalues* and *lvalues* in this context.
As long as no store operation is involved, any given register is treated
exactly *as-if* it were a literal value. Its current value is retrieved
as soon as the register is parsed and used in the calculation from then on.
This is the reason why simply entering `a` and pressing enter will yield `0.0`
(or whatever the current register value is) instead of the token `a` itself.
In this sense, the register is treated as an *rvalue*.
By contrast, the following instance of `a` is understood as an *lvalue*:

Input | Output
----- | :----:
3 3 * a <= | 9

This is due to the application of the store operator `<=`, which presumes
a valid register to its immediate left. It stores the topmost value of
the stack in the register without popping the value off the stack first.
As such, the use of the `<=` does not break the calculation flow and may
appear in any position or any number of times, including chained application.

Input | Output
----- | :----:
1 a <= b <= c <= | 1
20 x <= 40 y <= + | 60
PI z <= sqrt | 1.772...
5 f <= f * | 25

## Pitfalls

The IEEE-754 standard allows for three sentinel values to appear under
certain conditions. Once introduced, each of these values will propagate
through the rest of the expression (although special cases do exist).
They can appear as the result of a calculation and may serve as inputs as well.
This includes the assignment of sentinels to registers.

Sentinel | Example | Description
:-------:| --------| ------------
NaN       | 0 0 /  | Short for *not a number*
Infinity  | 1 0 /  | Same as +Infinity
+Infinity | 1 0 /  | Positive infinity
-Infinity | -1 0 / | Negative infinity

Input | Output
----- | :----:
NaN 1 + | NaN
NaN Infinity * | NaN
Infinity Infinity / | NaN
Infinity neg  | -Infinity
-Infinity neg | Infinity

## Development Build

Execute the following commands for an immediate build:

```shell
cd rpn-calc
mvn compile
mvn exec:java -Dexec.mainClass=ch.bztf.Main
```

## Documentation

The documentation must be generated before it can be viewed:

```shell
cd rpn-calc
mvn javadoc:javadoc
```

This will place the HTML documentation in `rpn-calc/target/site/apidocs/`.
The main page can be accessed via `index.html`.

## Testing

Execute the following to run all unit tests:

```shell
cd rpn-calc
mvn test
```

## Bundling

Issue the following commands to build a so-called uber-`.jar`:

```shell
cd rpn-calc
mvn clean package
```

This will automatically execute all unit tests prior to bundling.
The shaded file can be found at `rpn-calc/target/rpn-calc-1.0-SNAPSHOT.jar`.
It is a non-portable executable that can only be executed on the same platform
that the project was compiled on. In order to run the application on other
platforms, the JavaFX SDK must be referenced. This can be achieved by entering (for example, on a Windows system):

```batch
java -p "path/to/javafx-sdk" --add-modules javafx.controls,javafx.fxml -jar rpn-calc-1.0-SNAPSHOT.jar
```

If supporting other platforms is not required, simply enter this command instead:

```shell
java -jar target/rpn-calc-1.0-SNAPSHOT.jar
```
