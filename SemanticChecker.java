import SymbolTableBuilders.Symbol;
import SymbolTableBuilders.SymbolTableBuilder;

class SemanticChecker extends GJDepthFirst<String, String> {
    private SymbolTableBuilder symbolTable;

    public SemanticChecker(SymbolTableBuilder symbolTable) {
        this.symbolTable = symbolTable; // Pass the symbol table from the first pass
    }

    // Now you can use symbolTable to check semantic errors, such as undeclared variables, etc.

    @Override
    public String visit(Identifier n, String argu) throws Exception {
        // Example check: Is this variable declared?
        Symbol symbol = symbolTable.lookup(n.f0.toString());
        if (symbol == null) {
            throw new SemanticError("Variable " + n.f0.toString() + " not declared!");
        }
        return null;
    }
}
