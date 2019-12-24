public class IzravniDeklarator extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
            case 0:
                if (properties.getNtip() == Type.VOID) {
                    errorHappened();
                }
                String idnIme = ((UniformCharacter) rightSide.get(0)).getText();
                if (blockTable.containsVariableInLocalBlock(idnIme)) {
                    errorHappened();
                }
                blockTable.addVariableToBlockTable(idnIme, properties.getNtip(), "");
                properties.setTip(properties.getNtip());
                break;
            case 1:
                if (properties.getNtip() == Type.VOID) {
                    errorHappened();
                }
                String idnIme2 = ((UniformCharacter) rightSide.get(0)).getText();
                if (blockTable.containsVariableInLocalBlock(idnIme2)) {
                    errorHappened();
                }
                int broj = Integer.parseInt(((UniformCharacter) rightSide.get(2)).getText());
                if (broj < 1 || broj > 1024) {
                    errorHappened();
                }
                properties.setBrElem(broj);
                Type ntip = properties.getNtip();
                if (ntip == Type.INT) {
                    properties.setTip(Type.ARRAY_INT);
                } else if (ntip == Type.CHAR) {
                    properties.setTip(Type.ARRAY_CHAR);
                } else if (ntip == Type.CONST_INT) {
                    properties.setTip(Type.CONST_ARRAY_INT);
                } else if (ntip == Type.CONST_CHAR) {
                    properties.setTip(Type.CONST_ARRAY_CHAR);
                }
                blockTable.addVariableToBlockTable(idnIme2, properties.getTip(), "");
                break;
            case 2:

                break;
            case 3:
                break;
        }

        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.IZRAVNI_DEKLARATOR;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            rightSideType = 0;
        } else if (len == 4) {
            String elem3 = rightSide.get(2).getName();
            if (elem3.equals(Identifiers.BROJ)) {
                rightSideType = 1;
            } else if (elem3.equals((Identifiers.KR_VOID))) {
                rightSideType = 2;
            } else {
                rightSideType = 3;
            }
        } else {
            errorHappened();
        }
    }
}

