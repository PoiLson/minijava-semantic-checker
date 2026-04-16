# MiniJava Semantic Checker

## About

This repository contains coursework developed for the *Compilers* course at the Department of Informatics and Telecommunications, National and Kapodistrian University of Athens (NKUA).

The project implements a parser and semantic analyzer for **MiniJava**, a subset of Java designed for compiler construction exercises. The implementation performs static semantic checking on MiniJava programs and reports errors related to declarations, inheritance, typing, and method usage.

## Main Idea

The goal of this project is to perform semantic analysis for MiniJava programs after parsing.

The semantic checker validates key language constraints, including:

- class and inheritance correctness
- field and method declarations
- type consistency in expressions and assignments
- method overriding rules
- identifier resolution across scopes

In addition, the project computes and prints memory offsets for fields and methods of each class, providing structural information that is useful for later compilation stages.

## Usage
In order to run the semantic checker for MiniJava you can check out the [`Makefile`](Makefile) I have made.

To compile the project and run it on the included test files:

```make all```

To compile only:

``` make compile ```

To run manually on the provided test cases:

```make run```

You can replace the included test files with your own MiniJava programs as needed.
