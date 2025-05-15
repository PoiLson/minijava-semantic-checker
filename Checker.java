
import java.util.*;
import SymbolTableBuilders.*;

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
    public boolean IsIdentifierDeclared(String identifier)
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
        if(SearchAncestors(currentClass,identifier) != "")
            declaredAsFieldInParentClass = true;

        if( !(declaredAsClassField || declaredAsMethodParameter || declaredAsMethodVariable || declaredAsFieldInParentClass) )
            throw new RuntimeException("This identifier: " + identifier + " is not declared anywhere.");
    
        return true;
    }   
    
    // Return identifier's type that has been declared to an ancestor
    public String SearchAncestors(String className, String identifier)
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
    public String FindIdentifier(String identifier)
    {
        IsIdentifierDeclared(identifier);

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

        identifierType = SearchAncestors(currentClass, identifier);
        if(identifierType != "")
            return identifierType;

        return "error";
    }

    // This function checks if the types given for the assignment are compatible
    // e.g. int x = 5; -> allow it
    //      String x = 5; -> do not allow it
    public boolean checkAssignmentStatement(String identifier, String expression)
    {
        String identifierType = FindIdentifier(identifier);

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

    // This function checks if the type given is of correct form
    public String checkExpression(String expression)
    {
        String typeOfExpression = null;

        // if expression is this then it refers to current class
        if(expression == "this")
            typeOfExpression = currentClass;
        // if expression returns a string starting with "/", then a new allocation expression occured of type /<type>
        else if(expression.startsWith("/"))
            typeOfExpression = expression.substring(1); 
        // if expression returns an identifier
        else if( !expression.startsWith("/") && expression != "int" && expression != "boolean" && expression != "int array" && !(symboltable.classRecord.containsKey(expression)))
            typeOfExpression = FindIdentifier(expression);
        
        return typeOfExpression;
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