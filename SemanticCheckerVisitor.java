import syntaxtree.*;
import visitor.*;

import java.util.ArrayList;
import java.util.Stack;

import SymbolTableBuilders.*;


// Each visitor method returns the type as a String, like "int", "boolean", "int[]", etc.
class SemanticCheckerVisitor extends GJDepthFirst <String, String>
{
    Checker checker;

    // We need a stack to store the
    // Argument type lists for nested method calls
    Stack <ArrayList <String> > methodCalls;

    // Constructor
    public SemanticCheckerVisitor(SymbolTable symbolTable)
    {
        // Pass the symbol table to our semantic checker
        checker = new Checker(symbolTable);
    }

    // Override one of the visitors functions
    // Not the user ones
    public String visit(NodeToken n, String argu)
    {
        return n.toString();
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

    /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
    @Override
    public String visit(AssignmentStatement n, String argu)
    {
        try
        {
            String identifier = n.f0.accept(this,null);
            String expression = n.f2.accept(this,null);

            checker.checkAssignmentStatement(identifier, expression);
            return "Check AssignmentStatement";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in AssignmentStatement: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> AndExpression()
    *       | CompareExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | Clause()
    */
    @Override
    public String visit(Expression n, String argu) 
    {
        try
        {
            String expression = n.f0.accept(this,null);

            return checker.checkExpression(expression);
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in Expression: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */
    @Override
    public String visit(AndExpression n, String argu)
    {
        try
        {
            String firstClause = n.f0.accept(this,null);
            String secondClause = n.f2.accept(this,null);

            checker.checkAndExpression(firstClause, secondClause);

            // Return the clause's types
            return "boolean";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in AndExpression: " + e.getMessage());
            return null;
        }   
    }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    @Override
    public String visit(CompareExpression n, String argu)
    {
        try
        {
            String firstPrimaryExpr = n.f0.accept(this,null);
            String secondPrimaryExpr = n.f2.accept(this,null);

            checker.checkPrimaryExpression(firstPrimaryExpr, secondPrimaryExpr);
            
            return "boolean";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in CompareExpression: " + e.getMessage());
            return null;
        }  
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    @Override
    public String visit(PlusExpression n, String argu)
    {
        try
        {
            String firstPrimaryExpr = n.f0.accept(this,null);
            String secondPrimaryExpr = n.f2.accept(this,null);

            checker.checkPrimaryExpression(firstPrimaryExpr, secondPrimaryExpr);
            
            return "int";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in PlusExpression: " + e.getMessage());
            return null;
        }  
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    @Override
    public String visit(MinusExpression n, String argu)
    {
        try
        {
            String firstPrimaryExpr = n.f0.accept(this,null);
            String secondPrimaryExpr = n.f2.accept(this,null);

            checker.checkPrimaryExpression(firstPrimaryExpr, secondPrimaryExpr);
            
            return "int";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in MinusExpression: " + e.getMessage());
            return null;
        }  
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
    @Override
    public String visit(TimesExpression n, String argu)
    {
        try
        {
            String firstPrimaryExpr = n.f0.accept(this,null);
            String secondPrimaryExpr = n.f2.accept(this,null);

            checker.checkPrimaryExpression(firstPrimaryExpr, secondPrimaryExpr);
            
            return "int";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in TimesExpression: " + e.getMessage());
            return null;
        }  
    }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    @Override
    public String visit(ArrayLookup n, String argu)
    {
        try
        {
            String arrayName = n.f0.accept(this,null);
            String arraySize = n.f2.accept(this,null);

            return checker.checkArrayLookup(arrayName, arraySize);
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in ArrayLookup: " + e.getMessage());
            return null;
        }
    }
  
   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    @Override
    public String visit(ArrayLength n, String argu)
    {
        try
        {
            String arrayName = n.f0.accept(this,null);

            return checker.checkArrayLength(arrayName);
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in ArrayLength: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
    @Override
    public String visit(MessageSend n, String argu)
    {
        try
        {
            // Collect the types of the arguments of this method call
            methodCalls.push(new ArrayList<String>());

            String caller = n.f0.accept(this,null);
            String method = n.f2.accept(this,null);

            // Visit EpxressionList
            n.f4.accept(this,null);

            String methodType = checker.checkMessageSend(caller, method, methodCalls.pop());

            return methodType;
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in MessageSend: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> "!"
    * f1 -> Clause()
    */
    @Override
    public String visit(NotExpression n, String argu)
    {
        try
        {
            String clause = n.f1.accept(this,null);
            checker.checkNotOperation(clause);
            return "boolean";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in NotExpression: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> BooleanArrayAllocationExpression()
    *       | IntegerArrayAllocationExpression()
    */
    @Override
    public String visit(ArrayAllocationExpression n, String argu)
    {
        try
        {
            String expr = n.f0.accept(this,null);

            return checker.checkArrayAllocationExpression(expr);
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in ArrayAllocationExpression: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> "new"
    * f1 -> "boolean"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    @Override
    public String visit(BooleanArrayAllocationExpression n, String argu)
    {
        try
        {
            String expressionType = n.f3.accept(this, null);

            if (!expressionType.equals("boolean"))
            {
                throw new RuntimeException("Invalid size in 'new boolean[]'. Size must be of type boolean, but got: " + expressionType);
            }

            return "boolean[]";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in BooleanArrayAllocationExpression: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    @Override
    public String visit(IntegerArrayAllocationExpression n, String argu)
    {
        try
        {
            String expressionType = n.f3.accept(this, null);

            if (!expressionType.equals("int"))
            {
                throw new RuntimeException("Invalid size in 'new int[]'. Size must be of type int, but got: " + expressionType);
            }

            return "int[]";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in IntegerArrayAllocationExpression: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
    @Override
    public String visit(AllocationExpression n, String argu)
    {
        try
        {
            String className = n.f1.accept(this,null);

            checker.checkClass(className);
            
            // add special characters, so we can now that a primary expression if an allocation expression
            return "/" + className;
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in AllocationExpression: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    @Override
    public String visit(BracketExpression n, String argu)
    {
        try
        {
            // Visits expression
            String expression = n.f1.accept(this,null);

            return expression;

            // So no need for that after all!
            // return checker.checkExpression(expression);
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in BracketExpression: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
    @Override
    public String visit(ArrayAssignmentStatement n, String argu)
    {
        try
        {
            String arrType = n.f0.accept(this,null);
            String typeOfIndex = n.f2.accept(this,null);
            String typeOfExpr = n.f5.accept(this,null);
            
            checker.checkArrayAssignmentStatement(arrType, typeOfIndex, typeOfExpr);
            return "Check ArrayAssignmentStatement";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in ArrayAssignmentStatement: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
    @Override
    public String visit(IfStatement n, String argu)
    {
        try
        {
            // Visit expression and get its type back
            String expressionType = n.f2.accept(this,null);

            // Check if it is boolean
            checker.checkConditionStatement(expressionType);

            // Visit statement of if
            n.f4.accept(this,null);

            // Visit statement of else
            n.f6.accept(this,null);

            return "Check IfStatement";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in IfStatement: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
    @Override
    public String visit(WhileStatement n, String argu)
    {
        try
        {
            // Visit expression and get its type back
            String expressionType = n.f2.accept(this,null);

            // Check if it is boolean
            checker.checkConditionStatement(expressionType);

            // Visit statement of while
            n.f4.accept(this,null);
            
            return "Check WhileStatement";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in WhileStatement: " + e.getMessage());
            return null;
        }
    }
  
    /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
    @Override
    public String visit(PrintStatement n, String argu)
    {
        try
        {
            // Visit expression and get its type back
            String expressionType = n.f2.accept(this,null);

            // Check if it is boolean or int
            checker.checkPrintStatement(expressionType);
            
            return "Check PrintStatement";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in PrintStatement: " + e.getMessage());
            return null;
        }
    }
  
    /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
    @Override
    public String visit(ExpressionList n, String argu)
    {
        try
        {
            String firstParameter = n.f0.accept(this,argu);
            methodCalls.peek().add(firstParameter);

            // Visit ExpressionTail
            n.f1.accept(this,null);

            return "Check ExpressionList";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in ExpressionList: " + e.getMessage());
            return null;
        }
    }
  
    /**
    * f0 -> ","
    * f1 -> Expression()
    */
    @Override
    public String visit(ExpressionTerm n, String argu)
    {
        try
        {
            String parameter = n.f1.accept(this,argu);
            methodCalls.peek().add(parameter);

            return "Check ExpressionTerm";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in ExpressionTerm: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
    @Override
    public String visit(ClassDeclaration n, String argu)
    {
        try
        {
            String className = n.f1.accept(this,null);
            checker.setCurrentClass(className);
            checker.setCurrentMethod("");

            // Visit VarDeclaration
            n.f3.accept(this,null);

            // Visit MethodDeclaration
            n.f4.accept(this,null);

            return "Check ClassDeclaration";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in ClassDeclaration: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
    @Override
    public String visit(MethodDeclaration n, String argu)
    {
        try
        {
            String methodName = n.f2.accept(this,null);
            checker.setCurrentMethod(methodName);

            // Visit Type
            n.f1.accept(this,null);

            // Visit FormalParameterList
            n.f4.accept(this,null);

            // Visit VarDeclaration
            n.f7.accept(this,null);

            // Visit Statement
            n.f8.accept(this,null);

            // Visit Return Expression
            String returnType = n.f10.accept(this,null);
            checker.checkReturnType(returnType);

            return "Check MethodDeclaration";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in MethodDeclaration: " + e.getMessage());
            return null;
        }
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
    @Override
    public String visit(ClassExtendsDeclaration n, String argu)
    {
        try
        {
            String className = n.f1.accept(this,null);
            checker.setCurrentClass(className);
            checker.setCurrentMethod("");

            // Visit VarDeclaration
            n.f5.accept(this,null);

            // Visit MethodDeclaration
            n.f6.accept(this,null);

            return "Check ClassExtendsDeclaration";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in ClassExtendsDeclaration: " + e.getMessage());
            return null;
        }
    }




    






    
    // I will not override the Type and the ArrayType as well -> eventualy I need the Type
    // Eventually, we need to override the Type so that we will be able to recognise a semantic error

    /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
    @Override
    public String visit(Type n, String argu)
    {
        try
        {
            String type = n.f0.accept(this,null);
            System.out.println(type);
            checker.checkType(type);
            return "Check Type";
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in Type: " + e.getMessage());
            return null;
        }
    }

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
    @Override
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
    @Override
    public String visit(IntegerType n, String argu)
    {
        try
        {
            String type = n.f0.accept(this,argu);
            System.out.println(type);
            checker.checkIntegerType(type);
            return type;
        }
        catch (Exception e)
        {
            System.err.println("Exception thrown in IntegerType: " + e.getMessage());
            return null;
        }

        // return n.f0.toString();
    }

    // AT THE END RE-OVERRIDE THE FUNCTIONS THAT GIVE TERMINAL SYMBOLS!!
    // AND THOSE THAT WERE OVERRIDEN DURING THE SYMBOL TABLE

    @Override
    public String visit(Identifier n, String argu)
    {
        return n.f0.toString();
    }

    // AND THE NEW TERMINAL SYMBOLS APPEARING!!!

    /**
    * f0 -> <INTEGER_LITERAL>
    */
    @Override
    public String visit(IntegerLiteral n, String argu)
    {
        // Can it be anything else?
        return "int";
    }

    /**
    * f0 -> "true"
    */
    @Override
    public String visit(TrueLiteral n, String argu)
    {
        return "true";
    }

    /**
    * f0 -> "false"
    */
    @Override
    public String visit(FalseLiteral n, String argu)
    {
        return "false";
    }

    /**
    * f0 -> "this"
    */
    @Override
    public String visit(ThisExpression n, String argu)
    {
        return "this";
    }
}