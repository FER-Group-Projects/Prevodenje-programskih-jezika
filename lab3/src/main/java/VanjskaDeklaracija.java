public class VanjskaDeklaracija extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        if(currentRightSideIndex==0) {
        	currentRightSideIndex++;
        	return rightSide.get(0);
        }
        
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.VANJSKA_DEKLARACIJA;
    }

    @Override
    public void determineRightSideType() {
    	if(rightSide.size()!=1 || rightSide.get(0).getName()!=LeftSideNames.DEFINICIJA_FUNKCIJE && rightSide.get(0).getName()!=LeftSideNames.DEKLARACIJA)
	        errorHappened();
    	rightSideType = 0;
    }
}

