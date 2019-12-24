import java.util.ArrayList;
import java.util.List;

public class ListaParametara extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    List<Type> tipList = new ArrayList<>();
                    List<String> imeList = new ArrayList<>();
                    tipList.add(rightSide.get(0).properties.getTip());
                    imeList.add(rightSide.get(0).properties.getIme());
                    properties.setTipovi(tipList);
                    properties.setImena(imeList);
                } else {
                    errorHappened();
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
                    List<Type> tipList = new ArrayList<>(rightSide.get(0).properties.getTipovi());
                    List<String> imeList = new ArrayList<>(rightSide.get(0).properties.getImena());
                    tipList.add(rightSide.get(2).properties.getTip());
                    imeList.add(rightSide.get(2).properties.getIme());
                    properties.setTipovi(tipList);
                    properties.setImena(imeList);
                } else {
                    errorHappened();
                }
                break;
        }

        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.LISTA_PARAMETARA;
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

