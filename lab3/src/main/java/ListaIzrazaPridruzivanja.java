public class ListaIzrazaPridruzivanja extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.LISTA_IZRAZA_PRIDRUZIVANJA;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

