public class LogIIzraz extends AbstractIzraz {

    @Override
    public String toText() {
        return LeftSideNames.LOG_I_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        switch (rightSide.get(0).getName()) {
            case LeftSideNames.BIN_ILI_IZRAZ:
                rightSideType = 0;
                break;
            case LeftSideNames.LOG_I_IZRAZ:
                rightSideType = 1;
                break;
            default:
                errorHappened();
        }
    }

}

