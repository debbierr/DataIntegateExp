package other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import utils.CollectionTool;
import utils.FileTool;

public class GeneratePublicInfo {
   public void generatePublicInfo(String sourceId,String publicInfoDir,float train,float test) throws IOException{
	   Map<String,List<String>> labelIds=IO.readLabelIdsMap(sourceId, "\t");
	   List<String> label1Ids=labelIds.get("1");
	   List<String> label2Ids=labelIds.get("2");
	   float tr=train;float te=test;float le=(1-train-test);
	   int size=label1Ids.size();
	   List<String> trainIds=CollectionTool.mergeStrList(getSubPortion(label1Ids,0,tr),getSubPortion(label2Ids,0,tr));
	   List<String> testIds=CollectionTool.mergeStrList(getSubPortion(label1Ids,tr,tr+te), getSubPortion(label2Ids,tr,tr+te));
	   List<String> learnIds=CollectionTool.mergeStrList(getSubPortion(label1Ids,tr+te,tr+te+le), getSubPortion(label2Ids,tr+te,tr+te+le));
	   
	   IO.writeId(publicInfoDir+"trainId.txt", trainIds);
	   IO.writeId(publicInfoDir+"testId.txt", testIds);
	   IO.writeId(publicInfoDir+"learnId.txt", learnIds);
   }
   protected List<String> getSubPortion(List<String> list,float sp,float ep){
	   int size=list.size();
	   int fromIndex=(int) (sp*size);
	   int toIndex=(int) (ep*size);
	   return list.subList(fromIndex, toIndex);
   }
   public void generatePublicInfoSpecial(String sourceId,String seperater,String publicInfoDir,float train,float test,int mod) throws IOException{
	   BufferedReader reader=FileTool.getBufferedReaderFromFile(sourceId);
	   PrintWriter writerTrainId=FileTool.getPrintWriterForFile(publicInfoDir+"trainId.txt");
	   PrintWriter writerTestId=FileTool.getPrintWriterForFile(publicInfoDir+"testId.txt");
	   PrintWriter writerLearnId=FileTool.getPrintWriterForFile(publicInfoDir+"learnId.txt");
	   String line="";
	   int trainNum=(int) (train*mod);
	   int testNum=(int) (test*mod);
	   int learnNum=mod-trainNum-testNum;
	   int lineNum=1;
	   int count=1;
	   while((line=reader.readLine())!=null){
		   String[] elms=line.split(seperater);
		   String id=elms[0];
		   if(count<=trainNum){
			   writerTrainId.write(id+"\r\n");
		   }else if(count<=trainNum+testNum){
			   writerTestId.write(id+"\r\n");
		   }else{
			   if(count==mod){
				   count=0;
			   }
			   writerLearnId.write(id+"\r\n");
		   }
		   ++count;
		   ++lineNum;
	   }
	   writerTrainId.close();
	   writerTestId.close();
	   writerLearnId.close();
   }
   
   
   public static void  main(String [] args) throws IOException{
	   String sourceId="F:\\ExpData\\DataIntegate\\source\\mutiCategory\\PublicInfo\\Data\\IMDB_Freq_Char3Gram_New\\id.txt";
	   String publicInfoDir="F:\\ExpData\\DataIntegate\\source\\mutiCategory\\PublicInfo\\ExpSet\\";
	   GeneratePublicInfo gp=new GeneratePublicInfo();
	   //gp.generatePublicInfo(sourceId, publicInfoDir,0.01f,0.05f);
	   gp.generatePublicInfoSpecial(sourceId, "\t", publicInfoDir, 0.01f, 0.2f,1000);
   }
}
