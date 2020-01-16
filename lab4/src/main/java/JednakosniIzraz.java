public class JednakosniIzraz extends AbstractIzraz {

    @Override
    protected void afterLast() {
        FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();
        String endLabel = LabelMaker.getEndLabel();

        if (((UniformCharacter) rightSide.get(1)).getIdentifier() == Identifiers.OP_EQ) {
            writer.add("", "POP R0", "OP_EQ");
            writer.add("", "POP R1");
            writer.add("", "CMP R0, R1");

            writer.add("", "JP_NZ " + endLabel + "_NEQ");
            writer.add(endLabel + "_EQ", "MOVE 1, R0");
            writer.add("", "JP " + endLabel);
            writer.add(endLabel + "_NEQ", "MOVE 0, R0");
            writer.add(endLabel, "PUSH R0");
        }
        else {
            writer.add("", "POP R0", "OP_NEQ");
            writer.add("", "POP R1");
            writer.add("", "CMP R0, R1");

            writer.add("", "JP_NZ " + endLabel + "_NEQ");
            writer.add(endLabel + "_EQ", "MOVE 0, R0");
            writer.add("", "JP " + endLabel);
            writer.add(endLabel + "_NEQ", "MOVE 1, R0");
            writer.add(endLabel, "PUSH R0");
        }
    }

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

