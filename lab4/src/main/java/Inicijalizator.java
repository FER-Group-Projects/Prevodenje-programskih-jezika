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


                    FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();

                    String arrayElementsStr = lista.properties.getVrijednost();

                    String arrLabel;
                    int[] intElements;

                    Type tip = properties.getTip();
                    if (tip == Type.ARRAY_INT || tip == Type.CONST_ARRAY_INT) {
                        String[] intStrElements = arrayElementsStr.split(Identifiers.ZAREZ);
                        intElements = new int[intStrElements.length];

                        for (int i=0; i < intElements.length; i++) {
                            intElements[i] = Integer.parseInt(intStrElements[i]);
                        }

                    } else { // tip == Type.ARRAY_CHAR || tip == Type.CONST_ARRAY_CHAR
                        int k = 0;
                        List<Integer> intElementsList = new ArrayList<>();
                        while (k < arrayElementsStr.length()) {

                            int firstIndexOfQuotationmark = arrayElementsStr.substring(k).indexOf("'");
                            int secondIndexOfQuotationmark = arrayElementsStr.substring(k+1).indexOf("'");

                            intElementsList.add(Integer.parseInt(arrayElementsStr.substring(firstIndexOfQuotationmark+1, secondIndexOfQuotationmark)));

                            k = secondIndexOfQuotationmark + 1;
                        }

                        intElements = new int[intElementsList.size()];
                        for (int ind=0; ind < intElements.length; ind++) {
                            intElements[ind] = intElementsList.get(ind);
                        }
                    }

                    arrLabel = writer.addConstant(intElements);


                    // R0 (address of array), R1 (address of current element in array)
                    // R2 (current element in array)

                    writer.add("", "MOVE " + arrLabel + ", R1" , arrayElementsStr);
                    writer.add("", "LOAD R2, (R1)", arrayElementsStr);


                    // push for each element of NIZ_ZNAKOVA (niz(const(char))) - lIzraz = 0
                    int arrLen = intElements.length;
                    for (int i=0; i < arrLen; i++) {
                        writer.add("", "PUSH R2", "currentElement");

                        if (i < arrLen-1) {
                            writer.add("", "ADD R1, 4, R1", "");
                            writer.add("", "LOAD R2, (R1)", arrayElementsStr);
                        }
                    }
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

