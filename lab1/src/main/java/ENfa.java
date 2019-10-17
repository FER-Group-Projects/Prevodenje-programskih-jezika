import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
	
	private String startingState;
	
	private Set<String> allStates;
	private Set<String> acceptableStates;
	
	private Set<String> currentActiveStates;
	
	// Mapping : stateFrom -> (character -> nextStates)
	private Map<String, Map<Character, Set<String>>> transitions;
	
	public ENfa() {
		allStates = new HashSet<>();
		acceptableStates = new HashSet<>();
		currentActiveStates = new HashSet<>();
		transitions = new HashMap<>();
	}
	
	public ENfa(String startingState, boolean startingStateAcceptance) {
		this();
		addState(startingState, startingStateAcceptance);
		setStartingState(startingState);
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
	
	//TODO
	//mijenjanje prihvatljivosti stanja
	//copy from
	//step
	//gettere za sve (ali immutable)
	//postoji li prijelaz

}
