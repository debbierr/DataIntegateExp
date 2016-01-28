package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import bean.Result;
import bean.Sample;
import other.IO;
import utils.NormalizeTool;

public class ClassifierDataFileManager {
	private String dataDir;
	private String trainDataFileName;
	private String trainIdFileName;
	private String testDataFileName;
	private String testIdFileName;
	private String testResultFileName;
	private String learningDataFileName;
	private String learningIdFileName;
	private String learningResultFileName;
	private Map<Integer,Double> diemsMaxValueMap;
	public final static String TEST="test";
	public final static String LEARN="learn";
	public final static String TRAIN="train";

	public ClassifierDataFileManager(String dataDir){
		this.dataDir=dataDir;
		this.setDefaultFileName();
	}
	protected void setDefaultFileName(){
		this.setTrainDataAndIdFileName("training");
		this.setTestDataAndIdAndResultFileName("testing");
		this.setLearningDataAndIdAndResultFileName("learning");
	}

	public void setTrainDataAndIdFileName(String dataTypeName){
		this.trainDataFileName=dataTypeName+"_data.txt";
		this.trainIdFileName=dataTypeName+"_id.txt";
	}
	public void setTestDataAndIdAndResultFileName(String dataTypeName){
		this.testDataFileName=(dataTypeName+"_data.txt");
		this.testIdFileName=(dataTypeName+"_id.txt");
		this.testResultFileName=(dataTypeName+"_result.txt");
	}
	public void setLearningDataAndIdAndResultFileName(String dataTypeName){
		this.learningDataFileName=(dataTypeName+"_data.txt");
		this.learningIdFileName=(dataTypeName+"_id.txt");
		this.learningResultFileName=(dataTypeName+"_result.txt");
	}

	public List<Result> getResult(String type) throws Exception{
		switch(type){
		case LEARN:
			return IO.readResultSimply(this.dataDir, this.learningIdFileName, this.learningResultFileName);
		case TEST:
		default:
			return IO.readResultSimply(this.dataDir, this.testIdFileName, this.testResultFileName);
		}
	}

	public List<Result> getKBestResult(int k,String filetType) throws Exception{
		List<Result> results=getResult(filetType);
		Collections.sort(results, new ComparatorResult());
		List<Result> kbestResult=new LinkedList<Result>();
		for(int i=0;i<k;++i)
			kbestResult.add(results.get(i));
		return kbestResult;
	}
//	public void updateIdAndData(Map<String,Sample> samples,String fileType) throws Exception{
//		switch(fileType){
//		case TRAIN:
//			IO.writeIDAndData(dataDir, this.trainIdFileName, this.trainDataFileName, samples);
//			break;
//		case LEARN:
//			IO.writeIDAndData(dataDir, this.learningIdFileName, this.learningDataFileName, samples);
//			break;
//		case TEST:
//			IO.writeIDAndData(dataDir, this.testIdFileName, this.testDataFileName, samples);
//			break;
//		default:
//			break;
//		}
//	}
	public void updateIdAndData(List<Sample> samples,String fileType) throws Exception{
		switch(fileType){
		case TRAIN:
			IO.writeIDAndData(dataDir, this.trainIdFileName, this.trainDataFileName, samples);
			break;
		case LEARN:
			IO.writeIDAndData(dataDir, this.learningIdFileName, this.learningDataFileName, samples);
			break;
		case TEST:
			IO.writeIDAndData(dataDir, this.testIdFileName, this.testDataFileName, samples);
			break;
		default:
			break;
		}
	}

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}
	public String getTrainDataFileName() {
		return trainDataFileName;
	}
	public void setTrainDataFileName(String trainDataFileName) {
		this.trainDataFileName = trainDataFileName;
	}
	public String getTrainIdFileName() {
		return trainIdFileName;
	}
	public void setTrainIdFileName(String trainIdFileName) {
		this.trainIdFileName = trainIdFileName;
	}
	public String getTestDataFileName() {
		return testDataFileName;
	}
	public void setTestDataFileName(String testDataFileName) {
		this.testDataFileName = testDataFileName;
	}
	public String getTestIdFileName() {
		return testIdFileName;
	}
	public void setTestIdFileName(String testIdFileName) {
		this.testIdFileName = testIdFileName;
	}
	public String getTestResultFileName() {
		return testResultFileName;
	}
	public void setTestResultFileName(String testResultFileName) {
		this.testResultFileName = testResultFileName;
	}
	public String getLearningResultFileName() {
		return learningResultFileName;
	}
	public void setLearningResultFileName(String learningResultFileName) {
		this.learningResultFileName = learningResultFileName;
	}
	public String getLearningDataFileName() {
		return learningDataFileName;
	}
	public void setLearningDataFileName(String learningDataFileName) {
		this.learningDataFileName = learningDataFileName;
	}
	public String getLearningIdFileName() {
		return learningIdFileName;
	}
	public void setLearningIdFileName(String learningIdFileName) {
		this.learningIdFileName = learningIdFileName;
	}
	public Map<Integer, Double> getDiemsMaxValueMap() {
		return diemsMaxValueMap;
	}
	public void setDiemsMaxValueMap(Map<Integer, Double> diemsMaxValueMap) {
		this.diemsMaxValueMap = diemsMaxValueMap;
	}
}
