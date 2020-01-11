public class ListaInitDeklaratora extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    rightSide.get(0).properties.setNtip(properties.getNtip());
                    return rightSide.get(0);
                }
                break;
            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    rightSide.get(0).properties.setNtip(properties.getNtip());
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;
                    rightSide.get(2).properties.setNtip(properties.getNtip());
                    return rightSide.get(2);
                }
                break;
        }

        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.LISTA_INIT_DEKLARATORA;
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

