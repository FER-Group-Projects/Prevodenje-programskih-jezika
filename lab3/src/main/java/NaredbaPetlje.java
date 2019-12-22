public class NaredbaPetlje extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.NAREDBA_PETLJE;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

