import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import syntaxtree.*;

public class Main {
    public static void main(String[] args) throws RuntimeException
    {
        if(args.length < 1)
        {
            System.err.println("Usage: java Main <inputFile> <...>");
            System.exit(1);
        }

        FileInputStream fis = null;
        boolean semanticCheckPassed = true;

        //Check every file that we have as input
        for(int i = 0; i < args.length; i++)
        {
            try{
                System.out.println("###################################################################");
                System.out.println("Checking file: " + args[i]);
                fis = new FileInputStream(args[i]);

                MiniJavaParser parser = new MiniJavaParser(fis);
                semanticCheckPassed = true;

                //Now root is my abstract syntax tree
                Goal root = parser.Goal();

                // BUILD SYMBOL TABLE, first pass of the code!
                SymbolTableVisitor builder = new SymbolTableVisitor();
                try
                {
                    root.accept(builder, null);
                }
                catch (Exception e)
                {
                    semanticCheckPassed = false;
                    System.err.println("Exception thrown in the making of the symbol table: " + e.getMessage());
                }

                builder.getSymbolTable().printSymbolTable();


                // SEMANTIC CHECKING, second pass of the code!
                SemanticCheckerVisitor checker = new SemanticCheckerVisitor(builder.getSymbolTable());
                try
                {
                    root.accept(checker, null); //recursively visits every node, letting you add logic at each visit (e.g. store variables, check types, print info).
                }
                catch (Exception e)
                {
                    semanticCheckPassed = false;
                    System.err.println("Exception thrown in the semantic checking of the code: " + e.getMessage());
                }

                if(semanticCheckPassed)
                {
                    System.out.println("Semantic checks passed.");

                    // // Produce offset results
                    // TypeChecker.getTypeCheck().StartCalculation();

                    System.err.println("Program parsed successfully.");
                }
            }
            catch(ParseException ex)
            {
                System.err.println("Parse error in file: " + args[i]);
                System.out.println(ex.getMessage());
            }
            catch(FileNotFoundException ex)
            {
                System.err.println("File not found: " + args[i]);
                System.err.println(ex.getMessage());
            }
            finally
            {
                try
                {
                    if(fis != null) fis.close();
                }
                catch(IOException ex)
                {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
}