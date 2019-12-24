public class DeklaracijaParametra extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;
                    if (rightSide.get(0).properties.getTip() == Type.VOID) {
                        errorHappened();
                    }
                    properties.setTip(rightSide.get(0).properties.getTip());
                    properties.setIme(((UniformCharacter) rightSide.get(1)).getText());
                } else {
                    errorHappened();
                }
                break;
            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;
                    if (rightSide.get(0).properties.getTip() == Type.VOID) {
                        errorHappened();
                    }
                    Type imeTipaTip = rightSide.get(0).properties.getTip();
                    if (imeTipaTip == Type.INT) {
                        properties.setTip(Type.ARRAY_INT);
                    } else if (imeTipaTip == Type.CHAR) {
                        properties.setTip(Type.ARRAY_CHAR);
                    } else if (imeTipaTip == Type.CONST_CHAR) {
                        properties.setTip(Type.CONST_ARRAY_CHAR);
                    } else if (imeTipaTip == Type.CONST_INT) {
                        properties.setTip(Type.CONST_ARRAY_INT);
                    } else {
                        errorHappened();
                    }
                    properties.setIme(((UniformCharacter) rightSide.get(1)).getText());
                } else {
                    errorHappened();
                }
                break;
        }

        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.DEKLARACIJA_PARAMETRA;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 2) {
            rightSideType = 0;
        } else if (len == 4) {
            rightSideType = 1;
        } else {
            errorHappened();
        }
    }
}

