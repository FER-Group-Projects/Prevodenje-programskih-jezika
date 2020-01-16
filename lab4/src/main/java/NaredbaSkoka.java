public class NaredbaSkoka extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

		FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();

		switch(rightSideType) {
        
        	case 0:
        		boolean foundLoop = false;
        		for(Node n=parent; n!=null; n=n.parent) {
        			if(n.getName().equals(LeftSideNames.NAREDBA_PETLJE)) {
        				foundLoop = true;
        				break;
        			}
        		}
        		
        		if(!foundLoop) {
        			errorHappened();
        		}
        		
        		return null;
        	case 1:
        		boolean foundVoidFunction = false;
        		for(Node n=parent; n!=null; n=n.parent) {
        			if(n.getName().equals(LeftSideNames.DEFINICIJA_FUNKCIJE) && n.rightSide.get(0).properties.getTip() == Type.VOID) {
        				foundVoidFunction = true;
        				break;
        			}
        		}
        		
        		if(!foundVoidFunction) {
        			errorHappened();
        		}

				for (int i = 4; i >= 0; i--) {
					writer.add("", "POP R" + i);
				}

				writer.add("", "RET", "void return");
        		
        		return null;
        	case 2:
        		if(currentRightSideIndex==0) {
        			currentRightSideIndex+=2;
        			return rightSide.get(1);
        		} else if(currentRightSideIndex==2) {
        			currentRightSideIndex++;
        			
        			Type functionType = null;
            		for(Node n=parent; n!=null; n=n.parent) {
            			if(n.getName().equals(LeftSideNames.DEFINICIJA_FUNKCIJE)) {
            				functionType = n.rightSide.get(0).properties.getTip();
            				break;
            			}
            		}
            		
            		if(functionType == null || !Checkers.checkImplicitCast(rightSide.get(1).properties.getTip(), functionType)) {
            			errorHappened();
            		}

            		writer.add("", "POP R6", "value return");

					for (int i = 4; i >= 0; i--) {
						writer.add("", "POP R" + i);
					}

					writer.add("", "RET");

            		return null;
        		}
				
				return null;
        	default:
        		errorHappened();
        }
        
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.NAREDBA_SKOKA;
    }

    @Override
    public void determineRightSideType() {
        String rightFirst = rightSide.get(0).getName();
        if(rightFirst.equals(Identifiers.KR_CONTINUE) || rightFirst.equals(Identifiers.KR_BREAK)) {
        	rightSideType = 0;
        } else if(rightFirst.equals(Identifiers.KR_RETURN) && rightSide.size()==2) {
        	rightSideType = 1;
        } else if(rightFirst.equals(Identifiers.KR_RETURN) && rightSide.size()==3) {
        	rightSideType = 2;
        } else {
        	errorHappened();
        }
    }
}

