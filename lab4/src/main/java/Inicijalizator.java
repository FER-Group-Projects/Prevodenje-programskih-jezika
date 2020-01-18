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
                    if (generatesNizZnakova(rightSide.get(0))) {
                        int brelem = countNizZnakovaLength(getNizZnakova(rightSide.get(0)).getText()) + 1;
                        List<Type> tipovi = new ArrayList<>();
                        for (int i = 0; i < brelem; i++) {
                            tipovi.add(Type.CHAR);
                        }
                        properties.setTip(Type.CONST_ARRAY_CHAR);
                        properties.setBrElem(brelem);
                        properties.setTipovi(tipovi);

                        FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();

                        String rightSideConstCharArr = getNizZnakova(rightSide.get(0)).getText();
                        String stringLabel = writer.addConstant(Checkers.parseCharacterArray(rightSideConstCharArr));

                        writer.add("", "MOVE " + stringLabel + ", R0", rightSideConstCharArr);
                        writer.add("", "MOVE R0, R1", rightSideConstCharArr);

                        // push for each element of NIZ_ZNAKOVA (niz(const(char))) - - lIzraz = 1
                        for (int i=0; i < rightSideConstCharArr.length(); i++) {
                            writer.add("", "PUSH R1", "index");
                            writer.add("", "PUSH R0", "array");
                            writer.add("", "ADD R1, 4, R1", "");
                        }


                    } else {
                        Type izrazTip = rightSide.get(0).properties.getTip();
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
                    Type listaTip = lista.properties.getTipovi().get(0);
                    if (listaTip == Type.INT || listaTip == Type.CONST_INT) properties.setTip(Type.CONST_ARRAY_INT);
                    if (listaTip == Type.CHAR || listaTip == Type.CONST_CHAR) properties.setTip(Type.CONST_ARRAY_CHAR);
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

    private boolean generatesNizZnakova(Node node) {
        Node next = node;
        while (next.rightSide.size() == 1) {
            next = next.rightSide.get(0);
            if (next.rightSide.size() > 1) return false;
        }
        if (next instanceof UniformCharacter) {
            return ((UniformCharacter) next).getIdentifier().equals(Identifiers.NIZ_ZNAKOVA);
        }
        return false;
    }

    private UniformCharacter getNizZnakova(Node node) {
        Node next = node;
        while (next.rightSide.size() == 1) {
            next = next.rightSide.get(0);
        }
        return (UniformCharacter) next;
    }

    private int countNizZnakovaLength(String text) {
        int i = 1;
        int len = 0;
        while (i < text.length() - 1) {
            if (text.charAt(i) == '\\') {
                i += 2;
            } else {
                i += 1;
            }
            len++;
        }
        return len;
    }
}

