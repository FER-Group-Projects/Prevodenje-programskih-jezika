public class Izraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

