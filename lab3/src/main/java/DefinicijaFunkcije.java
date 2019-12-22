public class DefinicijaFunkcije extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.DEFINICIJA_FUNKCIJE;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

