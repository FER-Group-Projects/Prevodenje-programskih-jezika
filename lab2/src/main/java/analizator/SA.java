package analizator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
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
    private static Stack<TerminalNode> terminalNodes = new Stack<>();
    private static Stack<NonTerminalNode> nonTerminalNodes = new Stack<>();

    public static void main(String[] args) {
        //deserialize sa-descriptor
        descriptor = DescriptorSerializer.deserialize();
        //init stack
        pdaStack = new Stack<>();
        pdaStack.push(new PDAStackItem(descriptor.startingState, Symbol.STACK_BOTTOM));
        //init UniformCharacterStream with stdin
        inputTape = new UniformCharacterStream(System.in);
        //do actions until done
        //and watch out for errors
        parse();
        tree.printTree(0);
    }

    private static void parse() {
        loadInputCharacter(); //load the first character
        while (isParsing) {
            UniformCharacter currentCharacter = lastInputCharacter;
            String currentPdaState = getTopState();
            Symbol currentPdaSymbol = getTopSymbol();

            PDAAction action = getActionFromDescriptor(currentPdaState, currentCharacter.getIdSymbol());
            switch (action.getActionType()) {
                case ACCEPT: //special case of REDUCE
                    performReductionRule(getReductionRuleFromIndex(action.getNumber()));
                    isParsing = false;
                    break;
                case REDUCE:
                    //remove symbols from right side of reduction rule from stack
                    //push next state along with symbol from left side of reduction rule to stack
                    actionReduce(getReductionRuleFromIndex(action.getNumber()));
                    break;
                case PUT: //part of REDUCE
                    break;
                case SHIFT:
                    //push current character to stack along with next state
                    //move input one place to the right
                    pdaStack.push(new PDAStackItem(Integer.toString(action.getNumber()), null, true, currentCharacter));
                    //save the character for building the syntax tree
                    terminalNodes.push(new TerminalNode(currentCharacter));
                    inputTape.step();
                    loadInputCharacter(); //load the character
                    break;
                case REJECT:
                    //error happened, handle it
                    handleError();
                    break;
            }
        }

        // get first child, instead of <S'>
        tree = nonTerminalNodes.pop().getChildren().get(0);
    }

    private static void loadInputCharacter() {
        if (lastInputCharacter != null && lastInputCharacter.getIdSymbol().equals(Symbol.TAPE_END)) return;
        UniformCharacter c = inputTape.getCurrent();
        lastInputCharacter = c;
        characterInLineIndex++;
        if (lastLineIndex + 1 == c.getLine()) {
            characterInLineIndex = 0;
        }
        lastLineIndex = c.getLine();
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
        return descriptor.actionTable.getOrDefault(pdaState, Collections.emptyMap()).getOrDefault(symbol, new PDAAction(ActionType.REJECT));
    }

    private static GrammarRule getReductionRuleFromIndex(int index) {
        return descriptor.grammarReductionRules.get(index);
    }

    private static boolean isSyncSymbol(Symbol symbol) {
        return descriptor.syncSymbolSet.contains(symbol);
    }

    private static void handleError() {
        System.err.println("Error while parsing [" +
                lastInputCharacter.getIdSymbol() + "] " + lastInputCharacter.getText() +
                " in line " + lastInputCharacter.getLine() + " at index " + characterInLineIndex);
        //output expected characters on stderr
        Map<Symbol, PDAAction> stateActions = descriptor.actionTable.getOrDefault(getTopState(), Collections.emptyMap());
        System.err.println("Expected one of the following: ");
        for (Map.Entry<Symbol, PDAAction> e : stateActions.entrySet()) {
            if (e.getKey().isTerminal()) {
                if (e.getValue().getActionType() != ActionType.REJECT) {
                    System.err.println(e.getKey().toString());
                }
            }
        }
        //Skip input until a sync character appears
        while (!isSyncSymbol(lastInputCharacter.getIdSymbol())) {
            System.err.println("Skipping [" +
                    lastInputCharacter.getIdSymbol() + "] " + lastInputCharacter.getText() +
                    " in line " + lastInputCharacter.getLine() + " at index " + characterInLineIndex);
            loadInputCharacter();
            inputTape.step();
        }
        System.err.println("Found sync character [" +
                lastInputCharacter.getIdSymbol() + "] " + lastInputCharacter.getText() +
                " in line " + lastInputCharacter.getLine() + " at index " + characterInLineIndex);
        //pop elements from stack until an action is defined (different from reject or put)
        while (!actionIsDefined(getTopState(), lastInputCharacter.getIdSymbol())) {
            pdaStack.pop();
        }
    }

    private static boolean actionIsDefined(String pdaState, Symbol symbol) {
        return descriptor.actionTable.getOrDefault(pdaState, Collections.emptyMap()).getOrDefault(symbol, new PDAAction(ActionType.REJECT)).getActionType() != ActionType.REJECT;
    }

    private static void performReductionRule(GrammarRule rule) {
        //remove right side from pdastack
        ArrayList<TreeNode> children = new ArrayList<>();
        for (Symbol s : rule.getToList()) {
            if (s.isTerminal()) {
                children.add(terminalNodes.pop());
            } else {
                children.add(nonTerminalNodes.pop());
            }

            pdaStack.pop();
        }

        Collections.reverse(children);

        //put this node on the non terminal stack
        NonTerminalNode thisNode = new NonTerminalNode(rule.getFrom(), children);
        nonTerminalNodes.push(thisNode);
    }

    private static void actionReduce(GrammarRule rule) {
        performReductionRule(rule);

        //get next state based on left side and state on top of stack
        //this is the PUT action
        String nextState = Integer.toString(getActionFromDescriptor(getTopState(), rule.getFrom()).getNumber());
        //push left side along with nextState
        pdaStack.push(new PDAStackItem(nextState, rule.getFrom()));
    }

}
