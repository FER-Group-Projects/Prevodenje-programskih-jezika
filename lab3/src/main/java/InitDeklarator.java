public class InitDeklarator extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.INIT_DEKLARATOR;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

