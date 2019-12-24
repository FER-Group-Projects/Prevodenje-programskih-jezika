public class IzrazNaredba extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
            case 0:
                properties.setTip(Type.INT);

                return null;

            case 1:
                if (currentRightSideIndex == 0) {
                    ++currentRightSideIndex;

                    return rightSide.get(0);
                }
                else if (currentRightSideIndex == 1) {
                    ++currentRightSideIndex;

                    properties.setTip(rightSide.get(0).properties.getTip());

                    return null;
                }
                else {
                    return null;
                }

            default:
                errorHappened();
        }


        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.IZRAZ_NAREDBA;
    }

    @Override
    public void determineRightSideType() {
        switch (rightSide.get(0).getName()) {
            case Identifiers.TOCKAZAREZ:
                rightSideType = 0;
                break;
            case LeftSideNames.IZRAZ:
                rightSideType = 1;
                break;
            default:
                errorHappened();
        }
    }
}

