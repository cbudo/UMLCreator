package TestingProjects.problem1_3;

import java.util.Collection;

public interface IApplicationLauncher extends IDirectoryListener {
    Collection<ProcessRunner> getRunners();

    void addRunner(String extension, ProcessRunner runner);

    void shutDown();
}
