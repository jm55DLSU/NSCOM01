package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utility {
	private static boolean state = false;
	private DateTimeFormatter datetimeFormat;
	private final String TEMP_OUTPUT_DIR = ".\\temp\\"; //Saves on a temporary location for file
	public Utility() {
		datetimeFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	}
	public void setState(boolean state) {
		Utility.state = state;
	}
	public void printMessage(String className, String methodName, String text) {
		if(state)
			System.out.println("(" + dtNow()  + ") " + className + "." + methodName + ": " + text);
	}
	public String getGUIConsoleMessage(String message){
		return dtNow() + ": " + message;
	}
	public String dtNow() {
		return datetimeFormat.format(LocalDateTime.now());
	}
	public void printBytes(byte[] bytes) {
		for(byte b: bytes)
			System.out.print(b);
		System.out.println("");
	}
	public void printByteAsString(byte[] bytes) {
		System.out.println(new String(bytes, StandardCharsets.UTF_8)); 
	}
    public void cls() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {}
    }
    public String getTempOutPath(String filename) {
		return this.TEMP_OUTPUT_DIR + filename;
	}
    public void writeMonitor(String className, String method, int bytesRead, int remainingBytes, int threshold) {
		//Confirmatory for 
		if(bytesRead == 512 && remainingBytes <= threshold)
			printMessage(className, method, "Last " + threshold + " bytes...");
		if(bytesRead < 512)
			printMessage(className, method, "Last piece of data[]");
	}
}
