import syntaxtree.*;
import visitor.*;

import SymbolTableBuilders.*;

public class SymbolTableVisitor extends GJDepthFirst <String,String>
{
  SymbolTable symbolTable; // our symbol table
  String currentClass;
  String currentFunction;

  public SymbolTableVisitor()
  {
    symbolTable = new SymbolTable();
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> "public"
   * f4 -> "static"
   * f5 -> "void"
   * f6 -> "main"
   * f7 -> "("
   * f8 -> "String"
   * f9 -> "["
   * f10 -> "]"
   * f11 -> Identifier()
   * f12 -> ")"
   * f13 -> "{"
   * f14 -> ( VarDeclaration() )*
   * f15 -> ( Statement() )*
   * f16 -> "}"
   * f17 -> "}"
   */
  @Override
  public String visit(MainClass n, String argu)
  {
    try
    {
      // Take all the important info to store the class
      // Inside of the symbol table
      String MainClassName = n.f1.accept(this,null);
      String MainArg = n.f11.accept(this,null);
      String MainName = "main";

      currentClass = MainClassName;
      currentFunction = MainName;

      ClassData MainClassInfo = new ClassData();
      MainClassInfo.name = MainClassName;

      FunctionData MainFunctionInfo = new FunctionData();
      MainFunctionInfo.name = MainName;
      MainFunctionInfo.returnType = "void";

      MainFunctionInfo.addParameter(MainArg,"String Array");
      MainClassInfo.addFunction(MainName,MainFunctionInfo);
      symbolTable.addClass(MainClassName,MainClassInfo);
      
      // Visit Var Declaration
      n.f14.accept(this,null);

      return "MainClass";
    }
    catch (Exception e)
    {
      System.err.println("Exception thrown in MainClass: " + e.getMessage());
      return null;
    }
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> ( VarDeclaration() )*
   * f4 -> ( MethodDeclaration() )*
   * f5 -> "}"
   */
  @Override
  public String visit(ClassDeclaration n,String argu)
  {
    try
    {
      String className = n.f1.accept(this,null);
      ClassData classInfo = new ClassData();

      currentClass = className;
      currentFunction = "";
      classInfo.name = currentClass;

      symbolTable.addClass(className,classInfo);

      // Visit VarDeclaration
      n.f3.accept(this,null);

      // Visit Function Declaration
      n.f4.accept(this,null);

      return "ClassDeclaration";
    }
    catch (Exception e)
    {
      System.err.println("Exception thrown in ClassDeclaration: " + e.getMessage());
      return null;
    }
  }

  /**
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "extends"
   * f3 -> Identifier()
   * f4 -> "{"
   * f5 -> ( VarDeclaration() )*
   * f6 -> ( MethodDeclaration() )*
   * f7 -> "}"
   */
  @Override
  public String visit(ClassExtendsDeclaration n,String argu)
  {
    try
    {
      String className = n.f1.accept(this,null);
      String extendsfrom = n.f3.accept(this,null);

      ClassData classInfo = new ClassData();
      currentClass = className;
      currentFunction = "";
      classInfo.extendsFrom = extendsfrom;
      classInfo.name = currentClass;

      symbolTable.addClass(className,classInfo);

      // Visit VarDeclaration
      n.f5.accept(this,null);

      // Visit Function Declaration
      n.f6.accept(this,null);

      return "ClassExtendsDeclaration";
    }
    catch (Exception e)
    {
      System.err.println("Exception thrown in ClassExtendsDeclaration: " + e.getMessage());
      return null;
    }
  }

  /**
  * f0 -> Type()
  * f1 -> Identifier()
  * f2 -> ";"
  */
  @Override
  public String visit(VarDeclaration n,String argu)
  {
    try
    {
      String vartype = n.f0.accept(this,null);
      String varname = n.f1.accept(this,null);

      if(currentFunction == "")
      {
        // Insert fields to class that belongs on symbol table
        symbolTable.classRecord.get(currentClass).addVariable(varname,vartype);
      }
      else
      {
        // Insert fields to the function of the class that belongs on symbol table
        symbolTable.classRecord.get(currentClass).functions.get(currentFunction).addLocalVariable(varname,vartype);
      }

      return "VarDeclaration";
    }
    catch (Exception e)
    {
      System.err.println("Exception thrown in VarDeclaration: " + e.getMessage());
      return null;
    }
  }
   /**
   * f0 -> "public"
   * f1 -> Type()
   * f2 -> Identifier()
   * f3 -> "("
   * f4 -> ( FormalParameterList() )?
   * f5 -> ")"
   * f6 -> "{"
   * f7 -> ( VarDeclaration() )*
   * f8 -> ( Statement() )*
   * f9 -> "return"
   * f10 -> Expression()
   * f11 -> ";"
   * f12 -> "}"
   */
  @Override
  public String visit(MethodDeclaration n,String argu)
  {
    try
    {
      String MethodType = n.f1.accept(this,null);
      String MethodName = n.f2.accept(this,null);
      currentFunction = MethodName;

      FunctionData newFunction = new FunctionData();
      newFunction.returnType = MethodType;
      newFunction.name = MethodName;

      // Add method to the current class' info
      symbolTable.classRecord.get(currentClass).addFunction(MethodName,newFunction);

      // Visit FormalParameterList
      n.f4.accept(this,null);

      // Before continuing, do a polymorph check
      ClassData parentClass = null;
      String parentClassName = symbolTable.classRecord.get(currentClass).extendsFrom;

      if( parentClassName != "")
      {
        parentClass = symbolTable.classRecord.get(parentClassName);
        symbolTable.classRecord.get(currentClass).validateOverriding(MethodName,parentClass);
      }

      // Visit VarDeclaration
      n.f7.accept(this,null);

      return "MethodDeclaration";
    }
    catch (Exception e)
    {
      System.err.println("Exception thrown in MethodDeclaration: " + e.getMessage());
      return null;
    }
  }
  
  /*
   * f0 -> Type()
   * f1 -> Identifier()
   */
  @Override
  public String visit(FormalParameter n, String argu)
  {
    try
    {
      String parameter_type = n.f0.accept(this,null);
      String parameter_name = n.f1.accept(this,null);

      // Insert argument to current function of current class
      symbolTable.classRecord.get(currentClass).functions.get(currentFunction).addParameter(parameter_name,parameter_type);

      return "FormalParameter";
    }
    catch (Exception e)
    {
      System.err.println("Exception thrown in FormalParameter: " + e.getMessage());
      return null;
    }
  }

  /**
   * f0 -> BooleanArrayType()
   * f1 -> IntegerArrayType()
   */
  //Maybe it is better to handle the different scenarios explicitly
    // @Override
  // public String visit(ArrayType n, String argu)
  // {
  //     return n.f0.accept(this, argu); 
  // }
  @Override
  public String visit(BooleanArrayType n, String argu)
  {
      return "boolean[]";
  }

  @Override
  public String visit(IntegerArrayType n, String argu)
  {
      return "int[]";
  }

  @Override
  public String visit(BooleanType n, String argu)
  {
    return "boolean";
  }

  @Override
  public String visit(IntegerType n, String argu)
  {
    return "int";
  }

  @Override
  public String visit(Identifier n, String argu)
  {
     return n.f0.toString();
  }

  public SymbolTable getSymbolTable()
  {
    return this.symbolTable;
  }
}