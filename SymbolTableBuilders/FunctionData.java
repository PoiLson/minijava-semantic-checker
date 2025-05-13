package SymbolTableBuilders;

import java.util.*;

// Class containing data about a function
public class FunctionData
{
  public String name = "";
  public String returnType; // function's type
  public LinkedHashMap <String,String> parameters; // [ parameter name , type ]
  public LinkedHashMap <String,String> variables; // [ function's variable name , type ]

  // Constructor
  public FunctionData()
  {
    parameters = new LinkedHashMap<String,String>();
    variables = new LinkedHashMap<String,String>();
  }

  // Adds a parameter to the function, ensuring uniqueness
  public void addParameter(String paramName, String paramType)
  {
    // check if argument has already been in decalred in function
    if(parameters.containsKey(paramName))
      throw new RuntimeException("Multiple declaration of parameter " + paramName + " in function " + this.name + "\n");

    parameters.put(paramName,paramType);
  }

  // Adds a local variable to the function, ensuring no conflict with parameters or existing locals
  public void addLocalVariable(String varName, String varType)
  {
    // check if variable has already been in declared in function
    if(variables.containsKey(varName))
      throw new RuntimeException("Multiple declaration of variable " + varName + " in function " + this.name + "\n");

    // check if variable has already been in declared in function as parameter
    if(parameters.containsKey(varName))
      throw new RuntimeException("Variable " + varName + " has already been declared as parameter in function " + this.name + "\n");
    
    variables.put(varName,varType);
  }

  // Print function's parameters
  public void printParameters()
  {
    if (parameters != null && !parameters.isEmpty())
    {
      System.out.println("\t|--> Function contains the following parameters:");
      Set< Map.Entry <String,String> > symboltable = parameters.entrySet();

      for (Map.Entry<String,String> currentEntry:symboltable)
      {
          System.out.print("\t|\t" + currentEntry.getKey()+":");
          System.out.println(currentEntry.getValue());
      }

    }
    else
    {
      System.out.println("\t|--> With no parameters");
    }
  }

  // Print function's local variables
  public void printVariables()
  {
    if (variables != null && !variables.isEmpty())
    {
      System.out.println("\t|--> Function contains the following variables:");
      Set< Map.Entry <String,String> > symboltable = variables.entrySet();

      for (Map.Entry<String,String> currentEntry:symboltable)
      {
          System.out.print("\t|\t" + currentEntry.getKey()+":");
          System.out.println(currentEntry.getValue());
      }

    }
    else
    {
      System.out.println("\t|--> With no variables");
    }
  }

}