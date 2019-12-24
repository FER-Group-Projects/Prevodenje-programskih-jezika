public class PrijevodnaJedinica extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        
        switch (rightSideType) {
	        case 0:
	            if (currentRightSideIndex == 0) {
	                currentRightSideIndex++;
	                return rightSide.get(0);
	            } else {
	                return null;
	            }
	
	        case 1:
	            if (currentRightSideIndex == 0) {
	                currentRightSideIndex++;
	                return rightSide.get(0);
	            } else if (currentRightSideIndex == 1) {
	                currentRightSideIndex++;
	                return rightSide.get(1);
	            } else {
	                return null;
	            }
	
	        default:
	            errorHappened();
        }
        
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.PRIJEVODNA_JEDINICA;
    }

    @Override
    public void determineRightSideType() {
    	switch (rightSide.get(0).getName()) {
	        case LeftSideNames.VANJSKA_DEKLARACIJA:
	            rightSideType = 0;
	            break;
	        case LeftSideNames.PRIJEVODNA_JEDINICA:
	            rightSideType = 1;
	            break;
	        default:
	            errorHappened();
    	}
    }
}

