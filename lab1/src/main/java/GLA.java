import org.w3c.dom.stylesheets.LinkStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GLA {


    public static void main(String[]args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<String> regexes = new ArrayList<>();

        String input = reader.readLine();
        while (input.startsWith("{")) {
            regexes.add(input);
            input = reader.readLine();
        }

        if (input.startsWith("%X")) {
            input = reader.readLine();
        }

        if (input.startsWith("%L")) {
            input = reader.readLine();
        }


        List<LexRule> lexRules = new ArrayList<>();
        while (input.startsWith("<")) {
            int indexOfRightBracket = input.indexOf('>');

            String beginState = input.substring(1, indexOfRightBracket);
            String regexTransition = input.substring(indexOfRightBracket+1);

            List<String> actions = new ArrayList<>();
            input = reader.readLine();
            input = reader.readLine();
            while (!input.startsWith("}")) {
                actions.add(input);
                input = reader.readLine();
            }

            lexRules.add(new LexRule(beginState, regexTransition, actions));
            input = reader.readLine();
        }

        reader.close();



        for (String regex : regexes) {
            System.out.println(regex);
        }
        System.out.println("-----------------------------------------");
        for (LexRule lexRule : lexRules) {
            System.out.println(lexRule);
        }



    }

    private static class LexRule {

        private String beginState;
        private String regexTransition;
        private List<String> actions;

        LexRule(String beginState, String regexTransition, List<String> actions) {
            this.beginState = beginState;
            this.regexTransition = regexTransition;
            this.actions = actions;
        }

        @Override
        public String toString() {
            return "LexRule{" +
                    "beginState='" + beginState + '\'' +
                    ", regexTransition='" + regexTransition + '\'' +
                    ", actions=" + actions +
                    '}';
        }
    }


}
