package analizator;

import java.util.Objects;

public class PDAAction {

    private ActionType actionType;

    private String nextState;

    private int reductionRuleIndex;

    public PDAAction(ActionType actionType, String nextState, int reductionRuleIndex) {
        this.actionType = actionType;
        this.nextState = nextState;
        this.reductionRuleIndex = reductionRuleIndex;
    }

    public PDAAction(ActionType actionType, String nextState) {
        this(actionType, nextState, -1);
    }

    public PDAAction(ActionType actionType) {
        this(actionType, null, -1);
    }

    public PDAAction(ActionType actionType, int reductionRuleIndex) {
        this(actionType, null, reductionRuleIndex);
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getNextState() {
        return nextState;
    }

    public int getReductionRuleIndex() {
        return reductionRuleIndex;
    }

}
