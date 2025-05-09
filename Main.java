import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import syntaxtree.*;

public class Main {
    public static void main(String[] args) throws Exception
    {
        if(args.length < 1)
        {
            System.err.println("Usage: java Main <inputFile> <...>");
            System.exit(1);
        }

        FileInputStream fis = null;

        //Check every file that we have as input
        for(int i = 0; i < args.length; i++)
        {
            try{
                System.out.println("\nStatic Checking file: " + args[i]);
                fis = new FileInputStream(args[i]);

                MiniJavaParser parser = new MiniJavaParser(fis);

                //Now root is my abstract syntax tree
                Goal root = parser.Goal();

                // BUILD SYMBOL TABLE
                SymbolTableBuilder builder = new SymbolTableBuilder();
                root.accept(builder, null);

                // // SEMANTIC CHECKING
                // SemanticChecker checker = new SemanticChecker(symtab);
                // root.accept(checker, null); //recursively visits every node, letting you add logic at each visit (e.g. store variables, check types, print info).

                // System.out.println("Semantic checks passed.");


                System.err.println("Program parsed successfully.");

                Visitor eval = new Visitor();
                root.accept(eval, null);

            }
            catch(ParseException ex){
                System.out.println(ex.getMessage());
            }
            catch(FileNotFoundException ex){
                System.err.println(ex.getMessage());
            }
            finally{
                try{
                    if(fis != null) fis.close();
                }
                catch(IOException ex){
                    System.err.println(ex.getMessage());
                }
            }

        }
    }
}