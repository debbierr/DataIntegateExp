package bean;

import utils.CollectionTool;
import utils.MyToolKit;

public class Result {
	private String predictLabel;
	private String[] resultProbability;
	private double supportForPredict;
	private Sample sample;
	public Result(){}
	public Result(String idLine,String resultLine){
		String seperater="\\s";
		String[] elmsResult=resultLine.split(seperater);
		this.setPredictLabel(elmsResult[0]);
		this.resultProbability=CollectionTool.getSubStrArry(elmsResult, 1, elmsResult.length);
		this.setSupportForPredict(MyToolKit.getMaxValueFromStrArray(this.resultProbability));
		this.setSample(new Sample(idLine,"\t"));
	}
	public Result(String idLine,String resultLine,String dataLine){
		String seperater="\\s";
		String[] elmsResult=resultLine.split(seperater);
		this.setPredictLabel(elmsResult[0]);
		this.resultProbability=CollectionTool.getSubStrArry(elmsResult, 1, elmsResult.length);
		this.setSupportForPredict(MyToolKit.getMaxValueFromStrArray(this.resultProbability));
		this.setSample(new Sample(idLine,dataLine,"\t"));
	}
	public void setPredictLabel(String predictLabel) {
		this.predictLabel = predictLabel;
	}
	public String getPredictLabel() {
		return predictLabel;
	}
	public double getSupportForPredict() {
		return supportForPredict;
	}
	public void setSupportForPredict(double supportForPredict) {
		this.supportForPredict = supportForPredict;
	}
	public Sample getSample() {
		return sample;
	}
	public void setSample(Sample sample) {
		this.sample = sample;
	}
	public String[] getResultProbability() {
		return resultProbability;
	}
	public void setResultProbability(String[] resultProbability) {
		this.resultProbability = resultProbability;
	}
	public String  getResultProbabilityStr(){
		return getResultProbabilityStr(" ");
	}
	public String getResultProbabilityStr(String seperater){
		StringBuilder stb=new StringBuilder();
		for(int i=0;i<this.resultProbability.length;++i){
			if(i==this.resultProbability.length-1) stb.append(resultProbability[i]);
			else stb.append(resultProbability[i]+seperater);
		}
		return stb.toString();
	}

}
