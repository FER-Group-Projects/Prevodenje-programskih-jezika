public class ImeTipa extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.IME_TIPA;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

