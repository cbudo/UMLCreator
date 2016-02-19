package InputHandling;

import DataStorage.DataStore.IDataStorage;
import DataStorage.DataStore.ParsedDataStorage;
import Visitors.PatternVisitors.AbstractVisitorTemplate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by efronbs on 2/18/2016.
 */
public class PhaseHandler {
    private List<String> phases;
    private String[] phaseClasses;
    private Map<String, PhaseExecution> phasesToActions;

    public PhaseHandler(String[] phases, String[] phaseClasses, Map<String, PhaseExecution> phasesToActions) {
        this.phases = Arrays.asList(phases);
        this.phaseClasses = phaseClasses;
        this.phasesToActions = phasesToActions;
    }

    public void generateAllPhaseClasses() {
        for (String cpath : phaseClasses) {
            try {
                Constructor c = Class.forName(cpath).getConstructor(IDataStorage.class);
                Object o = c.newInstance(ParsedDataStorage.getInstance());
                AbstractVisitorTemplate avt = (AbstractVisitorTemplate) o;
                String phaseName = avt.getPhaseName();
                if (phases.contains(phaseName)) {
                    phasesToActions.put(phaseName, () -> {
                        avt.doTheStuff();
                    });
                    //phases.remove(phaseName);
                }

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
