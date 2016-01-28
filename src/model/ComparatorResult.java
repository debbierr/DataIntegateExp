package model;

import java.util.Comparator;

import bean.Result;

public class ComparatorResult implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		Result sp1=(Result)o1;
		Result sp2=(Result)o2;
		if(sp1.getSupportForPredict()>sp2.getSupportForPredict()) return -1;
		else if(sp1.getSupportForPredict()<sp2.getSupportForPredict()) return 1;
		else return 0;
	}

}
