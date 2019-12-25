import java.util.List;
import java.util.Objects;

public class Function {
    private String functionName;
    private Type returnType;
    private List<Type> inputTypes;

    private boolean isDefined;

    public Function(String functionName, Type returnType, List<Type> inputTypes) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.inputTypes = inputTypes;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public List<Type> getInputTypes() {
        return inputTypes;
    }

    public void setInputTypes(List<Type> inputTypes) {
        this.inputTypes = inputTypes;
    }

    public boolean isDefined() {
        return isDefined;
    }

    public void setDefined(boolean defined) {
        isDefined = defined;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Function function = (Function) o;
        return Objects.equals(functionName, function.functionName) &&
                returnType == function.returnType &&
                Objects.equals(inputTypes, function.inputTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName, returnType, inputTypes);
    }

    @Override
    public String toString() {
        return "Function{" +
                "functionName='" + functionName + '\'' +
                ", returnType=" + returnType +
                ", inputTypes=" + inputTypes +
                ", isDefined=" + isDefined +
                '}';
    }
}
