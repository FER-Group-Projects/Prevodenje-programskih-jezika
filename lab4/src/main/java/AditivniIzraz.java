public class AditivniIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else {
                    Properties rightSideCharProperties = rightSide.get(0).properties;

                    properties.setTip(rightSideCharProperties.getTip());
                    properties.setlIzraz(rightSideCharProperties.getlIzraz());
                }
                break;
            case 1: case 2:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;

                    if (!Checkers.checkImplicitCast(rightSide.get(0).properties.getTip(), Type.INT))
                        errorHappened();

                    return rightSide.get(2);
                } else {
                    if (!Checkers.checkImplicitCast(rightSide.get(2).properties.getTip(), Type.INT))
                        errorHappened();

                    properties.setTip(Type.INT);
                    properties.setlIzraz(0);
                }
                break;
        }

        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.ADITIVNI_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            rightSideType = 0;
        } else if (len == 3) {
            String idn = ((UniformCharacter) rightSide.get(1)).getIdentifier();
            switch (idn) {
                case Identifiers.PLUS:
                    rightSideType = 1;
                    break;
                case Identifiers.MINUS:
                    rightSideType = 2;
                    break;
            }
        }
    }
}

