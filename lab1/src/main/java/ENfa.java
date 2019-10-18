import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * 
 * This class models an epsilon non-deterministic final automata. <br>
 * <strong> State </strong> of the automata is an acronym for java.lang.String. <br>
 * <strong> Trigger </strong> of the automata is a character which triggers the automata to enter a new state. <br>
 * <strong> Epsilon trigger </strong> of the automata is represented by '$' sign. <br>
 * 
 * @author Matija
 *
 */
public class ENfa {
	
	private String name;
	
	private String startingState;
	
	private Set<String> allStates;
	private Set<String> acceptableStates;
	
	private Set<String> currentActiveStates;
	
	// Mapping : stateFrom -> (character -> nextStates)
	private Map<String, Map<Character, Set<String>>> transitions;
	
	/**
	 * Creates ENfa with given name.
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
	 * @param name name of the ENfa
	 * @param startingState ENfa's starting state
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
	 * @return the name of the ENfa
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the starting state of the ENfa. Given state name must be in
	 * ENfa's collection of states.
	 * @param name name of the starting state
	 * @throws NullPointerException if name is <code>null</code>
	 * @throws IllegalArgumentException if given state name is not a state in this ENfa
	 */
	public void setStartingState(String name) {
		if(!allStates.contains(Objects.requireNonNull(name))) 
			throw new IllegalArgumentException("State with name " + name + " does not exists!");
		
		this.startingState = name;
		reset();
	}
	
	/**
	 * Resets the ENfa (clears current state set)
	 * @throws NullPointerException if starting state is not defined!
	 */
	public void reset() {
		if(startingState==null) 
			throw new NullPointerException("Cannot reset the ENfa : Starting state is undefined!");
		
		currentActiveStates.clear();
		currentActiveStates.add(startingState);
	}
	
	/**
	 * Adds a new state to the ENfa's collection of states
	 * @param name name of the new state
	 * @param acceptable acceptability of the new state
	 * @throws NullPointerException if given name is <code>null</code>
	 * @throws IllegalArgumentException if a state with the same name already exists in this ENfa
	 */
	public void addState(String name, boolean acceptable) {
		if(allStates.contains(Objects.requireNonNull(name))) throw new IllegalArgumentException("State with name " + name + " already exists!");
		
		allStates.add(name);
		if(acceptable) acceptableStates.add(name);
	}
	
	/**
	 * Removes a state with the given name if one existed and resets ENfa. Does nothing otherwise.
	 * @param name name of a state to be removed
	 * @throws NullPointerException if given name is <code>null</code>
	 */
	public void removeState(String name) {
		if(!allStates.remove(Objects.requireNonNull(name)))
			return;
		acceptableStates.remove(name);
		currentActiveStates.clear();
		transitions.remove(name);
		
		for(Map<Character, Set<String>> transitionMapping : transitions.values()) {
			for(Set<String> nextStates : transitionMapping.values()) {
				nextStates.remove(name);
			}
		}
		
		if(startingState.equals(name)) startingState = null;
		else currentActiveStates.add(startingState);
	}
	
	/**
	 * If state with a given stateName exists, changes acceptability to acceptable
	 * parameter. Otherwise, creates a new state with the given name and acceptability.
	 * @param stateName name of the state whose acceptability is to be changed
	 * @param acceptable acceptability of the state to be set
	 * @return true if ENfa already contained the state, false otherwise
	 * @throws NullPointerException if stateName is <code>null</code>
	 */
	public boolean setStateAcceptability(String stateName, boolean acceptable) {
		if(allStates.contains(Objects.requireNonNull(stateName))) {
			if(acceptable) acceptableStates.add(stateName);
			else acceptableStates.remove(stateName);
			return true;
		} 

		addState(stateName, acceptable);
		return false;
	}
	
	/**
	 * Adds a transition from 'stateFrom' to 'stateTo' with trigger 'trigger'.
	 * @param stateFrom
	 * @param trigger
	 * @param stateTo
	 * @throws NullPointerException if stateFrom or stateTo are <code>null</code>
	 * @throws IllegalArgumentException if stateFrom or stateTo are not in ENfa
	 */
	public void addTransition(String stateFrom, char trigger, String stateTo) {
		if(!allStates.contains(Objects.requireNonNull(stateFrom)) || !allStates.contains(Objects.requireNonNull(stateTo)))
			throw new IllegalArgumentException("state1 or state2 not in enfa");
		
		Map<Character, Set<String>> transitionMapping = transitions.get(stateFrom);
		if(transitionMapping==null) {
			transitionMapping = new HashMap<>();
			transitions.put(stateFrom, transitionMapping);
		}
		Set<String> nextStates = transitionMapping.get(trigger);
		if(nextStates==null) {
			nextStates = new HashSet<>();
			transitionMapping.put(trigger, nextStates);
		}
		nextStates.add(stateTo);
	}
	
