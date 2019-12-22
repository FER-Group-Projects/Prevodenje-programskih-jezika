public class ListaDeklaracija extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.LISTA_DEKLARACIJA;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

