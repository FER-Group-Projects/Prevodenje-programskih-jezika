public class IzrazPridruzivanja extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.IZRAZ_PRIDRUZIVANJA;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

