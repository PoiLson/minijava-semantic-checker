package SymbolTableBuilders;

import java.util.*;

// Class containing data for a Class
public class ClassData
{
  public String name = "";
  public String extendsFrom = ""; // inheritance relationship
  public String extendsTo = ""; // inheritance relationship
  public LinkedHashMap <String, String> variables; // variable name -> type
  public LinkedHashMap <String, FunctionData> functions; // function name -> function info

  // Constructor
  public ClassData()
  {
    variables = new LinkedHashMap<String, String>();
    functions = new LinkedHashMap<String, FunctionData>();
  }

  // Adds a variable to a the class, checking for unsupported occassions
  public void addVariable(String name, String variableType)
  {
    // check if variable has already been declared in class
    if(variables.containsKey(name))
      throw new RuntimeException("Multiple declaration of variable " + name + " in class " + this.name + "\n");

    variables.put(name, variableType);
  }

  // Adds a function to a the class, checking for unsupported occassions
  public void addFunction(String name, FunctionData info)
  {
    // Check if function has already been declared in class
    if(functions.containsKey(name))
      throw new RuntimeException("Function: " + name + " in class " + this.name + " has already been declared\n");

    functions.put(name,info);
  }

  // Checks function overriding
  public void validateOverriding(String name, ClassData parentClass)
  {
      if (parentClass != null && parentClass.functions.containsKey(name))
      {
        // check for the return type of polymorphed function  
        if(parentClass.functions.get(name).returnType != functions.get(name).returnType)
          throw new RuntimeException("Functions " + name + " return type in class " + name + " must be the same with superclass' " + parentClass.name + " inheritant function\n");

        // check for the arguments of the polymorphed function
        ArrayList<String> parentclass_args_types = new ArrayList<>(parentClass.functions.get(name).parameters.values());
        ArrayList<String> derived_args_types =    new ArrayList<>(functions.get(name).parameters.values());

        if( !(parentclass_args_types.equals(derived_args_types)) )
          throw new RuntimeException("Functions " + name + " arguments in class " + name + " must be the same with superclass' " + parentClass.name + " inheritant function\n");
    
      }
  }

  // Print all variables of the class
  public void printVariables()
  {
    if (variables != null && !variables.isEmpty())
    {
      System.out.println("|-> With the variables:");
      Set< Map.Entry <String,String> > symboltable = variables.entrySet();

      for (Map.Entry<String,String> currentEntry:symboltable)
      {
        System.out.print("\t" + currentEntry.getKey()+":");
        System.out.println(currentEntry.getValue());
      }

    }
    else
    {
      System.out.println("|-> With no variables");
    }

  }

  // Print all functions of class
  public void printFunctionDetails()
  {
    if (functions != null && !functions.isEmpty())
    {
      System.out.println("|-> With the following functions:");
      Set< Map.Entry <String, FunctionData> > symboltable = functions.entrySet();

      for (Map.Entry<String, FunctionData> currentEntry:symboltable)
      {
        System.out.println("\t○ " + currentEntry.getKey() + ", with return type: "+ currentEntry.getValue().returnType);
        currentEntry.getValue().printParameters();
        currentEntry.getValue().printVariables();
      }

    }
    else
    {
      System.out.println("|-> With no functions");
    }
  }
}