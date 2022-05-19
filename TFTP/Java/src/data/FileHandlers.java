package data;

/**
 * References:
 * https://www.programiz.com/java-programming/examples/get-file-extension
 * https://stackoverflow.com/a/17011063
 * https://mkyong.com/java/how-to-convert-array-of-bytes-into-file
 * https://www.baeldung.com/java-write-byte-array-file
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import utils.Monitor;

public class FileHandlers {
	private File f;
	private Monitor m = new Monitor(true);
	private final String className = "FileHandlers";
	
	public FileHandlers() {
		this.f = null;
	}
	
	/**
	 * Returns this File object.
	 * @return File set for FileHandlers.
	 */
	public File getFile() {
		m.printMessage(this.className, "getFile", "Returning this.f ...");
		return this.f;
	}
	
	/**
	 * Get the file path of the specified in the parameters.
	 * @param f File where the path will be extracted
	 * @param canonical Set as true if the path needed is in canonical, false if absolute.
	 * @return Path of File f.
	 */
    public String getFilePath(File f, boolean canonical){
    	m.printMessage(this.className, "getFilePath(File, canonical)", "Getting filepath (" + canonical + ")...");
        if(!canonical)
            return f.getAbsolutePath();
        try {
			return f.getCanonicalPath();
		} catch (IOException e) {
			m.printMessage(this.className, "getFilePath(File, canonical)", "TryCatch: Canonical path of File cannot be retrieved, returning absolute path instead.");
			return f.getAbsolutePath();
		}
    }
    
    /**
     * Get the file path of the FileHandler's File object.
     * @param canonical Set as true if the path needed is in canonical, false if absolute.
     * @return Path of the File in FileHandler. Null if File is null.
     */
    public String getFilePath(boolean canonical) {
    	m.printMessage(this.className, "getFilePath(canonical)", "Getting filepath of this.f (" + canonical + ")...");
    	return getFilePath(this.f, canonical);
    }
    
    /**
     * Get the file path of a file specified by user through JFileChooser, which
     * can be in either absolute or canonical form.
     * @param canonical Set as true if the path needed is in canonical, false if absolute.
     * @return Path of specified file in JFileChooser.
     */
    public String getNewFilePath(boolean canonical) {
    	m.printMessage(this.className, "getNewFilePath(canonical)", "Getting filepath of new File (" + canonical + ")...");
    	return getFilePath(openFile(), canonical);
    }
    
    /**
     * Set this File f from path.
     * @param path Path of File to be set to this.
     */
    public void setFile(String path) {
    	m.printMessage(this.className, "setFile(path)", "Setting this.f ...");
    	Path p = Paths.get(path);
    	this.f = p.toFile();
    }
    
    /**
     * Set this File f from File f.
     * @param f File to be set as this.f
     */
    public void setFile(File f) {
    	m.printMessage(this.className, "setFile(File)", "Setting this.f ...");
    	this.f = f;
    }
    
    /**
	 * Opens and returns a File object specified by user through through JFileChooser.
	 * It also sets FileHandlers File object.
	 * If file selection fails (i.e. file selection was cancelled) it retains the File of this.
	 * @return File opened or null if it fails.
	 */
	public File openFile() {
		m.printMessage(this.className, "openFile()", "Opening file from JFileChooser...");
		JFileChooser fileChooser = new JFileChooser();
		int choice = fileChooser.showOpenDialog(null);
        if(choice == JFileChooser.APPROVE_OPTION) {
        	m.printMessage(this.className, "openFile()", "Set chosen file as this.f ...");
        	this.f = fileChooser.getSelectedFile();
        }else {
        	m.printMessage(this.className, "openFile()", "File selection failed, retaning this.f and returning null...");
        	return null;
        }	
        return this.f;
	}
    
    /**
     * Saves filebytes[] as a File.
     * Assumes that the user will enter the file extension as part of the filename at the save dialog.
     * @param filebytes byte[] of file to be saved as File.
     * @return True if saving file successful or not.
     */
    public boolean saveFile(byte[] filebytes) {
    	m.printMessage(this.className, "saveFile(filebytes)", "Saving filebytes as file (null extension)...");
    	return saveFile(filebytes, null);
    }
    
    public boolean saveFile(byte[] filebytes, String extension){
    	m.printMessage(this.className, "saveFile(filebytes, extension)", "Saving filebytes as file (with extension)...");
        JFileChooser fChooser = new JFileChooser();
        fChooser.showSaveDialog(null);
        File file = fChooser.getSelectedFile();
        if(file.exists()){
        	m.printMessage(this.className, "saveFile(filebytes, extension)", "File exists, asking for rewrite...");
            int choice = JOptionPane.showConfirmDialog(null, "Replace file?");
            if(choice == 0){ //choice being yes (0)
            	m.printMessage(this.className, "saveFile(filebytes, extension)", "Overwriting file...");
                return writeFile(filebytes, file, extension);
            }else{ //choice being no (1) or cancel (2)
            	m.printMessage(this.className, "saveFile(filebytes, extension)", "Rewrite not selected, file saving failed...");
            	return false;
            }
        }else{
        	m.printMessage(this.className, "saveFile(filebytes, extension)", "File !exists, creating new file...");
            return writeFile(filebytes, file, extension);
        }
    }
    
    /**
     * Get the file extension of this object's File.
     * @return Extension of the 
     */
    public String getFileExt() {
    	m.printMessage(this.className, "getFileExt()", "Getting file extension of this.f ...");
    	return getFileExt(this.f);
    }
    
    public String getFileExt(File file){
    	m.printMessage(this.className, "getFileExt(file)", "Getting file extension of file...");
        return getFileExt(file.getAbsolutePath());
    }

    public String getFileExt(String path){
    	m.printMessage(this.className, "getFileExt(path)", "Getting file extension of File pointed by path...");
        int index = path.lastIndexOf('.');
        String extension = "";
        if(index > 0)
            extension = path.substring(index + 1);
        return "."+extension;
    }
    
    /**
     * Writes the actual File to the system.
     * @param filebytes
     * @param file
     * @param extension
     * @return
     */
    private boolean writeFile(byte[] filebytes, File file, String extension){
    	m.printMessage(this.className, "writeFile(filebytes,file,extension)", "Writing file...");
    	if(!file.getName().contains(".")) {
    		file = new File(file.toString() + extension);
    	}
    	
        try {
            if(!file.exists()) {
            	m.printMessage(this.className, "writeFile(filebytes,file,extension)", "file does not exist, Creating new file...");
            	file.createNewFile();
            }
            m.printMessage(this.className, "writeFile(filebytes,file,extension)", "Writing filebytes to file...");
            Files.write(file.toPath(), filebytes);
            filebytes = null;
            this.f = file;
            return true;
        } catch (IOException e) {
        	m.printMessage(this.className, "writeFile(filebytes,file,extension)", "TryCatch: Error occured while writing file.");
        	filebytes = null;
            return false;
        }
    }
}
