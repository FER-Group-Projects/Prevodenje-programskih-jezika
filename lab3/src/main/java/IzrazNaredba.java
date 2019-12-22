public class IzrazNaredba extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.IZRAZ_NAREDBA;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

