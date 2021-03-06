package TestingProjects.problem1_3;

import java.io.IOException;
import java.util.*;

public class ApplicationLauncher implements IApplicationLauncher {
	private final Map<String, ProcessRunner> extensionToRunnerMap;
	

	public ApplicationLauncher() {
		this.extensionToRunnerMap = Collections.synchronizedMap(new HashMap<>());
	}

	@Override
	public Collection<ProcessRunner> getRunners() {
		return Collections.unmodifiableCollection(this.extensionToRunnerMap.values());
	}

	@Override
	public void addRunner(String extension, ProcessRunner runner) {
		this.extensionToRunnerMap.put(extension, runner);
	}

	@Override
	public void shutDown() {
		synchronized(this.extensionToRunnerMap) {
			for(ProcessRunner runner: this.extensionToRunnerMap.values()) {
				for(Process p : runner.getProcesses())
					p.destroy();
			}
		}
	}
	
	@Override
	public void directoryChanged(DirectoryEvent e) {
		// Only interested in file create event
		if (!Objects.equals(e.getEventType(), DirectoryEvent.ENTRY_CREATE))
			return;
		
		String[] parts = e.getFile().toString().split("\\.");
		if(parts.length == 0)
			return;
		
		ProcessRunner runner = this.extensionToRunnerMap.get(parts[parts.length - 1]);
		if(runner == null)
			return;
		
		try {
			runner.execute(e.getFile());
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
