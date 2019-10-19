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


        List<RegexDefName> regexDefNameList = new ArrayList<>();
        for (String regex : regexes) {
            int endOfRegDef = regex.indexOf("}");
            regexDefNameList.add(new RegexDefName(regex.substring(0, endOfRegDef+1), regex.substring(endOfRegDef+2)));
        }

        //List<RegexDefName> regexListCopy = new ArrayList<>();
        //regexListCopy.addAll(regexDefNameList);

        int regListLen = regexDefNameList.size();
        for (int i = 0; i < regListLen; i++) {
            RegexDefName reg = regexDefNameList.get(i);
            String regName = reg.getRegName();
            String regDef = reg.getRegDef();

            for (int j = i+1; j < regListLen; j++) {
                RegexDefName regCmp = regexDefNameList.get(j);
                String regCmpDef = regCmp.getRegDef();

                if (regCmpDef.contains(regName)) {
                    regCmpDef = regCmpDef.replace(regName, "(" + regDef + ")");
                    regCmp.setRegDef(regCmpDef);
                }
            }
        }

        System.out.println("-----------------------------------------");
        for (RegexDefName regexDefName : regexDefNameList) {
            System.out.println(regexDefName);
        }


        System.out.println("-----------------------------------------");
        for (LexRule lexRule : lexRules) {
            String regTrans = lexRule.getRegexTransition();

            for (RegexDefName regexDefName : regexDefNameList) {
                String regNameToFind = regexDefName.getRegName();

                if (regTrans.contains(regNameToFind)) {
                    String regDefToReplaceWith = regexDefName.getRegDef();
                    regTrans = regTrans.replace(regNameToFind, "(" + regDefToReplaceWith + ")");
                }
            }

            lexRule.setRegexTransition(regTrans);
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

        public String getBeginState() {
            return beginState;
        }

        public void setBeginState(String beginState) {
            this.beginState = beginState;
        }

        public String getRegexTransition() {
            return regexTransition;
        }

        public void setRegexTransition(String regexTransition) {
            this.regexTransition = regexTransition;
        }

        public List<String> getActions() {
            return actions;
        }

        public void setActions(List<String> actions) {
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

    private static class RegexDefName {
        private String regDef;
        private String regName;

        RegexDefName(String regName, String regDef) {
            this.regName = regName;
            this.regDef = regDef;
        }

        public String getRegDef() {
            return regDef;
        }

        public void setRegDef(String regDef) {
            this.regDef = regDef;
        }

        public String getRegName() {
            return regName;
        }

        public void setRegName(String regName) {
            this.regName = regName;
        }

        @Override
        public String toString() {
            return "RegexDefName{" +
                    "regDef='" + regDef + '\'' +
                    ", regName='" + regName + '\'' +
                    '}';
        }
    }

}
