

public class PrimarniIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {

                    VariableTypeValueLExpression variableTypeValue = null;

                    try {
                        variableTypeValue = blockTable.getVariableTypeValueLExpression(((UniformCharacter) rightSide.get(0)).getText());
                        properties.setTip(variableTypeValue.getTip());
                        properties.setlIzraz(variableTypeValue.getlIzraz());
                    } catch (NullPointerException ex) {
                        errorHappened();
                    }
                }
                break;
            case 1:
                if (currentRightSideIndex == 0) {
                    if (!Checkers.checkInt(((UniformCharacter) rightSide.get(0)).getText()))
                        errorHappened();

                    properties.setTip(Type.INT);
                    properties.setlIzraz(0);
                }
                break;
            case 2:
                if (currentRightSideIndex == 0) {
                    if (!Checkers.checkCharacterConst(((UniformCharacter) rightSide.get(0)).getText()))
                        errorHappened();

                    properties.setTip(Type.CHAR);
                    properties.setlIzraz(0);
                }
                break;
            case 3:
                if (currentRightSideIndex == 0) {
                    if (!Checkers.checkCharacterArray(((UniformCharacter) rightSide.get(0)).getText()))
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

