public class NodeFactory {
    public static Node getNode(String input){
        // UniformCharacter
        if (!input.startsWith("<")) {
            String[] parts = input.split(" ");

            String identifier = parts[0];
            int lineNumber = Integer.parseInt(parts[1]);
            String text = input.substring(parts[0].length() + parts[1].length() + 2);

            return new UniformCharacter(identifier, lineNumber, text);
        }

        switch(input){
            case "<primarni_izraz>":
                return new PrimarniIzraz();
            default:
                //this should never happen
                return null;
        }
    }
}
