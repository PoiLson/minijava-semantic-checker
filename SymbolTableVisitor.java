// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import SymbolTableBuilders.ClassSymbol;
// import SymbolTableBuilders.MethodSymbol;
// import SymbolTableBuilders.Symbol;
// import SymbolTableBuilders.SymbolTableBuilder;
// import SymbolTableBuilders.VariableSymbol;
// import syntaxtree.*;
// import visitor.*;


// class SymbolTableVisitor extends GJDepthFirst <String, String>
// {
//     private SymbolTableBuilder symbolTable;
//     private List<Map<String, Symbol>> allScopes;

//     public SymbolTableVisitor() {
//         this.symbolTable = new SymbolTableBuilder(); // Initialize the symbol table
//         this.allScopes = new ArrayList<>();
//     }

//     @Override
//     public String visit(MainClass n, String argu) throws Exception {
//         String classname = n.f1.accept(this, null); //get it untill the end of args
//         System.out.println("Class: " + classname);
//         symbolTable.enter();  // Main class is a new scope

//         // Add Main class to the symbol table
//         symbolTable.insert(classname, new ClassSymbol(classname));

//         super.visit(n, argu);
//         // Save the scope before exiting it
//         allScopes.add(new HashMap<>(symbolTable.getCurrentScope()));  // Save the current scope

//         symbolTable.exit();  // Exit the main class scope

//         System.out.println();

//         return null;
//     }

//     /**
//      * f0 -> "class"
//      * f1 -> Identifier()
//      * f2 -> "{"
//      * f3 -> ( VarDeclaration() )*
//      * f4 -> ( MethodDeclaration() )*
//      * f5 -> "}"
//      */
//     @Override
//     public String visit(ClassDeclaration n, String argu) throws Exception {
//         n.f0.accept(this, argu);
        
//         String classname = n.f1.accept(this, argu);
//         System.out.println("Class: " + classname);

//         // Add class to the symbol table
//         ClassSymbol classSymbol = new ClassSymbol(classname);
//         symbolTable.insert(classname, classSymbol);
        
//         symbolTable.enter();  // Enter a new scope for the class

//         // Visit fields and methods for the class
//         n.f2.accept(this, argu);
//         System.out.println("Fields: ");
//         n.f3.accept(this, argu);
//         System.out.println("Methods: ");
//         n.f4.accept(this, argu);
//         n.f5.accept(this, argu);

//         System.out.println();

//         // Save the scope before exiting it
//         allScopes.add(new HashMap<>(symbolTable.getCurrentScope()));  // Save the current scope

//         symbolTable.exit();  // Exit the class scope after processing

//         System.out.println();
//         return null;
//     }

//     /**
//     * f0 -> Type()
//     * f1 -> Identifier()
//     * f2 -> ";"
//     */
//     @Override
//    public String visit(VarDeclaration n, String argu) throws Exception {
    
//         String type = n.f0.accept(this, argu);
//         String var = n.f1.accept(this, argu);
//         System.out.println(var + " " + type);
//         VariableSymbol varSymbol = new VariableSymbol(var, type);
        
//         // Add variable to the current scope (method or class)
//         symbolTable.insert(var, varSymbol);
//         return null;
//     }

//     /**
//      * f0 -> "class"
//      * f1 -> Identifier()
//      * f2 -> "extends"
//      * f3 -> Identifier()
//      * f4 -> "{"
//      * f5 -> ( VarDeclaration() )*
//      * f6 -> ( MethodDeclaration() )*
//      * f7 -> "}"
//      */
//     @Override
//     public String visit(MethodDeclaration n, String argu) throws Exception {
//         String returnType = n.f1.accept(this, argu);
//         String methodName = n.f2.accept(this, argu);
//         List<String> paramList = new ArrayList<>();
        
//         if (n.f4.present()) {
//             paramList.add(n.f4.accept(this, argu)); // Accept parameter list
//         }
        
//         MethodSymbol methodSymbol = new MethodSymbol(methodName, returnType, paramList);
        
//         // Assuming we already have the class symbol, add method to it
//         ClassSymbol classSymbol = (ClassSymbol) symbolTable.lookup(argu); // Lookup class symbol by class name
//         classSymbol.addMethod(methodName, methodSymbol);
        
//         symbolTable.enter();  // Enter method's scope
//         super.visit(n, argu); // Visit the body of the method (statements, variables)

//          // Save the current method scope before exiting it
//         allScopes.add(new HashMap<>(symbolTable.getCurrentScope()));  // Save the current method scope


