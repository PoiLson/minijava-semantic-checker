import java.util.*;
import SymbolTableBuilders.*;

// A .java file that implements the function checkers
// For everything we encounter in our progra
// Functions, types, parameters, etc.

public class Checker
{
    public SymbolTable symboltable;
    String currentClass="";
    String currentFunction="";

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

    public void setCurrentFunction(String currentFunction)
    {
        this.currentFunction = currentFunction;
    }

    // Get functions (getters)
    public String getCurrentClass()
    {
        return this.currentClass;
    }

    public String getCurrentFunction()
    {
        return this.currentFunction;
    }

    // This function checks if the currentClass exists in our symbol table
    public void checkExistingClass()
    {
        
    }



}