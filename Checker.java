
import java.util.*;
import SymbolTableBuilders.*;
import syntaxtree.AndExpression;
import syntaxtree.PlusExpression;

// A .java file that implements the function checkers
// For everything we encounter in our progra
// Functions, types, parameters, etc.

public class Checker
{
    public SymbolTable symboltable;
    String currentClass="";
    String currentMethod="";

    // Constructor
    public Checker(SymbolTable symboltable)
    {
        this.symboltable = symboltable;
    }

    // Set functions (setters)
    public void setCurrentClass(String currentClass)
    {
        this.currentClass = currentClass;
    }

    public void setCurrentMethod(String currentMethod)
    {
        this.currentMethod = currentMethod;
    }

    // Get functions (getters)
    public String getCurrentClass()
    {
        return this.currentClass;
    }

    public String getCurrentMethod()
    {
        return this.currentMethod;
    }

    // This function checks if the currentClass exists in our symbol table
    // Maybe bollean 0 -> it does not exist, 1 -> it exists
    public boolean checkExistingClass()
    {
        if(symboltable.classRecord.containsKey(currentClass))
        {
            return true;
        }
        
        throw new RuntimeException("The class: " + currentClass + " has not been declared in the program");
    }

    // This function checks if the currentMethod exists in our symbol table
    // Maybe bollean 0 -> it does not exist, 1 -> it exists
    public boolean checkExistingMethod()
    {
        // NEEDS FIXING!!!
        if(symboltable.classRecord.containsKey(currentMethod))
        {
            return true;
        }
        
        throw new RuntimeException("The method: " + currentMethod + " has not been declared inside the class: " + currentClass );
    }

    // Check that identifier has been declared in the appropriate scope
    public boolean isIdentifierDeclared(String identifier)
    {
        // Checks inside Class fields
        boolean declaredAsClassField = symboltable.classRecord.get(currentClass).variables.containsKey(identifier);
        
        // Checks inside Local variables inside a method
        boolean declaredAsMethodVariable = false;

        // Checks inside Method parameters
        boolean declaredAsMethodParameter = false;

        if(currentMethod != "")
        {
            declaredAsMethodVariable = symboltable.classRecord.get(currentClass).functions.get(currentMethod).variables.containsKey(identifier);
            declaredAsMethodParameter = symboltable.classRecord.get(currentClass).functions.get(currentMethod).parameters.containsKey(identifier);
        }

        // Checks inside Inherited fields from parent classes
        boolean declaredAsFieldInParentClass = false;
        if(searchAncestors(currentClass,identifier) != "")
            declaredAsFieldInParentClass = true;

        if( !(declaredAsClassField || declaredAsMethodParameter || declaredAsMethodVariable || declaredAsFieldInParentClass) )
            throw new RuntimeException("This identifier: " + identifier + " is not declared anywhere.");
    
        return true;
    }   
    
    // Return identifier's type that has been declared to an ancestor
    public String searchAncestors(String className, String identifier)
    {
        String parentClass = symboltable.classRecord.get(className).extendsFrom;

        while(parentClass != "")
        {
            String type = symboltable.classRecord.get(parentClass).variables.get(identifier);
            if(type != null)
                return type;

            parentClass = symboltable.classRecord.get(parentClass).extendsFrom;
        }
        return "";
    }

    // Get the type of the identifier given by checking its declaration scope
    public String findType(String identifier)
    {
        isIdentifierDeclared(identifier);

        String identifierType;

        if(currentMethod != "")
        {
            // Get type from method's variables
            identifierType = symboltable.classRecord.get(currentClass).functions.get(currentMethod).variables.get(identifier);
            if( identifierType != null)
                return identifierType;
            
            // Get type from method's parameters
            identifierType = symboltable.classRecord.get(currentClass).functions.get(currentMethod).parameters.get(identifier);
            if( identifierType != null)
                return identifierType;
        }

        // Get type from class's field
        identifierType = symboltable.classRecord.get(currentClass).variables.get(identifier);
        if(identifierType != null)
            return identifierType;

        identifierType = searchAncestors(currentClass, identifier);
        if(identifierType != "")
            return identifierType;

        return "error";
    }

