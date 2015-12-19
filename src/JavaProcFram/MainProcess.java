package JavaProcFram;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;

/**
 * Implements the main process that monitors the specified files and maintains
 * the database.
 */
public class MainProcess {
	
	/**
	 * The poll period, i.e. the period that the process will check the files 
	 * for modifications.
	 */
	private final static long POLL_PERIOD = 5000L;
	
	/**
	 * Holds metadata about the monitored files.
	 */
	private Hashtable<String,FileMetaData> files;
	
	/**
	 * Timer used to schedule file reads.
	 */
	private Timer timer;
	
	/**
	 * Singleton instance.
	 */
	private static MainProcess sharedInstance;
	
	/**
	 * Constructor.
	 * 
	 * @param filesToRead list of files to monitor and periodically read.
	 */
	public MainProcess(List<File> filesToRead) {
		sharedInstance = this;
		files = new Hashtable<String,FileMetaData>();
		
		// For each file we check if a serialized metadata already exists
		for (File f : filesToRead) {
			File metadataFile = new File(f.getAbsolutePath()+ ".ser");
			if (metadataFile.exists()) {
				files.put(f.getAbsolutePath(), (FileMetaData)FileUtilities.fileToObject(metadataFile));
			} else {
				files.put(f.getPath(), new FileMetaData(f));
			}
		}
		this.timer = new Timer();
	}
	
	/**
	 * Gets the shared instance.
	 * 
	 * @return
	 */
	public static MainProcess getSharedInstance() {
		return sharedInstance;
	}
	
	/**
	 * Gets the specified file metadata.
	 * 
	 * @param filePath
	 * @return
	 */
	public FileMetaData getFileMetaData(String filePath) {
		return files.get(filePath);
	}
	
	/**
	 * Sets teh metadata of the specified file.
	 * 
	 * @param filePath
	 * @param fileMetaData
	 */
	public void setFileMetaData(String filePath, FileMetaData fileMetaData) {
		files.put(filePath, fileMetaData);
	}
	
	/**
	 * Runs this process.
	 */
	public void run() {
		for (FileMetaData fileMetaData : files.values()) {
			FileReaderTask task = new FileReaderTask(fileMetaData.getFile().getAbsolutePath());
			timer.schedule(task, 0, POLL_PERIOD);
		}
	}
	
	/**
	 * Cancels this process and all scheduled file read tasks.
	 */
	public void cancel() {
		timer.cancel();
		timer.purge();
		for (FileMetaData f : files.values()) {
			FileUtilities.objectToFile(f, f.getFile().getAbsolutePath() + ".ser");
		}
		Database.getSharedInstance().disconnect();
	}

	/**
	 * Main method.
	 * 
	 * @param args the full paths of the files to be monitored by this process.
	 * 
	 * @throws InterruptedException
	 */
	public static void main(String... args) throws InterruptedException {
		List<File> filesToRead = new ArrayList<File>();
		for (String fileName : args) {
			File file = new File(fileName);
			if (file.exists() && !file.isDirectory()) {
				filesToRead.add(file);
			}
		}
		final MainProcess proc = new MainProcess(filesToRead);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				proc.cancel();
			}
		});
		proc.run();
		while (true) {
			Thread.sleep(2000);
		}
	}

}
