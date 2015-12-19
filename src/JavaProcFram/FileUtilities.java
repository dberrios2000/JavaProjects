package JavaProcFram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Implements various file utility operations.
 * 
 * @author aztekos
 *
 */
public class FileUtilities {
	
	/**
	 * Persists the specified serializable object to the specified file.
	 * 
	 * @param object
	 * @param filePath
	 * @return
	 */
	public static File objectToFile(Object object, String filePath) {
		ObjectOutputStream out = null;
		try {
			File file = new File(filePath);
			FileOutputStream fout = new FileOutputStream(file);
			out = new ObjectOutputStream(fout);
			out.writeObject(object);
			return file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Loads the specified file into an object.
	 * 
	 * @param file
	 * @return
	 */
	public static Object fileToObject(File file) {
		ObjectInputStream in = null;
		try {
			FileInputStream fin = new FileInputStream(file);
			in = new ObjectInputStream(fin);
			return in.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
