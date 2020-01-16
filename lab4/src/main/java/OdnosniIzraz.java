public class OdnosniIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else {
                    Properties rightSideCharProperties = rightSide.get(0).properties;

                    properties.setTip(rightSideCharProperties.getTip());
                    properties.setlIzraz(rightSideCharProperties.getlIzraz());
                }
                break;
            case 1: case 2: case 3: case 4:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;

                    if (!Checkers.checkImplicitCast(rightSide.get(0).properties.getTip(), Type.INT))
                        errorHappened();

                    return rightSide.get(2);
                } else {
                    if (!Checkers.checkImplicitCast(rightSide.get(2).properties.getTip(), Type.INT))
                        errorHappened();

                    properties.setTip(Type.INT);
                    properties.setlIzraz(0);

                    afterLast();
                }
                break;
        }

        return null;
    }

    protected void afterLast() {
        FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();
        String endLabel = LabelMaker.getEndLabel();
        String condition = "";

        String idn = ((UniformCharacter) rightSide.get(1)).getIdentifier();
        switch (idn) {
            case Identifiers.OP_LT:
                condition = "_SLT";
                break;
            case Identifiers.OP_GT:
                condition = "_SGT";
                break;
            case Identifiers.OP_LTE:
                condition = "_SLE";
                break;
            case Identifiers.OP_GTE:
                condition = "_SGE";
                break;
        }

        writer.add("", "POP R0", idn);
        writer.add("", "POP R1");
        writer.add("", "CMP R0, R1");

        writer.add("", "JP" + condition + " " + endLabel + "_NO");
        writer.add(endLabel + "_NO", "MOVE 1, R0");
        writer.add("", "JP " + endLabel);
        writer.add(endLabel + "_YES", "MOVE 0, R0");
        writer.add(endLabel, "PUSH R0");
    }

    @Override
    public String toText() {
        return LeftSideNames.ODNOSNI_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            rightSideType = 0;
        } else if (len == 3) {
            String idn = ((UniformCharacter) rightSide.get(1)).getIdentifier();
            switch (idn) {
                case Identifiers.OP_LT:
                    rightSideType = 1;
                    break;
                case Identifiers.OP_GT:
                    rightSideType = 2;
                    break;
                case Identifiers.OP_LTE:
                    rightSideType = 3;
                    break;
                case Identifiers.OP_GTE:
                    rightSideType = 4;
                    break;
            }
        }
    }

}

