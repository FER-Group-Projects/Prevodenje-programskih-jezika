package analizator;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

/**
 * This class models an epsilon non-deterministic finite automata. <br>
 * <strong> State </strong> of the automata is an java.lang.String. <br>
 * <strong> Trigger </strong> of the automata is a character which triggers the transition to the next state. <br>
 * <strong> Epsilon trigger </strong> of the automata is represented by the '$' character. <br>
 * <strong> ENfa is stuck </strong> if ENfa is not in any state. ENfa gets stuck if it gets triggered by a
 * character for which there are no known transitions from current state of ENfa. When ENfa gets stuck, it
 * needs to be reset.
 *
 * @author Matija
 */
public class ENfa {

    /**
     * Character used for epsilon transitions.
     */
    public static final char EPSILON = 'ɛ';

    private String name;

    private String startingState;

    private Set<String> allStates;
    private Set<String> acceptableStates;

    private Set<String> currentActiveStates;

    // Mapping : stateFrom -> (character -> nextStates)
    private Map<String, Map<Character, Set<String>>> transitions;

    /**
     * Creates ENfa with the given name.
     *
     * @param name name of the ENfa
     * @throws NullPointerException if name is <code>null</code>
     */
    public ENfa(String name) {
        this.name = Objects.requireNonNull(name);

        allStates = new HashSet<>();
        acceptableStates = new HashSet<>();
        currentActiveStates = new HashSet<>();
        transitions = new HashMap<>();
    }

    /**
     * Crates ENfa with given name and given starting state.
     *
     * @param name                       name of the ENfa
     * @param startingState              ENfa's starting state
     * @param startingStateAcceptability ENfa's starting state acceptability
     * @throws NullPointerException if name or startingState are <code>null</code>
     */
    public ENfa(String name, String startingState, boolean startingStateAcceptability) {
        this(name);
        addState(startingState, startingStateAcceptability);
        setStartingState(startingState);
    }

    /**
     * Returns the name of the ENfa
     *
     * @return the name of the ENfa
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the starting state of the ENfa. Given state name must be in
     * ENfa's collection of states.
     *
     * @param name name of the starting state
     * @throws NullPointerException     if name is <code>null</code>
     * @throws IllegalArgumentException if given state name is not a state in this ENfa
     */
    public void setStartingState(String name) {
        if (!allStates.contains(Objects.requireNonNull(name)))
            throw new IllegalArgumentException("State with name " + name + " does not exist!");

        this.startingState = name;
        reset();
    }

    /**
     * Resets the ENfa (clears current state set)
     *
     * @throws NullPointerException if starting state is not defined!
     */
    public void reset() {
        if (startingState == null)
            throw new NullPointerException("Cannot reset the ENfa : Starting state is undefined!");

        currentActiveStates.clear();
        currentActiveStates.add(startingState);
        performEpsilonTransitions();
    }

    /**
     * Adds a new state to the ENfa's collection of states
     *
     * @param name       name of the new state
     * @param acceptable acceptability of the new state
     * @throws NullPointerException     if given name is <code>null</code>
     * @throws IllegalArgumentException if a state with the same name already exists in this ENfa
     */
    public void addState(String name, boolean acceptable) {
        if (allStates.contains(Objects.requireNonNull(name)))
            throw new IllegalArgumentException("State with name " + name + " already exists!");

        allStates.add(name);
        if (acceptable) acceptableStates.add(name);
    }

    /**
     * Removes state with the given name if it exists and resets the ENfa. Does nothing otherwise.
     *
     * @param name name of state to be removed
     * @throws NullPointerException if given name is <code>null</code>
     */
    public void removeState(String name) {
        if (!allStates.remove(Objects.requireNonNull(name)))
            return;
        acceptableStates.remove(name);
        currentActiveStates.clear();
        transitions.remove(name);

        for (Map<Character, Set<String>> transitionMapping : transitions.values()) {
            for (Set<String> nextStates : transitionMapping.values()) {
                nextStates.remove(name);
            }
        }

        if (startingState.equals(name)) {
            startingState = null;
        } else {
            currentActiveStates.add(startingState);
            performEpsilonTransitions();
        }
    }

    /**
     * If state with a given stateName exists, changes acceptability to acceptable
     * parameter. Otherwise, creates a new state with the given name and acceptability.
     *
     * @param stateName  name of the state whose acceptability is to be changed
     * @param acceptable acceptability of the state to be set
     * @return true if ENfa already contained the state, false otherwise
     * @throws NullPointerException if stateName is <code>null</code>
     */
    public boolean setStateAcceptability(String stateName, boolean acceptable) {
        if (allStates.contains(Objects.requireNonNull(stateName))) {
            if (acceptable) acceptableStates.add(stateName);
            else acceptableStates.remove(stateName);
            return true;
        }

        addState(stateName, acceptable);
        return false;
    }

