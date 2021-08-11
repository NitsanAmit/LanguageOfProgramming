import java.util.HashMap;
import java.util.Map;

public class IdentifierState {
    private Map<String, Integer> identifiersCount = new HashMap<>();
    private Map<Integer, Integer> identifiersLengthCount = new HashMap<>();

    public void addIdentifierUse(String identifier) {
        identifiersCount.merge(identifier, 1, Integer::sum);
        identifiersLengthCount.merge(identifier.length(), 1, Integer::sum);
    }

    public Map<String, Integer> getIdentifiersCount() {
        return identifiersCount;
    }

    public Map<Integer, Integer> getIdentifiersLengthCount() {
        return identifiersLengthCount;
    }
}
