import java.util.Objects;

public class UniformCharacter extends Node {

    private String identifier;
    private int line;
    private String text;

    public UniformCharacter(){
        super();
    }

    public UniformCharacter(String identifier, int line, String text) {
        this.identifier = Objects.requireNonNull(identifier, "Identifier cannot be null.");
        this.line = line;
        this.text = Objects.requireNonNull(text, "Text cannot be null.");
    }

    @Override
    public Node analyze() {
        return null;
    }

    @Override
    public String toText() {
        return identifier + "(" + line + "," + text + ")";
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniformCharacter that = (UniformCharacter) o;
        return line == that.line &&
                identifier.equals(that.identifier) &&
                text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, line, text);
    }

    @Override
    public String toString() {
        return toText();
    }

    @Override
    public String getName() {
        return identifier;
    }

    @Override
    public void printTree(int depth) {
        //output space 'depth' times
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(" ");
        }
        System.out.print(sb.toString());
        //print symbol
        System.out.println(identifier + " " + line + " " + text);
    }
}
