public class Naredba extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        if (currentRightSideIndex == 0) {
            ++currentRightSideIndex;

            return rightSide.get(0);
        }

        FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();

        writer.add("", "MOVE R5, R7", "remove expression leftovers");

        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.NAREDBA;
    }

    @Override
    public void determineRightSideType() {
        switch (rightSide.get(0).getName()) {
            case LeftSideNames.SLOZENA_NAREDBA:
                rightSideType = 0;
                break;
            case LeftSideNames.IZRAZ_NAREDBA:
                rightSideType = 1;
                break;
            case LeftSideNames.NAREDBA_GRANANJA:
                rightSideType = 2;
                break;
            case LeftSideNames.NAREDBA_PETLJE:
                rightSideType = 3;
                break;
            case LeftSideNames.NAREDBA_SKOKA:
                rightSideType = 4;
                break;
            default:
                errorHappened();
        }
    }
}

