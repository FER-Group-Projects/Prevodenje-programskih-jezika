import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionTable {
    //map<imeFunkcije, Map<return tip, lista tipova argumenata>>
    public Map<String, Function> functionNameToInOutTypeMap = new HashMap<>();

    // node which has this function table
    private Node node;

    //metoda contains

    /**
     *
     * @throws
     */
    public boolean containsFunction(Node currentNode, String funName, String funOutType, List<String> funInType) {
        Function funInMap = functionNameToInOutTypeMap.get(funName);
        if (funInMap == null) {
            try {
                return getFunctionFromParentBlock(currentNode, funName, funOutType, funInType);   //potencijalno: NullPointerException (vidi javadoc metode: @throws)
            } catch (NullPointerException ex) {
                return false;
            }
        }
        return funInMap.getInputTypes().equals(funInType) && funInMap.getReturnType().equals(funOutType);
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
    // RJESENJE - provjeriti: u Node staviti flag "isFunction" i ovisno o tome postupiti:
    // if isFunction==false: radi ovako rekurzivno kao ovdje sve dok ne dode do nekog function bloka
    // else: samo pogledaj deklaracije u globalnom scopeu
    public boolean getFunctionFromParentBlock(Node currentNode, String funName, String funOutType, List<String> funInType) {

        // ako je blok = blok funkcije
        if (currentNode.isFunction) {
            Map<String, Function> currentNodeFunctionMap = currentNode.functionTable.functionNameToInOutTypeMap;
            // ako je ovaj blok funkcija i nema deklariranu funkciju pogledaj u global scope
            if (!currentNodeFunctionMap.containsKey(funName)) {
                // doci do roota (njegov parent je null) i potraziti deklaraciju funkcije u njemu
                while (currentNode.parent != null) {
                    currentNode = currentNode.parent;
                }
                return getFunctionFromParentBlock(currentNode, funName, funOutType, funInType);
            }
            // ako funkcija sadrzi deklaraciju funkcije istih tipova -> true
            Function function = currentNodeFunctionMap.get(funName);
            return function.getInputTypes().equals(funInType) && function.getReturnType().equals(funOutType);
        }

        //////////////////////////////////////

        // ako blok = obican blok (NE-funkcija)
        // ako blok sadrzi deklaraciju funkcije istih tipova -> true
        if (containsFunction(currentNode, funName, funOutType, funInType))
            return true;
        // inace pitaj blok roditelja rekurzivno
        return getFunctionFromParentBlock(currentNode.parent, funName, funOutType, funInType);
    }



    //metoda za dodavanje
    public void addFunctionToFunctionTable(Node node, String funName, String funOutType, List<String> funInType) {
        // if the block or one of block's parents declared the function, do not add it again
        if (containsFunction(node, funName, funOutType, funInType))
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



