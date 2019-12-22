public class DeklaracijaParametra extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.DEKLARACIJA_PARAMETRA;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

