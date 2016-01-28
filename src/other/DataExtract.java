package other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import utils.FileTool;

public class DataExtract {
	public static void extract(String sourceFilePath,String sourceSeperater,
			String destUid,String destData,String destSeperater,int type) throws IOException{
		BufferedReader br=FileTool.getBufferedReaderFromFile(sourceFilePath);
		PrintWriter pwUid=FileTool.getPrintWriterForFile(destUid,true);
		PrintWriter pwData=FileTool.getPrintWriterForFile(destData,true);
		int lineNum=1;
		int start=(type-1)*1001+1;//type从1开始顺序编号
		int end=type*1001;
		String line; 
		while((line=br.readLine())!=null&&lineNum<=end){
			if(lineNum>start){//已经读到每个type下的具体向量
				int id=lineNum;
				pwUid.write(id+destSeperater+type+"\r\n");
				String[] elmsVec=line.split(sourceSeperater);
				pwData.write(type+"");
				for(String item:elmsVec){ 
					pwData.write(destSeperater+item);
				}
				pwData.write("\r\n");
			}
			++lineNum;
		}
		br.close();
		pwUid.close();
		pwData.close();	
	}
	public static void extract(String sourceFilePath,String sourceSeperater,
			String destDir,String destSeperater,int type){
		extract(sourceFilePath,sourceSeperater,destDir+"id.txt",destDir+"data.txt",type);
	}
	public static void extract(String sourceFilePath,String sourceSeperater,String destSeperater,int type) throws IOException{
		String destDir=FileTool.getParentPath(sourceFilePath)+"\\"+FileTool.getPureFileName(sourceFilePath)+"\\";
		extract(sourceFilePath,sourceSeperater,destDir+"id.txt",destDir+"data.txt",destSeperater,type);
	}
	public static void extract(String  sourceFilePath,String sourceSeperater,String destSeperater,int startType,int endType) throws IOException{
		if(startType>endType){
			System.out.println("Error: startType can't large than endType");
			return;
		}
		for(int i=startType;i<=endType;++i)
			extract(sourceFilePath,sourceSeperater,destSeperater,i);
	}
	public static void extractAllTypes(String sourceFilePath,String sourceSeperater,
			String destUid,String destData,String destSeperater) throws IOException{
		BufferedReader br=FileTool.getBufferedReaderFromFile(sourceFilePath);
		PrintWriter pwUid=FileTool.getPrintWriterForFile(destUid);
		PrintWriter pwData=FileTool.getPrintWriterForFile(destData);
		int lineNum=1;
		int type=1;
		int start=(type-1)*1001+1;//type从1开始顺序编号
		int end=type*1001;
		String line;
		while((line=br.readLine())!=null){
			if(lineNum>start&&lineNum<=end){//已经读到每个type下的具体向量
				int id=lineNum;
				pwUid.write(id+destSeperater+type+"\r\n");
				String[] elmsVec=line.split(sourceSeperater);
				pwData.write(type+"");
				for(String item:elmsVec){
					pwData.write(destSeperater+item);
				}
				pwData.write("\r\n");
			}
			++lineNum;
			if(lineNum>end){
				++type;
				start=(type-1)*1001+1;
				end=type*1001;
			}
		}
		br.close();
		pwUid.close();
		pwData.close();	
	}
	public static void extractAllTypes(String  sourceFilePath,String sourceSeperater,String destSeperater) throws IOException{
		String destDir=FileTool.getParentPath(sourceFilePath)+"\\"+FileTool.getPureFileName(sourceFilePath)+"\\";
		extractAllTypes(sourceFilePath,sourceSeperater,destDir+"id.txt",destDir+"data.txt",destSeperater);
	}
	public static void selectSmallAllTypes(String sourceFileDir,int start,int end,String destFileDir) throws IOException{
		BufferedReader readerId=FileTool.getBufferedReaderFromFile(sourceFileDir+"id.txt");
		BufferedReader readerData=FileTool.getBufferedReaderFromFile(sourceFileDir+"data.txt");
		PrintWriter writerId=FileTool.getPrintWriterForFile(destFileDir+"id.txt");
		PrintWriter writerData=FileTool.getPrintWriterForFile(destFileDir+"data.txt");
		
		String idLine="";
		String dataLine="";
		int lineNum=0;
		while((idLine=readerId.readLine())!=null&&(dataLine=readerData.readLine())!=null){
			if(lineNum>=start&&lineNum<end){
				writerId.write(idLine+"\r\n");
				writerData.write(dataLine+"\r\n");
			}
			lineNum=(lineNum+1)%1000;
		}
		writerId.close();
		writerData.close();
	}

	public static void excExtractAllTypes() throws IOException{
		String base="F:\\ExpData\\DataFromOther\\qty\\Data4Tritrain\\";
		String view1=base+"IMDB_Freq_Char3Gram_New.txt";
		String view2=base+"IMDB_Freq_DataTextTf.txt";
		String view3=base+"IMDB_Freq_OnlySyntac.txt";
		String destDir1=base+FileTool.getPureName(view1)+"\\";
		String destDir2=base+FileTool.getPureName(view2)+"\\";
		String destDir3=base+FileTool.getPureName(view3)+"\\";
		extractAllTypes(view1,"\\s{1,}","\t");
		extractAllTypes(view2,"\\s{1,}","\t");
		extractAllTypes(view3,"\\s{1,}","\t");
	}
	public static void excSelectSmallAllTypes() throws IOException{
		String base="F:/ExpData/DataIntegate/source/mutiCategory/PublicInfo/AllData/";
	     File dir=new File(base);
	     File[] ds=dir.listFiles();
	     for(File d:ds){
	    	 selectSmallAllTypes(d.getAbsolutePath()+"\\",0,500,FileTool.backReplaceDirNode(d.getAbsolutePath(), "AllData", "Data"));
	     }
	}
	public static void main(String[] args) throws IOException{
		excSelectSmallAllTypes();
	}

}
