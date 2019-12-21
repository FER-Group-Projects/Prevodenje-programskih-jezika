nodes="""<primarni_izraz>
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
nodes=nodes.split("\n")
for i in range(len(nodes)):
    temp=nodes[i][1:len(nodes[i])-1]
    nodes[i]="public static final String "+temp.upper()+" = \""+nodes[i]+"\";"
    
for i in nodes:
    print(i)
