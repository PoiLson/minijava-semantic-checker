import syntaxtree.*;
import visitor.*;

import SymbolTableBuilders.*;

class SemanticCheckerVisitor extends GJDepthFirst<String, String>
{
    private SymbolTable symbolTable;

    public SemanticCheckerVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable; // Pass the symbol table to our semantic checker
    }

    // Now we can use symbolTable to check semantic errors, such as undeclared variables, etc.

    // @Override
    // public String visit(Identifier n, String argu) throws Exception {
    //     // Example check: Is this variable declared?
    //     Symbol symbol = symbolTable.lookup(n.f0.toString());
    //     if (symbol == null) {
    //         throw new SemanticError("Variable " + n.f0.toString() + " not declared!");
    //     }
    //     return null;
    // }
}
