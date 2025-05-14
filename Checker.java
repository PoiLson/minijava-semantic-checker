
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

    // This function checks if the type given is of correct form
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