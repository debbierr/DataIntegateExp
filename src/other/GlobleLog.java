package other;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.FileTool;

public class GlobleLog {
	private static Log log;

	public static void setLogPath(String logDir) throws UnsupportedEncodingException, FileNotFoundException {
		log=null;
		log=new Log(logDir);
	}
	
	public static void write(String s) throws UnsupportedEncodingException, FileNotFoundException{
		log.write(s);
	}

}
