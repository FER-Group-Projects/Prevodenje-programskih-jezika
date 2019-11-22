package analizator;

import javafx.scene.effect.SepiaTone;

import javax.swing.plaf.nimbus.State;
import java.nio.file.FileSystemNotFoundException;
import java.rmi.activation.ActivationGroup_Stub;
import java.util.*;

public class Dfa {


    private String name;

    // maps Dfa starting state ("d0") to enfa starting states set
    private Map<String, Set<String>> startingState = new HashMap<>();

    private Map<String, Set<String>> states = new HashMap<>();
    private int stateNumber = 1;

    private Map<String, Map<Character, String>> transitions = new HashMap<>();


    public Dfa(ENfa enfa) {

        this.name = enfa.getName();

        Set<String> startingStateSet = enfa.getCurrentActiveStates();
        startingState.put("d0", startingStateSet);
        states.put("d0", startingStateSet);


        List<String> discoveredStates = new ArrayList<>();

        discoveredStates.add("d0");


        while (!discoveredStates.isEmpty()) {
            //
            // System.out.println("DISCOVERED: "+discoveredStates);


            Set<Character> charTriggersForStateSet = new HashSet<>();


            Set<String> enfaStatesInDfaState = new HashSet<>();
            for (String state : discoveredStates) {
                enfaStatesInDfaState.addAll(states.get(state));
            }

            for (String state : enfaStatesInDfaState) {
                //System.out.println("state "+state);
                Map<Character, Set<String>> transForState = enfa.getTransitionsForState(state);
                if (transForState == null) continue;
                Set<Character> chars = transForState.keySet();
                Set<Character> charTrans = new HashSet<>();

                for (Character c : chars) {
                    if (c != ENfa.EPSILON) {
                        charTrans.add(c);
                    }
                }

                charTriggersForStateSet.addAll(charTrans);
            }

            //if (charTriggersForStateSet.isEmpty()) break;


            List<String> newDiscStates = new ArrayList<>();
            //System.out.println();
            for (Character character : charTriggersForStateSet) {

                //System.out.println(character);




                for (String state : discoveredStates) {

                    Set<String> newEnfaStates = new HashSet<>();

                    //System.out.println("DISCstate="+state);
                    Set<String> enfaStateClosure = states.get(state);
                    for (String enfaStateFrom : enfaStateClosure) {
                       // System.out.println("      enfastate="+enfaStateFrom);
                        Set<String> newEnfaSearchEpsClos = enfa.getTransition(enfaStateFrom, character);
                        for (String newEnfaEpsClosState : newEnfaSearchEpsClos) {
                            newEnfaStates.addAll(enfa.epsilonClosure(newEnfaEpsClosState));
                        }
                    }


                    if (states.values().contains(newEnfaStates)) {
                        //System.out.println("2 states.values(): "+states.values());
                        for (String s :newEnfaStates) {
                            //System.out.println("-"+s);
                        }

                        Map<Character, String> transitionTriggerNewState = transitions.get(state);
                        if (transitionTriggerNewState == null)
                            transitionTriggerNewState = new HashMap<>();

                        String existingStateName = null;
                        for (Map.Entry<String,Set<String>> dfaEnfaMapping : states.entrySet()) {
                            if (dfaEnfaMapping.getValue().equals(newEnfaStates))
                                existingStateName = dfaEnfaMapping.getKey();
                        }

                        if (existingStateName != null) {
                            transitionTriggerNewState.put(character, existingStateName);
                            transitions.put(state, transitionTriggerNewState);
                        }
                        continue;
                    } else {
                        //System.out.println("1 states.values(): "+states.values());
                    }


                    if (newEnfaStates.isEmpty()) continue;

                    String newStateName = "d" + stateNumber;
                    states.put(newStateName, newEnfaStates);
                    stateNumber++;

                    newDiscStates.add(newStateName);

                    Map<Character, String> transitionTriggerNewState = transitions.get(state);
                    if (transitionTriggerNewState == null)
                        transitionTriggerNewState = new HashMap<>();

                    transitionTriggerNewState.put(character, newStateName);
                    transitions.put(state, transitionTriggerNewState);


                }

               // System.out.println("-------------");
            }


            discoveredStates.clear();
            discoveredStates.addAll(newDiscStates);
        }

        System.out.println("************");
        for (Map.Entry<String, Set<String>> dfaState : states.entrySet()) {
            System.out.println(dfaState.getKey() + " : " + dfaState.getValue());
        }

        System.out.println("++++++++++");
        for (Map.Entry<String, Map<Character, String>> dfaTransition : transitions.entrySet()) {
            String initState = dfaTransition.getKey();
            Map<Character,String> transitionValue = dfaTransition.getValue();
            for (Map.Entry<Character, String> transEntry : transitionValue.entrySet()) {
                System.out.println(initState + " -" + transEntry.getKey() + "-> " + transEntry.getValue());
            }
        }


    }

    /**
     *
     * @return states of Dfa
     */
    public Set<String> getStates() {
        return states.keySet();
    }

    /**
     * Returns all transitions for a state.
     *
     * NOTE: returns null if the state does not have any transitions.
     *
     * @param stateFrom state whose transitions are to be returned
     * @return map of all transitions for given state stateFrom (trigger -> state of Dfa)
     */
    public Map<Character, String> getTransitionsForState(String stateFrom) {
        return transitions.get(stateFrom);
    }

    /**
     * Returns specific transition for one state-trigger pair.
     *
     * NOTE: returns null
     * - if the state does not have any transitions
     * - if the state does not have transition for the given trigger
     *
     * @param stateFrom state whose specific transition is to be returned
     * @param trigger transition trigger character
     * @return state of Dfa after transition
     */
    public String getTransitionForStateByTrigger(String stateFrom, Character trigger) {
        Map<Character,String> allTransitionsFromState = getTransitionsForState(stateFrom);
        if (allTransitionsFromState == null)
            return null;

        return allTransitionsFromState.get(trigger);
    }


}