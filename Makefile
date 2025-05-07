#First of all we run the command:
javacc minijava.jj
#This produces 7 java files as output, including
#a lexer and a parser

# We'll look at three things you can do with JavaCC

# 1. Do a simple syntax check only
# 2. Make an actual interpreter
# 3. Generate code

# Run the command:
javac *.java
java MiniJavaParser "hello"

