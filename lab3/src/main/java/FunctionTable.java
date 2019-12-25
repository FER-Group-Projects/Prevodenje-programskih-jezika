import java.util.*;

public class FunctionTable {
    public static Set<Function> declaredFunctions = new HashSet<>();

    public static boolean containsFunction(String funName, Type funOutType, List<Type> funInType) {
        return declaredFunctions.contains(new Function(funName, funOutType, funInType));
    }

    public static void addFunctionToFunctionTable(Function function) {
        declaredFunctions.add(function);
    }

    public static boolean isDefinedFunction(Function function) {
        Optional<Function> declaredFunction = declaredFunctions
                .stream()
                .filter(function::equals)
                .findFirst();

        if (!declaredFunction.isPresent()) {
            return false;
        }

        return declaredFunction.get().isDefined();
    }

    public static void setDefinedFunction(Function function) {
        Optional<Function> declaredFunction = declaredFunctions
                .stream()
                .filter(function::equals)
                .findFirst();

        if (declaredFunction.isPresent()) {
            declaredFunction.get().setDefined(true);
        }
    }

}



