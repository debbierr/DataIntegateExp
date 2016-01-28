package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bean.Result;
import bean.Sample;
import other.IO;
import other.Log;
import utils.CollectionTool;
import utils.FileTool;
import utils.MyToolKit;
import utils.NormalizeTool;
import utils.TestTool;

public class Classifier {
	private CmdTrainAndPredict svm;
	private Map<String,Sample> allSamples;
	private String allSamplesDataDir;
	private List<String> trainIds;
	private List<String> testIds;
	private List<String> learnIds;
	private Log log; 
	private ClassifierDataFileManager fileManager;

	private List<Sample> trainSamples; 

	public Classifier(String svmDir,String allSamplesDir,String destDataDir) throws Exception{
		this(new CmdTrainAndPredict(svmDir),allSamplesDir,destDataDir+FileTool.getPureName(allSamplesDir)+"\\" );
	}
	public Classifier(CmdTrainAndPredict svm,String allSamplesDir,String dataDir) throws Exception{
		this(svm,allSamplesDir);
		setClassifierDestDataDir(dataDir);
	}
	public void setClassifierDestDataDir(String dataDir) throws UnsupportedEncodingException, FileNotFoundException{
		fileManager=new ClassifierDataFileManager(dataDir);
		log=new Log(dataDir);
		svm.attach(log);
	}

	public Classifier(String svmDir,String allSamplesDir) throws Exception{
		this(new CmdTrainAndPredict(svmDir),allSamplesDir);
	}

	public Classifier(CmdTrainAndPredict svm,String allSamplesDir) throws Exception{
		this.setSvm(svm);
		this.setAllSamples(IO.readSample(allSamplesDir, "id.txt", "data.txt"));
		this.setAllSamplesDataDir(allSamplesDir);
	}

	public void initTrainAndTestAndLearnData(List<String> trainIds,List<String> testIds,List<String> learnIds,boolean isNormalize) throws Exception{
		this.setTrainIds(MyToolKit.cloneStrList(trainIds));
		this.setTestIds(MyToolKit.cloneStrList(testIds));
		this.setLearnIds(MyToolKit.cloneStrList(learnIds));
		this.updateAllIdAndData(isNormalize);
	}
	public void initTrainAndTestAndLearnData(IDSetManager ism,boolean isNormalize) throws Exception{
		this.initTrainAndTestAndLearnData(ism.getTrainIds(), ism.getTestIds(), ism.getAllLearnIds(), isNormalize);
	}

	protected Map<String,Sample> selectIdSampleMap(List<String> ids){
		Map<String,Sample> selectSamples=new LinkedHashMap<String,Sample>(ids.size()*2);
		for(String id:ids){
			Sample sp=allSamples.get(id);
			if(sp!=null)
				selectSamples.put(id, sp);
		}
		return selectSamples;
	}
	protected List<Sample> selectSamples(List<String> ids){
		List<Sample> samples=new LinkedList<Sample>();
		for(String id:ids){
			Sample sp=allSamples.get(id);
			if(sp!=null)
				samples.add(sp);
		}
		return samples;
	}
	public void train() throws IOException{
		this.svm.train(fileManager.getDataDir(), fileManager.getTrainDataFileName());
	}
	public double predictForTest() throws IOException{
		return this.svm.predict(fileManager.getDataDir(),fileManager.getTestDataFileName(), fileManager.getTestResultFileName());
	}
	public double predictForLearn() throws IOException{
		return this.svm.predict(fileManager.getDataDir(), fileManager.getLearningDataFileName(),fileManager.getLearningResultFileName());
	}
	public List<String> popKBestResultIdFromLearn(int k) throws Exception{
		List<String> ids=new LinkedList<String>();
		List<Result> results=this.popKBestResultFromLearn(k);
		for(Result r:results){
			ids.add(r.getSample().getId());
		}
		return ids;
	}
	public List<Result> popKBestResultFromLearn(int k) throws Exception{
		List<Result> kbestResult=fileManager.getKBestResult(k, fileManager.LEARN);
		List<String> popIds=MyToolKit.getIdsFromResults(kbestResult);
		TestTool.print(fileManager.getDataDir()+"learn pop :"+popIds.size()+" ids ");
		this.learnIds=CollectionTool.trimStrListBFromA(this.learnIds, popIds);
		return kbestResult;
	}

	public List<Result> popEachCateKBestResultFromLearn(int k) throws Exception{
		List<Result> allEachCateKbest=this.getEachCateKBestResultsFromLearn(k);
		List<String> popIds=MyToolKit.getIdsFromResults(allEachCateKbest);
		TestTool.print(fileManager.getDataDir()+"learn pop :"+popIds.size()+" ids ");
		this.learnIds=CollectionTool.trimStrListBFromA(this.learnIds, popIds);
		return allEachCateKbest;
	}
	protected Map<String,List<Result>> getCateDescResultsMapFromLearn(int k) throws Exception{
		List<Result> results=fileManager.getResult(fileManager.LEARN);
		return  MyToolKit.getCateDescResultsMap(results);
	}

	public List<Result> getEachCateKBestResultsFromLearn(int k) throws Exception{
		Map<String,List<Result>> cateResultsMap=this.getCateDescResultsMapFromLearn(k);
		return MyToolKit.getEachCateKBestResults(cateResultsMap, k);
	}

	public List<Result> getLearnResult() throws Exception{
		return fileManager.getResult(fileManager.LEARN);
	}
	public Map<String,Result> getLearnIdResultMap() throws Exception{
		Map<String,Result> resultMap=new LinkedHashMap<String,Result>(); 
		List<Result> results=this.getLearnResult();
		for(Result r:results){
			resultMap.put(r.getSample().getId(), r);
		}
		return resultMap;
	}

