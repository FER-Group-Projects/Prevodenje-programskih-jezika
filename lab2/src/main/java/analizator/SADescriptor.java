package analizator;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

//serialize to /analizator/sa-descriptor
public class SADescriptor implements Serializable {

    private static final long serialVersionUID = -1261702926857192083L;

    /**
     * Sync characters used for error recovery
     */
    public Set<String> syncCharacterSet;

    /**
     * PDAState -> (Character -> PDAAction)
     * Characters are terminals
     * Actions are type of SHIFT(int nextState), REDUCE(int nextState), ACCEPT
     */
    public Map<Integer, Map<String, PDAAction>> ActionTable;

    /**
     * PDAState -> (Character -> NextState)
     * Characters are non-terminals
     * Actions in this table are only type of PUT(int nextState)
     */
    public Map<Integer, Map<String, Integer>> NextStateTable;
}
