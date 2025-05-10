import syntaxtree.*;
import visitor.*;


class SymbolTableBuilder extends GJDepthFirst <String, String>
{

    @Override
    public String visit(MainClass n, String argu) throws Exception {
        String classname = n.f1.accept(this, null); //get it untill the end of args
        System.out.println("Class: " + classname);

        super.visit(n, argu);

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

        n.f2.accept(this, argu);
        System.out.println("Fields: ");
        n.f3.accept(this, argu);
        System.out.println("Methods: ");
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);

        System.out.println();

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
    public String visit(ClassExtendsDeclaration n, String argu) throws Exception {
        n.f0.accept(this, argu);

        String classname = n.f1.accept(this, null);
        System.out.println("Class: " + classname);

        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        System.out.println("Fields: ");
        n.f5.accept(this, argu);
        System.out.println("Methods: ");
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);

        System.out.println();

        return null;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    @Override
   public String visit(VarDeclaration n, String argu) throws Exception {
        String _ret=null;
        String type = n.f0.accept(this, argu);
        String var = n.f1.accept(this, argu);
        System.out.println(var + " " + type);
        
        return _ret;
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
    public String visit(MethodDeclaration n, String argu) throws Exception {
        String argumentList = n.f4.present() ? n.f4.accept(this, null) : "";

        String myType = n.f1.accept(this, null);
        String myName = n.f2.accept(this, null);

        System.out.println("Method: " + myType + " " + myName + " (" + argumentList + ")");
        System.out.println("Local vars:");

        super.visit(n, argu);
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
    @Override
    public String visit(ArrayType n, String argu)
    {
        String BooleanArrayType = n.f0.accept(this, null);
        String IntegerArrayType = n.f1.accept(this, null);

        return BooleanArrayType + " " + IntegerArrayType;
    }
    
    //No need for them for now, we shall see
    // @Override
    // public String visit(BooleanArrayType n, String argu) {
    //     return "boolean[]";
    // }

    // @Override
    // public String visit(IntegerArrayType n, String argu) {
    //     return "int[]";
    // }

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
}