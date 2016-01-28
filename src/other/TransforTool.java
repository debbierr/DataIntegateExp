package other;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bean.Result;
import bean.Sample;

public class TransforTool {
	public Map<String,Result> resultList2IdResultMap(List<Result> results) throws Exception{
		Map<String,Result> resultMap=new LinkedHashMap<String,Result>(); 
		for(Result r:results){
			resultMap.put(r.getSample().getId(), r);
		}
		return resultMap;
	}
	public Map<String,Sample> sampleList2IdSampleMap(List<Sample> samples){
		Map<String,Sample> sampleMap=new LinkedHashMap<String,Sample>();
		for(Sample sp:samples){
			sampleMap.put(sp.getId(), sp);
		}
		return sampleMap;
	}
}
