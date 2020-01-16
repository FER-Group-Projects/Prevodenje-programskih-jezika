public class LabelMaker {

    private static int constantCounter = 0;

    public static String getGlobalVariableLabel(String variableName) {
        return "G_" + variableName.toUpperCase();
    }

    public static String getConstantLabel() {
        constantCounter++;

        return "CONST_" + constantCounter;
    }

    public static String getFunctionLabel(String functionName) {
        return "F_" + functionName.toUpperCase();
    }

}
