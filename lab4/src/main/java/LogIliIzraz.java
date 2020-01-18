public class LogIliIzraz extends AbstractIzraz {

    private String endLabel;

    @Override
    protected void afterFirst() {
        FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();
        endLabel = LabelMaker.getEndLabel();

        writer.add("", "POP R0", "<log_ili_izraz> OP_ILI <log_i_izraz>");
        writer.add("", "MOVE 1, R1");
        writer.add("", "PUSH R1");
        writer.add("", "CMP R0, 0");
        writer.add("", "JP_NZ " + endLabel, "evaluate to 1 and jump over stuff");
        writer.add("", "POP R1");
        writer.add("", "PUSH R0", "otherwise continue");
    }

    @Override
    protected void afterLast() {
        FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();

        writer.add("", "POP R0");
        writer.add("", "POP R1");
        writer.add("", "OR R0, R1, R0");
        writer.add("", "PUSH R0");
        writer.add(endLabel, "CMP R0, R0", "no op, label used for jumping around");
    }

    @Override
    public String toText() {
        return LeftSideNames.LOG_ILI_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        switch (rightSide.get(0).getName()) {
            case LeftSideNames.LOG_I_IZRAZ:
                rightSideType = 0;
                break;
            case LeftSideNames.LOG_ILI_IZRAZ:
                rightSideType = 1;
                break;
            default:
                errorHappened();
        }
    }

}

