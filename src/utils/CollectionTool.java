package utils;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import bean.Result;
import bean.Sample;
import other.GlobleLog;

public class CollectionTool {
	public static List<String> trimStrListBFromA(List<String> a,List<String> b) throws UnsupportedEncodingException, FileNotFoundException{
		int lastSize=a.size();
		Map<String,String> bMap=new HashMap<String,String>();
		for(String s:b){
		  bMap.put(s, s);	
		}
		List<String> newAList=new ArrayList<String>();
		for(int i=0;i<a.size();++i){
			if(bMap.containsKey(a.get(i))){
				//TestTool.print(" "+a.get(i));//一定注意：不能直接用a.remove()，这样可能导致中间会跳过几个id
			}else{
				newAList.add(a.get(i));
			}
		}
		int curSzie=newAList.size();
		TestTool.println("("+lastSize+"-->"+curSzie+")");
		return newAList;
	}
	public static List<String> popSubList(List<String> list,int fromIndex,int toIndex){
		List<String> subList=new LinkedList<String>();
		int count=0;
		//Collections.shuffle(list);
		while(toIndex>fromIndex&&count<(toIndex-fromIndex)){
			subList.add(list.get(fromIndex));
			list.remove(fromIndex);
			++count;
		}
		return subList;
	}
	public static List<String> mergeStrList(List<String> list1,List<String> list2){
		List<String> list=new ArrayList<String>();
		for(String s: list1) list.add(s);
		for(String s: list2) list.add(s);
		return list;
	}
	public static List<Result> mergeResultList(List<Result>list1,List<Result> list2){
		List<Result> list=new LinkedList<Result>();
		for(Result r:list1) list.add(r);
		for(Result r:list2) list.add(r);
		return list;
	}
	public static List<Object> mergeList(List<Object> list1,List<Object> list2){
		List<Object> list=new LinkedList<Object>();
		for(Object o:list1) list.add(o);
		for(Object o:list2) list.add(o);
		return list;
	}
	public static Map<String,Sample> mergeIdSampleMap(Map<String,Sample> spMap1,Map<String,Sample> spMap2){
		Map<String,Sample> spMap=new LinkedHashMap<String,Sample>();
		for(Map.Entry<String, Sample> entry:spMap1.entrySet()){
			spMap.put(entry.getKey(), entry.getValue());
		}
		for(Map.Entry<String, Sample> entry:spMap2.entrySet()){
			spMap.put(entry.getKey(), entry.getValue());
		}
		return spMap;
	}
	public static Map<String,Sample> mergeIdSampleMap(Map<String,Sample> spMap1,Map<String,Sample> spMap2,Map<String,Sample> spMap3){
		Map<String,Sample> spMap=new LinkedHashMap<String,Sample>((int) Math.ceil((spMap1.size()+spMap2.size()+spMap3.size())*1.5));
		for(Map.Entry<String, Sample> entry:spMap1.entrySet()){
			spMap.put(entry.getKey(), entry.getValue());
		}
		for(Map.Entry<String, Sample> entry:spMap2.entrySet()){
			spMap.put(entry.getKey(), entry.getValue());
		}
		for(Map.Entry<String, Sample> entry:spMap3.entrySet()){
			spMap.put(entry.getKey(), entry.getValue());
		}
		return spMap;
	}
	public static List<Sample> mergeSamples(List<Sample> samples1,List<Sample> samples2,List<Sample> samples3){
		List<Sample> merge=new LinkedList<Sample>();
		merge.addAll(samples1);
		merge.addAll(samples2);
		merge.addAll(samples3);
		return merge;
	}
	public static List<String> increStrListAbyB(List<String> a,List<String> b){
		for(String s:b){
			a.add(s);
		}
		return a;
	}
	public static List<String> toLinkedList(String[] t){
		LinkedList<String> list=new LinkedList<String>();
		for(String s:t) list.add(s);
		return list;
	}
	public static String[] set2Array(Set<String> set){
		String[] array=new String[set.size()];
		int i=0;
		for(String s:set) {
			array[i]=s;
			++i;
		}
		return array;
	}
	public static Set<String> trimDuplicate(List<String> lists){
		Set set=new LinkedHashSet<String>();
		for(String s:lists){
			set.add(s);
		}
		return set;
	}
	public static String[] getSubStrArry(String[] strArry,int start,int end){
		if(start>end||start<0||end>strArry.length||(end-start)>strArry.length) return null;
		String[] subArry=new String[end-start];
		int k=0;
		for(int i=0;i<strArry.length;++i){
			if(i>=start&&i<end){
				subArry[k]=strArry[i];
				++k;	
			}
			if(i>=end) break;
		}
		return subArry;
	}
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException{
	 List<String> a=new ArrayList<String>();
	 a.add("1");a.add("2");a.add("3");a.add("4");
	 List<String> b=new ArrayList<String>();
	 b.add("3");b.add("1");
	 System.out.println(CollectionTool.trimStrListBFromA(a, b));
	 
	}
}