	/**
	 * Removes a transition from 'stateFrom' to 'stateTo' by 'trigger'
	 * @param stateFrom
	 * @param trigger
	 * @param stateTo
	 * @throws NullPointerException if stateFrom or stateTo are <code>null</code>
	 * @throws IllegalArgumentException if stateFrom or stateTo are not in ENfa
	 */
	public void removeTransition(String stateFrom, char trigger, String stateTo) {
		if(!allStates.contains(Objects.requireNonNull(stateFrom)) || !allStates.contains(Objects.requireNonNull(stateTo)))
			throw new IllegalArgumentException("state1 or state2 not in enfa");
		
		Map<Character, Set<String>> transitionMapping = transitions.get(stateFrom);
		if(transitionMapping==null) return;
		
		Set<String> nextStates = transitionMapping.get(trigger);
		if(nextStates==null) return;
		
		nextStates.remove(stateTo);
	}
	
	/**
	 * Returns an unmodifiable set of all states
	 * @return an unmodifiable set of all states
	 */
	public Set<String> getAllStates() {
		return Collections.unmodifiableSet(allStates);
	}
	
	/**
	 * Returns an unmodifiable set of all acceptable states
	 * @return an unmodifiable set of all acceptable states
	 */
	public Set<String> getAcceptableStates() {
		return Collections.unmodifiableSet(acceptableStates);
	}
	
	/**
	 * Returns starting state
	 * @return starting state
	 */
	public String getStartingState() {
		return startingState;
	}
	
	/**
	 * Returns an unmodifiable set of all current states
	 * @return an unmodifiable set of all current states
	 */
	public Set<String> getCurrentActiveStates() {
		return Collections.unmodifiableSet(currentActiveStates);
	}
	
	/**
	 * Returns an unmodifiable set of destination states for a transition
	 * defined by 'stateFrom' and 'trigger'
	 * @param stateFrom
	 * @param trigger
	 * @return an unmodifiable set of destination states
	 */
	public Set<String> getTransition(String stateFrom, char trigger) {
		Map<Character, Set<String>> transitionMapping = transitions.get(stateFrom);
		if(transitionMapping==null) return Collections.emptySet();
		
		Set<String> nextStates = transitionMapping.get(trigger);
		if(nextStates==null) return Collections.emptySet();
		
		return Collections.unmodifiableSet(nextStates);
	}
	
	/**
	 * True if transition from 'stateFrom' with given 'trigger' exists, false otherwise
	 * @param stateFrom
	 * @param trigger
	 * @return true if transition from 'stateFrom' with given 'trigger' exists, false otherwise
	 */
	public boolean transitionExists(String stateFrom, char trigger) {
		return getTransition(stateFrom, trigger).size()!=0;
	}
	
	/**
	 * Copies all states with their acceptability and all transitions from ENfa other to
	 * this ENfa.
	 * @param other ENfa from which to copy
	 * @throws IllegalAccessException if any state from other already exists in this ENfa
	 */
	public void copyFrom(ENfa other) {
		for(String otherState : other.allStates) {
			if(this.allStates.contains(otherState))
				throw new IllegalArgumentException("State " + otherState + " is already in enfa! No changes have been made");
		}
		
		allStates.addAll(other.allStates);
		acceptableStates.addAll(other.acceptableStates);
		for(Entry<String, Map<Character, Set<String>>> transitionMapValue : other.transitions.entrySet()) {
			transitions.put(transitionMapValue.getKey(), transitionMapValue.getValue());
		}
	}
	
	/**
	 * Triggers ENfa with the given trigger
	 * @param trigger trigger with which to trigger ENfa
	 */
	public void step(char trigger) {
		
	}
	
	/**
	 * Returns true if ENfa is in acceptable state (if any of current states are acceptable)
	 * @return true if ENfa is in acceptable state
	 */
	public boolean isInAcceptableState() {
		for(String state : currentActiveStates) {
			if(acceptableStates.contains(state)) return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(acceptableStates, allStates, currentActiveStates, startingState, transitions);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ENfa))
			return false;
		ENfa other = (ENfa) obj;
		return Objects.equals(acceptableStates, other.acceptableStates) && Objects.equals(allStates, other.allStates)
				&& Objects.equals(currentActiveStates, other.currentActiveStates)
				&& Objects.equals(startingState, other.startingState) && Objects.equals(transitions, other.transitions);
	}

}
