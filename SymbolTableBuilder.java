import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import syntaxtree.*;
import visitor.*;


class SymbolTableBuilder extends GJDepthFirst <String, String>
{
    private SymbolTable symbolTable;

    public SymbolTableBuilder() {
        this.symbolTable = new SymbolTable(); // Initialize the symbol table
    }

    @Override
    public String visit(MainClass n, String argu) throws Exception {
        String classname = n.f1.accept(this, null); //get it untill the end of args
        System.out.println("Class: " + classname);
        symbolTable.enter();  // Main class is a new scope

        // Add Main class to the symbol table
        symbolTable.insert(classname, new ClassSymbol(classname));

        super.visit(n, argu);
        symbolTable.exit(); // Exit the scope after visiting MainClass

        System.out.println();

        return null;
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
    public String visit(ClassDeclaration n, String argu) throws Exception {
        n.f0.accept(this, argu);
        
        String classname = n.f1.accept(this, argu);
        System.out.println("Class: " + classname);

        // Add class to the symbol table
        ClassSymbol classSymbol = new ClassSymbol(classname);
        symbolTable.insert(classname, classSymbol);
        
        symbolTable.enter();  // Enter a new scope for the class

        // Visit fields and methods for the class
        n.f2.accept(this, argu);
        System.out.println("Fields: ");
        n.f3.accept(this, argu);
        System.out.println("Methods: ");
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);

        System.out.println();

        symbolTable.exit(); // Exit the class scope

        return null;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    @Override
   public String visit(VarDeclaration n, String argu) throws Exception {
    
        String type = n.f0.accept(this, argu);
        String var = n.f1.accept(this, argu);
        System.out.println(var + " " + type);
        VariableSymbol varSymbol = new VariableSymbol(var, type);
        
        // Add variable to the current scope (method or class)
        symbolTable.insert(var, varSymbol);
        return null;
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
    public String visit(MethodDeclaration n, String argu) throws Exception {
        String returnType = n.f1.accept(this, argu);
        String methodName = n.f2.accept(this, argu);
        List<String> paramList = new ArrayList<>();
        
        if (n.f4.present()) {
            paramList.add(n.f4.accept(this, argu)); // Accept parameter list
        }
        
        MethodSymbol methodSymbol = new MethodSymbol(methodName, returnType, paramList);
        
        // Assuming we already have the class symbol, add method to it
        ClassSymbol classSymbol = (ClassSymbol) symbolTable.lookup(argu); // Lookup class symbol by class name
        classSymbol.addMethod(methodName, methodSymbol);
        
        symbolTable.enter();  // Enter method's scope
        super.visit(n, argu); // Visit the body of the method (statements, variables)
        symbolTable.exit(); // Exit method scope
        
        return null;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    @Override
    public String visit(FormalParameter n, String argu) throws Exception
    {
        String type = n.f0.accept(this, null);
        String name = n.f1.accept(this, null);
        return type + " " + name;
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
    public String visit(BooleanArrayType n, String argu) {
        return "boolean[]";
    }

    @Override
    public String visit(IntegerArrayType n, String argu) {
        return "int[]";
    }

    @Override
    public String visit(BooleanType n, String argu) {
        return "boolean";
    }

    @Override
    public String visit(IntegerType n, String argu) {
        return "int";
    }

    @Override
    public String visit(Identifier n, String argu) {
        return n.f0.toString();
    }



    // Add method to print symbol table contents after traversal
    public void printSymbolTable() {
        System.out.println("Printing Symbol Table:");

        Map<String, Symbol> currentScope = symbolTable.getCurrentScope(); // Access current scope

        for (Map.Entry<String, Symbol> entry : currentScope.entrySet()) {
            Symbol symbol = entry.getValue();
            if (symbol instanceof ClassSymbol) {
                System.out.println("Class: " + symbol.getName());
                ClassSymbol classSymbol = (ClassSymbol) symbol;
                for (Map.Entry<String, MethodSymbol> method : classSymbol.methods.entrySet()) {
                    System.out.println("\tMethod: " + method.getKey() + " Return type: " + method.getValue().getReturnType());
                }
            } else if (symbol instanceof MethodSymbol) {
                System.out.println("Method: " + symbol.getName());
            } else if (symbol instanceof VariableSymbol) {
                System.out.println("\tVariable: " + symbol.getName() + " Type: " + ((VariableSymbol) symbol).getType());
            }
        }
    }
}