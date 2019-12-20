public class NodeFactory {
    public static Node getNode(String input){
        switch(input){
            case "<primarni_izraz>":
                return new PrimarniIzraz();
            default:
                //this should never happen
                return null;
        }
    }
}
