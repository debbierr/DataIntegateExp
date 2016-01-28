package other;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bean.Result;
import bean.Sample;
import utils.FileTool;

public class IO {
	public static List<Result> readResultSimply(String dataDir,String idFile,String resultFile) throws Exception{
		List<Result> results=new LinkedList<Result>();
		BufferedReader brId=FileTool.getBufferedReaderFromFile(dataDir+idFile, "utf-8");
		BufferedReader brResult=FileTool.getBufferedReaderFromFile(dataDir+resultFile, "utf-8");
		String idLine;
		String resultLine;
		while((resultLine=brResult.readLine())!=null&&(idLine=brId.readLine())!=null){
			results.add(new Result(idLine, resultLine));
		}
		brId.close();
		brResult.close();
		return results;
	}
	public static List<Result> readResult(String dataDir,String idFile,String dataFile,String resultFile) throws Exception{
		List<Result> results=new LinkedList<Result>();
		BufferedReader brId=FileTool.getBufferedReaderFromFile(dataDir+idFile, "utf-8");
		BufferedReader brData=FileTool.getBufferedReaderFromFile(dataDir+dataFile, "utf-8");
		BufferedReader brResult=FileTool.getBufferedReaderFromFile(dataDir+resultFile, "utf-8");
		String dataLine;
		String idLine;
		String resultLine;
		while((resultLine=brResult.readLine())!=null&&(idLine=brId.readLine())!=null&&(dataLine=brData.readLine())!=null){
			results.add(new Result(idLine, resultLine, dataLine));
		}
		brData.close();
		brId.close();
		brResult.close();
		return results;
	}
	public static Map<String,Sample> readSample(String dataDir,String idFile,String dataFile) throws Exception{
		Map<String,Sample> samples=new LinkedHashMap<String,Sample>(1000*62*2);
		BufferedReader brId=FileTool.getBufferedReaderFromFile(dataDir+idFile);
		BufferedReader brData=FileTool.getBufferedReaderFromFile(dataDir+dataFile);
		String idLine="";
		String dataLine="";
		Sample sample=null;
		while((idLine=brId.readLine())!=null&&(dataLine=brData.readLine())!=null){
			sample=new Sample(idLine,dataLine,"\t");
			samples.put(sample.getId(), sample);
			sample=null;
		}
		brId.close();
		brData.close();
		return samples;
	}
	public static List<String> readId(String filePath,String seperater) throws IOException{
		BufferedReader br=FileTool.getBufferedReaderFromFile(filePath);
		String line="";
		List<String> ids=new ArrayList<String>();
		while((line=br.readLine())!=null){
			String[] elms=line.split(seperater);
			ids.add(elms[0]);
		}
		br.close();
		return ids;
	}
	public static List<String> readIdLabel(String filePath) throws IOException{
		BufferedReader br=FileTool.getBufferedReaderFromFile(filePath);
		String line="";
		List<String> lines=new ArrayList<String>();
		while((line=br.readLine())!=null){
			lines.add(line);
		}
		br.close();
		return lines;
	}
	public static Map<String,List<String>> readLabelIdsMap(String filePath,String seperater) throws IOException{
		BufferedReader br=FileTool.getBufferedReaderFromFile(filePath);
		String line="";
		Map<String,List<String>> labelIds=new HashMap<String,List<String>>();
		String label="";
		while((line=br.readLine())!=null){
			String[] elms=line.split(seperater);
			String id=elms[0];
			label=elms[1];
			List<String> ids=labelIds.get(label);
			if(ids==null)
				ids=new ArrayList<String>();
			ids.add(id);
			labelIds.put(label, ids);
		}
		br.close();
		return labelIds;
	}
	public static List<String> readLines(String filePath) throws IOException{
		BufferedReader br=FileTool.getBufferedReaderFromFile(filePath);
		String line="";
		List<String> lines=new ArrayList<String>();
		while((line=br.readLine())!=null){
			lines.add(line);
		}
		br.close();
		return lines;
	}
	public static void writeId(String filePath,List<String> ids) throws UnsupportedEncodingException, FileNotFoundException{
		PrintWriter pw=FileTool.getPrintWriterForFile(filePath);
		for(String id:ids){
			pw.write(id+"\r\n");
		}
		pw.close();
	}
	public static void writeIDAndDataAndResult(String dataDir,String idFile,String dataFile
			,String resultFile,Map<String,Result> resultMap) throws Exception{
		PrintWriter writerId=FileTool.getPrintWriterForFile(dataDir+idFile );
		PrintWriter writerData=FileTool.getPrintWriterForFile(dataDir+dataFile);
		PrintWriter writerResult=FileTool.getPrintWriterForFile(dataDir+resultFile);
		for(Map.Entry<String, Result> entry:resultMap.entrySet()){
			Result r=entry.getValue();
			writerId.write(r.getSample().getId()+"\t"+r.getSample().getLable()+"\r\n");
			writerData.write(r.getSample().getLabelAndVec()+"\r\n");
			writerResult.write(r.getPredictLabel()+" "+r.getResultProbabilityStr()+"\r\n");
		}
		writerId.close();
		writerData.close();
		writerResult.close();
	}
//	public static void writeIDAndData(String dataDir,String idFile,String dataFile
//			,Map<String,Sample> sampleMap) throws Exception{
//		PrintWriter writerId=FileTool.getPrintWriterForFile(dataDir+idFile);
//		PrintWriter writerData=FileTool.getPrintWriterForFile(dataDir+dataFile);
//		Sample sp=null;
//		for(Map.Entry<String, Sample> entry:sampleMap.entrySet()){
//			sp=entry.getValue();
//			writerId.write(sp.getId()+"\t"+sp.getLable()+"\r\n");
//			writerData.write(sp.getLabelAndVec()+"\r\n");
//			sp=null;
//		}
//		writerId.close();
//		writerData.close();
//	}
	public static void writeIDAndData(String dataDir,String idFile,String dataFile
			,List<Sample> samples) throws Exception{
		PrintWriter writerId=FileTool.getPrintWriterForFile(dataDir+idFile);
		PrintWriter writerData=FileTool.getPrintWriterForFile(dataDir+dataFile);
		for(Sample sp:samples){
			writerId.write(sp.getId()+"\t"+sp.getLable()+"\r\n");
			writerData.write(sp.getLabelAndVec()+"\r\n");
			sp=null;
		}
		writerId.close();
		writerData.close();
	}
	public static void appendIdAndData(String dataDir,String idFile,String dataFile,List<Sample> addSamples) throws UnsupportedEncodingException, FileNotFoundException{
		PrintWriter writerId=FileTool.getPrintWriterForFile(dataDir+idFile, true, "utf-8");
		PrintWriter writerData=FileTool.getPrintWriterForFile(dataDir+dataFile, true, "utf-8");

		for(Sample sample:addSamples){
			writerId.write(sample.getId()+"\t"+sample.getLable()+"\r\n");
			writerData.write(sample.getLabelAndVec()+"\r\n");
		}
		writerId.close();
		writerData.close();	
	}
	public static void appendIdAndData(String dataDir,String idFile,String dataFile,Map<String,Sample> addSamples) throws UnsupportedEncodingException, FileNotFoundException{
		PrintWriter writerId=FileTool.getPrintWriterForFile(dataDir+idFile, true, "utf-8");
		PrintWriter writerData=FileTool.getPrintWriterForFile(dataDir+dataFile, true, "utf-8");

		for(Map.Entry<String,Sample> entry:addSamples.entrySet()){
			Sample sample=entry.getValue();
			writerId.write(sample.getId()+"\t"+sample.getLable()+"\r\n");
			writerData.write(sample.getLabelAndVec()+"\r\n");
		}
		writerId.close();
		writerData.close();	
	}
}
