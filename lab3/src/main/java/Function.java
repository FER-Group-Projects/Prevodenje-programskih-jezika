import java.util.List;

public class Function {
    private String returnType;
    private List<String> inputTypes;

    //TODO: vidjeti sto s ovim atributom - treba na kraju poslije obilaska stabla provjeriti je li svaka deklarirana funkcija ima definiciju
    // mozda treba neka klasa FunctionDefinition ili sl. koja ce zapisati definiciju
    private boolean isDefined;

    public Function(String returnType, List<String> inputTypes) {
        this.returnType = returnType;
        this.inputTypes = inputTypes;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<String> getInputTypes() {
        return inputTypes;
    }

    public void setInputTypes(List<String> inputTypes) {
        this.inputTypes = inputTypes;
    }
}
