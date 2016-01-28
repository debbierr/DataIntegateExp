package bean;

public class Sample {
	private String id;
	private String lable;
	private String[] vec;
	public Sample(){}
	public Sample(String idLine,String seperater){
		String[] elmsId=idLine.split(seperater);
		this.setId(elmsId[0]);
		this.setLable(elmsId[1]);
	}
	public Sample(String idLine,String dataLine,String seperater){
		this(idLine,seperater);
		this.setVec(dataLine, seperater);	
	}
	public Sample(Sample sp){
		this.id=sp.getId();
		this.lable=sp.getLable();
		cloneVec(sp.getVec());
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public String[] getVec() {
		return vec;
	}
	public void setVec(String[] vec) {
		this.vec = vec;
	}
	public void setVec(String labelVecStr,String seperater){
		String[] elms=labelVecStr.split(seperater);
		String[] vec=new String[elms.length-1];
		for(int i=1;i<elms.length;++i){
			vec[i-1]=elms[i];
		}
		this.setVec(vec);
	}
	public String getVecStr(String seperater){
		StringBuilder stb=new StringBuilder();
		for(int i=0;i<vec.length;++i){
			if(i==(vec.length-1))
				stb.append(vec[i]);
			else
				stb.append(vec[i]+seperater);
		}
		return stb.toString();
	}
	public String getVecStr(){
		return this.getVecStr("\t");
	}
	public String getLabelAndVec(String seperater){
		StringBuilder stb=new StringBuilder();
		stb.append(this.lable+seperater);
		stb.append(this.getVecStr(seperater));
		return stb.toString();
	}
	public String getLabelAndVec(){
		return this.getLabelAndVec("\t");
	}
	public void cloneVec(String[] vec){
		this.vec=new String[vec.length];
		for(int i=0;i<vec.length;++i){
			this.vec[i]=vec[i];
		}
	}

}
