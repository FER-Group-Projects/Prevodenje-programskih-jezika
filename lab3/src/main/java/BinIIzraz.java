public class BinIIzraz extends AbstractIzraz {

    @Override
    public String toText() {
        return LeftSideNames.BIN_I_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        switch (rightSide.get(0).getName()) {
            case LeftSideNames.JEDNAKOSNI_IZRAZ:
                rightSideType = 0;
                break;
            case LeftSideNames.BIN_I_IZRAZ:
                rightSideType = 1;
                break;
            default:
                errorHappened();
        }
    }
}

