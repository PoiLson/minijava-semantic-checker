import syntaxtree.*;
import visitor.*;

import java.util.ArrayList;
import java.util.Stack;

import SymbolTableBuilders.*;


// Each visitor method returns the type as a String, like "int", "boolean", "int[]", etc.
// Your visitors should return the type of the expression, not the literal string itself.
class SemanticCheckerVisitor extends GJDepthFirst <String, String>
{
    Checker checker;

    // We need a stack to store the
    // Argument type lists for nested method calls
    Stack <ArrayList<String>> methodCalls;

    // Constructor
    public SemanticCheckerVisitor(SymbolTable symbolTable)
    {
        // Pass the symbol table to our semantic checker
        checker = new Checker(symbolTable);
        methodCalls = new Stack <ArrayList<String>>() ;
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
    public String visit(MainClass n, String argu) throws Exception
    {
        System.out.println("We are in MainClass Declaration");
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

    /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
    @Override
    public String visit(AssignmentStatement n, String argu) throws Exception
    {
        System.out.println("We are in AssignmentStatement Declaration");

        String identifier = n.f0.accept(this,null);
        String expression = n.f2.accept(this,null);

        checker.checkAssignmentStatement(identifier, expression);
        return "Check AssignmentStatement";
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
    public String visit(Expression n, String argu) throws Exception
    {
        System.out.println("We are in Expression Declaration");

        String expression = n.f0.accept(this,null);

        String result = checker.checkExpression(expression);

        return result;
    }

    /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */
    @Override
    public String visit(AndExpression n, String argu) throws Exception
    {
        System.out.println("We are in AndExpression Declaration");
        
        String firstClause = n.f0.accept(this,null);
        String secondClause = n.f2.accept(this,null);

        checker.checkAndExpression(firstClause, secondClause);

        // Return the clause's types
        return "boolean";
    }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    @Override
    public String visit(CompareExpression n, String argu) throws Exception
    {
        System.out.println("We are in CompareExpression Declaration");
        
        String firstPrimaryExpr = n.f0.accept(this,null);
        String secondPrimaryExpr = n.f2.accept(this,null);

        checker.checkPrimaryExpression(firstPrimaryExpr, secondPrimaryExpr);
        
        return "boolean";
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    @Override
    public String visit(PlusExpression n, String argu) throws Exception
    {
        System.out.println("We are in PlusExpression Declaration");
    
        String firstPrimaryExpr = n.f0.accept(this,null);
        String secondPrimaryExpr = n.f2.accept(this,null);

        checker.checkPrimaryExpression(firstPrimaryExpr, secondPrimaryExpr);
        
        return "int";
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    @Override
    public String visit(MinusExpression n, String argu) throws Exception
    {
        System.out.println("We are in MinusExpression Declaration");

        String firstPrimaryExpr = n.f0.accept(this,null);
        String secondPrimaryExpr = n.f2.accept(this,null);

        checker.checkPrimaryExpression(firstPrimaryExpr, secondPrimaryExpr);
        
        return "int";
    }

    /**
     * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
    @Override
    public String visit(TimesExpression n, String argu) throws Exception
    {
        System.out.println("We are in TimesExpression Declaration");
        
        String firstPrimaryExpr = n.f0.accept(this,null);
        String secondPrimaryExpr = n.f2.accept(this,null);

        checker.checkPrimaryExpression(firstPrimaryExpr, secondPrimaryExpr);
        
        return "int";
    }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    @Override
    public String visit(ArrayLookup n, String argu) throws Exception
    {
        System.out.println("We are in ArrayLookup Declaration");
        
        String arrayName = n.f0.accept(this,null);
        String arraySize = n.f2.accept(this,null);

        return checker.checkArrayLookup(arrayName, arraySize);
    }
  
   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    @Override
    public String visit(ArrayLength n, String argu) throws Exception
    {
        System.out.println("We are in ArrayLength Declaration");
        
        String arrayName = n.f0.accept(this,null);

        return checker.checkArrayLength(arrayName);
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
    public String visit(MessageSend n, String argu) throws Exception
    {
        System.out.println("We are in MessageSend Declaration");
        
        // Collect the types of the arguments of this method call
        methodCalls.push(new ArrayList<String>());

        String caller = n.f0.accept(this,null);
        String method = n.f2.accept(this,null);

        // Visit EpxressionList
        n.f4.accept(this,null);

        String methodType = checker.checkMessageSend(caller, method, methodCalls.pop());
        return methodType;
    }

    /**
    * f0 -> "!"
    * f1 -> Clause()
    */
    @Override
    public String visit(NotExpression n, String argu) throws Exception
    {
        System.out.println("We are in NotExpression Declaration");
        
        String clause = n.f1.accept(this,null);
        checker.checkNotOperation(clause);
        return "boolean";
    }

    /**
    * f0 -> BooleanArrayAllocationExpression()
    *       | IntegerArrayAllocationExpression()
    */
    @Override
    public String visit(ArrayAllocationExpression n, String argu) throws Exception
    {
        System.out.println("We are in ArrayAllocationExpression Declaration");
        
        String expr = n.f0.accept(this,null);

        return checker.checkArrayAllocationExpression(expr);
    }

    /**
    * f0 -> "new"
    * f1 -> "boolean"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    @Override
    public String visit(BooleanArrayAllocationExpression n, String argu) throws Exception
    {
        System.out.println("We are in BooleanArrayAllocationExpression Declaration");
        
        String expressionType = n.f3.accept(this, null);

        if (!expressionType.equals("boolean"))
        {
            throw new RuntimeException("Invalid size in 'new boolean[]'. Size must be of type boolean, but got: " + expressionType);
        }

        return "boolean[]";
    }

    /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    @Override
    public String visit(IntegerArrayAllocationExpression n, String argu) throws Exception
    {
        System.out.println("We are in IntegerArrayAllocationExpression Declaration");
        
        String expressionType = n.f3.accept(this, null);

        if (!expressionType.equals("int"))
        {
            throw new RuntimeException("Invalid size in 'new int[]'. Size must be of type int, but got: " + expressionType);
        }

        return "int[]";
    }

    /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
    @Override
    public String visit(AllocationExpression n, String argu) throws Exception
    {
        System.out.println("We are in AllocationExpression Declaration");
        
        String className = n.f1.accept(this,null);

        checker.checkClass(className);
        
        // add special characters, so we can now that a primary expression if an allocation expression
        return "/" + className;
    }

    /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    @Override
    public String visit(BracketExpression n, String argu) throws Exception
    {
        System.out.println("We are in BracketExpression Declaration");
        
        // Visits expression
        String expression = n.f1.accept(this,null);

        return expression;

        // So no need for that after all!
        // return checker.checkExpression(expression);
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
    public String visit(ArrayAssignmentStatement n, String argu) throws Exception
    {
        System.out.println("We are in ArrayAssignmentStatement Declaration");
        
        String arrType = n.f0.accept(this,null);
        String typeOfIndex = n.f2.accept(this,null);
        String typeOfExpr = n.f5.accept(this,null);
        
        checker.checkArrayAssignmentStatement(arrType, typeOfIndex, typeOfExpr);
        return "Check ArrayAssignmentStatement";
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
    public String visit(IfStatement n, String argu) throws Exception
    {
        System.out.println("We are in IfStatement Declaration");
        
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

    /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
    @Override
    public String visit(WhileStatement n, String argu) throws Exception
    {
        System.out.println("We are in WhileStatement Declaration");
        
        // Visit expression and get its type back
        String expressionType = n.f2.accept(this,null);

        // Check if it is boolean
        checker.checkConditionStatement(expressionType);

        // Visit statement of while
        n.f4.accept(this,null);
        
        return "Check WhileStatement";
    }
  
    /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
    @Override
    public String visit(PrintStatement n, String argu) throws Exception
    {
        System.out.println("We are in PrintStatement Declaration");
        
        // Visit expression and get its type back
        String expressionType = n.f2.accept(this,null);

        // Check if it is boolean or int
        checker.checkPrintStatement(expressionType);
        
        return "Check PrintStatement";
    }
  
    /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
    @Override
    public String visit(ExpressionList n, String argu) throws Exception
    {
        System.out.println("We are in ExpressionList Declaration");
        
        String firstParameter = n.f0.accept(this,argu);
        methodCalls.peek().add(firstParameter);

        // Visit ExpressionTail
        n.f1.accept(this,null);

        return "Check ExpressionList";
    }
  
    /**
    * f0 -> ","
    * f1 -> Expression()
    */
    @Override
    public String visit(ExpressionTerm n, String argu) throws Exception
    {
        System.out.println("We are in ExpressionTerm Declaration");
        
        String parameter = n.f1.accept(this,argu);
        methodCalls.peek().add(parameter);

        return "Check ExpressionTerm";
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
    public String visit(ClassDeclaration n, String argu) throws Exception
    {
        System.out.println("We are in ClassDeclaration Declaration");
        
        String className = n.f1.accept(this,null);
        checker.setCurrentClass(className);
        checker.setCurrentMethod("");

        // Visit VarDeclaration
        n.f3.accept(this,null);

        // Visit MethodDeclaration
        n.f4.accept(this,null);

        return "Check ClassDeclaration";
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
    public String visit(MethodDeclaration n, String argu) throws Exception
    {
        System.out.println("We are in MethodDeclaration Declaration");
        
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
    public String visit(ClassExtendsDeclaration n, String argu) throws Exception
    {
        System.out.println("We are in ClassExtendsDeclaration Declaration");
        
        String className = n.f1.accept(this,null);
        checker.setCurrentClass(className);
        checker.setCurrentMethod("");

        // Visit VarDeclaration
        n.f5.accept(this,null);

        // Visit MethodDeclaration
        n.f6.accept(this,null);

        return "Check ClassExtendsDeclaration";
    }




    










    
    // I will not override the Type and the ArrayType as well -> eventualy I need the Type
    // Eventually, we need to override the Type so that we will be able to recognise a semantic error
    // And the ArrayType as well ...

    /**
    * f0 -> BooleanArrayType()
    *       | IntegerArrayType()
    */
    public String visit(ArrayType n, String argu) throws Exception
    {
        System.out.println("We are in ArrayType Declaration");
        String arrayType =  n.f0.accept(this, null);

        return arrayType;

    }

    /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
    @Override
    public String visit(Type n, String argu) throws Exception
    {
        System.out.println("We are in Type Declaration");
        
        String type = n.f0.accept(this,null);
        checker.checkType(type);

        return "Check Type";
    }

    /**
    * f0 -> "boolean"
    * f1 -> "["
    * f2 -> "]"
    */
    @Override
    public String visit(BooleanArrayType n, String argu) throws Exception
    {
        System.out.println("We are in BooleanArrayType Declaration");
        
        String type = n.f0.accept(this,null);
        type = type + "[]";

        return checker.checkIntegerArrayType(type);
    }
  
    /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
    @Override
    public String visit(IntegerArrayType n, String argu) throws Exception
    {
        System.out.println("We are in IntegerArrayType Declaration");

        String type = n.f0.accept(this,null);
        type = type + "[]";
    
        return checker.checkIntegerArrayType(type);
    }

    /**
    * f0 -> "boolean"
    */
    @Override
    public String visit(BooleanType n, String argu) throws Exception
    {
        System.out.println("We are in BooleanType Declaration");
        
        return n.f0.toString();
    }

    /**
    * f0 -> "int"
    */
    @Override
    public String visit(IntegerType n, String argu) throws Exception
    {
        System.out.println("We are in IntegerType Declaration");

        return n.f0.toString();

        // return n.f0.toString();
    }

    // AT THE END RE-OVERRIDE THE FUNCTIONS THAT GIVE TERMINAL SYMBOLS!!
    // AND THOSE THAT WERE OVERRIDEN DURING THE SYMBOL TABLE

    @Override
    public String visit(Identifier n, String argu)
    {
        System.out.println("We are in Identifier Declaration");

        // Debugging reasons!!
        // String identifier = n.f0.toString();
        // System.out.println(identifier);
        
        return n.f0.toString();
    }

    // AND THE NEW TERMINAL SYMBOLS APPEARING!!!

    /**
    * f0 -> <INTEGER_LITERAL>
    */
    @Override
    public String visit(IntegerLiteral n, String argu)
    {
        System.out.println("We are in IntegerLiteral Declaration");
        
        // Can it be anything else?
        return "int";
    }

    /**
    * f0 -> "true"
    */
    @Override
    public String visit(TrueLiteral n, String argu)
    {
        System.out.println("We are in TrueLiteral Declaration");
        
        return "boolean";
    }

    /**
    * f0 -> "false"
    */
    @Override
    public String visit(FalseLiteral n, String argu)
    {
        System.out.println("We are in FalseLiteral Declaration");
        
        return "boolean";
    }

    /**
    * f0 -> "this"
    */
    @Override
    public String visit(ThisExpression n, String argu)
    {
        System.out.println("We are in ThisExpression Declaration");
        
        return "this";
    }
}