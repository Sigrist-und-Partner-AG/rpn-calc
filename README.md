# RPN Calculator
This is a Java project which provides a GUI-based RPN calculator.
__RPN__ is short for __Reverse Polish Notation__ (see the 
[Wikipedia entry](https://en.wikipedia.org/wiki/Reverse_Polish_notation)
for details). Some examples follow:

Infix | Postfix
----- | -------
2 + 3 | 2 3 +
7 - 9 | 7 9 -
4 - 6 * 2 | 4 6 2 * -
8 / 2 + 9 * 9 | 8 2 / 9 9 * +

## Operating principles
Because this calculator evaluates RPN expressions, things are a
bit different when contrasted with traditional infix usage.

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