import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionTable {
    //map<imeFunkcije, Map<return tip, lista tipova argumenata>>
    private Map<String, Function> functionNameToInOutTypeMap = new HashMap<>();

    // parent node's function table
    private FunctionTable parent;


    // TODO: REFACTOR -> prebaci u SemantickiAnalizator u functionTableCheck metodu
    //metoda containsMainFunction - specijalno za main
    public boolean containsMainFunction() {
        Function function = functionNameToInOutTypeMap.get("main");
        if (function == null)
            return false;

        List<String> inputTypes = function.getInputTypes();
        return function.getReturnType().equals("int") && inputTypes.contains("void") && inputTypes.size() == 1;
    }

    //metoda contains
    public boolean containsFunction(String funName, String funOutType, List<String> funInType) {
        Function funInMap = functionNameToInOutTypeMap.get(funName);
        if (funInMap == null) {
            return getFunctionFromParentBlock(funName, funOutType, funInType);   //potencijalno: NullPointerException (vidi javadoc metode: @throws)
        }
        return true;
    }

    /**
     *
     * metoda contains koja na kraju lokalne provjere pita roditelja
     * ako roditelj ima varijablu u svom FunctionTable, onda vraca true
     * inace pita svog roditelja itd. rekurzivno
     *
     *  @throws NullPointerException - ako ne nadje funkciju sve do root nodea
     */
    //TODO PROBLEM: sljedece ce vjerojatno raditi za nalazenje funkcije unutar parent bloka funkcija rekurzivno, ali sto s funkcijama? -> treba razlikovati funkcije od obicnih blokova
    // npr. funkcija A zove funkcije B i C -> A NE smije provjeravati deklaracije funkcija u B i C nego odmah direktno u globalnom scopeu
    // POTENCIJALNO RJESENJE: u BlockTable ili Node staviti neki flag "isFunction" i ovisno o tome postupiti:
    // if isFunction==false: radi ovako rekurzivno kao ovdje
    // else: samo pogledaj deklaracije u globalnom scopeu
    public boolean getFunctionFromParentBlock(String funName, String funOutType, List<String> funInType) {
        Map<String, Function> parentFunctionMap = parent.functionNameToInOutTypeMap;

        if (parentFunctionMap.containsKey(funName)) {
            return true;
        }

        return parent.getFunctionFromParentBlock(funName, funOutType, funInType);
    }



    //metoda za dodavanje
    public void addFunctionToFunctionTable(String funName, String funOutType, List<String> funInType) {
        // if the block or one of block's parents declared the function, do not add it again
        if (containsFunction(funName, funOutType, funInType))
            return;

        Function function = functionNameToInOutTypeMap.get(funName);

        // check if function does not exist then add it to the table
        // else just check that in and out types are the same
        if (function == null) {
            Function addFun = new Function(funOutType, funInType);
            functionNameToInOutTypeMap.put(funName, addFun);
        } else {
            if (!function.getReturnType().equals(funOutType))
                throw new IllegalArgumentException("Cannot change function return type!");
            if (!function.getInputTypes().equals(funInType))
                throw new IllegalArgumentException("Cannot change function input types!");
        }
    }

}



