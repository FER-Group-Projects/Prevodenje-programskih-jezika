public class ListaInitDeklaratora extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.LISTA_INIT_DEKLARATORA;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

