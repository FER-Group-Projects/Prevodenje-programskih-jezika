idn="""IDN BROJ ZNAK NIZ_ZNAKOVA KR_BREAK KR_CHAR KR_CONST KR_CONTINUE KR_ELSE KR_FOR KR_IF KR_INT KR_RETURN KR_VOID KR_WHILE PLUS OP_INC MINUS OP_DEC OP_PUTA OP_DIJELI OP_MOD OP_PRIDRUZI OP_LT OP_LTE OP_GT OP_GTE OP_EQ OP_NEQ OP_NEG OP_TILDA OP_I OP_ILI OP_BIN_I OP_BIN_ILI OP_BIN_XILI ZAREZ TOCKAZAREZ L_ZAGRADA D_ZAGRADA L_UGL_ZAGRADA D_UGL_ZAGRADA L_VIT_ZAGRADA D_VIT_ZAGRADA"""
idn=idn.split(" ")
prefiks="public static String "
rest=" = \""
rest2="\";"
for i in idn:
    print(prefiks+i+rest+i+rest2)
