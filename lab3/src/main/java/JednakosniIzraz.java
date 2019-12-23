public class JednakosniIzraz extends AbstractIzraz {

    @Override
    public String toText() {
        return LeftSideNames.JEDNAKOSNI_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        switch (rightSide.get(0).getName()) {
            case LeftSideNames.ODNOSNI_IZRAZ:
                rightSideType = 0;
                break;
            case LeftSideNames.JEDNAKOSNI_IZRAZ:
                rightSideType = 1;
                break;
            default:
                errorHappened();
        }
    }

}

