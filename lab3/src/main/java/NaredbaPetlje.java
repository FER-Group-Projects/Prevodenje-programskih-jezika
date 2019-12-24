public class NaredbaPetlje extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
        
        	case 0 :
        		if(currentRightSideIndex == 0) {
        			currentRightSideIndex+=3;
        			
        			return rightSide.get(2);
        		} else if(currentRightSideIndex == 3) {
        			if(!Checkers.checkImplicitCast(rightSide.get(2).properties.getTip(), Type.INT))
        				errorHappened();
        			
        			currentRightSideIndex+=2;
        			
        			return rightSide.get(4);
        		} else {
        			return null;
        		}
        	case 1 : 
        		if(currentRightSideIndex == 0) {
        			currentRightSideIndex+=3;
        			
        			return rightSide.get(2);
        		} else if(currentRightSideIndex == 3) {        			
        			currentRightSideIndex+=1;
        			
        			return rightSide.get(3);
        		} else if(currentRightSideIndex == 4) {
        			if(!Checkers.checkImplicitCast(rightSide.get(3).properties.getTip(), Type.INT))
        				errorHappened();
        			
        			currentRightSideIndex+=2;
        			
        			return rightSide.get(5);
        		} else {
        			return null;
        		}
        	case 2 : 
        		if(currentRightSideIndex == 0) {
        			currentRightSideIndex+=3;
        			
        			return rightSide.get(2);
        		} else if(currentRightSideIndex == 3) {        			
        			currentRightSideIndex+=1;
        			
        			return rightSide.get(3);
        		} else if(currentRightSideIndex == 4) {
        			if(!Checkers.checkImplicitCast(rightSide.get(3).properties.getTip(), Type.INT))
        				errorHappened();
        			
        			currentRightSideIndex++;
        			
        			return rightSide.get(4);
        		} else if(currentRightSideIndex == 5) {        			
        			currentRightSideIndex+=2;
        			
        			return rightSide.get(6);
        		} else {
        			return null;
        		}
        	default : errorHappened();
        
        }
        
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.NAREDBA_PETLJE;
    }

    @Override
    public void determineRightSideType() {
        switch(rightSide.get(0).getName()) {
        	case Identifiers.KR_WHILE :
        		rightSideType = 0;
        		break;
        	case Identifiers.KR_FOR :
        		if(rightSide.size()==6) {
        			rightSideType = 1;
        		} else if(rightSide.size()== 7) {
        			rightSideType = 2;
        		} else {
        			errorHappened();
        		}
        		break;
        	default :
        		errorHappened();
        }
    }
}

