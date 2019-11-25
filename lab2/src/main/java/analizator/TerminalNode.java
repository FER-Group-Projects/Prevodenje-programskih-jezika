package analizator;

public class TerminalNode extends TreeNode {
    //has no children nodes
    //UniformCharacter with terminal symbol
    private UniformCharacter character;

    public TerminalNode(UniformCharacter character) {
        this.character = character;
    }

    @Override
    public String toString(){
        //TODO
        return null;
    }
}
