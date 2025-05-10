import java.util.*;

class SymbolTable {
    // A stack of scopes (each being a map of symbol names to symbols)
    private Stack<Map<String, Symbol>> symbolStack;

    public SymbolTable() {
        symbolStack = new Stack<>();
        symbolStack.push(new HashMap<>()); // Push the global scope (the first scope)
    }

    public Map<String, Symbol> getCurrentScope() {
        return symbolStack.peek(); // Returns the top symbol map of the current scope
    }
    
    //IMPLEMENTATION OF THE 4 IMPORTANT FUNCTIONS OF THE SYMBOL TABLE
    //THE SYMBOL TABLE INTERFACE:

    //function to enter a new scope level
    public void enter() {
        symbolStack.push(new HashMap<>());
    }

    //leave scope, remove all names declared there
    public void exit() {
        if (!symbolStack.isEmpty()) {
            symbolStack.pop();
        }
    }

    //creates entry for name in current scope
    public void insert(String name, Symbol symbol) {
        symbolStack.peek().put(name, symbol); // Add to the current scope
    }

    //lookup a name, return an entry
    public Symbol lookup(String name) {
        // Search for the symbol starting from the most recent scope
        for (int i = symbolStack.size() - 1; i >= 0; i--) {
            Symbol symbol = symbolStack.get(i).get(name);
            if (symbol != null) {
                return symbol;
            }
        }
        return null; // Return null if the symbol is not found
    }
}

abstract class Symbol {
    String name;

    public Symbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class VariableSymbol extends Symbol {
    String type;

    public VariableSymbol(String name, String type) {
        super(name);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

class MethodSymbol extends Symbol {
    String returnType;
    List<String> parameters;

    public MethodSymbol(String name, String returnType, List<String> parameters) {
        super(name);
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<String> getParameters() {
        return parameters;
    }
}

class ClassSymbol extends Symbol {
    Map<String, MethodSymbol> methods;
    Map<String, VariableSymbol> fields;

    public ClassSymbol(String name) {
        super(name);
        this.methods = new HashMap<>();
        this.fields = new HashMap<>();
    }

    public void addMethod(String name, MethodSymbol method) {
        methods.put(name, method);
    }

    public void addField(String name, VariableSymbol field) {
        fields.put(name, field);
    }

    public MethodSymbol getMethod(String name) {
        return methods.get(name);
    }

    public VariableSymbol getField(String name) {
        return fields.get(name);
    }
}
