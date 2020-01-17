public class NaredbaGrananja extends Node {

	protected String endLabel;

	 @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();

        switch (rightSideType) {
	        case 0:
	        	if (currentRightSideIndex == 0) {
	        		endLabel = LabelMaker.getEndLabel();

	                currentRightSideIndex+=3;
	                return rightSide.get(2);
	            } else if (currentRightSideIndex == 3) {
	            	if (!Checkers.checkImplicitCast(rightSide.get(2).properties.getTip(), Type.INT)) {
                        errorHappened();
                    }

					writer.add("", "POP R0", "check condition if");
					writer.add("", "CMP R0, 0");
					writer.add("", "JP_Z " + endLabel + "_NO", "jump out");

	                currentRightSideIndex+=2;
	                return rightSide.get(4);
	            } else {
	        		writer.add(endLabel + "_NO", "CMP R0, R0", "condition was false");
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

					writer.add("", "POP R0", "check condition if else");
					writer.add("", "CMP R0, 0");
					writer.add("", "JP_Z " + endLabel + "_NO", "jump to else");

	                currentRightSideIndex+=2;
	                return rightSide.get(4);
	            } else if (currentRightSideIndex == 5) {
	        		writer.add("", "JP " + endLabel + "_END", "jump over else");
					writer.add(endLabel + "_NO", "CMP R0, R0", "else part");

					currentRightSideIndex+=2;
	                return rightSide.get(6);
	            } else {
					writer.add(endLabel + "_END", "CMP R0, R0", "end of if else");

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

