import java.util.List;

public class PostfiksIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();

        FRISCDocumentWriter writer = FRISCDocumentWriter.getFRISCDocumentWriter();

        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else {
                    Properties rightSideCharProperties = rightSide.get(0).properties;
                    properties.setTip(rightSideCharProperties.getTip());
                    properties.setlIzraz(rightSideCharProperties.getlIzraz());
                    properties.setTipovi(rightSideCharProperties.getTipovi());
                    properties.setPov(rightSideCharProperties.getPov());
                }
                break;
            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;
                    Type postfiksIzrazTip = rightSide.get(0).properties.getTip();
                    if (!(postfiksIzrazTip == Type.ARRAY_INT || postfiksIzrazTip == Type.CONST_ARRAY_INT || postfiksIzrazTip == Type.ARRAY_CHAR || postfiksIzrazTip == Type.CONST_ARRAY_CHAR))
                        errorHappened();
                    return rightSide.get(2);
                } else if (currentRightSideIndex == 2) {
                    currentRightSideIndex += 2;
                    Type izrazTip = rightSide.get(2).properties.getTip();
                    if (!Checkers.checkImplicitCast(izrazTip, Type.INT))
                        errorHappened();

                    Properties rightSideCharProperties = rightSide.get(0).properties;
                    Type postfiksIzrazTip = rightSideCharProperties.getTip();
                    Type typeToSet = null;
                    switch (postfiksIzrazTip) {
                        case ARRAY_INT:
                            typeToSet = Type.INT;
                            break;
                        case CONST_ARRAY_INT:
                            typeToSet = Type.CONST_INT;
                            break;
                        case ARRAY_CHAR:
                            typeToSet = Type.CHAR;
                            break;
                        case CONST_ARRAY_CHAR:
                            typeToSet = Type.CONST_CHAR;
                            break;
                    }
                    properties.setTip(typeToSet);
                    boolean notConstPrefixedTypeToSet = typeToSet != Type.CONST_CHAR && typeToSet != Type.CONST_INT;
                    properties.setlIzraz(notConstPrefixedTypeToSet ? 1 : 0);

                    writer.add("", "POP R0", "index");
                    writer.add("", "POP R1", "array");
                    writer.add("", "LOAD R1, (R1)");
                    writer.add("", "SHL R0, %D 2, R0");
                    writer.add("", "ADD R0, R1, R0");
                    writer.add("", "ADD R0, 4, R0");
                    writer.add("", "PUSH R0");
                }
                break;
            case 2:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    Properties rightSideCharProperties = rightSide.get(0).properties;

                    // check for postfiks_izraz: type = FUNCTION & no input params
                    if (!(rightSideCharProperties.getTip() == Type.FUNCTION && rightSideCharProperties.getTipovi().isEmpty()))
                        errorHappened();

                    properties.setTip(rightSideCharProperties.getPov());       // this is Type.FUNCTION
                    properties.setlIzraz(0);
                }

                String functionName = ((UniformCharacter) rightSide.get(0).rightSide.get(0).rightSide.get(0)).getText();

                writer.add("", "CALL " + LabelMaker.getFunctionLabel(functionName));
                writer.add("", "PUSH R6");
                break;
            case 3:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex += 2;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 2) {
                    currentRightSideIndex += 2;
                    return rightSide.get(2);
                } else {

                    Properties listArgumentsProperties = rightSide.get(2).properties;
                    List<Type> argTypes = listArgumentsProperties.getTipovi();

                    Properties postfixExpressionProperties = rightSide.get(0).properties;
                    List<Type> params = postfixExpressionProperties.getTipovi();

                    // check input params for postfiks_izraz: implicit <lista_argumenata>.tipovi (argType) -> <postfiks_izraz>.tipovi (params)
                    if (argTypes.size() != params.size())
                        errorHappened();

                    for (int i=0; i < argTypes.size(); i++) {
                        if (!Checkers.checkImplicitCast(argTypes.get(i), params.get(i)))
                            errorHappened();

                    }

                    // check for postfiks_izraz: type = FUNCTION
                    if (!(postfixExpressionProperties.getTip() == Type.FUNCTION))
                        errorHappened();



                    properties.setTip(postfixExpressionProperties.getPov());    // this is Type.FUNCTION
                    properties.setlIzraz(0);

                    String functionName2 = ((UniformCharacter) rightSide.get(0).rightSide.get(0).rightSide.get(0)).getText();

                    writer.add("", "CALL " + LabelMaker.getFunctionLabel(functionName2));
                    writer.add("", "ADD R7, %D " + (params.size() * 4) + ", R7", "remove args");
                    writer.add("", "PUSH R6");

                }
                break;
            case 4: case 5:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex += 2;
                    return rightSide.get(0);
                } else {
                    Properties rightSideCharProperties = rightSide.get(0).properties;
                    if (!(rightSideCharProperties.getlIzraz() == 1 && Checkers.checkImplicitCast(rightSideCharProperties.getTip(), Type.INT)))
                        errorHappened();

                    properties.setTip(Type.INT);
                    properties.setlIzraz(0);

                    String idn = ((UniformCharacter) rightSide.get(1)).getIdentifier();

                    writer.add("", "POP R1", idn);
                    writer.add("", "LOAD R0, (R1)", "address to value");
                    writer.add("", "PUSH R0", "push before change");

                    if (idn.equals(Identifiers.OP_INC))
                        writer.add("", "ADD R0, 1, R0");
                    else if (idn.equals(Identifiers.OP_DEC))
                        writer.add("", "SUB R0, 1, R0");

                    writer.add("", "STORE R0, (R1)");
                }
                break;
        }
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.POSTFIKS_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            rightSideType = 0;
        } else if (len == 4) {
            String idn1 = ((UniformCharacter) rightSide.get(1)).getIdentifier();
            String idn2 = ((UniformCharacter) rightSide.get(3)).getIdentifier();
            if (idn1.equals(Identifiers.L_UGL_ZAGRADA) && idn2.equals(Identifiers.D_UGL_ZAGRADA))
                rightSideType = 1;
            else if (idn1.equals(Identifiers.L_ZAGRADA) && idn2.equals(Identifiers.D_ZAGRADA))
                rightSideType = 3;
        } else if (len == 3) {
            String idn1 = ((UniformCharacter) rightSide.get(1)).getIdentifier();
            String idn2 = ((UniformCharacter) rightSide.get(2)).getIdentifier();
            if (idn1.equals(Identifiers.L_ZAGRADA) && idn2.equals(Identifiers.D_ZAGRADA))
                rightSideType = 2;
        } else if (len == 2) {
            String idn = ((UniformCharacter) rightSide.get(1)).getIdentifier();
            if (idn.equals(Identifiers.OP_INC))
                rightSideType = 4;
            else if (idn.equals(Identifiers.OP_DEC))
                rightSideType = 5;
        }

    }
}

