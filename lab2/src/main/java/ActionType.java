public enum ActionType {
    ACCEPT("Accept")
    ,SHIFT("S")
    ,REDUCE("R")
    ,PUT("P")
    ,REJECT("Reject");
    
    private String name;
    
    private ActionType(String name) {
    	this.name = name;
    }
    
    public String getName() {
		return name;
	}
    
    @Override
    public String toString() {
    	return name;
    }
}
