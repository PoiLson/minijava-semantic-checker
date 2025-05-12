package SymbolTableBuilders;

import java.util.*;

// The ClassTableManager class manages all the classes in the program. It uses a LinkedHashMap<String, ClassData> to store the name of each class and its associated ClassData.

// The class supports inserting classes into the symbol table, ensuring that class declarations are valid (e.g., no duplicate classes, no circular inheritance).

// It also supports listing all the class names or providing detailed information about each class (including fields and methods).

// It includes error handling for various cases such as duplicate class declarations, undeclared superclasses, and circular inheritance.

// Symbol table for our minijava semantic analysis
public class ClassTableManager {
  public LinkedHashMap <String, ClassData> classRecord; // [ class name , class info ]

  // Initialize our structure that holds the classes of our program
  public ClassTableManager(){
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


  // List all classes in symbol table
  public void ListClasses(){
    System.out.println("Symbol Table contains the following classes:");
    Set< Map.Entry< String,ClassData> > st = classRecord.entrySet();
     for (Map.Entry< String,ClassData> cur:st){
         System.out.print(cur.getKey()+", ");
     }
     System.out.println("");
  }

  // List everything in symbol table
  public void ListEverything(){
    System.out.println(" Symbol Table contains the following classes:");
    Set< Map.Entry <String,ClassData> > st = classRecord.entrySet();
     for (Map.Entry <String,ClassData> cur:st){
         System.out.println(" • " + cur.getKey() + " and extends from " + cur.getValue().extendsFrom);
         cur.getValue().printFields();
         cur.getValue().printMethodsDetails();
     }
     System.out.println("");
  }
}
