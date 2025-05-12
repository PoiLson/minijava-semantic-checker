package SymbolTableBuilders;

import java.util.*;

// Class containing data about a method
public class MethodData {
  public String name="";
  public String returnType; // method's type
  public LinkedHashMap <String,String> parameters; // [ parameter name , type ]
  public LinkedHashMap <String,String> variables; // [ methods variable name , type ]

  // Constructor
  public MethodData(){
    parameters = new LinkedHashMap<String,String>();
    variables = new LinkedHashMap<String,String>();
  }

  // Adds a parameter to the method, ensuring uniqueness
  public void addParameter(String paramName, String paramType)
  {
    // check if argument has already been in decalred in method
    if(parameters.containsKey(paramName))
      throw new RuntimeException("Multiple declaration of parameter " + paramName + " in method " + this.name + "\n");
    parameters.put(paramName,paramType);
  }

  // Adds a local variable to the method, ensuring no conflict with parameters or existing locals
  public void addLocalVariable(String varName, String varType)
  {
    // check if variable has already been in declared in method
    if(variables.containsKey(varName))
      throw new RuntimeException("Multiple declaration of variable " + varName + " in method " + this.name + "\n");

    // check if variable has already been in declared in method as parameter
    if(parameters.containsKey(varName))
      throw new RuntimeException("Variable " + varName + " has already been declared as parameter in method " + this.name + "\n");
    
    variables.put(varName,varType);
  }

  // Print method's parameters
  public void printParameters(){
    System.out.println("Method contains the following parameters:");
    Set< Map.Entry <String,String> > st = parameters.entrySet();
     for (Map.Entry<String,String> cur:st){
         System.out.print("         " + cur.getKey()+":");
         System.out.println(cur.getValue());
     }
     System.out.println("");
  }

  // Print the method's local variables
  public void printVariables(){
    System.out.println("Method contains the following variables:");
    Set< Map.Entry <String,String> > st = variables.entrySet();
     for (Map.Entry<String,String> cur:st){
         System.out.print("         " + cur.getKey()+":");
         System.out.println(cur.getValue());
     }
     System.out.println("");
  }

}