package analizator;

import java.util.Stack;

public class SA {
    private static UniformCharacterStream inputTape;
    private static Stack<PDAStackItem> pdaStack;
    private TreeNode tree;
    private static SADescriptor descriptor;

    private static boolean isParsing = true;

    private static int lastLineIndex = 0;
    private static int characterInLineIndex = 0;
    private static UniformCharacter lastInputCharacter;

    private Stack<TreeNode> nodestack = new Stack<>();

    public static void main(String[] args) {
        //init stack
        pdaStack = new Stack<>();
        pdaStack.push(new PDAStackItem("0", Symbol.STACK_BOTTOM));
        //init UniformCharacterStream with stdin
        inputTape = new UniformCharacterStream(System.in);
        //deserialize sa-descriptor
        descriptor = DescriptorSerializer.deserialize();
        //do actions until done
        //and watch out for errors
        parse();
        printSyntaxTree();
    }

    private static void parse() {
        while (isParsing) {
            UniformCharacter currentCharacter = getInputCharacter();
            String currentPdaState = getTopState();
            Symbol currentPdaSymbol = getTopSymbol();

            PDAAction action;

            if (currentPdaSymbol.equals(Symbol.STACK_BOTTOM)) {
                action = getActionFromDescriptor(currentPdaState, currentCharacter.getIdSymbol());
            } else {
                action = getActionFromDescriptor(currentPdaState, currentPdaSymbol);
            }

            switch (action.getActionType()) {
                case ACCEPT: //special case of REDUCE
                    makeReduction(getReductionRuleFromIndex(action.getReductionRuleIndex()), action.getNextState());
                    isParsing = false;
                    break;
                case REDUCE:
                    //remove symbols from right side of reduction rule from stack
                    //push next state along with symbol from left side of reduction rule to stack
                    makeReduction(getReductionRuleFromIndex(action.getReductionRuleIndex()), action.getNextState());
                    break;
                case PUT: //special case of REDUCE(?)
                    //TODO
                    break;
                case SHIFT:
                    //push current character to stack along with next state
                    //move input one place to the right
                    pdaStack.push(new PDAStackItem(action.getNextState(), null, true, currentCharacter));
                    inputTape.step();
                    break;
                case REJECT:
                    //error happened, handle it
                    handleError();
                    break;
            }
        }
    }

    private static UniformCharacter getInputCharacter() {
        if (lastInputCharacter.getIdSymbol().equals(Symbol.TAPE_END)) return lastInputCharacter;
        UniformCharacter c = inputTape.getCurrent();
        characterInLineIndex++;
        if (lastLineIndex + 1 == c.getLine()) {
            characterInLineIndex = 0;
        }
        lastLineIndex = c.getLine();
        return c;
    }

    private void makeStepOnTape() {
        inputTape.step();
    }

    private static Symbol getTopSymbol() {
        return pdaStack.peek().getStackSymbol();
    }

    private static String getTopState() {
        return pdaStack.peek().getState();
    }

    private static PDAAction getActionFromDescriptor(String pdaState, Symbol symbol) {
        return descriptor.actionTable.get(pdaState).get(symbol);
    }

    private static GrammarRule getReductionRuleFromIndex(int index) {
        return descriptor.grammarReductionRules.get(index);
    }

    private static boolean isSyncSymbol(Symbol symbol) {
        return descriptor.syncSymbolSet.contains(symbol);
    }

    private static void handleError() {
        //TODO
    }

    private static void makeReduction(GrammarRule rule, String nextState) {
        //build syntax tree here
        //TODO
    }

    private static void printSyntaxTree() {

    }

}
