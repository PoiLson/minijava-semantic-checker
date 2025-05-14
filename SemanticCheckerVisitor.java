import syntaxtree.*;
import visitor.*;

import java.util.ArrayList;
import java.util.Stack;

import SymbolTableBuilders.*;

class SemanticCheckerVisitor extends GJDepthFirst <String, String>
{
    Checker checker;

    public SemanticCheckerVisitor(SymbolTable symbolTable)
    {
        // Pass the symbol table to our semantic checker
        checker = new Checker(symbolTable);
    }

    // Now we can use our checker to check for semantic errors

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    @Override
    public String visit(MainClass n, String argu)
    {
        try
        {
            // Store the name of the class so the checker will check if it exists
            checker.setCurrentClass(n.f1.accept(this, argu));

            // Store the name of the function so the checker will check if it exists
            checker.setCurrentFunction("main");


            //sth for the args of the method
            n.f11.accept(this, argu);

            //sth for the vardeclaration
            n.f14.accept(this, argu);

            //sth for the statement
            n.f15.accept(this, argu);

            return "Check MainClass";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in MainClass: " + e.getMessage());
            return null;
        }
    }


}
