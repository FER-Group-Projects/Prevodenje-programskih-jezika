public class InitDeklarator extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    rightSide.get(0).properties.setNtip(properties.getNtip());
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    Type tip = rightSide.get(0).properties.getTip();
                    if (tip == Type.CONST_INT || tip == Type.CONST_CHAR ||
                            tip == Type.CONST_ARRAY_INT || tip == Type.CONST_ARRAY_CHAR) {
                        errorHappened();
                    }
                }
                break;
            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    rightSide.get(0).properties.setNtip(properties.getNtip());
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;
                    return rightSide.get(2);
                } else if (currentRightSideIndex == 2) {
                    currentRightSideIndex++;
                    Type deklaratorTip = rightSide.get(0).properties.getTip();
                    Type inicijalizatorTip = rightSide.get(2).properties.getTip();
                    if (deklaratorTip == Type.INT || deklaratorTip == Type.CHAR ||
                            deklaratorTip == Type.CONST_CHAR || deklaratorTip == Type.CONST_INT) {

                        if (!Checkers.checkImplicitCast(inicijalizatorTip, deklaratorTip)) {
                            errorHappened();
                        }
                    } else if (deklaratorTip == Type.VOID) {
                        errorHappened();
                    } else {
                        if (rightSide.get(2).properties.getBrElem() > rightSide.get(0).properties.getBrElem()) {
                            errorHappened();
                        }
                        Type elementTip = null;

                        if (deklaratorTip == Type.ARRAY_CHAR) elementTip = Type.CHAR;
                        if (deklaratorTip == Type.CONST_ARRAY_CHAR) elementTip = Type.CHAR;
                        if (deklaratorTip == Type.ARRAY_INT) elementTip = Type.INT;
                        if (deklaratorTip == Type.CONST_ARRAY_INT) elementTip = Type.INT;

                        if(!(inicijalizatorTip==Type.CONST_ARRAY_CHAR || inicijalizatorTip==Type.CONST_ARRAY_INT)){
                            errorHappened();
                        }

                        for (Type t : rightSide.get(2).properties.getTipovi()) {
                            if (!Checkers.checkImplicitCast(t, elementTip)) {
                                errorHappened();
                            }
                        }
                    }
                }
                break;
        }

        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.INIT_DEKLARATOR;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            rightSideType = 0;
        } else if (len == 3) {
            rightSideType = 1;
        } else {
            errorHappened();
        }
    }
}

