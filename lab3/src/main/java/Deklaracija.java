public class Deklaracija extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        if (currentRightSideIndex == 0) {
            currentRightSideIndex++;
            return rightSide.get(0);
        } else if (currentRightSideIndex == 1) {
            rightSide.get(1).properties.setNtip(rightSide.get(0).properties.getTip());
            return rightSide.get(1);
        }

        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.DEKLARACIJA;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 3) {
            rightSideType = 0;
        } else {
            errorHappened();
        }
    }
}

