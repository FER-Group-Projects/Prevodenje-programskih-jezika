public class MultiplikativniIzraz extends Node {

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
            case 1: case 2: case 3:
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
        String operation = "";

        String idn = ((UniformCharacter) rightSide.get(1)).getIdentifier();
        switch (idn) {
            case Identifiers.OP_PUTA:
                operation = "MUL";
                break;
            case Identifiers.OP_DIJELI:
                operation = "DIV";
                break;
            case Identifiers.OP_MOD:
                operation = "MOD";
                break;
        }

        writer.add("", "POP R0", idn);
        writer.add("", "POP R1");
        writer.add("", "CALL " + operation);
        writer.add("", "PUSH R6");
    }

    @Override
    public String toText() {
        return LeftSideNames.MULTIPLIKATIVNI_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            rightSideType = 0;
        } else if (len == 3) {
            String idn = ((UniformCharacter) rightSide.get(1)).getIdentifier();
            switch (idn) {
                case Identifiers.OP_PUTA:
                    rightSideType = 1;
                    break;
                case Identifiers.OP_DIJELI:
                    rightSideType = 2;
                    break;
                case Identifiers.OP_MOD:
                    rightSideType = 3;
                    break;
            }
        }
    }

}

