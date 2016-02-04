package TestingProjects.problem1_3;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Runs a process with supplied command and arguments.
 *
 * @author Chandan R. Rupakheti (chandan.rupakheti@rose-hulman.edu)
 */
public abstract class ProcessRunner {
    protected final String command;
    protected volatile List<Process> processes;

    public ProcessRunner(String command) {
        this.processes = Collections.synchronizedList(new ArrayList<>());
        this.command = command;
    }

    public List<Process> getProcesses() {
        return Collections.unmodifiableList(this.processes);
    }

    public String getCommand() {
        return this.command;
    }

    /**
     * Executes the process.
     */
    public abstract void execute(Path p) throws IOException;
}
