package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Sample;
import other.IO;

public class NormalizeTool {
	public static List<Sample> normalizeByColMax(List<Sample> samples){
		List<Sample> cloneSamples=MyToolKit.clone(samples);
		normalizeByColMaxEqual(cloneSamples);
		return cloneSamples;
	}

	public static Map<String,Sample> normalizeByColMax(Map<String,Sample> idSamplesMap){
		Map<String,Sample> cloneIdSamplesMap=MyToolKit.clone(idSamplesMap);
		normalizeByColMaxEqual(cloneIdSamplesMap);
		return cloneIdSamplesMap;
	}
	public static Map<String,Sample> normalizeByColMax(Map<String,Sample> idSamplesMap,Map<Integer,Double> maxMap){
		Map<String,Sample> cloneIdSamplesMap=MyToolKit.clone(idSamplesMap);
		return normalizeByColMaxEqual(cloneIdSamplesMap,maxMap);
	}
	public static List<Sample> normalizeByColMaxEqual(List<Sample> samples){
		Map<Integer,Double> allDiemMaxMap=getSamplesAllDiemMaxValue(samples);
		for(int i=0;i<samples.size();++i){
			normalizeByColMaxEqual(samples.get(i),allDiemMaxMap);
		}
		return samples;
	}
	public static Map<String,Sample> normalizeByColMaxEqual(Map<String,Sample> idSamplesMap){
		Map<Integer,Double> allDiemMaxMap=getSamplesAllDiemMaxValue(idSamplesMap);
		return normalizeByColMaxEqual(idSamplesMap,allDiemMaxMap);
	}
	public static Map<String,Sample> normalizeByColMaxEqual(Map<String,Sample> idSamplesMap,Map<Integer,Double> maxMap){
		for(Map.Entry<String, Sample> entry:idSamplesMap.entrySet()){
			normalizeByColMaxEqual(entry.getValue(),maxMap);
		}
		return idSamplesMap;
	}
	public static List<Sample> normalizeByColMaxEqual(List<Sample> samples,Map<Integer,Double> maxMap){
		for(Sample sp:samples){
			normalizeByColMaxEqual(sp,maxMap);
		}
		return samples;
	}
	protected static Sample normalizeByColMaxEqual(Sample sp,Map<Integer,Double> maxMap){
		Double times=(double) 1;
		String itemSeperater=":";
		String[] vec=sp.getVec();
		for(int k=0;k<vec.length;++k){
			String[] elmsItem=vec[k].split(itemSeperater);
			int diem=Integer.parseInt(elmsItem[0]);
			double value=Double.parseDouble(elmsItem[1]);
			Double theDiemMaxValue=maxMap.get(diem);
			if(theDiemMaxValue==null){
				//System.out.println(diem+" diem no max value in train file,set default maxValue 1");
				theDiemMaxValue=(double) 1;
			}
			value=(times*value)/theDiemMaxValue;
			String newDiemValueStr=""+diem+itemSeperater+value+"";
			vec[k]=newDiemValueStr;
		}
		sp.setVec(vec);
		return sp;
	}
	public static Map<Integer,Double> getSamplesAllDiemMaxValue(Map<String,Sample> idSamplesMap){
		Map<Integer,Double> allDiemMaxMap=new HashMap<Integer,Double>();
		String itemSeperater=":";
		for(Map.Entry<String, Sample> entry:idSamplesMap.entrySet()){
			Sample sp=entry.getValue();
			String[] vec=sp.getVec();
			for(String item:vec){
				String[] elmsItem=item.split(itemSeperater);
				int diem=Integer.parseInt(elmsItem[0]);
				double value=Double.parseDouble(elmsItem[1]);
				if(!allDiemMaxMap.containsKey(diem)){
					allDiemMaxMap.put(diem, value);
				}else{
					double lastMaxValue=allDiemMaxMap.get(diem);
					if(lastMaxValue<value){
						allDiemMaxMap.put(diem, value);
					}
				}
			}
		}
		return allDiemMaxMap;
	}

	public static Map<Integer,Double> getSamplesAllDiemMaxValue(List<Sample> samples){
		//获取每一维的最大值
		Map<Integer,Double> allDiemMaxMap=new HashMap<Integer,Double>();
		String itemSeperater=":";
		for(Sample sp:samples){
			String[] vec=sp.getVec();
			for(String item:vec){
				String[] elmsItem=item.split(itemSeperater);
				int diem=Integer.parseInt(elmsItem[0]);
				double value=Double.parseDouble(elmsItem[1]);
				if(!allDiemMaxMap.containsKey(diem)){
					allDiemMaxMap.put(diem, value);
				}else{
					double lastMaxValue=allDiemMaxMap.get(diem);
					if(lastMaxValue<value){
						allDiemMaxMap.put(diem, value);
					}
				}
			}
		}
		return allDiemMaxMap;
	}

	public static Map<String,Sample> normalizeByRowMaxEqual(Map<String,Sample> idSamplesMap){
		for(Map.Entry<String, Sample> entry:idSamplesMap.entrySet()){
			normalizeByRowMaxEqual(entry.getValue());
		}
		return idSamplesMap;
	}
	public static List<Sample> normalizeByRowMaxEqual(List<Sample> samples){
		Map<Integer,Double> allDiemMaxMap=getSamplesAllDiemMaxValue(samples);
		for(int i=0;i<samples.size();++i){
			normalizeByRowMaxEqual(samples.get(i));
		}
		return samples;
	}
	protected static Sample normalizeByRowMaxEqual(Sample sp){
		String itemSeperater=":";
		String[] vec=sp.getVec();
		String[] values=new String[vec.length];
		for(int k=0;k<vec.length;++k){
			String[] elmsItem=vec[k].split(itemSeperater);
			values[k]=elmsItem[1];
		}

		double theRowMaxValue=MyToolKit.getMaxValueFromStrArray(values);
		for(int k=0;k<vec.length;++k){
			String[] elmsItem=vec[k].split(itemSeperater);
			int diem=Integer.parseInt(elmsItem[0]);
			double value=Double.parseDouble(elmsItem[1]);
			value/=theRowMaxValue;
			String newDiemValueStr=""+diem+itemSeperater+value+"";
			vec[k]=newDiemValueStr;
		}
		sp.setVec(vec);
		return sp;
	}

	public static void main(String[] args) throws Exception{

	}

}
