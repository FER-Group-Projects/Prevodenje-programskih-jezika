import java.util.ArrayList;
import java.util.List;

public class Inicijalizator extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;
                    Type izrazTip = rightSide.get(0).properties.getTip();
                    if (izrazTip == Type.CONST_ARRAY_CHAR) {
                        int brelem = rightSide.get(0).properties.getBrElem();
                        List<Type> tipovi = new ArrayList<>();
                        for (int i = 0; i < brelem; i++) {
                            tipovi.add(Type.CHAR);
                        }
                        properties.setBrElem(brelem);
                        properties.setTipovi(tipovi);
                    } else {
                        properties.setTip(izrazTip);
                    }
                }
                break;
            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(1);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;
                    Node lista = rightSide.get(1);
                    properties.setBrElem(lista.properties.getBrElem());
                    properties.setTipovi(lista.properties.getTipovi());
                }
                break;
        }
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.INICIJALIZATOR;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            rightSideType = 0;
        } else if (len == 3) {
            rightSideType = 1;
        } else {
            errorHappened();
        }
    }
}

