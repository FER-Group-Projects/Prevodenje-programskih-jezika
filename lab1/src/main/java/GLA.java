import analizator.Action;
import analizator.ENfa;
import analizator.LA;
import analizator.LADescriptor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GLA {

    private static LADescriptor laDescriptor = new LADescriptor();
    private static final String GO_BACK_ACTION_STR = "VRATI_SE";
    private static final String NEW_LINE_ACTION_STR = "NOVI_REDAK";
    private static final String ENTER_STATE_ACTION_STR = "UDJI_U_STANJE";

    public static void main(String[]args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<String> regexes = new ArrayList<>();

        String input = reader.readLine();
        while (input.startsWith("{")) {
            regexes.add(input);
            input = reader.readLine();
        }

        if (input.startsWith("%X")) {
            laDescriptor.startingState = input.split(" ")[1];

            input = reader.readLine();
        }

        if (input.startsWith("%L")) {
            input = reader.readLine();
        }


        List<LexRule> lexRules = new ArrayList<>();
        int ordNum = 0;
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

            lexRules.add(new LexRule(beginState, regexTransition, actions, ordNum));
            ordNum++;

            input = reader.readLine();

            if (input == null) break;
        }

        reader.close();


        /*
        for (String regex : regexes) {
            System.out.println(regex);
        }
        System.out.println("-----------------------------------------");
        for (LexRule lexRule : lexRules) {
            System.out.println(lexRule);
        }
        */


        List<RegexDefName> regexDefNameList = new ArrayList<>();
        for (String regex : regexes) {
            int endOfRegDef = regex.indexOf("}");
            regexDefNameList.add(new RegexDefName(regex.substring(0, endOfRegDef+1), regex.substring(endOfRegDef+2)));
        }

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
/*
        System.out.println("----------------------------------------");
        for (RegexDefName regexDefName : regexDefNameList) {
            System.out.println(regexDefName);
        }


        System.out.println("-----------------------------------------");
        */
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
            //System.out.println(lexRule);
        }
/*
        System.out.println("***********");
        System.out.println("laDescriptor.startingState = <" + laDescriptor.startingState + ">");
        System.out.println("***********");
*/

        laDescriptor.enfaActionMap = new HashMap<>();
        fillUpLADescriptor(lexRules);

        serializeObjectToFile();
    }

    private static void serializeObjectToFile() throws IOException{
        FileOutputStream fileOutputStream = new FileOutputStream(LA.PATH_TO_DESCRIPTOR);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(laDescriptor);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    private static void fillUpLADescriptor(List<LexRule> lexRules) {
        for (LexRule lexRule : lexRules) {
            String startStateForAction = lexRule.getBeginState();
            String regTransitionForAction = lexRule.getRegexTransition();
            List<String> actionsToDo = lexRule.getActions();
            int ordinalNumber = lexRule.getOrdinalNumber();

            // enfa name in format "e<ordinalNumber>" - example: "e0"
            ENfa enfa = RegexENfaUtil.regexToENKA("e"+ordinalNumber, regTransitionForAction);

            Action action = new Action();
            action.ordinalNumber = ordinalNumber;
            action.goBack = checkGoBack(actionsToDo);   // null if there is no "VRATI_SE" in action
            action.tokenType = checkTokenType(actionsToDo);    // null if there is "-" in action (meaning there is no token to be recognized)
            action.newLine = checkNewLine(actionsToDo);
            action.enterState = checkEnterState(actionsToDo);

            Map<ENfa, Action> enfaActionMapForState;
            if (!laDescriptor.enfaActionMap.containsKey(startStateForAction)) {
                enfaActionMapForState = new HashMap<>();
            } else {
                enfaActionMapForState = laDescriptor.enfaActionMap.get(startStateForAction);
            }

            enfaActionMapForState.put(enfa, action);
            laDescriptor.enfaActionMap.put(startStateForAction, enfaActionMapForState);
        }
    }

    private static String checkEnterState(List<String> actionsToDo) {
        for (String act : actionsToDo) {
            if (act.contains(ENTER_STATE_ACTION_STR)) {
                return act.split(" ")[1];
            }
        }
        return null;
    }

    private static boolean checkNewLine(List<String> actionsToDo) {
        for (String act : actionsToDo) {
            if (act.equals(NEW_LINE_ACTION_STR)) {
                return true;
            }
        }
        return false;
    }

    private static String checkTokenType(List<String> actionsToDo) {
        String actToDo = actionsToDo.get(0);
        if (actToDo.equals("-"))
            return null;
        else
            return actToDo;
    }

    private static Integer checkGoBack(List<String> actionsToDo) {
        for (String act : actionsToDo) {
            if (act.contains(GO_BACK_ACTION_STR)) {
                return Integer.parseInt(act.split(" ")[1]);
            }
        }
        return null;
    }

    private static class LexRule {

        private String beginState;
        private String regexTransition;
        private List<String> actions;
        private int ordinalNumber;

        LexRule(String beginState, String regexTransition, List<String> actions, int ordinalNumber) {
            this.beginState = beginState;
            this.regexTransition = regexTransition;
            this.actions = actions;
            this.ordinalNumber = ordinalNumber;
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

        public int getOrdinalNumber() {
            return ordinalNumber;
        }

        public void setOrdinalNumber(int ordinalNumber) {
            this.ordinalNumber = ordinalNumber;
        }

        @Override
        public String toString() {
            return "LexRule{" +
                    "beginState='" + beginState + '\'' +
                    ", regexTransition='" + regexTransition + '\'' +
                    ", actions=" + actions +
                    ", ordinalNumber=" + ordinalNumber +
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