    /**
     * Adds a transition from 'stateFrom' to 'stateTo' with trigger 'trigger'.
     *
     * @param stateFrom
     * @param trigger
     * @param stateTo
     * @throws NullPointerException     if stateFrom or stateTo are <code>null</code>
     * @throws IllegalArgumentException if stateFrom or stateTo are not in ENfa
     */
    public void addTransition(String stateFrom, char trigger, String stateTo) {
        if (!allStates.contains(Objects.requireNonNull(stateFrom)) || !allStates.contains(Objects.requireNonNull(stateTo)))
            throw new IllegalArgumentException("state1 or state2 not in enfa");

        Map<Character, Set<String>> transitionMapping = transitions.get(stateFrom);
        if (transitionMapping == null) {
            transitionMapping = new HashMap<>();
            transitions.put(stateFrom, transitionMapping);
        }
        Set<String> nextStates = transitionMapping.get(trigger);
        if (nextStates == null) {
            nextStates = new HashSet<>();
            transitionMapping.put(trigger, nextStates);
        }
        nextStates.add(stateTo);

        performEpsilonTransitions();
    }

    /**
     * Removes a transition from 'stateFrom' to 'stateTo' by 'trigger'
     *
     * @param stateFrom
     * @param trigger
     * @param stateTo
     * @throws NullPointerException     if stateFrom or stateTo are <code>null</code>
     * @throws IllegalArgumentException if stateFrom or stateTo are not in ENfa
     */
    public void removeTransition(String stateFrom, char trigger, String stateTo) {
        if (!allStates.contains(Objects.requireNonNull(stateFrom)) || !allStates.contains(Objects.requireNonNull(stateTo)))
            throw new IllegalArgumentException("state1 or state2 not in enfa");

        Map<Character, Set<String>> transitionMapping = transitions.get(stateFrom);
        if (transitionMapping == null) return;

        Set<String> nextStates = transitionMapping.get(trigger);
        if (nextStates == null) return;

        nextStates.remove(stateTo);

        performEpsilonTransitions();
    }

    /**
     * Returns an unmodifiable set of all states
     *
     * @return an unmodifiable set of all states
     */
    public Set<String> getAllStates() {
        return Collections.unmodifiableSet(allStates);
    }

    /**
     * Returns an unmodifiable set of all acceptable states
     *
     * @return an unmodifiable set of all acceptable states
     */
    public Set<String> getAcceptableStates() {
        return Collections.unmodifiableSet(acceptableStates);
    }

    /**
     * Returns starting state
     *
     * @return starting state
     */
    public String getStartingState() {
        return startingState;
    }

    /**
     * Returns an unmodifiable set of all current states
     *
     * @return an unmodifiable set of all current states
     */
    public Set<String> getCurrentActiveStates() {
        return Collections.unmodifiableSet(currentActiveStates);
    }

    /**
     * Returns an unmodifiable set of destination states for a transition
     * defined by 'stateFrom' and 'trigger'
     *
     * @param stateFrom
     * @param trigger
     * @return an unmodifiable set of destination states
     */
    public Set<String> getTransition(String stateFrom, char trigger) {
        Map<Character, Set<String>> transitionMapping = transitions.get(stateFrom);
        if (transitionMapping == null) return Collections.emptySet();

        Set<String> nextStates = transitionMapping.get(trigger);
        if (nextStates == null) return Collections.emptySet();

        return Collections.unmodifiableSet(nextStates);
    }

    /**
     * True if transition from 'stateFrom' with given 'trigger' exists, false otherwise
     *
     * @param stateFrom
     * @param trigger
     * @return true if transition from 'stateFrom' with given 'trigger' exists, false otherwise
     */
    public boolean transitionExists(String stateFrom, char trigger) {
        return getTransition(stateFrom, trigger).size() != 0;
    }

    /**
     * Copies all states with their acceptability and all transitions from ENfa other to
     * this ENfa. Does not modify the starting state.
     *
     * @param other ENfa from which to copy
     * @throws IllegalAccessException if any state from other already exists in this ENfa
     */
    public void copyFrom(ENfa other) {
        for (String otherState : other.allStates) {
            if (this.allStates.contains(otherState))
                throw new IllegalArgumentException("State " + otherState + " is already in enfa! No changes have been made");
        }

        allStates.addAll(other.allStates);
        acceptableStates.addAll(other.acceptableStates);
        for (Entry<String, Map<Character, Set<String>>> transitionMapValue : other.transitions.entrySet()) {
            transitions.put(transitionMapValue.getKey(), transitionMapValue.getValue());
        }

        if (startingState != null) reset();
    }

