import java.util.ArrayList;
import java.util.List;

public class ListaIzrazaPridruzivanja extends Node {

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
                    properties.setBrElem(1);
                    List<Type> izrazTip = new ArrayList<>();
                    izrazTip.add(rightSide.get(0).properties.getTip());
                    properties.setTipovi(izrazTip);

                    properties.setVrijednost(((UniformCharacter) rightSide.get(0)).getText());
                }
                break;
            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;
                    return rightSide.get(2);
                } else if (currentRightSideIndex == 2) {
                    currentRightSideIndex++;
                    List<Type> tipovi = new ArrayList<>(rightSide.get(0).properties.getTipovi());
                    tipovi.add(rightSide.get(2).properties.getTip());
                    properties.setTipovi(tipovi);
                    properties.setBrElem(rightSide.get(0).properties.getBrElem() + 1);

                    properties.setVrijednost(((UniformCharacter) rightSide.get(0)).getText() + ((UniformCharacter) rightSide.get(1)).getIdentifier() + ((UniformCharacter) rightSide.get(2)).getText());
                }
                break;
        }

        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.LISTA_IZRAZA_PRIDRUZIVANJA;
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

