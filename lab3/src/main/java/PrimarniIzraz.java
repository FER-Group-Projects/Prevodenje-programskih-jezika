

public class PrimarniIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        if (currentRightSideIndex > rightSide.size()) return null;

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    try {
                        blockTable.getVariableValue(((UniformCharacter) rightSide.get(0)).getText());
                    } catch (NullPointerException ex) {
                        errorHappened();
                    }
                } else {
                    Properties rightSideCharProperties = rightSide.get(0).properties;
                    properties.setTip(rightSideCharProperties.getTip());
                    properties.setlIzraz(rightSideCharProperties.getlIzraz());
                }
                break;
            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    if (!Checkers.checkInt(((UniformCharacter) rightSide.get(0)).getText()))
                        errorHappened();
                } else {
                    properties.setTip(Type.INT);
                    properties.setlIzraz(0);
                }
                break;
            case 2:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    if (!Checkers.checkCharacterConst(((UniformCharacter) rightSide.get(0)).getText()))
                        errorHappened();
                } else {
                    properties.setTip(Type.CHAR);
                    properties.setlIzraz(0);
                }
                break;
            case 3:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    if (!Checkers.checkCharacterArray(((UniformCharacter) rightSide.get(0)).getText()))
                        errorHappened();
                } else {
                    properties.setTip(Type.CONST_ARRAY_CHAR);
                    properties.setlIzraz(0);
                }
                break;
            case 4:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    //TODO je li potrebno?
                    if (!((UniformCharacter) rightSide.get(0)).getIdentifier().equals(Identifiers.L_ZAGRADA))
                        errorHappened();
                    return rightSide.get(currentRightSideIndex);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;
                } else if (currentRightSideIndex == 2) {
                    currentRightSideIndex++;
                    //TODO je li potrebno?
                    if (!((UniformCharacter) rightSide.get(2)).getIdentifier().equals(Identifiers.D_ZAGRADA))
                        errorHappened();
                } else {
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
                default:
                    errorHappened();
            }
        } else if (len == 3) {
            rightSideType = 4;
        } else {
            errorHappened();
        }
    }
}

