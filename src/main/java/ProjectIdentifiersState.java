import java.util.HashMap;
import java.util.Map;

public class ProjectIdentifiersState {
    Map<String, Integer> identifiersCount = new HashMap<>();
    Map<Integer, Integer> identifiersLengthCount = new HashMap<>();

    public Map<String, Integer> getIdentifiersCount() {
        return identifiersCount;
    }

    public Map<Integer, Integer> getIdentifiersLengthCount() {
        return identifiersLengthCount;
    }
}
