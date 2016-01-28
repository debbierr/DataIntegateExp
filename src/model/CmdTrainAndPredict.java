package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import other.GlobleLog;
import other.Log;
import utils.FileTool;
import utils.TestTool;

public class CmdTrainAndPredict {
	private String svmPath;
	private String type;
	private Log log;
	public CmdTrainAndPredict(String svmPath){
		this.setSvmPath(svmPath);
		String svmName=FileTool.getPureName(svmPath);
		String[] elms=svmName.split("_");
		this.setType(elms[elms.length-1]); 
	}
	public CmdTrainAndPredict(String svmPath,String type){
		this.setSvmPath(svmPath);
		this.setType(type);
	}
	
	public double trainAndPredict(String dataDir,String train_data_file,String test_data_file,String result_file) throws IOException{
		this.train(dataDir, train_data_file);
		Double accuracy=this.predict(dataDir, test_data_file, result_file);
		return accuracy;
	}
	/**
	 * cmd /c dir 是执行完dir命令后关闭命令窗口。 
	 * cmd /k dir 是执行完dir命令后不关闭命令窗口。 
	 * cmd /c start dir 会打开一个新窗口后执行dir指令，原窗口会关闭。 
	 * cmd /k start dir 会打开一个新窗口后执行dir指令，原窗口不会关闭。 
	 * @param group
	 * @throws IOException 
	 */
	public void train(String dataDir, String train_data_file) throws IOException {
		//svm-train .\1\training_data.txt
		String cmdStr =this.getTrainCmdStr(dataDir, train_data_file);
		Runtime run = Runtime.getRuntime(); 
		Process process = run.exec(cmdStr); 
		//将调用结果打印到控制台上
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));  
		String message;       
		while((message=br.readLine())!=null&&!message.equals("")){  
			//将信息输出  
			//System.out.println(message); 
		}
	}
	public double predict(String dataDir,String test_data_file,String result_file) throws IOException{
		String cmdStr=this.getPredictCmdStr(dataDir, test_data_file, result_file);
		Runtime run = Runtime.getRuntime(); 
		Process process = run.exec(cmdStr);   
		//将调用结果打印到控制台上  
		BufferedReader br = new BufferedReader( new InputStreamReader(process.getInputStream()));  
		String message;  
		double accuracy = 0;
		while((message=br.readLine())!=null&&!message.equals("")){  
			//将信息输出  
			if(message.contains("Accuracy")){
				accuracy = Double.parseDouble(message.split("(Accuracy = )|(%)")[1]);
				TestTool.println(dataDir+result_file+"----"+message);
				log.write(dataDir+result_file+"----"+message+"\r\n");
				//testTool.println(message.split(" = |%")[1]);
			}else if(message.contains("Zero/one-error")){
				accuracy = 100 - Double.parseDouble(message.split("(: )|(%)")[1]);
				TestTool.println(dataDir+result_file+"----"+message);
				log.write(dataDir+result_file+"----"+message+"\r\n");
			}
		}
		return accuracy;
	}
	

	protected String getTrainCmdStr(String dataDir, String train_data_file){
		String cmdStr = "";
		String svm_path = this.svmPath;
		String type = this.type;
		if(type.equals("lg")){
			cmdStr = "cmd /k "+svm_path+"train -s 0 "+dataDir+train_data_file;
		}else if(type.equals("svm")){
			cmdStr = "cmd /k "+svm_path+"svm-train "+dataDir+train_data_file;
		}else if(type.equals("muti")){
			cmdStr = "cmd /k "+svm_path+"svm_multiclass_learn -c 0.01 "+dataDir+train_data_file+" "+dataDir+"mutisvm_struct_model";
		}
		return cmdStr;
	}
	protected String getPredictCmdStr(String dataDir,String test_data_file,String result_file){
		String save_path = this.svmPath;
		String cmdStr = "";
		String type = this.type;
		if(type.equals("lg")){
			cmdStr = "cmd /k "+save_path+"predict -b 1 "
					+dataDir+"\\"+test_data_file+" "
					+dataDir+"\\training_data.txt.model "
					+dataDir+"\\"+result_file/*+"_lg.txt"*/;
		}else if(type.equals("svm")){
			cmdStr = "cmd /k "+save_path+"svm-predict "
					+dataDir+"\\"+test_data_file+" "
					+dataDir+"\\training_data.txt.model "
					+dataDir+"\\"+result_file/*+"_svm.txt"*/;
		}else if(type.equals("muti")){
			cmdStr = "cmd /k "+save_path+"svm_multiclass_classify "
					+dataDir+"\\"+test_data_file+" "
					+dataDir+"\\mutisvm_struct_model "
					+dataDir+"\\"+result_file/*+"_muti.txt"*/;
		}
		return cmdStr;
	}
	public void attach(Log log){
		this.log=log;
	}

	public String getSvmPath() {
		return svmPath;
	}
	public void setSvmPath(String svmPath) {
		this.svmPath = svmPath;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
