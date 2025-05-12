package SymbolTableBuilders;

import java.lang.reflect.Method;
import java.util.*;

// Class containing data for a Class
public class ClassData {
  public String name="";
  public String extendsFrom=""; // inheritance relationship
  public String extendsTo=""; // inheritance relationship
  public LinkedHashMap <String, String> fields; // field name -> type
  public LinkedHashMap <String, MethodData> methods; // method name -> method info

  // Constructor
  public ClassData(){
    fields = new LinkedHashMap<String, String>();
    methods = new LinkedHashMap<String, MethodData>();
  }

  // Adds a field to a the class, checking for unsupported occassions
  public void addField(String name, String fieldType)
  {
    // check if field has already been declared in class
    if(fields.containsKey(name))
      throw new RuntimeException("Multiple declaration of field " + name + " in class " + this.name + "\n");

    fields.put(name, fieldType);
  }

  // Adds a method to a the class, checking for unsupported occassions
  public void addMethod(String name, MethodData info)
  {
    // Check if method has already been declared in class
    if(methods.containsKey(name))
      throw new RuntimeException("Method: " + name + " in class " + this.name + " has already been declared\n");

    methods.put(name,info);
  }

  // Checks method overriding
  public void validateOverriding(String name, ClassData parentClass) {
      if (parentClass != null && parentClass.methods.containsKey(name)) {
        // check for the return type of polymorphed function  
        if(parentClass.methods.get(name).returnType != methods.get(name).returnType)
        throw new RuntimeException("Methods " + name + " return type in class " + name + " must be the same with superclass' " + parentClass.name + " inheritant function\n");

        // check for the arguments of the polymorphed function
        ArrayList<String> parentclass_args_types = new ArrayList<>(parentClass.methods.get(name).parameters.values());
        ArrayList<String> derived_args_types =    new ArrayList<>(methods.get(name).parameters.values());
        if( !(parentclass_args_types.equals(derived_args_types)) )
          throw new RuntimeException("Methods " + name + " arguments in class " + name + " must be the same with superclass' " + parentClass.name + " inheritant function\n");
    
      }
  }

  // Print all fields of the class
  public void printFields(){
    System.out.println("Class contains the following fields:");
    Set< Map.Entry <String,String> > st = fields.entrySet();
    for (Map.Entry<String,String> cur:st){
      System.out.print("      " + cur.getKey()+":");
      System.out.println(cur.getValue());    }
    System.out.println("");
  }

  // Print all methods in a class
  public void printMethods(){
    System.out.println("Class contains the following methods:");
    Set< Map.Entry <String,MethodData> > st = methods.entrySet();
     for (Map.Entry<String,MethodData> cur:st){
         System.out.print(cur.getKey()+", ");
     }
     System.out.println("");
  }

  // Print all methods with details
  public void printMethodsDetails(){
    System.out.println("Class contains the following methods:");
    Set< Map.Entry <String,MethodData> > st = methods.entrySet();
    for (Map.Entry<String,Method> cur:st){
      System.out.println("    • " + cur.getKey());
      cur.getValue().ListArguments();
      cur.getValue().ListVariables();
      System.out.println("     and it's return type is " + cur.getValue().type);
    }
    System.out.println("");
  }
}