//         symbolTable.exit(); // Exit method scope
        
//         return null;
//     }

//     /**
//      * f0 -> Type()
//      * f1 -> Identifier()
//      */
//     @Override
//     public String visit(FormalParameter n, String argu) throws Exception
//     {
//         String type = n.f0.accept(this, null);
//         String name = n.f1.accept(this, null);
//         return type + " " + name;
//     }

//     /**
//      * f0 -> BooleanArrayType()
//      * f1 -> IntegerArrayType()
//      */
//     //Maybe it is better to handle the different scenarios explicitly
//      // @Override
//     // public String visit(ArrayType n, String argu)
//     // {
//     //     return n.f0.accept(this, argu); 
//     // }
    
//     @Override
//     public String visit(BooleanArrayType n, String argu) {
//         return "boolean[]";
//     }

//     @Override
//     public String visit(IntegerArrayType n, String argu) {
//         return "int[]";
//     }

//     @Override
//     public String visit(BooleanType n, String argu) {
//         return "boolean";
//     }

//     @Override
//     public String visit(IntegerType n, String argu) {
//         return "int";
//     }

//     @Override
//     public String visit(Identifier n, String argu) {
//         return n.f0.toString();
//     }



//     // Add method to print symbol table contents after traversal
//     // public void printSymbolTable() {
//     //     System.out.println("Printing Symbol Table:");

//     //     Map<String, Symbol> currentScope = symbolTable.getFirstScope();

//     //     for (Map.Entry<String, Symbol> entry : currentScope.entrySet()) {
//     //         Symbol symbol = entry.getValue();
//     //         if (symbol instanceof ClassSymbol) {
//     //             System.out.println("Class: " + symbol.getName());
//     //             ClassSymbol classSymbol = (ClassSymbol) symbol;
//     //             for (Map.Entry<String, MethodSymbol> method : classSymbol.methods.entrySet()) {
//     //                 System.out.println("\tMethod: " + method.getKey() + " Return type: " + method.getValue().getReturnType());
//     //             }
//     //         } else if (symbol instanceof MethodSymbol) {
//     //             System.out.println("Method: " + symbol.getName());
//     //         } else if (symbol instanceof VariableSymbol) {
//     //             System.out.println("\tVariable: " + symbol.getName() + " Type: " + ((VariableSymbol) symbol).getType());
//     //         }
//     //     }
//     // }




//     public void printSymbolTable() {
//         System.out.println("Printing Symbol Table:");

//         // Iterate over all saved scopes
//         for (Map<String, Symbol> scope : allScopes) {
//             for (Map.Entry<String, Symbol> entry : scope.entrySet()) {
//                 Symbol symbol = entry.getValue();
//                 if (symbol instanceof ClassSymbol) {
//                     System.out.println("Class: " + symbol.getName());
//                     ClassSymbol classSymbol = (ClassSymbol) symbol;
//                     for (Map.Entry<String, MethodSymbol> method : classSymbol.methods.entrySet()) {
//                         System.out.println("\tMethod: " + method.getKey() + " Return type: " + method.getValue().getReturnType());
//                     }
//                 } else if (symbol instanceof MethodSymbol) {
//                     System.out.println("Method: " + symbol.getName());
//                 } else if (symbol instanceof VariableSymbol) {
//                     System.out.println("\tVariable: " + symbol.getName() + " Type: " + ((VariableSymbol) symbol).getType());
//                 }
//             }
//         }
//     }

// }



import syntaxtree.*;
import visitor.*;
import SymbolTableBuilders.*;
import staticheckingexception.StatiCheckingException;


public class SymbolTableVisitor extends GJDepthFirst <String,String> {
  ClassTableManager ST; // our symbol table
  String currentClass;
  String currentMethod;

  SymbolTableVisitor(){
    ST = new ClassTableManager();
  }

  public String visit(MainClass n,String argu) {
    //System.out.println("We are in Main Class Declaration");
    try {
      String MainClassName = n.f1.accept(this,null);

      String MainName = "main";
      String MainArg = n.f11.accept(this,null);
      currentClass = MainClassName;
      currentMethod = MainName;
      ClassData MainClassInfo = new ClassData();
      MainClassInfo.name = MainClassName;
      MethodData MainMethodsInfo = new MethodData();
      MainMethodsInfo.name = MainName;
      MainMethodsInfo.returnType = "void";
      MainMethodsInfo.addParameter(MainArg,"String Array");
      MainClassInfo.addMethod(MainName,MainMethodsInfo);
      ST.addClass(MainClassName,MainClassInfo);
      // Visit VarDeclaration
      n.f14.accept(this,null);
      return "MainClassVisited";

    } catch (Exception e) {
      System.err.println("Exception during evaluation: " + e.getMessage());
    }
    return "MainClassVisited";
  }

