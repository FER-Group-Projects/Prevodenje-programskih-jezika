package analizator;

public class PDAAction {

    private ActionType actionType;
    
    // if actionType=PUT or actionType=SHIFT => number = state
    // else if actionType=REDUCE or actionType=ACCEPT => number = reductionRuleIndex
    // else no meaning
    private int number;

    public PDAAction(ActionType actionType, int number) {
        this.actionType = actionType;
        this.number = number;
    }


    public ActionType getActionType() {
        return actionType;
    }

    public int getNumber() {
		return number;
	}
    
    @Override
    public String toString() {
    	return actionType.toString() + number;
    }

}
