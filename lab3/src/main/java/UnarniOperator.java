public class UnarniOperator extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        //TODO
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.UNARNI_OPERATOR;
    }

    @Override
    public void determineRightSideType() {
        //TODO
    }
}

