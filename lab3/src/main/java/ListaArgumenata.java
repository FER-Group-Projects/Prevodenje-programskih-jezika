import java.util.ArrayList;
import java.util.List;

public class ListaArgumenata extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else {
                    List<Type> types = new ArrayList<>();
                    types.add(rightSide.get(0).properties.getTip());
                    properties.setTipovi(types);
                }
                break;
            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex += 2;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 2){
                    currentRightSideIndex++;
                    return rightSide.get(2);
                } else {
                    List<Type> types = new ArrayList<>();
                    types.addAll(rightSide.get(0).properties.getTipovi());
                    types.add(rightSide.get(2).properties.getTip());
                    properties.setTipovi(types);
                }
                break;
        }
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.LISTA_ARGUMENATA;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            rightSideType = 0;
        } else if (len == 3) {
            String idn = ((UniformCharacter) rightSide.get(1)).getIdentifier();
            if (idn.equals(Identifiers.ZAREZ))
                rightSideType = 1;
        }
    }
}

