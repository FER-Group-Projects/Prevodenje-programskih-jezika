public class SlozenaNaredba extends Node {

    public SlozenaNaredba() {
        this.blockTable = new BlockTable();
    }

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex += 3;

                    return rightSide.get(1);
                }
                else {
                    return null;
                }
            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex += 2;

                    return rightSide.get(1);
                }
                else if (currentRightSideIndex == 2) {
                    currentRightSideIndex += 2;

                    return rightSide.get(2);
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
        return LeftSideNames.SLOZENA_NAREDBA;
    }

    @Override
    public void determineRightSideType() {
        switch (rightSide.get(1).getName()) {
            case LeftSideNames.LISTA_NAREDBI:
                rightSideType = 0;
                break;
            case LeftSideNames.LISTA_DEKLARACIJA:
                rightSideType = 1;
                break;
            default:
                errorHappened();
        }
    }
}

