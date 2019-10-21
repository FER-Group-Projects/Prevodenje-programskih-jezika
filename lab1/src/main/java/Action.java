import java.io.Serializable;

/**
 * 
 * @author Matija
 *
 */
public class Action implements Serializable {
	
	/**
	 * Serial version for compatibility check between sender and receiver 
	 */
	private static final long serialVersionUID = -844643957610749481L;	
	
	public int ordinalNumber;
	public Integer goBack;
	public String tokenType;
	public boolean newLine;
	public String enterState;

}
