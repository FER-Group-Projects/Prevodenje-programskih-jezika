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
	
	public ENfa(String name) {
		this.name = Objects.requireNonNull(name);
		
		allStates = new HashSet<>();
		acceptableStates = new HashSet<>();
		currentActiveStates = new HashSet<>();
		transitions = new HashMap<>();
	}
	
	public ENfa(String name, String startingState, boolean startingStateAcceptability) {
		this(name);
		addState(startingState, startingStateAcceptability);
		setStartingState(startingState);
	}
	
	public String getName() {
		return name;
	}
	
	public void setStartingState(String name) {
		if(!allStates.contains(Objects.requireNonNull(name))) 
			throw new IllegalArgumentException("State with name " + name + " does not exists!");
		
		this.startingState = name;
		reset();
	}
	
	public void reset() {
		if(startingState==null) 
			throw new NullPointerException("Cannot reset the ENfa : Starting state is undefined!");
		
		currentActiveStates.clear();
		currentActiveStates.add(startingState);
	}
	
	public void addState(String name, boolean acceptable) {
		if(allStates.contains(name)) throw new IllegalArgumentException("State with name " + name + " already exists!");
		
		allStates.add(name);
		if(acceptable) acceptableStates.add(name);
	}
	
	public void removeState(String name) {
		allStates.remove(name);
		acceptableStates.remove(name);
		currentActiveStates.clear();
		transitions.remove(name);
		if(startingState.equals(name)) startingState = null;
	}
	
	public void setStateAcceptability(String stateName, boolean acceptable) {
		if(allStates.contains(stateName)) {
			if(acceptable) acceptableStates.add(stateName);
			else acceptableStates.remove(stateName);
			return;
		} 

		addState(stateName, acceptable);
	}
	
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
	
	public void removeTransition(String stateFrom, char trigger, String stateTo) {
		if(!allStates.contains(Objects.requireNonNull(stateFrom)) || !allStates.contains(Objects.requireNonNull(stateTo)))
			throw new IllegalArgumentException("state1 or state2 not in enfa");
		
		Map<Character, Set<String>> transitionMapping = transitions.get(stateFrom);
		if(transitionMapping==null) return;
		
		Set<String> nextStates = transitionMapping.get(trigger);
		if(nextStates==null) return;
		
		nextStates.remove(stateTo);
	}
	
	public Set<String> getAllStates() {
		return Collections.unmodifiableSet(allStates);
	}
	
	public Set<String> getAcceptableStates() {
		return Collections.unmodifiableSet(acceptableStates);
	}
	
	public String getStartingState() {
		return startingState;
	}
	
	public Set<String> getCurrentActiveStates() {
		return Collections.unmodifiableSet(currentActiveStates);
	}
	
	public Set<String> getTransition(String stateFrom, char trigger) {
		Map<Character, Set<String>> transitionMapping = transitions.get(stateFrom);
		if(transitionMapping==null) return Collections.emptySet();
		
		Set<String> nextStates = transitionMapping.get(trigger);
		if(nextStates==null) return Collections.emptySet();
		
		return Collections.unmodifiableSet(nextStates);
	}
	
	public boolean transitionExists(String stateFrom, char trigger) {
		return getTransition(stateFrom, trigger).size()!=0;
	}
	
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
	
	public void step(char trigger) {
		
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
