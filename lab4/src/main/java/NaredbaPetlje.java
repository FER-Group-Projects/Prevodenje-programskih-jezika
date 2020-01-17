public class NaredbaPetlje extends Node {

	protected String endLabel;

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();

        switch (rightSideType) {
        
        	case 0 :
        		if(currentRightSideIndex == 0) {
        			currentRightSideIndex+=3;

        			endLabel = LabelMaker.getEndLabel();
        			writer.add(endLabel + "_COND", "CMP R0, R0", "condition for while loop");
        			
        			return rightSide.get(2);
        		} else if(currentRightSideIndex == 3) {
        			if(!Checkers.checkImplicitCast(rightSide.get(2).properties.getTip(), Type.INT))
        				errorHappened();
        			
        			currentRightSideIndex+=2;

					writer.add("", "POP R0", "check condition for while loop");
					writer.add("", "CMP R0, 0", "check condition for while loop");
					writer.add("", "JP_Z " + endLabel + "_END", "jump out");

        			return rightSide.get(4);
        		} else {
        			writer.add("", "JP " + endLabel + "_COND", "loop around");
					writer.add(endLabel + "_END", "CMP R0, R0", "loop exit");

					return null;
        		}
        	case 1 : 
        		if(currentRightSideIndex == 0) {
        			currentRightSideIndex+=3;


        			return rightSide.get(2);
        		} else if(currentRightSideIndex == 3) {        			
        			currentRightSideIndex+=1;

					endLabel = LabelMaker.getEndLabel();
					writer.add(endLabel + "_COND", "CMP R0, R0", "condition for while loop");
        			
        			return rightSide.get(3);
        		} else if(currentRightSideIndex == 4) {
        			if(!Checkers.checkImplicitCast(rightSide.get(3).properties.getTip(), Type.INT))
        				errorHappened();
        			
        			currentRightSideIndex+=2;

					writer.add("", "POP R0", "check condition for while loop");
					writer.add("", "CMP R0, 0", "check condition for while loop");
					writer.add("", "JP_Z " + endLabel + "_END", "jump out");
        			
        			return rightSide.get(5);
        		} else {
					writer.add("", "JP " + endLabel + "_COND", "loop around");
					writer.add(endLabel + "_END", "CMP R0, R0", "loop exit");

        			return null;
        		}
        	case 2 : 
        		if(currentRightSideIndex == 0) {
        			currentRightSideIndex+=3;
        			
        			return rightSide.get(2);
        		} else if(currentRightSideIndex == 3) {        			
        			currentRightSideIndex+=1;

					endLabel = LabelMaker.getEndLabel();
					writer.add(endLabel + "_COND", "CMP R0, R0", "condition for while loop");

        			return rightSide.get(3);
        		} else if(currentRightSideIndex == 4) {
        			if(!Checkers.checkImplicitCast(rightSide.get(3).properties.getTip(), Type.INT))
        				errorHappened();

					writer.add("", "POP R0", "check condition for while loop");
					writer.add("", "CMP R0, 0", "check condition for while loop");
					writer.add("", "JP_Z " + endLabel + "_END", "jump out");
					writer.add("", "JP " + endLabel + "_FIRST");
					writer.add(endLabel + "_NEXT", "CMP R0, R0", "handle ++i");

        			currentRightSideIndex++;
        			
        			return rightSide.get(4);
        		} else if(currentRightSideIndex == 5) {        			
        			currentRightSideIndex+=2;

        			writer.add("", "JP " + endLabel + "_COND");
					writer.add(endLabel + "_FIRST", "CMP R0, R0", "skip ++i first time");

					return rightSide.get(6);
        		} else {
					writer.add("", "JP " + endLabel + "_NEXT", "loop around");
					writer.add(endLabel + "_END", "CMP R0, R0", "loop exit");

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

