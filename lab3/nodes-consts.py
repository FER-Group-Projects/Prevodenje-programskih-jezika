prdac="""<primarni izraz>
<postfiks_izraz>
<lista_argumenata>
<unarni_izraz>
<unarni_operator>
<cast_izraz>
<ime_tipa>
<specifikator_tipa>
<multiplikativni_izraz>
<aditivni_izraz>
<odnosni_izraz>
<jednakosni_izraz>
<bin_i_izraz>
<bin_xili_izraz>
<bin_ili_izraz>
<log_i_izraz>
<log_ili_izraz>
<izraz_pridruzivanja>
<izraz>
<slozena_naredba>
<lista_naredbi>
<naredba>
<izraz_naredba>
<naredba_grananja>
<naredba_petlje>
<naredba_skoka>
<prijevodna_jedinica>
<vanjska_deklaracija>
<definicija_funkcije>
<lista_parametara>
<deklaracija_parametra>
<lista_deklaracija>
<deklaracija>
<lista_init_deklaratora>
<init_deklarator>
<izravni_deklarator>
<inicijalizator>
<lista_izraza_pridruzivanja>"""
prdac=prdac.split("\n")
for i in range(len(prdac)):
    temp=prdac[i][1:len(prdac[i])-1]
    prdac[i]="public static final String "+temp.upper()+" = \""+prdac[i]+"\";"
    
for i in prdac:
    print(i)
