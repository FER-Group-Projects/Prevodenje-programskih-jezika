public class UnarniIzraz extends Node {

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
                    return rightSide.get(1);
                } else {
                    Properties rightSideCharProperties = rightSide.get(1).properties;

                    if (!(rightSideCharProperties.getlIzraz() == 1 && Checkers.checkImplicitCast(rightSideCharProperties.getTip(), Type.INT)))
                        errorHappened();

                    properties.setTip(Type.INT);
                    properties.setlIzraz(0);
                }
                break;

            case 3:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else {
                    if (!Checkers.checkImplicitCast(rightSide.get(1).properties.getTip(), Type.INT))
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
        return LeftSideNames.UNARNI_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            rightSideType = 0;
        } else if (len == 2) {
            if (rightSide.get(0).getName() == LeftSideNames.UNARNI_OPERATOR) {
                rightSideType = 3;
            } else if (((UniformCharacter) rightSide.get(0)).getIdentifier().equals(Identifiers.OP_INC)) {
                rightSideType = 1;
            }else if (((UniformCharacter) rightSide.get(0)).getIdentifier().equals(Identifiers.OP_DEC)) {
                rightSideType = 2;
            }
        }
    }
}

