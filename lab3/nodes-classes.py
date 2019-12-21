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

classText=""" extends Node {

    @Override
    public Node analyze() {
        //TODO
        return null;
    }

    @Override
    public String toText() {
        //TODO
        return null;
    }
}

"""

import os

nodes=nodes.split("\n")
for i in range(len(nodes)):
    nodes[i]=nodes[i][1:len(nodes[i])-1]
    temp=nodes[i].split("_")
    temp2=""
    for t in temp:
        temp2 += t[0].upper()+t[1:]
    nodes[i]=temp2

if(os.path.isdir("nodes-classes")):
    os.rmdir("nodes-classes")
os.mkdir("nodes-classes")

for i in nodes:
    javaClass="public class "+i+classText
    #print(javaClass)
    with open("nodes-classes/"+i+".java", "a") as out:
        out.write(javaClass)

for i in nodes:
    print(i)
