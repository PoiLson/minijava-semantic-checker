compile:
	java -jar ./CompileTools/jtb132di.jar -te minijava.jj
	java -jar ./CompileTools/javacc5.jar minijava-jtb.jj
# -d indicates to save the files inside of the src directory
	javac -d ./ ./src/Main.java

run:
# Run the Main class, specifying the classpath to the compiled classes directory
	java -cp ./ src.Main ./FileInputs/Test.java

clean:
	rm -rf *.class syntaxtree visitor minijava-jtb.jj *~
	find . -maxdepth 1 -name '*.java' -delete