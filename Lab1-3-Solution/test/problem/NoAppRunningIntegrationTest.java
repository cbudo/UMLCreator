package problem;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NoAppRunningIntegrationTest {
	public static final String IN_OUT_DIR = "input_output";
	public static final String LOG_FILE = IN_OUT_DIR + "/" + "monitor.log";

	private DirectoryMonitorService monitorService;
	private Thread worker;
	private ApplicationLauncher launcher; 
	private ProcessRunner exeRunner;
	private ProcessRunner webPageViewer;
	private ProcessRunner txtViewer;
	private DirectoryChangeLogger logger;
	
	
	@Before
	public void setUp() throws Exception {
		// Register directory to be monitored
		Path dir = Paths.get(IN_OUT_DIR);
		
		// Let's create a directory monitor service to monitor the input_output dir
		monitorService = new DirectoryMonitorService(dir);
		
		// Let's setup the launcher for exe, txt, htm, and html files
		launcher = new ApplicationLauncher();
		
		// exe runner setup
		exeRunner = new ExecutableFileRunner("");
		launcher.addRunner("exe", exeRunner);
		
		// Web page viewer setup
		webPageViewer = new DataFileRunner("explorer");
		launcher.addRunner("htm", webPageViewer);
		launcher.addRunner("html", webPageViewer);
		
		// Text file viewer setup
		txtViewer = new DataFileRunner("Notepad");
		launcher.addRunner("txt", txtViewer);
		
		// Lets register the launcher to the directory monitor service to receive notifications
		monitorService.addListener(launcher);
		
		// Add a custom directory change listener
		logger = new DirectoryChangeLogger(Paths.get(LOG_FILE));
		monitorService.addListener(logger);
		
		// Start the worker thread
		this.worker = new Thread(monitorService);
		worker.start();
	}

	@After
	public void tearDown() throws IOException, InterruptedException {

	}

	
	@Test
	public final void testGracefulStop() throws IOException, InterruptedException {
		Thread.sleep(200);
		
		monitorService.stopGracefully();
		worker.join();
	}
}
