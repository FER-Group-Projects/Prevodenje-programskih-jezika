import java.util.Collections;
import java.util.List;

public class DefinicijaFunkcije extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

		FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();

		if (currentRightSideIndex == 0) {
			String functionName = ((UniformCharacter) rightSide.get(1)).getText();

			writer.add(LabelMaker.getFunctionLabel(functionName), "PUSH R0", functionName);

			for (int i = 1; i < 6; i++) {
				writer.add("", "PUSH R" + i);
			}

			writer.add("", "MOVE R7, R5");
		}

        switch(rightSideType) {
        	case 0:
        		if(currentRightSideIndex==0) {
        			currentRightSideIndex++;
        			return rightSide.get(0);
        		} else if(currentRightSideIndex==1) {
        			currentRightSideIndex+=5;
        			
        			// Provjera 2) jeli ti razlicit od const(T)
        			Type returnType = rightSide.get(0).properties.getTip();
        			if(returnType ==Type.CONST_CHAR || returnType ==Type.CONST_INT) {
        				errorHappened();
        			}
        			
        			// Provjera 3) postoji li vec prije definirana f-ja istog imena
        			String imeFje = ((UniformCharacter) rightSide.get(1)).getText();
        			Function f = blockTable.getFunctionByName(imeFje);
        			if(f!=null && f.isDefined())
        				errorHappened();
        			
        			// Provjera 4) postoji li vec prije deklarirana globalna varijabla istog imena
        			try {
        				blockTable.getVariableType(imeFje);
        				errorHappened();
        			} catch (NullPointerException ex) {
        				// Ignore
        			}
        			
        			// Provjera 4) potsoji li vec prije deklarirana funkcija istog imena (ako da, jesu li ista svojstva)
        			if(blockTable.containsFunctionByName(imeFje) && 
        					(f.getInputTypes().size()!=0 || f.getReturnType()!=returnType)) 
        				errorHappened();
        			
        			// Provjera 5) zabiljezi definiciju i deklaraciju funkcije
        			if(f!=null) {
        				if (FunctionTable.isDefinedFunction(f)) {
        					errorHappened();
						}

        				FunctionTable.setDefinedFunction(f);
        			} else {
        				Function newFun = new Function(imeFje, returnType, Collections.emptyList());
						parent.blockTable.addFunctionToBlockTable(imeFje, returnType, Collections.emptyList());
						FunctionTable.setDefinedFunction(newFun);
        			}
        			
        			return rightSide.get(5);
        		} else {
					return null;
        		}
        	case 1:
        		if(currentRightSideIndex==0) {
        			currentRightSideIndex++;
        			return rightSide.get(0);
        		} else if(currentRightSideIndex==1) {
        			currentRightSideIndex+=3;
        			
        			// Provjera 2) jeli ti razlicit od const(T)
        			Type returnType = rightSide.get(0).properties.getTip();
        			if(returnType ==Type.CONST_CHAR || returnType ==Type.CONST_INT) {
        				errorHappened();
        			}
        			
        			// Provjera 3) postoji li vec prije definirana f-ja istog imena
        			String imeFje = ((UniformCharacter) rightSide.get(1)).getText();
        			Function f = blockTable.getFunctionByName(imeFje);
        			if(f!=null && f.isDefined())
        				errorHappened();
        			
        			return rightSide.get(3);
        		} else if(currentRightSideIndex==4) {
        			currentRightSideIndex+=2;
        			
        			// Provjera 5) postoji li vec prije deklarirana globalna varijabla istog imena
        			Type returnType = rightSide.get(0).properties.getTip();
        			String imeFje = ((UniformCharacter) rightSide.get(1)).getText();
        			List<Type> inputTypes = rightSide.get(3).properties.getTipovi();
        			List<String> inputNames = rightSide.get(3).properties.getImena();
        			Function f = blockTable.getFunctionByName(imeFje);
        			try {
        				blockTable.getVariableType(imeFje);
        				errorHappened();
        			} catch (NullPointerException ex) {
        				// Ignore
        			}
        			
        			// Provjera 5) potsoji li vec prije deklarirana funkcija istog imena (ako da, jesu li ista svojstva)
        			if(blockTable.containsFunctionByName(imeFje) && 
        					(!f.getInputTypes().equals(inputTypes) || f.getReturnType()!=returnType)) 
        				errorHappened();
        			
        			// Provjera 6) zabiljezi definiciju i deklaraciju funkcije
        			if(f!=null) {
						if (FunctionTable.isDefinedFunction(f)) {
							errorHappened();
						}

						FunctionTable.setDefinedFunction(f);
        			} else {
        				Function newFun = new Function(imeFje, returnType, inputTypes);
        				parent.blockTable.addFunctionToBlockTable(imeFje, returnType, inputTypes);
						FunctionTable.setDefinedFunction(newFun);
					}

        			// 6 registers for context, 1 address for returning
        			int offset = 4 * 6 + 4 * inputTypes.size();
        			// Ugradnja parametara f-je u lokalni djelokrug
        			for(int i=0; i<inputTypes.size(); i++) {
						rightSide.get(5).blockTable.addVariableToBlockTable(inputNames.get(i), inputTypes.get(i), null, 4);
						writer.add("", "MOVE R7, R0", inputNames.get(0));
						writer.add("", "ADD R0, %D " + offset + ", R0");
						writer.add("", "LOAD R0, (R0)");
						writer.add("", "PUSH R0");
						writer.add("", "MOVE R7, R5");
						offset -= 4;
        			}
        			
        			return rightSide.get(5);
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