	public void learnFileTrime(List<String> popIds) throws Exception{
		TestTool.print(fileManager.getDataDir()+"learn pop :"+popIds.size()+" ids");
		this.learnIds=CollectionTool.trimStrListBFromA(this.learnIds, popIds);
	}
	public void learnFilePop(List<Result> popResult) throws Exception{
		List<String> popIds=new LinkedList<String>();
		for(Result r:popResult){
			popIds.add(r.getSample().getId());
		}
		this.learnFileTrime(popIds);
		popIds=null;
	}
	//将result中置信度高的id的 预测label 加到train中，同时加入该视图该id对应data(注意是该View对应的data)
	public List<Sample> increTrainFile(List<Result> results) throws Exception{
		List<Sample> predictGoodSample=new LinkedList<Sample>();
		List<String> increIds=new ArrayList<String>();
		int correctPredictNum=0;
		TestTool.print(fileManager.getDataDir()+fileManager.getTrainIdFileName()+" ---- incre :");
		Sample pSmp=null;
		for(Result r:results){
			pSmp=r.getSample();
			if(pSmp.getLable().equals(r.getPredictLabel())){
				++correctPredictNum;
			}
			pSmp.setLable(r.getPredictLabel());//id的 预测label 
			pSmp.setVec(getVec(pSmp.getId()));//该视图该id对应data
			predictGoodSample.add(pSmp);
			increIds.add(pSmp.getId());
			//TestTool.print(" "+pSmp.getId());
			pSmp=null;
		}
		TestTool.println(increIds.size()+" id , increTrain correct rate="+(float)correctPredictNum*100.0f/results.size()+"%"+"("+correctPredictNum+"/"+results.size()+")");
		
		
		CollectionTool.increStrListAbyB(this.trainIds, increIds);
		TestTool.println(fileManager.getDataDir()+fileManager.getTrainIdFileName()+" after incre, the size: "+trainIds.size());

		//将预测得好的sample加入train集中
		for(Sample sp:predictGoodSample){
			this.trainSamples.add(sp);
		}
		return this.trainSamples;

	}
	public void increLearnFile(List<String> ids) throws Exception{
		CollectionTool.increStrListAbyB(this.learnIds, ids);
	}

	public void updateAllIdAndData(boolean isNormalize) throws Exception{
		List<Sample> trainSamples=this.getTrainSamples();
		List<Sample> learnSamples=this.getLearnSamples();
		List<Sample> testSamples=this.getTestSamples();
		if(isNormalize){
			List<Sample>  trainSamplesClone=MyToolKit.clone(trainSamples);
			List<Sample>  learnSamplesClone=MyToolKit.clone(learnSamples);
			List<Sample>  testSamplesClone=MyToolKit.clone(testSamples);
			Map<Integer,Double> trainMaxDiemValueMap=NormalizeTool.getSamplesAllDiemMaxValue(trainSamplesClone);
			NormalizeTool.normalizeByColMaxEqual(trainSamplesClone,trainMaxDiemValueMap);
			NormalizeTool.normalizeByColMaxEqual(learnSamplesClone,trainMaxDiemValueMap);
			NormalizeTool.normalizeByColMaxEqual(testSamplesClone,trainMaxDiemValueMap);
			fileManager.updateIdAndData(trainSamplesClone, fileManager.TRAIN);
			fileManager.updateIdAndData(learnSamplesClone, fileManager.LEARN);
			fileManager.updateIdAndData(testSamplesClone, fileManager.TEST);
		}else{
			fileManager.updateIdAndData(trainSamples, fileManager.TRAIN);
			fileManager.updateIdAndData(learnSamples, fileManager.LEARN);
			fileManager.updateIdAndData(testSamples, fileManager.TEST);
		}
	}

	public CmdTrainAndPredict getSvm() {
		return svm;
	}
	public void setSvm(CmdTrainAndPredict svm) {
		svm.attach(log);
		this.svm = svm;
	}
	public Map<String,Sample> getAllSamples() {
		return allSamples;
	}
	public void setAllSamples(Map<String,Sample> allSamples) {
		this.allSamples = allSamples;
	}
	private String[] getVec(String id){
		String[] vec=null;
		if(allSamples.containsKey(id))
			vec=allSamples.get(id).getVec();
		return vec;
	}
	public List<String> getTestIds() {
		return testIds;
	}
	public void setTestIds(List<String> testIds) {
		this.testIds = testIds;
	}
	public List<String> getTrainIds() {
		return trainIds;
	}
	public void setTrainIds(List<String> trainIds) {
		this.trainIds = trainIds;
	}
	public List<String> getLearnIds() {
		return learnIds;
	}
	public void setLearnIds(List<String> learnIds) {
		this.learnIds = learnIds;
	}
	public ClassifierDataFileManager getFileManager() {
		return fileManager;
	}
	public String getAllSamplesDataDir() {
		return allSamplesDataDir;
	}
	public void setAllSamplesDataDir(String allSamplesDataDir) {
		this.allSamplesDataDir = allSamplesDataDir;
	}
	public List<Sample> getTrainSamples() {
		if(null==trainSamples){
			this.setTrainSamples(this.selectSamples(this.trainIds));
		}
		return trainSamples;
	}
	public void setTrainSamples(List<Sample> trainSamples) {
		this.trainSamples = trainSamples;
	}
	public 	List<Sample> getLearnSamples(){
		return this.selectSamples(this.learnIds);
	}
	public  List<Sample> getTestSamples(){
		return this.selectSamples(this.testIds);
	}
}
