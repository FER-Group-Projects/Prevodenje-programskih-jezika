public class NaredbaGrananja extends Node {

	 @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        
        switch (rightSideType) {
	        case 0:
	        	if (currentRightSideIndex == 0) {
	                currentRightSideIndex+=3;
	                return rightSide.get(2);
	            } else if (currentRightSideIndex == 3) {
	            	if (!Checkers.checkImplicitCast(rightSide.get(2).properties.getTip(), Type.INT)) {
                        errorHappened();
                    }
	            	
	                currentRightSideIndex+=2;
	                return rightSide.get(4);
	            } else {
	                return null;
	            }
	
	        case 1:
	        	if (currentRightSideIndex == 0) {
	                currentRightSideIndex+=3;
	                return rightSide.get(2);
	            } else if (currentRightSideIndex == 3) {
	            	if (!Checkers.checkImplicitCast(rightSide.get(2).properties.getTip(), Type.INT)) {
                        errorHappened();
                    }
	            	
	                currentRightSideIndex+=2;
	                return rightSide.get(4);
	            } else if (currentRightSideIndex == 5) {	            	
	                currentRightSideIndex+=2;
	                return rightSide.get(6);
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
        return LeftSideNames.NAREDBA_GRANANJA;
    }

    @Override
    public void determineRightSideType() {
    	switch (rightSide.size()) {
	        case 5:
	            rightSideType = 0;
	            break;
	        case 7:
	            rightSideType = 1;
	            break;
	        default:
	            errorHappened();
    	}
    }
    
}

