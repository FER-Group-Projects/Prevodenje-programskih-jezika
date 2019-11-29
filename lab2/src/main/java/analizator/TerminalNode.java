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
        return character.toString();
    }

    @Override
    public void printTree(int depth) {
        //output space 'depth' times
        for(int i =0;i<depth;i++){
            System.out.print(" ");
        }
        //then the character
        System.out.println(character);
    }
}
