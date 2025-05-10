class SymbolTable {
    Map<String, ClassInfo> classes;
}

class ClassInfo {
    String name;
    String parent;
    Map<String, String> fields;
    Map<String, MethodInfo> methods;
}

class MethodInfo {
    String returnType;
    List<Parameter> parameters;
    Map<String, String> localVariables;
}

class Parameter {
    String name;
    String type;
}