    // This function checks if the types given for the assignment are compatible
    // e.g. int x = 5; -> allow it
    //      String x = 5; -> do not allow it
    public boolean checkAssignmentStatement(String identifier, String expression)
    {
        String identifierType = findType(identifier);

        // Direct type match
        if(identifierType.equals(expression))
            return true;

        // If the expression is a class, check if it's a subclass of identifierType
        if(symboltable.classRecord.containsKey(expression))
        {
            String parentClass = symboltable.classRecord.get(expression).extendsFrom;
            while(parentClass != null && !parentClass.equals(""))
            {
                if(parentClass.equals(identifierType))
                    return true;

                parentClass = symboltable.classRecord.get(parentClass).extendsFrom;
            }
        }

        throw new RuntimeException("This: " + identifier + " and this one: " + expression + " are not compatible for assignent.");
    }

    // This function returns the expression's type
    public String checkExpression(String expression)
    {
        String typeOfExpression = expression;

        // if expression is this then it refers to current class
        if(expression == "this")
            typeOfExpression = currentClass;
        // if expression returns a string starting with "/", then a new allocation expression occured of type /<type>
        else if(expression.startsWith("/"))
            typeOfExpression = expression.substring(1); 
        // if expression returns an identifier
        else if( !expression.startsWith("/") && expression != "int" && expression != "boolean" && expression != "int[]" && expression != "boolean[]" && !(symboltable.classRecord.containsKey(expression)))
            typeOfExpression = findType(expression);
        
        return typeOfExpression;
    }

    // Check if the clause given is boolean
    public void isClauseBoolean(String clause)
    {
        String clauseType = findType(clause);

        if(clauseType != "boolean")
            throw new RuntimeException("Clause: " + clause + " in method " + currentMethod + " of class " + currentClass + " isn't a boolean.");
    }

    // This function checks if the AND expression is correct
    public void checkAndExpression(String firstClause, String secondClause)
    {
        // Check first variable
        if(firstClause == "int" || firstClause == "this" || firstClause == "int[]" || firstClause == "boolean[]" || firstClause.startsWith("/") || symboltable.classRecord.containsKey(firstClause))
            throw new RuntimeException("Invalid AND operation in class: " + currentClass + " of method: " + currentMethod + ", first clause must be of type boolean");
        
        if(firstClause != "boolean")
            isClauseBoolean(firstClause);
        
        // Check second variable
        if(secondClause == "int" || secondClause == "this" || secondClause == "int[]" || firstClause == "boolean[]" || secondClause.startsWith("/") || symboltable.classRecord.containsKey(secondClause))
            throw new RuntimeException("Invalid AND operation in class " + currentClass + " of method " + currentMethod + ", second clause must be of type boolean");
        
        if(secondClause != "boolean")
            isClauseBoolean(secondClause);
    }

    // Check if the primary expression given is int
    public void isPrimaryExprInt(String primaryExpr)
    {
        String primaryExprType = findType(primaryExpr);

        if(primaryExprType != "int")
            throw new RuntimeException("Clause: " + primaryExpr + " in method " + currentMethod + " of class " + currentClass + " isn't an int.");
    }

    // This function checks if the primary expression given is able of arithmetic operations
    public void checkPrimaryExpression(String firstPrimaryExpr, String secondPrimaryExpr)
    {
        // Check first variable
        if(firstPrimaryExpr == "boolean" || firstPrimaryExpr == "this" || firstPrimaryExpr == "int[]" || firstPrimaryExpr == "boolean[]" || firstPrimaryExpr.startsWith("/") || symboltable.classRecord.containsKey(firstPrimaryExpr))
            throw new RuntimeException("Invalid AND operation in class: " + currentClass + " of method: " + currentMethod + ", first primary expression must be of type int");
        
        if(firstPrimaryExpr != "int")
            isPrimaryExprInt(firstPrimaryExpr);
        
        // Check second variable
        if(secondPrimaryExpr == "boolean" || secondPrimaryExpr == "this" || secondPrimaryExpr == "int[]" || secondPrimaryExpr == "boolean[]" || secondPrimaryExpr.startsWith("/") || symboltable.classRecord.containsKey(secondPrimaryExpr))
            throw new RuntimeException("Invalid AND operation in class " + currentClass + " of method " + currentMethod + ", second primary expression must be of type int");
        
        if(secondPrimaryExpr != "int")
            isPrimaryExprInt(secondPrimaryExpr);
    }

