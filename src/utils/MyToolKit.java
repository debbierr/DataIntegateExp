package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bean.Result;
import bean.Sample;
import model.ComparatorResult;

public class MyToolKit {
	public static int getTypeNum(String filePath,String seperater) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(filePath);
		int typeNum=0;
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater);
			if(elms.length==2){
				++typeNum;
			}
		}
		System.out.println("this file have "+typeNum+" type");
		return typeNum;
	}
	public static void testResult(String filePath,String seperater) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(filePath);
		int maxLikehoodType=0;
		double maxValue=0;
		double total=0;
		String line="";
		int lineNum=1;
		int end=40;
		while((line=reader.readLine())!=null){
			if(lineNum>end) break;
			String[] elms=line.split(seperater);
			String[] vElms=new String[elms.length-1];
			for(int i=1;i<elms.length;++i){
				double d=Double.parseDouble(elms[i]);
				if(d>maxValue){
					maxValue=d;
					maxLikehoodType=i;
				}
				total+=d;
				vElms[i-1]=elms[i];
			}
			System.out.println(lineNum+" row predict type is " +elms[0]+", the "+maxLikehoodType+"th Value is maxValue "
					+maxValue+", total have "+(elms.length-1)+" probality value,"+",Sum all value is "+total);
			System.out.println(getMaxValueFromStrArray(vElms));
			maxLikehoodType=0;
			maxValue=0;
			total=0;
			++lineNum;
		}

	}
	public static double getMaxValueFromStrArray(String[] dStr){
		double maxValue=-1000000000;
		for(int i=0;i<dStr.length;++i){
			double dValue=Double.parseDouble(dStr[i]);
			if(maxValue<dValue){
				maxValue=dValue;
			}
		}
		return maxValue;
	}
	public static List<String> cloneStrList(List<String> list){
		List<String> clone=new LinkedList<String>();
		for(String s:list){
			clone.add(s);
		}
		return clone;
	}
	public static List<Sample> clone(List<Sample> samples){
		List<Sample> cloneSamples=new ArrayList<Sample>();
		for(Sample sp:samples){
			Sample cloneSample=new Sample(sp);
			cloneSamples.add(cloneSample);
		}
		return cloneSamples;
	}
	public static Map<String,Sample> clone(Map<String,Sample> maps){
		Map<String,Sample> cloneMaps=new LinkedHashMap<String,Sample>();
		for(Map.Entry<String, Sample> entry:maps.entrySet()){
			Sample cloneSample=new Sample(entry.getValue());
			cloneMaps.put(entry.getKey(),cloneSample);
		}
		return cloneMaps;
	}
	public static List<String> getIdsFromResults(List<Result> results){
		List<String> ids=new ArrayList<String>();
		for(Result r:results){
			ids.add(r.getSample().getId());
		}
		return ids;
	}
	public static Map<String,List<Result>> getCateResultsMap(List<Result> results){
		Map<String,List<Result>> cateResultsMap=new LinkedHashMap<String,List<Result>>(62*2);
		for(Result r:results){
			String cate=r.getPredictLabel();
			List<Result> cateResults=cateResultsMap.get(cate);
			if(null==cateResults){
				List<Result> newCateResults=new ArrayList<Result>();
				newCateResults.add(r);
				cateResultsMap.put(cate, newCateResults);
			}else{
				cateResults.add(r);
				//cateResultsMap.put(cate, cateResults);
			}
		}
		return cateResultsMap;
	}
	public static Map<String,List<Result>> getCateDescResultsMap(List<Result> results){
		Map<String,List<Result>> cateResultsMap=getCateResultsMap(results);
		for(Map.Entry<String, List<Result>> entry:cateResultsMap.entrySet()){
			Collections.sort(entry.getValue(), new ComparatorResult());
		}
		return cateResultsMap;
	}
	public static int max(int a,int b,int c){
		return Math.max(Math.max(a, b), c);
	}
	public static int min(int a,int b,int c){
		return Math.min(Math.min(a, b), c);
	}
	public static List<Result> getEachCateKBestResults(Map<String,List<Result>> cateResultsMap,int k ){
		List<Result> allEachCateKbest=new ArrayList<Result>();
		for(Map.Entry<String, List<Result>> entry:cateResultsMap.entrySet()){
			int kThreshold=k;
			List<Result> curCateResults=entry.getValue();
			if(curCateResults.size()<k){
				kThreshold=curCateResults.size();
			}
			for(int i=0;i<kThreshold;++i){
				allEachCateKbest.add(curCateResults.get(i));
			}
		}
		return allEachCateKbest;
	}
	public static void main(String [] args) throws IOException{
		//getTypeNum("F:/ExpData/DataFromOther/qty/Data4Tritrain/IMDB_Freq_DataTextTf.txt","\\s{1,}");
		//testResult("F:/wy1223/svm/SVM_lg/result_lg.txt","\\s{1,}");
		System.out.println(max(1,5,1));
	}
}
