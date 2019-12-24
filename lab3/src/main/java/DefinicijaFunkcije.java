public class DefinicijaFunkcije extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch(rightSideType) {
        
        	case 0:
        		if(currentRightSideIndex==0) {
        			currentRightSideIndex++;
        			return rightSide.get(0);
        		} else if(currentRightSideIndex==1) {
        			currentRightSideIndex+=2;
        			
        			Type t = rightSide.get(0).properties.getTip();
        			if(t == Type.CONST_ARRAY_CHAR || t ==Type.CONST_ARRAY_INT || t ==Type.CONST_CHAR || t ==Type.CONST_INT) {
        				errorHappened();
        			}
        			
        			Function f = FunctionTable.getFunctionFromFunctionTable(rightSide.get(1).properties.getIme());
        			if(f!=null && f.isDefined())
        				errorHappened();
        			
        			// WIP: Jos provjeriti 4, 5 i 6
        			// TODO
        			
        			return rightSide.get(0);
        		}
        	case 1:
        		// TODO
        	default:
        		errorHappened();
        
        }
        
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.DEFINICIJA_FUNKCIJE;
    }

    @Override
    public void determineRightSideType() {
        switch(rightSide.get(3).getName()) {
        	case Identifiers.KR_VOID:
        		rightSideType = 0;
        		break;
        	case LeftSideNames.LISTA_PARAMETARA:
        		rightSideType = 1;
        		break;
        	default:
        		errorHappened();
        }
    }
}

