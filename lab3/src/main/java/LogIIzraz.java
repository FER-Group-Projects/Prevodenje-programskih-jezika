public class LogIIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.LOG_I_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

