public class BinIliIzraz extends AbstractIzraz {

    @Override
    protected void afterLast() {
        FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();

        writer.add("", "POP R0");
        writer.add("", "POP R1");
        writer.add("", "OR R0, R1, R0");
        writer.add("", "PUSH R0");
    }

    @Override
    public String toText() {
        return LeftSideNames.BIN_ILI_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        switch (rightSide.get(0).getName()) {
            case LeftSideNames.BIN_XILI_IZRAZ:
                rightSideType = 0;
                break;
            case LeftSideNames.BIN_ILI_IZRAZ:
                rightSideType = 1;
                break;
            default:
                errorHappened();
        }
    }
}