    // Check if it is a valid array name
    public String isArrNameValid(String arrayName)
    {
        String typeArrName = findType(arrayName);

        // Check if type is int array or a boolean array
        if(!(typeArrName.equals("int[]") || typeArrName.equals("boolean[]")))
            throw new RuntimeException("This array: " + arrayName + " in method " + currentMethod + " of class " + currentClass + " isn't an int or a boolean array");
   
        if(typeArrName.equals("int[]"))
            return "int";
        else if(typeArrName.equals("boolean[]"))
            return "boolean";

        return null;
    }

    // This function checks if the array name and size given are semantically correct
    public String checkArrayLookup(String arrayName, String arraySize)
    {
        String typeArrName = null;

        // Check if the arrayName is actually an array's name
        if(arrayName == "boolean" || arrayName == "this" || arrayName == "int" || arrayName.startsWith("/") || symboltable.classRecord.containsKey(arrayName))
            throw new RuntimeException("Invalid array lookup in class: " + currentClass + " of method: " + currentMethod + ", expression must be of type array.");
        
        if(!(arrayName.equals("int[]") || arrayName.equals("boolean[]")))
            typeArrName = isArrNameValid(arrayName);
        
        // Check second variable
        if(arraySize == "boolean" || arraySize == "this" || arraySize == "int[]" || arraySize.startsWith("/") || symboltable.classRecord.containsKey(arraySize))
            throw new RuntimeException("Invalid array lookup in class " + currentClass + " of method " + currentMethod + ", size expression must be of type int");
        
        if(arraySize != "int")
            isPrimaryExprInt(arraySize);

        return typeArrName;
    }

    // This function checks if tof the MessageSend rule of our grammarhe array name has been declarfed before in our program and in the current scope
    public String checkArrayLength(String arrayName)
    {
        // Check if the arrayName is actually an array's name
        if(arrayName == "boolean" || arrayName == "this" || arrayName == "int" || arrayName.startsWith("/") || symboltable.classRecord.containsKey(arrayName))
            throw new RuntimeException("Invalid array length operation in class: " + currentClass + " of method: " + currentMethod + ", expression must be of type array.");
        
        if(!(arrayName.equals("int[]") || arrayName.equals("boolean[]")))
            return isArrNameValid(arrayName);
        
        return null;
    }

    // Check if variable is an existing class
    public String isVarDeclaredClass(String var)
    {
        String type = findType(var);

        if( type == "int" || type == "int[]" || type == "boolean[]" || type == "boolean" )
            throw new RuntimeException("Var " + var + " in method " + currentMethod + " of class " + currentClass + " must be a declared class");
        
        return type;
    }

    // Check if a method can be called from primary expression specified
    public void canBeCalled(String expr)
    {
        if(expr == "this" || expr.startsWith("/"))
            return;

        if(symboltable.classRecord.containsKey(expr))
            return;

        if( expr == "boolean" || expr == "int" || expr == "int[]" || expr == "boolean[]" )
            throw new RuntimeException("Function call in class " + currentClass + " of method " + currentMethod + ", cannot be called by a non class object");
        
        isIdentifierDeclared(expr);
        isVarDeclaredClass(expr);
    }

