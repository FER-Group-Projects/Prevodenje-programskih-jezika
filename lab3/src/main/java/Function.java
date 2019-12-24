import java.util.List;

public class Function {
    private Type returnType;
    private List<Type> inputTypes;

    private boolean isDefined;

    public Function(Type returnType, List<Type> inputTypes) {
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
}
