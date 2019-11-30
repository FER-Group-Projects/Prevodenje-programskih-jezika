import java.io.Serializable;

public class PDAAction implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2598133677637491697L;

	private ActionType actionType;
    
    // if actionType=PUT or actionType=SHIFT => number = state
    // else if actionType=REDUCE or actionType=ACCEPT => number = reductionRuleIndex
    // else no meaning
    private int number;

    public PDAAction(ActionType actionType, int number) {
        this.actionType = actionType;
        this.number = number;
    }
    
    public PDAAction(ActionType actionType) {
        this(actionType, -1);
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
