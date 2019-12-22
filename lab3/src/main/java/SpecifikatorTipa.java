public class SpecifikatorTipa extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.SPECIFIKATOR_TIPA;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

