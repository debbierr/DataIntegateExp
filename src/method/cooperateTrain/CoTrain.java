package method.cooperateTrain;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import bean.Result;
import bean.Sample;
import model.Classifier;
import model.IDSetManager;
import other.GlobleLog;
import other.IO;
import utils.CollectionTool;
import utils.FileTool;
import utils.TestTool;

public class CoTrain {
	private Classifier classfierA;
	private Classifier classfierB;
	private IDSetManager idsManager;

	public CoTrain(Classifier ca,Classifier cb,String publicInfoDir) throws Exception{
		this.setclassfierA(ca);
		this.setclassfierB(cb);
		this.setIdsManager(new IDSetManager(publicInfoDir));
		Collections.shuffle(idsManager.getAllLearnIds(),new Random(100000));
		GlobleLog.setLogPath(FileTool.getParentPath(ca.getFileManager().getDataDir())+"\\");
	}

	public void train(int maxIter,int incrementK,boolean isPopEachCateKBest,boolean isNormalize) throws Exception{
		train(maxIter,incrementK,0.025f,isPopEachCateKBest,isNormalize);
	}

	public void train(int maxIter,int incrementK,float initLearnPart,boolean isPopEachCateKBest,boolean isNormalize) throws Exception{
		int initLearnSize=(int) (idsManager.getAllLearnIds().size()*initLearnPart);
		List<String> learnId=CollectionTool.popSubList(idsManager.getAllLearnIds(), 0, initLearnSize);
		this.classfierA.initTrainAndTestAndLearnData(idsManager.getTrainIds(), idsManager.getTestIds(), learnId,isNormalize);
		this.classfierB.initTrainAndTestAndLearnData(idsManager.getTrainIds(), idsManager.getTestIds(), learnId,isNormalize);

		int iter=0;
		int k=incrementK;
		while(iter<maxIter){
			TestTool.println("---------------------------iter "+iter+"-------------------------------------------------------------------------------");
			classfierA.train();
			classfierB.train();

			classfierA.predictForLearn();
			classfierB.predictForLearn();

			TestTool.println("------------------------------------------------");
			classfierA.predictForTest();
			classfierB.predictForTest();
			TestTool.println("------------------------------------------------");

			List<Result> bestResultFromA=null;
			List<Result> bestResultFromB=null;
			if(isPopEachCateKBest){
				bestResultFromA=classfierA.popEachCateKBestResultFromLearn(k);
				bestResultFromB=classfierB.popEachCateKBestResultFromLearn(k);
			}
			else{
				bestResultFromA=classfierA.popKBestResultFromLearn(k);
				bestResultFromB=classfierB.popKBestResultFromLearn(k);
			}

			classfierA.increTrainFile(bestResultFromB);
			classfierB.increTrainFile(bestResultFromA);

			int totalPopNum=Math.max(bestResultFromA.size(), bestResultFromB.size());
			if(idsManager.getAllLearnIds().size()>(totalPopNum+1)){
				List<String> increLearnId=CollectionTool.popSubList(idsManager.getAllLearnIds(), 0, totalPopNum);

				classfierA.increLearnFile(increLearnId);
				classfierB.increLearnFile(increLearnId);
			}
			
			if(classfierA.getLearnIds().size()<initLearnSize||classfierB.getLearnIds().size()<initLearnSize) break;

			classfierA.updateAllIdAndData(isNormalize);
			classfierB.updateAllIdAndData(isNormalize);
			TestTool.println("--------------------------------------------------------------------------------------------------------------------------");
			++iter;			
		}
		TestTool.println("total iter "+iter);
	}

	public List<Double> predict() throws IOException{
		List<Double> accuracy=new LinkedList<Double>();
		accuracy.add(classfierA.predictForTest());
		accuracy.add(classfierB.predictForTest());
		return accuracy;
	}

	public Classifier getclassfierA() {
		return classfierA;
	}
	public void setclassfierA(Classifier classfierA) {
		this.classfierA = classfierA;
	}
	public Classifier getclassfierB() {
		return classfierB;
	}
	public void setclassfierB(Classifier classfierB) {
		this.classfierB = classfierB;
	}

	public IDSetManager getIdsManager() {
		return idsManager;
	}

	public void setIdsManager(IDSetManager idsManager) {
		this.idsManager = idsManager;
	}


}