  public String visit(ClassDeclaration n,String argu) throws RuntimeException
  {
    try
    {
      //System.out.println("We are in Class Declaration");
      String className = n.f1.accept(this,null);
      ClassData classInfo = new ClassData();
      currentClass = className;
      currentMethod = "";
      classInfo.name = currentClass;
      ST.addClass(className,classInfo);
      // Visit VarDeclaration
      n.f3.accept(this,null);
      // Visit Method Declaration
      n.f4.accept(this,null);
      return "ClassVisited";
    } catch (Exception e) {
      System.err.println("Exception during evaluation: " + e.getMessage());
    }
    return "ClassVisited";
  }

  public String visit(ClassExtendsDeclaration n,String argu) throws RuntimeException
  {
    try
    {
      //System.out.println("We are in ClassExtends Declaration");
      String className = n.f1.accept(this,null);
      String extendsfrom = n.f3.accept(this,null);
      ClassData classInfo = new ClassData();
      currentClass = className;
      currentMethod = "";
      classInfo.extendsFrom = extendsfrom;
      classInfo.name = currentClass;
      ST.addClass(className,classInfo);
      // Visit VarDeclaration
      n.f5.accept(this,null);
      // Visit MethodDeclaration
      n.f6.accept(this,null);
      return "ClassExtendsVisited";
    } catch (Exception e) {
      System.err.println("Exception during evaluation: " + e.getMessage());
    }
    return "ClassExtendsVisited";
  }

  public String visit(VarDeclaration n,String argu) throws RuntimeException
  {
    //System.out.println("We are in Var Declaration");
    try{
      String vartype = n.f0.accept(this,null);
      String varname = n.f1.accept(this,null);
      if(currentMethod == ""){
        // Insert fields to class that belongs on symbol table
        ST.classRecord.get(currentClass).addField(varname,vartype);
      }
      else {
        // Insert fields to the method of the class that belongs on symbol table
        ST.classRecord.get(currentClass).methods.get(currentMethod).addLocalVariable(varname,vartype);
      }
      return "VarDeclarationVisited";
    } catch (Exception e) {
      System.err.println("Exception during evaluation: " + e.getMessage());
    }
    return "VarDeclarationVisited";
  }

  public String visit(MethodDeclaration n,String argu) throws RuntimeException
  {
    try{
      //System.out.println("We are in Method Declaration");
      String MethodType = n.f1.accept(this,null);
      String MethodName = n.f2.accept(this,null);
      currentMethod = MethodName;
      MethodData newMethod = new MethodData();
      newMethod.returnType = MethodType;
      newMethod.name = MethodName;
      // Add method to the current class' info
      ST.classRecord.get(currentClass).addMethod(MethodName,newMethod);
      // Visit FormalParameterList
      n.f4.accept(this,null);
      // Before continuing, do a polymorph check
      ClassData superclass = null;
      String superclassName = ST.classRecord.get(currentClass).extendsFrom;
      if( superclassName != "")
        superclass = ST.classRecord.get(superclassName);
      ST.classRecord.get(currentClass).validateOverriding(MethodName,superclass);
      // Visit VarDeclaration
      n.f7.accept(this,null);
      return "MethodDeclarationVisited";
    } catch (Exception e) {
        System.err.println("Exception during evaluation: " + e.getMessage());
    }
    return "MethodDeclarationVisited";
  }

  public String visit(FormalParameter n, String argu) throws RuntimeException
  {
    try{
      //System.out.println("We are in Formal Parameter");
      String parameter_type = n.f0.accept(this,null);
      String parameter_name = n.f1.accept(this,null);
      // Insert argument to current method of current class
      ST.classRecord.get(currentClass).methods.get(currentMethod).addParameter(parameter_name,parameter_type);
      return "FormalParameterVisited";
    } catch (Exception e) {
      System.err.println("Exception during evaluation: " + e.getMessage());
    }
    return "FormalParameterVisited";
  }

  public String visit(IntegerType n, String argu) {
     return n.f0.toString();
  }

  public String visit(ArrayType n, String argu) {
    return "int array";
  }

  public String visit(BooleanType n, String argu){
    return n.f0.toString();
  }

  public String visit(Identifier n, String argu) {
     return n.f0.toString();
  }

  public ClassTableManager getSymbolTable(){
    return this.ST;
  }
}
