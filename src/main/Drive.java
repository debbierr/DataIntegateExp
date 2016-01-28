package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import method.cooperateTrain.CoTrain;
import method.cooperateTrain.TriTrain;
import model.Classifier;
import model.CmdTrainAndPredict;
import model.IDSetManager;
import utils.FileTool;

public class Drive {
	static String svmDir="F:\\ExpData\\DataIntegate\\svm\\SVM_lg\\";
	static String publicInfo="F:\\ExpData\\DataIntegate\\source\\mutiCategory\\PublicInfo\\";
	public static void excCotrain(boolean isNormalize) throws Exception{
		String base="F:\\ExpData\\DataIntegate\\";
		String aAllSamplesDir=publicInfo+"Data\\IMDB_Freq_Char3Gram_New\\";
		String bAllSamplesDir=publicInfo+"Data\\IMDB_Freq_DataTextTf\\";
		String cAllSamplesDir=publicInfo+"Data\\IMDB_Freq_OnlySyntac\\";
		
		String normolizeStr="noNormalize";
		if(isNormalize){
			normolizeStr="normalizeByColMax";
		}
		String destDir=base+"dest\\mutiCategory\\coTrain-"+normolizeStr+"\\";

		Classifier ca=new Classifier(svmDir,aAllSamplesDir);
		Classifier cb=new Classifier(svmDir,bAllSamplesDir);
		Classifier cc=new Classifier(svmDir,cAllSamplesDir);
		List<Classifier> triViewClassifiers=new ArrayList<Classifier>();
		triViewClassifiers.add(ca);
		triViewClassifiers.add(cb);
		triViewClassifiers.add(cc);
		for(int i=0;i<triViewClassifiers.size();++i){
			Classifier c1 = triViewClassifiers.get(i);
			Classifier c2 = triViewClassifiers.get((i+1)%triViewClassifiers.size());
			String view1=FileTool.getPureName(c1.getAllSamplesDataDir());
			String view2=FileTool.getPureName(c2.getAllSamplesDataDir());
			String coDestDir=destDir+view1+"+"+view2+"\\";
			c1.setClassifierDestDataDir(coDestDir+view1+"\\");
			c2.setClassifierDestDataDir(coDestDir+view2+"\\");
			CoTrain coTrain=new CoTrain(c1,c2,publicInfo+"ExpSet\\");
			coTrain.train(100,10,0.3f,true,isNormalize);
			coTrain.predict();
		}

	}
//	public static void excSingleTrain() throws Exception{
//		String base="F:\\ExpData\\DataIntegate\\";
//		String aAllSamplesDir=publicInfo+"Data\\IMDB_Freq_Char3Gram_New\\";
//		String bAllSamplesDir=publicInfo+"Data\\IMDB_Freq_DataTextTf\\";
//		String cAllSamplesDir=publicInfo+"Data\\IMDB_Freq_OnlySyntac\\";
//		String destDir= base+"dest\\mutiCategory\\singleTrain\\";   
//		Classifier ca=new Classifier(svmDir,aAllSamplesDir,destDir);
//		singleTrainRun(ca,publicInfo+"ExpSet\\");
//
//		Classifier cb=new Classifier(svmDir,bAllSamplesDir,destDir);
//		singleTrainRun(cb,publicInfo+"ExpSet\\");
//
//		Classifier cc=new Classifier(svmDir,cAllSamplesDir,destDir);
//		singleTrainRun(cc,publicInfo+"ExpSet\\");
//	}
//	private static void singleTrainRun(Classifier c,String publicInfo,boolean isNormalize) throws IOException, Exception{
//		c.initTrainAndTestData(new IDSetManager(publicInfo),isNormalize);
//		c.train();
//		c.predictForTest();
//	}
	public static void excTriTrain(boolean isNormalize) throws Exception{
		String base="F:\\ExpData\\DataIntegate\\";
		String aAllSamplesDir=publicInfo+"Data\\IMDB_Freq_Char3Gram_New\\";
		String bAllSamplesDir=publicInfo+"Data\\IMDB_Freq_DataTextTf\\";
		String cAllSamplesDir=publicInfo+"Data\\IMDB_Freq_OnlySyntac\\";
		
		String normolizeStr="noNormalize";
		if(isNormalize){
			normolizeStr="normalizeByColMax";
		}
		String destDir=base+"dest\\mutiCategory\\triTrain-"+normolizeStr+"\\";
		Classifier ca=new Classifier(svmDir,aAllSamplesDir,destDir);
		Classifier cb=new Classifier(svmDir,bAllSamplesDir,destDir);
		Classifier cc=new Classifier(svmDir,cAllSamplesDir,destDir);

		TriTrain triTrain=new TriTrain(ca,cb,cc,publicInfo+"ExpSet\\");
		triTrain.train(140,10,0.3f,true,isNormalize);
		triTrain.predict();
	}
//	public static void excSimMaSingleTrain() throws Exception{
//		String dir="F:\\ExpData\\DataIntegate\\source\\colMatrix\\";
//		String subPath="simMa\\";
//		File f=new File(dir);
//		File[] fs=f.listFiles();
//		for(File d:fs){
//			Classifier c=new Classifier(svmDir,d.getAbsolutePath()+"\\"+subPath);
//			singleTrainRun(c,publicInfo+"ExpSet\\");
//		}
//	}

	public static void main(String[] args) throws   Exception {
		// TODO Auto-generated method stub
		//excTriTrain(false);
	     excCotrain(false);
		//excTriTrain(true);
		//excCotrain(true);
		//excSingleTrain();
		//excSimMaSingleTrain();
	}

}
