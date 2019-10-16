import java.util.Map;
import java.util.Set;

public class ENKA {

	public ENKA(String name, State startingState) {

	}

	public boolean isAcceptable() {
		return false;

	}

	public void transition(String trigger) {

	}

	public Set<State> getCurrentStates() {
		return null;

	}

	public static class State {

		public State(String name, boolean isAcceptable) {

		}

		public State(String name, boolean isAcceptable, Map<String, Set<State>> transitions, Set<State> epsilonTransitions) {

		}

		public void addTransition(String trigger, State state) {

		}

		public void addEpsilonTransition(State state) {

		}

		public String getName() {
			return null;

		}

		Set<State> getTransition(String trigger) {
			return null;

		}

		Set<State> getEpsilonTransitions() {
			return null;

		}

		boolean isAcceptable() {
			return false;

		}

		void setAcceptable(boolean isAcceptable) {

		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return super.equals(obj);
		}

		@Override
		public String toString() {
			return super.toString();
		}

	}

}