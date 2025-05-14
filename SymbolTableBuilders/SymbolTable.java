package SymbolTableBuilders;

import java.util.*;

// Symbol table for our minijava semantic analysis
public class SymbolTable
{
  public LinkedHashMap <String, ClassData> classRecord; // [ class name , class info ]

  // Initialize our structure that holds the structure of our program
  public SymbolTable()
  {
    classRecord = new LinkedHashMap<String, ClassData>();
  }

  // Register a new class into the symbol table
  public void addClass(String name, ClassData info)
  {
    // throw error if the class is already declared inside the program
    if(classRecord.containsKey(name))
      throw new RuntimeException("The class: " + name + " has already been declared!\n");

    // if class extends from another class, which is called parent class
    if(info.extendsFrom != "")
    {
      // the parent class has to be declared
      if(classRecord.containsKey(info.extendsFrom) == false)
        throw new RuntimeException("Parent Class: " + info.extendsFrom + " has not been declared to extend from the class " + name + "\n");

      // a class cannot extend from itself
      if(info.extendsFrom == name)
        throw new RuntimeException("Parent Class: " + info.extendsFrom + " cannot be the same with child class " + name + "\n");

      // check for circural inheritance
      String grandparent = classRecord.get(info.extendsFrom).extendsFrom;
      if(grandparent == name)
        throw new RuntimeException("The class: " + name + " can't have circural inheritance");

    }

    // after al the checks we can safely add the class inside our symbol table
    classRecord.put(name,info);
  }

  // Function to print the symbol table -- debugging info
  public void printSymbolTable()
  {
    if (classRecord != null && !classRecord.isEmpty())
    {
      System.out.println("Symbol Table contains the following classes:");
      System.out.println("------------------------------------------------------------------");

      Set< Map.Entry <String,ClassData> > symboltable = classRecord.entrySet();

      for (Map.Entry <String,ClassData> currentEntry:symboltable)
      {
        System.out.print("-> " + currentEntry.getKey());

        if(currentEntry.getValue().extendsFrom != "")
        {
          System.out.print(" and extends from " + currentEntry.getValue().extendsFrom);
        }

        System.out.println("");

        currentEntry.getValue().printVariables();
        currentEntry.getValue().printFunctionDetails();
        System.out.println("------------------------------------------------------------------");
      }

    }
    else
    {
      System.out.println("The current program has no classes!");
    }
  }
}