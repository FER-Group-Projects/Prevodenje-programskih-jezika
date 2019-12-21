import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionTable {
    //map<imeFunkcije, Map<return tip, lista tipova argumenata>>
    private Map<String, Map<String, List<String>>> functionNameToInOutTypeMap = new HashMap<>();

    //metoda contains
    public boolean containsFunction(String funName, String funOutType, List<String> funInType) {
        return functionNameToInOutTypeMap.get(funName) != null;
    }


    //metoda za dodavanje
    public void addFunctionToFunctionTable(String funName, String funOutType, List<String> funInType) {
        Map<String,List<String>> types = functionNameToInOutTypeMap.get(funName);

        // check if function does not exist then add it to the table
        // else just check that in and out types are the same
        if (types == null) {
            Map<String,List<String>> addTypes = new HashMap<>();
            addTypes.put(funOutType, funInType);
            functionNameToInOutTypeMap.put(funName, addTypes);
        } else {
            if (!types.containsKey(funOutType))
                throw new IllegalArgumentException("Cannot change function return type!");
            if (!types.get(funOutType).equals(funOutType))
                throw new IllegalArgumentException("Cannot change function input types!");
        }
    }

}
