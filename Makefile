# Exercise for performing static analysis on MiniJava programs
all: clean compile run

compile:
	java -jar ./CompileTools/jtb132di.jar -te minijava.jj
	java -jar ./CompileTools/javacc5.jar minijava-jtb.jj
	javac Main.java


run:
# Run the Main class, specifying the classpath to the compiled classes directory
	clear
# java Main ./FileInputs/Tests.java
# Multiple Files
	java Main ./FileInputs/TestScopes.java ./FileInputs/Tests.java ./FileInputs/Test2.java 

clean:
	rm -rf *.class syntaxtree visitor minijava-jtb.jj *~
	find . -maxdepth 1 -name '*.java' ! -name 'Main.java' ! -name 'Visitor.java' ! -name 'SymbolTableVisitor.java' ! -name 'Checker.java' ! -name 'SemanticCheckerVisitor.java' -delete
	find . -maxdepth 2 -name '*.class' -delete
	clear
