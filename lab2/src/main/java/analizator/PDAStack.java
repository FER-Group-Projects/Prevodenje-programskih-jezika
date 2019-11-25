package analizator;

import java.util.Stack;

public class PDAStack {
    private Stack<PDAStackItem> stack;

    public PDAStack() {
        stack = new Stack<>();
        //put special symbol for bottom of stack
        init();
    }

    private void init() {
        //TODO
    }
}
