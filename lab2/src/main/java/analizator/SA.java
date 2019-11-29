package analizator;

import java.util.Stack;

public class SA {
    private static UniformCharacterStream inputTape;
    private static Stack<PDAStackItem> pdaStack;
    private static TreeNode tree;
    private static SADescriptor descriptor;

    private static boolean isParsing = true;

    private static int lastLineIndex = 0;
    private static int characterInLineIndex = 0;
    private static UniformCharacter lastInputCharacter;

    //used to fill children of parent node when reducing, previous parents are pushed on stack
    private Stack<TerminalNode> terminalStack = new Stack<>();
    private Stack<NonTerminalNode> nonTerminalStack = new Stack<>();

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
        printSyntaxTree(tree);
    }

    private static void parse() {
        while (isParsing) {
            UniformCharacter currentCharacter = lastInputCharacter;
            String currentPdaState = getTopState();
            Symbol currentPdaSymbol = getTopSymbol();

            PDAAction action = getActionFromDescriptor(currentPdaState, currentCharacter.getIdSymbol());

            switch (action.getActionType()) {
                case ACCEPT: //special case of REDUCE
                    reduceAndAccept(getReductionRuleFromIndex(action.getReductionRuleIndex()));
                    isParsing = false;
                    break;
                case REDUCE:
                    //remove symbols from right side of reduction rule from stack
                    //push next state along with symbol from left side of reduction rule to stack
                    makeReduction(getReductionRuleFromIndex(action.getReductionRuleIndex()));
                    break;
                case PUT: //part of REDUCE
                    break;
                case SHIFT:
                    //push current character to stack along with next state
                    //move input one place to the right
                    pdaStack.push(new PDAStackItem(action.getNextState(), null, true, currentCharacter));
                    inputTape.step();
                    getInputCharacter();
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
        lastInputCharacter = c;
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
        System.err.println("Error parsing [" +
                lastInputCharacter.getIdSymbol() + "] " + lastInputCharacter.getText() +
                " in line " + lastInputCharacter.getLine() + " at index " + characterInLineIndex);
        //TODO: output expected characters on stderr
        //Skip input until a sync character appears
        while (!isSyncSymbol(lastInputCharacter.getIdSymbol())) {
            System.err.println("Skipping [" +
                    lastInputCharacter.getIdSymbol() + "] " + lastInputCharacter.getText() +
                    " in line " + lastInputCharacter.getLine() + " at index " + characterInLineIndex);
            getInputCharacter();
        }
        //pop elements from stack until an action is defined (different from reject or put)
        while (!actionIsDefined(getTopState(), lastInputCharacter.getIdSymbol())) {
            pdaStack.pop();
        }
    }

    private static boolean actionIsDefined(String pdaState, Symbol symbol) {
        return descriptor.actionTable.get(pdaState).get(symbol).getActionType() != ActionType.REDUCE;
    }

    private static void makeReduction(GrammarRule rule) {
        //TODO: build syntax tree
        //remove right side from pdastack
        for (Symbol s : rule.getToList()) {
            pdaStack.pop();
        }
        //get next state based on left side and state on top of stack
        //this is the PUT action
        String nextState = getActionFromDescriptor(getTopState(), rule.getFrom()).getNextState();
        //push left side along with nextState
        pdaStack.push(new PDAStackItem(nextState, rule.getFrom()));
    }

    private static void reduceAndAccept(GrammarRule rule) {
        //TODO: connect elements from stacks to tree(root)
        for (Symbol s : rule.getToList()) {

        }
    }

    private static void printSyntaxTree(TreeNode tree) {
        //TODO
    }

}
