import java.util.*;

public class BlockTable {
    //map<imeVarijable, (tip, vrijednost)>
    private Map<String, VariableTypeValueLExpression> nameToTypeValueMap = new HashMap<>();

    // node which has this block table
    private Node node;

    // set containing function names declared in the block
    private Set<String> declaredFunctions = new HashSet<>();


    //////////////////// block VARIABLES ////////////////////

    /**
     * metoda za dohvat vrijednosti i tipa neke varijable
     *
     * @param varName
     * @return
     * @throws NullPointerException - ako ne nadje varijablu sve do root nodea
     */
    public VariableTypeValueLExpression getVariableTypeValueLExpression(String varName) {
        VariableTypeValueLExpression varInMap = nameToTypeValueMap.get(varName);
        if (varInMap == null) {
            return getVariableFromParentBlock(varName);   //potencijalno: NullPointerException (vidi javadoc metode: @throws)
        }
        return varInMap;
    }

    /**
     * metoda za dohvat tipa neke varijable
     *
     * @param varName
     * @return
     * @throws NullPointerException - ako ne nadje varijablu sve do root nodea
     */
    public Type getVariableType(String varName) {
        VariableTypeValueLExpression varInMap = nameToTypeValueMap.get(varName);
        if (varInMap == null) {
            return getVariableFromParentBlock(varName).getTip();   //potencijalno: NullPointerException (vidi javadoc metode: @throws)
        }
        return varInMap.getTip();
    }


    /**
     * metoda za dohvat vrijednosti neke varijable
     *
     * @param varName
     * @return
     * @throws NullPointerException - ako ne nadje varijablu sve do root nodea
     */
    public String getVariableValue(String varName) {
        VariableTypeValueLExpression varInMap = nameToTypeValueMap.get(varName);
        if (varInMap == null) {
            return getVariableFromParentBlock(varName).getVrijednost();   //potencijalno: NullPointerException (vidi javadoc metode: @throws)
        }
        return varInMap.getVrijednost();
    }


    /**
     * metoda contains koja na kraju lokalne provjere pita roditelja
     * ako roditelj ima varijablu u svom BlockTable, onda vraca tu varijablu
     * inace pita svog roditelja itd. rekurzivno
     *
     * @param varName
     * @return
     * @throws NullPointerException - ako ne nadje varijablu sve do root nodea
     */
    public VariableTypeValueLExpression getVariableFromParentBlock(String varName) {
        Node parent = node.parent;
        Map<String, VariableTypeValueLExpression> parentVariableMap = parent.blockTable.nameToTypeValueMap;

        if (parentVariableMap.containsKey(varName)) {
            return parentVariableMap.get(varName);
        }

        return parent.blockTable.getVariableFromParentBlock(varName);
    }


    /**
     * metoda za dodavanje nove varijable / mijenjanje vrijednosti postojece varijable
     *
     * @throws IllegalArgumentException ako se pokuša za varijablu istog imena promijeniti njezin tip
     */

    public void addVariableToBlockTable(String varName, Type varType, String varValue) {
        VariableTypeValueLExpression varTypeValueLExpression = nameToTypeValueMap.get(varName);

        if (varTypeValueLExpression == null) {
            nameToTypeValueMap.put(varName, new VariableTypeValueLExpression(varType, varValue));
        } else {
            // if the variable is already recorded in the block table
            if (!varTypeValueLExpression.getTip().equals(varType)) {
                throw new IllegalArgumentException("Cannot change variable type!");
            }
            // just change variable value
            varTypeValueLExpression.setVrijednost(varValue);
        }
    }


    //////////////////// block FUNCTIONS ////////////////////
    public boolean containsFunctionByNameLocally(String funName) {
        return declaredFunctions.contains(funName);
    }

    // method returning if the block contains the function identified ONLY by name, but not in-out types
    public boolean containsFunctionByName(String funName) {
        BlockTable currentScope = this;

        while (currentScope != null) {
            if (currentScope.declaredFunctions.contains(funName)) {
                return true;
            }

            if (currentScope.node == null || currentScope.node.parent == null) {
                break;
            }

            currentScope = currentScope.node.parent.blockTable;
        }

        return false;
    }

    public boolean containsFunctionLocally(String funName, Type funOutType, List<Type> funInType) {
        if (!declaredFunctions.contains(funName)) return false;
        Function function = FunctionTable.getFunctionFromFunctionTable(funName);
        return function.getReturnType().equals(funOutType) && function.getInputTypes().equals(funInType);
    }

    // method returning if the block contains the function
    public boolean containsFunction(String funName, Type funOutType, List<Type> funInType) {
        // if the block does not have the function declaration, see the parent nodes recursively
        if (!containsFunctionByName(funName)) {
            return false;
        }

        // if the block has the function declaration, check if the function in-out types are equal to those in FunctionTable
        // if yes, it is the function with the given declaration -> true
        // else the function with that name is recorded but with different in-out types -> false
        Function function = FunctionTable.getFunctionFromFunctionTable(funName);
        return function.getReturnType().equals(funOutType) && function.getInputTypes().equals(funInType);
    }


    //metoda za dodavanje
    public void addFunctionToBlockTable(String funName, Type funOutType, List<Type> funInType) {
        // if the block or one of block's parents declared the function -> do not add it again
        if (containsFunction(funName, funOutType, funInType))
            return;

        Function funInFunctionTable = FunctionTable.getFunctionFromFunctionTable(funName);

        // check if function does not exist then add it to the table
        // else just check that in and out types are the same
        if (funInFunctionTable == null) {
            declaredFunctions.add(funName);
            FunctionTable.addFunctionToFunctionTable(funName, new Function(funOutType, funInType));
        } else {
            if (!funInFunctionTable.getReturnType().equals(funOutType))
                throw new IllegalArgumentException("Cannot change function return type!");
            if (!funInFunctionTable.getInputTypes().equals(funInType))
                throw new IllegalArgumentException("Cannot change function input types!");

            // at this place, function is trying to be declared but it is already declared in one of the block's parents -> do not add it again
        }
    }

    public boolean containsVariableInLocalBlock(String varName) {
        return nameToTypeValueMap.containsKey(varName);
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

}
