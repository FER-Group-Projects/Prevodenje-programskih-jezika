public abstract class AbstractIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;

                    return rightSide.get(0);
                }
                else {
                    properties.setTip(rightSide.get(0).properties.getTip());
                    properties.setlIzraz(rightSide.get(0).properties.getlIzraz());

                    return null;
                }

            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;

                    return rightSide.get(currentRightSideIndex - 1);
                }
                else if (currentRightSideIndex == 1) {
                    // Skip uniform character
                    currentRightSideIndex += 2;

                    if (!Checkers.checkImplicitCast(Type.INT, rightSide.get(0).properties.getTip())) {
                        errorHappened();
                    }

                    return rightSide.get(currentRightSideIndex - 1);
                }
                else {
                    if (!Checkers.checkImplicitCast(Type.INT, rightSide.get(2).properties.getTip())) {
                        errorHappened();
                    }

                    properties.setTip(Type.INT);
                    properties.setlIzraz(0);

                    return null;
                }

            default:
                errorHappened();
        }

        return null;
    }

}
