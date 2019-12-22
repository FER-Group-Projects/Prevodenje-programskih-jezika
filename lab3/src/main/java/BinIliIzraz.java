public class BinIliIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.BIN_ILI_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

