import analizator.ENfa;

import java.util.ArrayList;
import java.util.List;

public class RegexENfaUtil {

    //enumerate each state with a unique id during the construction process
    private static Integer numOfStates = 0;

    /**
     * Create a non-deterministic finite automata for given regular expression with given name
     *
     * @param regexName       Name of the automata to be created
     * @param regexDefinition Regular expression used to create the automata
     * @return NFA representing the regular expression
     */
    public static ENfa regexToENKA(String regexName, String regexDefinition) {
        numOfStates = 0;

        ENfa automata = new ENfa(regexName);

        StatePair result = translate(regexDefinition, automata);
        automata.setStartingState(result.leftState);
        automata.setStateAcceptability(result.rightState, true);

        return automata;
    }

    /**
     * Split the expression into subexpression with respect to the choice/union operator
     * But those with depth 1(skip those inside parenthesis)
     *
     * @param expression expression to be split
     * @return List of all choices that form the original expression as an union of subexpression,
     * empty if there is no choice/union operator
     */
    public static List<String> findSubExpressions(String expression) {
        List<String> choices = new ArrayList<>();
        int numParenthesis = 0;
        int lastIndex = 0;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(' && isOperator(expression, i)) {
                numParenthesis++;
            } else if (expression.charAt(i) == ')' && isOperator(expression, i)) {
                numParenthesis--;
            } else if (numParenthesis == 0 && expression.charAt(i) == '|' && isOperator(expression, i)) {
                //found a choice/union operator
                choices.add(expression.substring(lastIndex, i));//does not include the choice/union operator
                lastIndex = i + 1;//skip the choice/union operator
            }
        }
        if (choices.size() > 0) {
            choices.add(expression.substring(lastIndex));
        }
        return choices;
    }

    /**
     * Translate the expression recursively to a single automata
     *
     * @param expression expression to be translated
     * @param automata   automata which is being constructed
     * @return StatePair which contains start and acceptable state of the final automata
     */
    private static StatePair translate(String expression, ENfa automata) {
        List<String> choices = findSubExpressions(expression);

        String leftState = newState(automata);
        String rightState = newState(automata);
        if (choices.size() > 0) { //there was at least one choice/union operator
            for (int i = 0; i < choices.size(); i++) { //create union of all choices using epsilon transitions
                StatePair temp = translate(choices.get(i), automata);
                automata.addEpsilonTransition(leftState, temp.leftState);
                automata.addEpsilonTransition(temp.rightState, rightState);
            }
        } else {
            boolean isPrefixed = false;
            String lastState = leftState;
            for (int i = 0; i < expression.length(); i++) {
                String a = "", b = "";
                if (isPrefixed) {
                    //case 1
                    //current character is prefixed/escaped and is not an operator
                    isPrefixed = false;
                    char trigger;
                    if (expression.charAt(i) == 't') {
                        trigger = '\t';
                    } else if (expression.charAt(i) == 'n') {
                        trigger = '\n';
                    } else if (expression.charAt(i) == '_') {
                        trigger = ' ';
                    } else {
                        trigger = expression.charAt(i);
                    }

                    a = newState(automata);
                    b = newState(automata);
                    automata.addTransition(a, trigger, b);
                } else {
                    //case 2
                    //next character will be prefixed/escaped
                    if (expression.charAt(i) == '\\') {
                        isPrefixed = true;
                        continue;
                    }

                    if (expression.charAt(i) != '(') {
                        //case 2a
                        a = newState(automata);
                        b = newState(automata);
                        if (expression.charAt(i) == '$') {
                            automata.addEpsilonTransition(a, b);
                        } else {
                            automata.addTransition(a, expression.charAt(i), b);
                        }
                    } else {
                        //case 2b
                        int j = i + 1; //find the index of the respective ')'
                        int numberOfParenthesis = 1;
                        while (numberOfParenthesis != 0) {
                            if (expression.charAt(j) == ')' && isOperator(expression, j)) {
                                --numberOfParenthesis;
                            } else if (expression.charAt(j) == '(' && isOperator(expression, j)) {
                                ++numberOfParenthesis;
                            }

                            j++;
                        }
                        //index of '(' found
                        StatePair temp = translate(expression.substring(i + 1, j - 1), automata);
                        a = temp.leftState;
                        b = temp.rightState;
                        i = j - 1;
                    }
                }

                //check for repeating/Kleene star operator
                if (i + 1 < expression.length() && expression.charAt(i + 1) == '*') {
                    String x = a;
                    String y = b;
                    a = newState(automata);
                    b = newState(automata);

                    automata.addEpsilonTransition(a, x);
                    automata.addEpsilonTransition(y, b);
                    automata.addEpsilonTransition(a, b);
                    automata.addEpsilonTransition(y, x);

                    i = i + 1;
                }

                //concat with previous subexpression
                automata.addEpsilonTransition(lastState, a);
                lastState = b;
            }
            automata.addEpsilonTransition(lastState, rightState);
        }
        return new StatePair(leftState, rightState);
    }

    /**
     * Create a new state with unique id inside the automata
     *
     * @param automata
     * @return Id of new state
     */
    private static String newState(ENfa automata) {
        numOfStates++;
        String state = numOfStates.toString();
        automata.addState(state);
        return state;
    }

    /**
     * Check if character at index i is an operator or a regular character
     *
     * @param expression regular expression being translated
     * @param i          index of current character
     * @return true if character is operator, false otherwise
     */
    private static boolean isOperator(String expression, int i) {
        int br = 0;
        while (i - 1 >= 0 && expression.charAt(i - 1) == '\\') {
            br++;
            i--;
        }
        return br % 2 == 0;
    }

    /**
     * Used during the construction process to pair the left(start) and right(acceptable) state
     */
    private static class StatePair {
        String leftState;
        String rightState;

        public StatePair(String left, String right) {
            leftState = left;
            rightState = right;
        }
    }
}
