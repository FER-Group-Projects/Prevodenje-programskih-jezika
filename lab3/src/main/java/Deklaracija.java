public class Deklaracija extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.DEKLARACIJA;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

