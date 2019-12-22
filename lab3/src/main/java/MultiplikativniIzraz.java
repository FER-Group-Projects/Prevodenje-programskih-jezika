public class MultiplikativniIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.MULTIPLIKATIVNI_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

