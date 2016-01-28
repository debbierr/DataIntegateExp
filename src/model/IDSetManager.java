package model;

import java.io.IOException;
import java.util.List;

import other.IO;

public class IDSetManager {
	private List<String> allLearnIds;//U
	private List<String> trainIds;
	private List<String> testIds;
	
	public IDSetManager(String publicInfoDir) throws IOException{
		this.setTrainIds(IO.readId(publicInfoDir+"trainId.txt", "\t"));
		this.setTestIds(IO.readId(publicInfoDir+"testId.txt", "\t"));
		this.setAllLearnIds(IO.readId(publicInfoDir+"learnId.txt", "\t"));
	}

	public List<String> getAllLearnIds() {
		return allLearnIds;
	}

	public void setAllLearnIds(List<String> allLearnIds) {
		this.allLearnIds = allLearnIds;
	}

	public List<String> getTrainIds() {
		return trainIds;
	}

	public void setTrainIds(List<String> trainIds) {
		this.trainIds = trainIds;
	}

	public List<String> getTestIds() {
		return testIds;
	}

	public void setTestIds(List<String> testIds) {
		this.testIds = testIds;
	}

}
