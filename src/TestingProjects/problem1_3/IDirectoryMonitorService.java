package TestingProjects.problem1_3;

import java.io.IOException;
import java.nio.file.Path;

/**
 * A {@link DirectoryMonitorService} will monitor a supplied directory for
 * any changes in the directory. In particular there are three kinds of changes
 * that a registered listener ({@link IDirectoryListener}) gets notified with.
 * <ol>
 * <li>ENTRY_CREATE - When a file/folder gets created.</li>
 * <li>ENTRY_DELETE - When a file/folder gets deleted.</li>
 * <li>ENTRY_MODIFY - When a file/folder gets modified.</li>
 * </ol>
 *
 * @author Chandan R. Rupakheti (chandan.rupakheti@rose-hulman.edu)
 * @see {@link IDirectoryListener}
 * @see {@link DirectoryEvent}
 */
public interface IDirectoryMonitorService extends Runnable {
    /**
     * Returns the directory being monitored.
     */
    Path getDirectory();

    /**
     * Returns true if the monitor service is running, otherwise false.
     */
    boolean isRunning();

    /**
     * This method tries to stop the monitor service gracefully by interrupting
     * the service.
     *
     * @throws IOException
     */
    void stopGracefully() throws IOException;

    /**
     * Registers the supplied listener to receive directory change
     * notifications.
     */
    void addListener(IDirectoryListener l);

    /**
     * Unregisters the supplied listener.
     */
    void removeListener(IDirectoryListener l);
}
