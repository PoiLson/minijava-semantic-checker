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

            // Store the name of the method so the checker will check if it exists
            checker.setCurrentMethod("main");

            // Visit the VarDeclaration -> no need to override her!!
            n.f14.accept(this, argu);

            // Visit the Statement -> no need to override her!!
            n.f15.accept(this, argu);

            return "Check MainClass";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in MainClass: " + e.getMessage());
            return null;
        }
    }

    // I will not override the Type and the ArrayType as well

    /**
    * f0 -> "boolean"
    * f1 -> "["
    * f2 -> "]"
    */
    @Override
    public String visit(BooleanArrayType n, String argu)
    {
        try
        {
            String type = n.f0.accept(this,null);
            checker.checkBooleanArrayType(type);
            return "Check BooleanArrayType";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in BooleanArrayType: " + e.getMessage());
            return null;
        }
    }
  
     /**
      * f0 -> "int"
      * f1 -> "["
      * f2 -> "]"
      */
    @Override
    public String visit(IntegerArrayType n, String argu) 
    {
        try
        {
            String type = n.f0.accept(this,null);
            checker.checkIntegerArrayType(type);
            return "Check IntegerArrayType";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in IntegerArrayType: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> "boolean"
    */
    public String visit(BooleanType n, String argu)
    {
        try
        {
            String type = n.f0.accept(this,null);
            checker.checkBooleanType(type);
            return "Check BooleanType";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in BooleanType: " + e.getMessage());
            return null;
        }
    }

    /**
     * f0 -> "int"
    */
    public String visit(IntegerType n, String argu)
    {
        try
        {
            String type = n.f0.accept(this,null);
            checker.checkIntegerType(type);
            return "Check IntegerType";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in IntegerType: " + e.getMessage());
            return null;
        }
    }



}
