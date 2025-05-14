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

}