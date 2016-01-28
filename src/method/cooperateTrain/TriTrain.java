package method.cooperateTrain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import bean.Result;
import model.Classifier;
import model.ComparatorResult;
import model.IDSetManager;
import other.GlobleLog;
import other.Log;
import utils.CollectionTool;
import utils.FileTool;
import utils.MyToolKit;
import utils.TestTool;

public class TriTrain {
	private Classifier classfierA;
	private Classifier classfierB;
	private Classifier classfierC;
	private IDSetManager idsManager;

	public TriTrain(Classifier ca,Classifier cb,Classifier cc,IDSetManager ism) throws UnsupportedEncodingException, FileNotFoundException{
		this.setClassfierA(ca);
		this.setClassfierB(cb);
		this.setClassfierC(cc);
		this.setIdsManager(ism);
		Collections.shuffle(idsManager.getAllLearnIds(),new Random(100000));
		GlobleLog.setLogPath(FileTool.getParentPath(ca.getFileManager().getDataDir())+"/");
	}
	public TriTrain(Classifier ca,Classifier cb,Classifier cc,String expSetDir) throws UnsupportedEncodingException, FileNotFoundException, IOException{
		this(ca,cb,cc,new IDSetManager(expSetDir));
	}

	public void train(int maxIter,int incrementK,boolean isPopEachCateKBest,boolean isNormalize) throws Exception{
		train(maxIter,incrementK,0.025f,isPopEachCateKBest,isNormalize);
	}

	public void train(int maxIter,int incrementK,float initLearnPart,boolean isPopEachCateKBest,boolean isNormalize) throws Exception{
		int initLearnSize=(int) (idsManager.getAllLearnIds().size()*initLearnPart);
		List<String> learnId=CollectionTool.popSubList(idsManager.getAllLearnIds(), 0, initLearnSize);
		this.classfierA.initTrainAndTestAndLearnData(idsManager.getTrainIds(), idsManager.getTestIds(), learnId,isNormalize);
		this.classfierB.initTrainAndTestAndLearnData(idsManager.getTrainIds(), idsManager.getTestIds(), learnId,isNormalize);
		this.classfierC.initTrainAndTestAndLearnData(idsManager.getTrainIds(), idsManager.getTestIds(), learnId,isNormalize);

		int iter=0;
		int k=incrementK;

		int maxIncrement=30;
		int minIncrement=5;
		int learnIncreCount=0;
		int minLearnSize=50*62;
		while(iter<maxIter){
			TestTool.println("---------------------------iter "+iter+"-----------------------------------------------------------------");
			classfierA.train();
			classfierB.train();
			classfierC.train();

			classfierA.predictForLearn();
			classfierB.predictForLearn();
			classfierC.predictForLearn();

			TestTool.println("------------------------------------------------");
			classfierA.predictForTest();
			classfierB.predictForTest();
			classfierC.predictForTest();
			TestTool.println("------------------------------------------------");

			List<Result> resultFromA=classfierA.getLearnResult();
			List<Result> resultFromB=classfierB.getLearnResult();
			List<Result> resultFromC=classfierC.getLearnResult();

			List<Result> resultKBestAB=null;
			if(isPopEachCateKBest){
				resultKBestAB=getEachCateKBestResults(resultFromA,resultFromB,k);
			}else{
				resultKBestAB=getKBestAgreeResult(resultFromA,resultFromB,k);
			}
			classfierA.learnFilePop(resultKBestAB);
			classfierB.learnFilePop(resultKBestAB);
			classfierC.increTrainFile(resultKBestAB);

			List<Result> resultKBestAC=null;
			if(isPopEachCateKBest){
				resultKBestAC=getEachCateKBestResults(resultFromA,resultFromC,k);
			}else{
				resultKBestAC=getKBestAgreeResult(resultFromA,resultFromC,k);
			}
			classfierA.learnFilePop(resultKBestAC);
			classfierC.learnFilePop(resultKBestAC);
			classfierB.increTrainFile(resultKBestAC);

			List<Result> resultKBestBC=null;
			if(isPopEachCateKBest){
				resultKBestBC=getEachCateKBestResults(resultFromB,resultFromC,k);
			}else{
				resultKBestBC=getKBestAgreeResult(resultFromB,resultFromC,k);
			}
			classfierB.learnFilePop(resultKBestBC);
			classfierC.learnFilePop(resultKBestBC);
			classfierA.increTrainFile(resultKBestBC);

			int totalPopNum=(resultKBestAB.size()+ resultKBestAC.size()+resultKBestBC.size())-MyToolKit.min(resultKBestAB.size(), resultKBestAC.size(), resultKBestBC.size());
			if(idsManager.getAllLearnIds().size()>(totalPopNum+1)){
				List<String>  increLearnId=CollectionTool.popSubList(idsManager.getAllLearnIds(), 0, totalPopNum);
				classfierA.increLearnFile(increLearnId);
				classfierB.increLearnFile(increLearnId);
				classfierC.increLearnFile(increLearnId);
				
//				//调整参数，优化
//				learnIncreCount++;
//				if(k>maxIncrement){
//					k/=2;
//				}else{
//					if(learnIncreCount%3==0) k+=3;
//				}
			}
//			else{
//				if(k<=minIncrement){
//					k=incrementK;
//				}else{
//					k=k-3;
//				}
//			}

			if(classfierA.getLearnIds().size()<minLearnSize
					||classfierB.getLearnIds().size()<minLearnSize
					||classfierC.getLearnIds().size()<minLearnSize){ break;}
			classfierA.updateAllIdAndData(isNormalize);
			classfierB.updateAllIdAndData(isNormalize);
			classfierC.updateAllIdAndData(isNormalize);
			TestTool.println("--------------------------------------------------------------------------------------------------");
			++iter;
		}
		TestTool.println("total iter "+iter);
	}

