public class ImeTipa extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        if (currentRightSideIndex >= rightSide.size()) return null;
        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(currentRightSideIndex - 1);
                }
                break;
            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(currentRightSideIndex);
                } else {
                    if (rightSide.get(1).properties.getTip() == Type.VOID) {
                        errorHappened();
                    } 
                    if (rightSide.get(1).properties.getTip() == Type.INT) {
                        properties.setTip(Type.CONST_INT);
                    } else if (rightSide.get(1).properties.getTip() == Type.CHAR) {
                        properties.setTip(Type.CONST_CHAR);
                    }
                }
                break;
        }
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.IME_TIPA;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            rightSideType = 0;
        } else {
            rightSideType = 1;
        }
    }
}

