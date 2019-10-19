package analizator;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @author Matija
 *
 */
public class LADescriptor  {
	
	/**
	 * Starting state of the LA
	 */
	public String startingState;
	
	/**
	 * Mapping : LAState -> (ENfa -> Action)
	 */
	public Map<String, Map<ENfa, Action>> enfaActionMap;

}
