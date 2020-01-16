

public class PrimarniIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {

                    UniformCharacter uniChar = (UniformCharacter) rightSide.get(0);

                    String idnName = uniChar.getText();

                    boolean foundIdnAsVariableOrFunction = false;

                    if (blockTable.containsFunctionByNameLocally(idnName)) {
                        Function function = blockTable.getFunctionByName(idnName);

                        if (function != null) {
                            foundIdnAsVariableOrFunction = true;

                            properties.setTip(Type.FUNCTION);
                            properties.setTipovi(function.getInputTypes());
                            properties.setPov(function.getReturnType());

                            properties.setlIzraz(0);
                        }
                    }

                    if (!foundIdnAsVariableOrFunction) {
                        try {
                            VariableTypeValueLExpression variableTypeValue = blockTable.getVariableTypeValueLExpression(((UniformCharacter) rightSide.get(0)).getText());

                            properties.setTip(variableTypeValue.getTip());
                            properties.setlIzraz(variableTypeValue.getlIzraz());

                            foundIdnAsVariableOrFunction = true;

                            writer.add("", "LOAD R0, (" + blockTable.getLocationOfVariable(idnName) + ")", idnName);
                            writer.add("", "PUSH R0");
                        } catch (NullPointerException ex) {
                        }
                    }

                    // didn't find variable with idnName, then try to find function with that name
                    if (!foundIdnAsVariableOrFunction) {
                        if (blockTable.containsFunctionByName(idnName)) {

                            Function function = blockTable.getFunctionByName(idnName);

                            if (function != null) {
                                foundIdnAsVariableOrFunction = true;

                                properties.setTip(Type.FUNCTION);
                                properties.setTipovi(function.getInputTypes());
                                properties.setPov(function.getReturnType());

                                properties.setlIzraz(0);
                            }
                        }
                    }


                    if (!foundIdnAsVariableOrFunction)
                        errorHappened();
                }
                break;
            case 1:
                String value = ((UniformCharacter) rightSide.get(0)).getText();

                if (currentRightSideIndex == 0) {
                    if (!Checkers.checkInt(value))
                        errorHappened();

                    properties.setTip(Type.INT);
                    properties.setlIzraz(0);
                }

                String label = writer.addConstant(Checkers.parseInt(value));
                writer.add("", "LOAD R0, (" + label + ")", value);
                writer.add("", "PUSH R0");

                break;
            case 2:
                if (currentRightSideIndex == 0) {
                    String character = ((UniformCharacter) rightSide.get(0)).getText();
                    character = character.substring(1, character.length() - 1);

                    if (!Checkers.checkCharacterConst(character))
                        errorHappened();

                    properties.setTip(Type.CHAR);
                    properties.setlIzraz(0);

                    String characterLabel = writer.addConstant(Checkers.parseCharacter(character));
                    writer.add("", "LOAD R0, (" + characterLabel + ")", character);
                    writer.add("", "PUSH R0");
                }
                break;
            case 3:
                if (currentRightSideIndex == 0) {
                    String string = ((UniformCharacter) rightSide.get(0)).getText();

                    if (!Checkers.checkCharacterArray(string.substring(1, string.length() - 1)))
                        errorHappened();

                    properties.setTip(Type.CONST_ARRAY_CHAR);
                    properties.setlIzraz(0);
                }
                break;
            case 4:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    if (!((UniformCharacter) rightSide.get(0)).getIdentifier().equals(Identifiers.L_ZAGRADA))
                        errorHappened();
                    return rightSide.get(1);
                } else {
                    if (!((UniformCharacter) rightSide.get(2)).getIdentifier().equals(Identifiers.D_ZAGRADA))
                        errorHappened();
                    properties.setTip(rightSide.get(1).properties.getTip());
                    properties.setlIzraz(rightSide.get(1).properties.getlIzraz());
                }
                break;
        }


        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.PRIMARNI_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            String idn = ((UniformCharacter) rightSide.get(0)).getIdentifier();
            switch (idn) {
                case Identifiers.IDN:
                    rightSideType = 0;
                    break;
                case Identifiers.BROJ:
                    rightSideType = 1;
                    break;
                case Identifiers.ZNAK:
                    rightSideType = 2;
                    break;
                case Identifiers.NIZ_ZNAKOVA:
                    rightSideType = 3;
                    break;
            }
        } else {
            rightSideType = 4;
        }
    }
}

