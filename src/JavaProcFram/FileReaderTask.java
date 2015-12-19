package JavaProcFram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Implements the file read task that is scheduled by the main process's 
 * timer.
 * 
 * @author aztekos
 *
 */
public class FileReaderTask extends TimerTask {
		
	private String filePath;
	private FileMetaData fileMetaData;

	/**
	 * Constructor.
	 * 
	 * @param filePath the file to read
	 */
	public FileReaderTask(String filePath) {
		this.filePath = filePath;
		this.fileMetaData = MainProcess.getSharedInstance().getFileMetaData(filePath);
	}
	
	/**
	 * Reads and returns all lines in the specified file, starting from the 
	 * specified line.
	 * 
	 * @param file
	 * @param lineStart
	 * @return
	 */
	private List<String> readLines(File file, int lineStart) {
		List<String> lines = new ArrayList<String>();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			String currentLine;
			int lineCount = 0;
			while ((currentLine = in.readLine()) != null) {
				if (lineCount < lineStart) {
					lineCount++;
					continue;
				} else {
					lines.add(currentLine);
					lineCount++;
				}
			}
			this.fileMetaData.setLastReadLineNumber(lineCount);
			return lines;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			MainProcess.getSharedInstance().setFileMetaData(filePath, fileMetaData);
		}
	}

	@Override
	public void run() {
		File file = this.fileMetaData.getFile();
		long lastModified = file.lastModified();
		if (lastModified != this.fileMetaData.getLastReadTime()) {
			List<String> newLines = this.readLines(file, this.fileMetaData.getLastReadLineNumber());
			System.out.println("Read " + newLines.size() + " new line(s) from file: " + file.getName());
			if (newLines.size() > 0) {
				Database.getSharedInstance().insertLines(newLines);
			}
			this.fileMetaData.setLastReadTime(lastModified);
		}
	}

}
