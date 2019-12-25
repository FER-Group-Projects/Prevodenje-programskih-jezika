import java.util.ArrayList;
import java.util.List;

public class IzravniDeklarator extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        String idnIme = ((UniformCharacter) rightSide.get(0)).getText();
        switch (rightSideType) {
            case 0:
                if (properties.getNtip() == Type.VOID) {
                    errorHappened();
                }
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
                if (blockTable.containsVariableInLocalBlock(idnIme)) {
                    errorHappened();
                }
                String brojString = ((UniformCharacter) rightSide.get(2)).getText();
                if (!Checkers.checkInt(brojString)) {
                    errorHappened();
                }
                int broj = Integer.parseInt(brojString);
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
                blockTable.addVariableToBlockTable(idnIme, properties.getTip(), "");
                break;
            case 2:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    List<Type> paramTypes = new ArrayList<>(); //empty -> void
                    functionProperties(idnIme, paramTypes);
                }
                break;
            case 3:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(2);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;

                    List<Type> paramTypes = new ArrayList<>(rightSide.get(2).properties.getTipovi());
                    functionProperties(idnIme, paramTypes);
                }
                break;
        }

        return null;
    }

    private void functionProperties(String idnIme, List<Type> paramTypes) {
        if (blockTable.containsFunctionByNameLocally(idnIme)) {
            if (!blockTable.containsFunctionLocally(idnIme, properties.getNtip(), paramTypes)) {
                errorHappened();
            }
        } else {
            blockTable.addFunctionToBlockTable(idnIme, properties.getNtip(), paramTypes);
        }
        properties.setTip(Type.FUNCTION);
        properties.setPov(properties.getNtip());
        properties.setTipovi(paramTypes);
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

