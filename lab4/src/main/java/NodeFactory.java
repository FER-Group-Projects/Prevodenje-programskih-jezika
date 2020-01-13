public class NodeFactory {
    public static Node getNode(String input) {
        // UniformCharacter
        if (!input.startsWith("<")) {
            String[] parts = input.split(" ");

            String identifier = parts[0];
            int lineNumber = Integer.parseInt(parts[1]);
            String text = input.substring(parts[0].length() + parts[1].length() + 2);

            return new UniformCharacter(identifier, lineNumber, text);
        }

        switch (input) {
            case LeftSideNames.PRIMARNI_IZRAZ:
                return new PrimarniIzraz();
            case LeftSideNames.POSTFIKS_IZRAZ:
                return new PostfiksIzraz();
            case LeftSideNames.LISTA_ARGUMENATA:
                return new ListaArgumenata();
            case LeftSideNames.UNARNI_IZRAZ:
                return new UnarniIzraz();
            case LeftSideNames.UNARNI_OPERATOR:
                return new UnarniOperator();
            case LeftSideNames.CAST_IZRAZ:
                return new CastIzraz();
            case LeftSideNames.IME_TIPA:
                return new ImeTipa();
            case LeftSideNames.SPECIFIKATOR_TIPA:
                return new SpecifikatorTipa();
            case LeftSideNames.MULTIPLIKATIVNI_IZRAZ:
                return new MultiplikativniIzraz();
            case LeftSideNames.ADITIVNI_IZRAZ:
                return new AditivniIzraz();
            case LeftSideNames.ODNOSNI_IZRAZ:
                return new OdnosniIzraz();
            case LeftSideNames.JEDNAKOSNI_IZRAZ:
                return new JednakosniIzraz();
            case LeftSideNames.BIN_I_IZRAZ:
                return new BinIIzraz();
            case LeftSideNames.BIN_XILI_IZRAZ:
                return new BinXiliIzraz();
            case LeftSideNames.BIN_ILI_IZRAZ:
                return new BinIliIzraz();
            case LeftSideNames.LOG_I_IZRAZ:
                return new LogIIzraz();
            case LeftSideNames.LOG_ILI_IZRAZ:
                return new LogIliIzraz();
            case LeftSideNames.IZRAZ_PRIDRUZIVANJA:
                return new IzrazPridruzivanja();
            case LeftSideNames.IZRAZ:
                return new Izraz();
            case LeftSideNames.SLOZENA_NAREDBA:
                return new SlozenaNaredba();
            case LeftSideNames.LISTA_NAREDBI:
                return new ListaNaredbi();
            case LeftSideNames.NAREDBA:
                return new Naredba();
            case LeftSideNames.IZRAZ_NAREDBA:
                return new IzrazNaredba();
            case LeftSideNames.NAREDBA_GRANANJA:
                return new NaredbaGrananja();
            case LeftSideNames.NAREDBA_PETLJE:
                return new NaredbaPetlje();
            case LeftSideNames.NAREDBA_SKOKA:
                return new NaredbaSkoka();
            case LeftSideNames.PRIJEVODNA_JEDINICA:
                return new PrijevodnaJedinica();
            case LeftSideNames.VANJSKA_DEKLARACIJA:
                return new VanjskaDeklaracija();
            case LeftSideNames.DEFINICIJA_FUNKCIJE:
                return new DefinicijaFunkcije();
            case LeftSideNames.LISTA_PARAMETARA:
                return new ListaParametara();
            case LeftSideNames.DEKLARACIJA_PARAMETRA:
                return new DeklaracijaParametra();
            case LeftSideNames.LISTA_DEKLARACIJA:
                return new ListaDeklaracija();
            case LeftSideNames.DEKLARACIJA:
                return new Deklaracija();
            case LeftSideNames.LISTA_INIT_DEKLARATORA:
                return new ListaInitDeklaratora();
            case LeftSideNames.INIT_DEKLARATOR:
                return new InitDeklarator();
            case LeftSideNames.IZRAVNI_DEKLARATOR:
                return new IzravniDeklarator();
            case LeftSideNames.INICIJALIZATOR:
                return new Inicijalizator();
            case LeftSideNames.LISTA_IZRAZA_PRIDRUZIVANJA:
                return new ListaIzrazaPridruzivanja();
            default:
                //this should never happen
                return null;
        }
    }
}
