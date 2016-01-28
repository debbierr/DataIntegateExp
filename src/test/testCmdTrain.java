package test;

import java.io.IOException;

import model.CmdTrainAndPredict;

public class testCmdTrain {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String base="F:\\ExpData\\DataIntegate\\CooperateTrain\\";
		String svm=base+"svm\\SVM_lg\\";
		String dataDir=base+"test\\";
		CmdTrainAndPredict cmdTrain=new CmdTrainAndPredict(svm,"lg");
		cmdTrain.train(dataDir, "training_data");
		cmdTrain.predict(dataDir, "testing_data", "result_lg");
	}

}
