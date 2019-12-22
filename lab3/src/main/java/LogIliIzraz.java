public class LogIliIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.LOG_ILI_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

