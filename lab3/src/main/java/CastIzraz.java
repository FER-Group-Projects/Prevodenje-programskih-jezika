import java.util.List;

public class CastIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();
        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else {
                    Properties rightSideCharProperties = rightSide.get(0).properties;
                    properties.setTip(rightSideCharProperties.getTip());
                    properties.setlIzraz(rightSideCharProperties.getlIzraz());
                }
                break;
            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(1);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex += 2;

                    // rule number 2 (continues in "else") - provjeri(cast_izraz)
                    return rightSide.get(3);
                } else {
                    Type expressionType = rightSide.get(3).properties.getTip();     // expression to be cast using (ime_tipa)
                    Type castType = rightSide.get(1).properties.getTip();   // <ime_tipa>.tip - <ime_tipa> is cast type in ()

                    // rule number 2 - the other check
                    //  document page 55: "Nadalje, vrijednost kojoj se tip pokusava promijeniti moze biti bilo kojeg tipa, pa se u tocki 2
                    //  mora provjeriti da se radi o brojevnom tipu, u skladu s pravilima iz poglavlja 4.3.1."
                    // see also: document page 56 at the top - "Ispis 4.22: Ispravne primjene cast-operatora."
                    if (!(expressionType == Type.INT || expressionType == Type.CONST_INT || expressionType == Type.CHAR || expressionType == Type.CONST_CHAR))
                        errorHappened();

                    // rule number 3
                    if (!Checkers.checkExplicitCast(expressionType, castType))
                        errorHappened();

                    properties.setTip(castType);
                    properties.setlIzraz(0);
                }
                break;
        }

        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.CAST_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            rightSideType = 0;
        } else if (len == 4){
            String idn1 = ((UniformCharacter) rightSide.get(0)).getIdentifier();
            String idn2 = ((UniformCharacter) rightSide.get(2)).getIdentifier();
            if (idn1.equals(Identifiers.L_ZAGRADA) && idn2.equals(Identifiers.D_ZAGRADA))
                rightSideType = 1;
        }
    }
}

