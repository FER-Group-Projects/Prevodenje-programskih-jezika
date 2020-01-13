public class SpecifikatorTipa extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //property analysis done in determineRightSideType()
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.SPECIFIKATOR_TIPA;
    }

    @Override
    public void determineRightSideType() {
        String idn = ((UniformCharacter) rightSide.get(0)).getIdentifier();
        switch (idn) {
            case Identifiers.KR_VOID:
                rightSideType = 0;
                properties.setTip(Type.VOID);
                break;
            case Identifiers.KR_CHAR:
                rightSideType = 1;
                properties.setTip(Type.CHAR);
                break;
            case Identifiers.KR_INT:
                rightSideType = 2;
                properties.setTip(Type.INT);
                break;
        }
    }
}

