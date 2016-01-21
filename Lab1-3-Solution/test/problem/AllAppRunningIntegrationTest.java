package problem;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AllAppRunningIntegrationTest {
	public static final String IN_OUT_DIR = "input_output";
	public static final String SRC_TEST = IN_OUT_DIR + "/test_files/test.";
	public static final String DEST_TEST = IN_OUT_DIR + "/test.";

	private static DirectoryMonitorService monitorService;
	private static Thread worker;
	private static ApplicationLauncher launcher; 
	private static ProcessRunner exeRunner;
	private static ProcessRunner webPageViewer;
	private static ProcessRunner txtViewer;
	private static DirectoryChangeLogger logger;
	
	public static void deleteAllFile() {
		File ioDir = new File(IN_OUT_DIR);
		File[] allFiles = ioDir.listFiles();
		for(File f: allFiles) {
			if(!f.isDirectory()) {
				f.delete();
			}
		}
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Start will an empty input_output directory
		deleteAllFile();
		
		// Copy the log file to be used
		Files.copy(Paths.get(SRC_TEST + "log"), Paths.get(DEST_TEST + "log"));
		
		// Let's create a directory monitor service to monitor the input_output dir
		monitorService = new DirectoryMonitorService(Paths.get(IN_OUT_DIR));
		
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
		logger = new DirectoryChangeLogger(Paths.get(DEST_TEST + "log"));
		monitorService.addListener(logger);
		
		// Start the worker thread
		worker = new Thread(monitorService);
		worker.start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException, InterruptedException {
//		System.out.println("Press the return key to shutdown the service. First, manually close all applications ...");
//		System.in.read();
		
		monitorService.stopGracefully();
		worker.join();		
	}
	
	
	@Test
	public final void testDirectoryChangeLogger() throws Exception {
		long initialSize = new File(DEST_TEST + "log").length();
		
		Files.copy(Paths.get(SRC_TEST + "bak"), Paths.get(DEST_TEST + "bak"));
		Thread.sleep(200);
		long finalSize = new File(DEST_TEST + "log").length();

		assertTrue("No data was logged by the directory change logger", finalSize > initialSize);
	}

	
	@Test
	public final void testTextViewer() throws Exception {
		int before = txtViewer.getProcesses().size();
		Files.copy(Paths.get(SRC_TEST + "txt"), Paths.get(DEST_TEST + "txt"));

		// Let's give some time for the app to load
		Thread.sleep(2000);
		
		int after = txtViewer.getProcesses().size();
		
		assertEquals("Problem running text editor!", after, before + 1);
	}
	
	@Test
	public final void testHtmlViewer() throws Exception {
		int before = webPageViewer.getProcesses().size();
		
		Files.copy(Paths.get(SRC_TEST + "html"), Paths.get(DEST_TEST + "html"));

		// Let's give some time for the app to load
		Thread.sleep(2000);
		
		// Let's check if it worked
		int after = webPageViewer.getProcesses().size();
		
		assertEquals("Problem running html viewer!", after, before + 1);
	}

	@Test
	public final void testHtmViewer() throws Exception {
		int before = webPageViewer.getProcesses().size();
		Files.copy(Paths.get(SRC_TEST + "htm"), Paths.get(DEST_TEST + "htm"));

		// Let's give some time for the app to load
		Thread.sleep(2000);
		
		// Let's check if it worked
		int after = webPageViewer.getProcesses().size();
		
		assertEquals("Problem running htm viewer!", after, before + 1);
	}
	
	@Test
	public final void testExeRunner() throws Exception {
		int before = exeRunner.getProcesses().size();
		Files.copy(Paths.get(SRC_TEST + "exe"), Paths.get(DEST_TEST + "exe"));

		// Let's give some time for the app to load
		Thread.sleep(2000);
		
		// Let's check if it worked
		int after = exeRunner.getProcesses().size();
		
		assertEquals("Problem running exe runner!", after, before + 1);
	}	
}
