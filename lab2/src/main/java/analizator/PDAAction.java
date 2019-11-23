package analizator;

public class PDAAction {

    private ActionType type;

    private int nextState;

    public PDAAction(ActionType type, int nextState) {
        this.type = type;
        this.nextState = nextState;
    }

    public ActionType getType() {
        return type;
    }

    public int getNextState() {
        return nextState;
    }

}
