public class UnarniOperator extends Node {


    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //property analysis done in determineRightSideType()
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.UNARNI_OPERATOR;
    }

    @Override
    public void determineRightSideType() {
        String idn = ((UniformCharacter) rightSide.get(0)).getIdentifier();
        switch (idn) {
            case Identifiers.PLUS:
                rightSideType = 0;
                break;
            case Identifiers.MINUS:
                rightSideType = 1;
                break;
            case Identifiers.OP_TILDA:
                rightSideType = 2;
                break;
            case Identifiers.OP_NEG:
                rightSideType = 3;
                break;
        }
    }
}

