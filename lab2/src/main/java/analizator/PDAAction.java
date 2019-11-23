package analizator;

import java.util.Objects;

public class PDAAction {

    private ActionType actionType;

    private int nextState;

    private GrammarRule reductionRule;

    public PDAAction(ActionType actionType, int nextState, GrammarRule reductionRule) {
        this.actionType = actionType;
        this.nextState = nextState;
        this.reductionRule = reductionRule;

        if (!reductionRule.getFrom().isTerminal() && !(actionType == ActionType.REJECT || actionType == ActionType.PUT)) {
            throw new IllegalArgumentException("Non terminals must map to PUT or REJECT actions only");
        }
        if (reductionRule.getFrom().isTerminal() && actionType == ActionType.PUT) {
            throw new IllegalArgumentException("Terminals must not map to PUT actions");
        }
        if (actionType == ActionType.REDUCE) {
            Objects.requireNonNull(reductionRule, "REDUCE actions must have a reduction rule");
        }
    }

    public PDAAction(ActionType actionType, int nextState) {
        this(actionType, nextState, null);
    }

    public PDAAction(ActionType actionType) {
        this(actionType, 0, null);
    }

    public ActionType getActionType() {
        return actionType;
    }

    public int getNextState() {
        return nextState;
    }

    public GrammarRule getReductionRule() {
        return reductionRule;
    }

}