	public List<Double>  predict() throws IOException{
		List<Double> accuracy=new LinkedList<Double>();
		accuracy.add(classfierA.predictForTest());
		accuracy.add(classfierB.predictForTest());
		accuracy.add(classfierC.predictForTest());
		return accuracy;
	}

	protected List<Result> getKBestAgreeResult(List<Result> result1,List<Result> result2,int k){
		List<Result> agreeResult=this.getAgreeResult(result1, result2);
		Collections.sort(agreeResult,new ComparatorResult());
		List<Result> kbestAgreeResult=new ArrayList<Result>();
		for(int i=0;i<k;++i){
			kbestAgreeResult.add(agreeResult.get(i));
		}
		return kbestAgreeResult;
	}
	protected List<Result> getEachCateKBestResults(List<Result> result1,List<Result> result2,int k){
		List<Result> agreeResult=this.getAgreeResult(result1, result2);
		Map<String,List<Result>> cateResultsMap=MyToolKit.getCateDescResultsMap(agreeResult);
		return MyToolKit.getEachCateKBestResults(cateResultsMap, k);
	}

	private List<Result> getAgreeResult(List<Result> result1,List<Result> result2){
		List<Result> agreeResult=new ArrayList<Result>();
		Map<String,Result> result1Map=new LinkedHashMap<String,Result>(result1.size()*2);
		for(Result r1:result1){
			result1Map.put(r1.getSample().getId(), r1);
		}
		for(Result r2:result2){//注意：在后面不同view的learnIds有可能不一样
			Result r1=result1Map.get(r2.getSample().getId());
			if(r1!=null){
				if(r1.getPredictLabel().equals(r2.getPredictLabel())){
					//若判断一致，则修改置信度为两者置信度之和
					r2.setSupportForPredict(r2.getSupportForPredict()+r1.getSupportForPredict());
					agreeResult.add(r2);
				}
			}
		}
		return agreeResult;
	}

	public Classifier getClassfierA() {
		return classfierA;
	}
	public void setClassfierA(Classifier classfierA) {
		this.classfierA = classfierA;
	}
	public Classifier getClassfierB() {
		return classfierB;
	}
	public void setClassfierB(Classifier classfierB) {
		this.classfierB = classfierB;
	}
	public Classifier getClassfierC() {
		return classfierC;
	}
	public void setClassfierC(Classifier classfierC) {
		this.classfierC = classfierC;
	}
	public IDSetManager getIdsManager() {
		return idsManager;
	}
	public void setIdsManager(IDSetManager idsManager) {
		this.idsManager = idsManager;
	}

}
