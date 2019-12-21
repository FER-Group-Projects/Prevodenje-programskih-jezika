public class UniformCharacter extends Node {

    private String identifier;
    private int line;
    private String text;

    public UniformCharacter(){
        super();
    }

    public UniformCharacter(String identifier, int line, String text) {
        this.identifier = identifier;
        this.line = line;
        this.text = text;
    }

    @Override
    public Node analyze() {
        //TODO
        return null;
    }

    @Override
    public String toText() {
        //TODO
        return null;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
