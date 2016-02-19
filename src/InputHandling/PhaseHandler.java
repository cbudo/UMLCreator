package InputHandling;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by efronbs on 2/18/2016.
 */
public class PhaseHandler {
    String[] phases;
    Map<String, Method> phasesMap;
    List<String> basePhases;

    public PhaseHandler(String[] phases) {
        this.phases = phases;
        this.phasesMap = new HashMap<String, Method>();
        basePhases = new ArrayList<String>() {{
            add("Class-Loading");
            add("Dot-Generation");
        }};
        setupPhases();
    }


    private void setupPhases() {
    }

    public void setupClassLoadingPhase(Method m) {
        phasesMap.put("Class-Loading", m);
    }

    public void setupDotGeneration() {

    }

    public void runTheTrap() throws InvocationTargetException, IllegalAccessException {
        for (Method m : phasesMap.values()) {
            m.invoke(null, null);
        }
    }

}