    /**
     * Triggers ENfa with the given trigger
     *
     * @param trigger trigger with which to trigger ENfa
     * @throws IllegalStateException if ENfa is stuck
     */
    public void step(char trigger) {

        if (trigger == EPSILON)
            throw new IllegalArgumentException("ENfa cannot be triggered using " + EPSILON + ". That character is used as epsilon trigger.");
        if (currentActiveStates.isEmpty())
            throw new IllegalStateException("ENfa is stuck. Check ENfa class javadoc for details.");

        //triggered transitions
        Set<String> nextStatesTriggered = new HashSet<>();
        for (String state : currentActiveStates) {
            if (transitions.get(state) == null || transitions.get(state).get(trigger) == null) continue;
            nextStatesTriggered.addAll(transitions.get(state).get(trigger));
        }
        currentActiveStates = nextStatesTriggered;

        //calculating epsilon closure for every current state
        performEpsilonTransitions();
    }

    private void performEpsilonTransitions() {
        Set<String> nextStatesEpsilon = new HashSet<>();
        for (String state : currentActiveStates) {
            nextStatesEpsilon.addAll(epsilonClosure(state));
        }
        currentActiveStates.addAll(nextStatesEpsilon);
    }

    /**
     * Returns the epsilon closure of a given state.
     *
     * @param state state for which to calculate epsilon closure
     * @return epsilon closure of given state
     * @throws NullPointerException     if given state is null
     * @throws IllegalArgumentException if this ENfa does not contain given state
     */
    public Set<String> epsilonClosure(String state) {
        Objects.requireNonNull(state);
        if (!allStates.contains(state))
            throw new IllegalArgumentException("Given state " + state + " is not a member of ENfa " + name);

        Set<String> epsilonClosure = new HashSet<>();
        Queue<String> next = new LinkedList<>();
        epsilonClosure.add(state);
        next.add(state);

        // BFS (Breadth first search)
        while (!next.isEmpty()) {
            String nextState = next.poll();
            if (transitions.get(nextState) == null ||
                    transitions.get(nextState).get(EPSILON) == null)
                continue;
            for (String s : transitions.get(nextState).get(EPSILON)) {
                if (epsilonClosure.contains(s)) continue;
                epsilonClosure.add(s);
                next.add(s);
            }
        }

        return epsilonClosure;
    }

    /**
     * Returns true if ENfa is in acceptable state (if any of current states are acceptable)
     *
     * @return true if ENfa is in acceptable state
     */
    public boolean isInAcceptableState() {
        for (String state : currentActiveStates) {
            if (acceptableStates.contains(state)) return true;
        }
        return false;
    }

    /**
     * Returns true if ENfa is stuck. See class javadoc for details.
     *
     * @return true if ENfa is stuck, false otherwise.
     */
    public boolean isStuck() {
        return currentActiveStates.isEmpty();
    }

    /**
     * Returns all transitions for a single state
     *
     * @param state
     * @return All transitions from state
     */
    public Map<Character, Set<String>> getTransitionsForState(String state) {
        return transitions.get(state);
    }

    /**
     * Returns the string representation of all states and transitions, respectively
     * One state or transition per line
     *
     * @return String representing all states and transitions of the automata
     */
    public String architectureToString() {
        String s = "";
        s += "States:\n";
        for (String state : allStates) {
            s += state + " " + acceptableStates.contains(state) + "\n";
        }
        s += "Transitions:\n";
        for (String stateKey : transitions.keySet()) {
            Map<Character, Set<String>> stateTransitions = transitions.get(stateKey);
            for (Character triggerKey : stateTransitions.keySet()) {
                Set<String> nextStates = stateTransitions.get(triggerKey);
                for (String next : nextStates) {
                    s += stateKey + " " + triggerKey + " " + next + "\n";
                }
            }
        }
        return s;
    }

    /**
     * Returns the string representation of the automata: all states, all transitions and current states, respectively
     * One state or transition per line
     *
     * @return String representing the automata along with its current states
     */
    @Override
    public String toString() {
        String s = architectureToString();
        Set<String> activeStates = getCurrentActiveStates();
        s += "Active:\n";
        for (String state : activeStates) {
            s += state + "\n";
        }
        return s;
    }
}
