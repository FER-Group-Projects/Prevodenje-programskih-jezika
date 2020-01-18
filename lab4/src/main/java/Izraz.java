public class Izraz extends Node {

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

                    return rightSide.get(currentRightSideIndex - 1);
                }
                else {
                    properties.setTip(rightSide.get(2).properties.getTip());
                    properties.setlIzraz(0);

                    FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();

                    writer.add("", "POP R0", "<izraz> ZAREZ <izraz_pridruzivanja>");
                    writer.add("", "POP R0");
                    writer.add("", "PUSH R0");

                    return null;
                }

            default:
                errorHappened();
        }

        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        switch (rightSide.get(0).getName()) {
            case LeftSideNames.IZRAZ_PRIDRUZIVANJA:
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

