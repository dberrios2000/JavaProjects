package JavaProcFram;

import java.io.File;
import java.io.Serializable;

/**
 * A simple java bean modelling the file metadata entity.
 * 
 * @author aztekos
 *
 */
public class FileMetaData implements Serializable {

	private static final long serialVersionUID = 201308231451L;
	
	private File file;
	
	private int lastReadLineNumber;
	
	private long lastReadTime;
	
	public FileMetaData(File file) {
		this.file = file;
		this.lastReadLineNumber = 0;
		this.lastReadTime = 0;
	}

	public File getFile() {
		return file;
	}

	public int getLastReadLineNumber() {
		return lastReadLineNumber;
	}

	public void setLastReadLineNumber(int lastReadLineNumber) {
		this.lastReadLineNumber = lastReadLineNumber;
	}

	public long getLastReadTime() {
		return lastReadTime;
	}

	public void setLastReadTime(long lastReadTime) {
		this.lastReadTime = lastReadTime;
	}

}
