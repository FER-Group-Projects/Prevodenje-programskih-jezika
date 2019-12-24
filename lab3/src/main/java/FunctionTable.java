import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionTable {
    //map<imeFunkcije, Map<return tip, lista tipova argumenata>>
    public static Map<String, Function> functionNameToInOutTypeMap = new HashMap<>();


    public static Function getFunctionFromFunctionTable(String funName) {
        return functionNameToInOutTypeMap.get(funName);
    }

    //metoda contains

    /**
     *
     * @throws
     */
    public static boolean containsFunction(String funName, Type funOutType, List<Type> funInType) {

        Function funInMap = functionNameToInOutTypeMap.get(funName);

        if (funInMap == null) {
            return false;
        }

        return funInMap.getInputTypes().equals(funInType) && funInMap.getReturnType().equals(funOutType);
    }


    //metoda za dodavanje
    public static void addFunctionToFunctionTable(String funName, Function function) {
        functionNameToInOutTypeMap.put(funName, function);
    }

}