    // This function is responsible for the semantic check of the calling of the methods
    public String checkMessageSend(String caller, String method, ArrayList<String> parameters)
    {
        // First of all check if we can call the method from the caller class
        canBeCalled(caller);

        // Determine from which class the method is called on
        String fromClass;

        // If caller is "this", the current class is used
        if(caller == "this")
            fromClass = currentClass;
        // If caller starts with "/", it is an allocated object and the class name is extracted
        else if(caller.startsWith("/"))
            fromClass = caller.substring(1);
        // If caller is directly a class name, use this of course
        else if( symboltable.classRecord.containsKey(caller) )
            fromClass = caller;
        // After everything else, resolve the class of the variable caller refers to
        else
            fromClass = isVarDeclaredClass(caller);
        
        // Now check for the method in the class or in one of its father classes, if it has
        String type = null;
        String parentClass = symboltable.classRecord.get(fromClass).extendsFrom;

        ArrayList<String> methodArgs = null;
        
        // Does it exist in the current class?
        if(symboltable.classRecord.get(fromClass).functions.containsKey(method))
        {
            type = symboltable.classRecord.get(fromClass).functions.get(method).returnType;
            methodArgs = new ArrayList<>(symboltable.classRecord.get(fromClass).functions.get(method).parameters.values());
        }
        // Or does it exist in one of the father classes
        // In which case we traverse up the inheritance chain to find it
        else if(parentClass != "")
        {
            boolean found = false;

            while(parentClass != "")
            {
                if(symboltable.classRecord.get(parentClass).functions.containsKey(method))
                {
                    type = symboltable.classRecord.get(parentClass).functions.get(method).returnType;
                    methodArgs = new ArrayList<>(symboltable.classRecord.get(parentClass).functions.get(method).parameters.values());
                    found = true;

                    break;
                }

                parentClass = symboltable.classRecord.get(parentClass).extendsFrom;
            }

            if(!found)
                throw new RuntimeException("There is no method " + method + " in class " + fromClass + " to call from. ( tried to call from method " + currentMethod + " of class " + currentClass + ")");
        }
        else
            throw new RuntimeException("There is no method " + method + " in class " + fromClass + " to call from. ( tried to call from method " + currentMethod + " of class " + currentClass + ")");
        
        // Check if the number of parameters given is the same with method's declared number of parameters
        if(methodArgs.size() != parameters.size())
            throw new RuntimeException("Parameters aren't the same type, as declared, in method " + method + " of class " + fromClass + " to call from. ( tried to call from method " + currentMethod + " of class " + currentClass + ")");
        
        // Check for type equality
        for(int i=0; i <methodArgs.size(); i++)
        {
            if(!(methodArgs.get(i).equals(parameters.get(i))))
            {
                // If type of expr is a class
                if(symboltable.classRecord.containsKey(parameters.get(i)))
                {
                    // Check if that class derives from a class with same type of destination's type
                    boolean found = false;
                    parentClass = symboltable.classRecord.get(parameters.get(i)).extendsFrom;
                    
                    while(parentClass != "")
                    {
                        if( methodArgs.get(i).equals(parentClass) )
                            found = true;
                        parentClass = symboltable.classRecord.get(parentClass).extendsFrom;
                    }

                    if(!found)
                        throw new RuntimeException("Parameters aren't the same type, as declared, in method " + method + " of class " + fromClass + " to call from. ( tried to call from method " + currentMethod + " of class " + currentClass + ")");
                }
            }
        }

        return type;
    }












    // This function checks if the type given is in correct form
    public boolean checkType(String type)
    {
        // check if type is one of the classes declared
        if( symboltable.classRecord.containsKey(type) || type.equals("boolean[]") || type.equals("int[]") || type.equals("boolean") || type.equals("int"))
            return true;
        
        throw new RuntimeException("This type: " + type + " is not acceptable.");
    }

    // This function checks if the type given is boolean array type
    public boolean checkBooleanArrayType(String type)
    {
        if(type == "boolean[]")
            return true;
        
        throw new RuntimeException("Was expecting boolean[] instead of: " + type );
    }

    // This function checks if the type given is integer array type
    public boolean checkIntegerArrayType(String type)
    {
        if(type == "int[]")
            return true;
        
        throw new RuntimeException("Was expecting int[] instead of: " + type );
    }

    // This function checks if the type given is boolean
    public boolean checkBooleanType(String type)
    {
        if(type == "boolean")
            return true;
        
        throw new RuntimeException("Was expecting boolean instead of: " + type );
    }

    // This function checks if the type given is integer
    public boolean checkIntegerType(String type)
    {
        if(type == "int")
            return true;
        
        throw new RuntimeException("Was expecting int instead of: " + type );
    }

}