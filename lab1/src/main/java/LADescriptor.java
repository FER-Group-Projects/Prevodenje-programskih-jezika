import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @author Matija
 *
 */

public class LADescriptor implements Serializable {
	
	/**
	 * Serial version for compatibility check between sender and receiver 
	 */
	private static final long serialVersionUID = -1261702926857192082L;

	/**
	 * Starting state of the LA
	 */
	public String startingState;
	
	/**
	 * Mapping : LAState -> (ENfa -> Action)
	 */
	public Map<String, Map<ENfa, Action>> enfaActionMap;

}
