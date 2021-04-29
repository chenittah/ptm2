package test;

import java.util.ArrayList;
import java.util.List;


public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector
{
	public static float threshold = (float) 0.95;
	private List<CorrelatedFeatures> NormalModel;

	public SimpleAnomalyDetector() {
		this.NormalModel = new ArrayList<>();
	}

	@Override
	public void learnNormal(TimeSeries ts)
	{
		float threshold = (float) 0.95;//0.9
		float tmpF = 0;
		float correlation = 0;
		float max = 0;
		int index = 0;
		Point[] pointarray;
		Line line;

		for (int i = 0; i < ts.getSizeTable(); i++) {
			/*for (int j = 0; j < ts.getSizeTable(); j++) {
				if (i != j) {
					tmpF = Math.abs(StatLib.pearson(ts.getArrayColumns(i), ts.getArrayColumns(j)));
					if (tmpF > correlation) {
						index = j;
						correlation = tmpF;
					}
				}
			}*/
			if (!(exist(ts, i, index))) {//check if exist a-c/c-a its the same
				//if (correlation >= threshold) {
					pointarray = TimeSeries.toArrayPoint(ts.getArrayColumns(i), ts.getArrayColumns(index));
					line = StatLib.linear_reg(pointarray);
					for (Point p : pointarray) {
						if ((tmpF = StatLib.dev(p, line)) > max) {
							max = tmpF;
						}
					}
					if (i < index)
						this.NormalModel.add(new CorrelatedFeatures(ts.getFeature(i), ts.getFeature(index), correlation, line, max * (float) 1.1));
					else
						this.NormalModel.add(new CorrelatedFeatures(ts.getFeature(index), ts.getFeature(i), correlation, line, max * (float) 1.1));
				//}

			}
			correlation = 0;
			max = 0;
		}
	}

	public boolean exist(TimeSeries s,int i, int j){
		for(CorrelatedFeatures cf:NormalModel)
		{
			if(Math.min(j,i) == s.featureIndex(cf.feature1))
			{
				if(Math.max(i,j) == s.featureIndex(cf.feature2))
				{
					return true;
				}
			}
		}
		return false;
	}


	@Override
	public List<AnomalyReport> detect(TimeSeries ts)
	{
		List<AnomalyReport> listAnomalyReport = new ArrayList<>();
		float num1,num2;
		Point p;
		for(int i=0;i<ts.getSizeList();i++){
			for(CorrelatedFeatures c:this.NormalModel){
				num1 = ts.getNum(ts.featureIndex(c.feature1),i);
				num2 = ts.getNum(ts.featureIndex(c.feature2),i);
				p = new Point(num1,num2);
				if(StatLib.dev(p,c.lin_reg) > c.threshold){
					//if(!(findRepotr(listAnomalyReport,c.feature1,c.feature2))) {
					listAnomalyReport.add(new AnomalyReport(c.feature1 + "-" + c.feature2, i+1 ));
					//System.out.println(c.feature1 + "-" + c.feature2);
					//}
				}
			}

		}
		return listAnomalyReport;
	}

	public List<CorrelatedFeatures> getNormalModel(){
		return this.NormalModel;
	}


}
