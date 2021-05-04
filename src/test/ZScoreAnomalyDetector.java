package test;

import java.util.ArrayList;
import java.util.List;

public class ZScoreAnomalyDetector implements TimeSeriesAnomalyDetector {
    private List<ZScoreCorrelatedFeatures> NormalZscore;
    private ArrayList <Float> ZCompare;


    @Override
    public void learnNormal(TimeSeries ts) {
        float A;
        float max=0;
        for(int i=0;i<ts.getSizeTable();i++)
        {
            ZCompare.add(ts.getNum(i,0));//לוקח את האיבר הראשון בעמודה
            for(int j=1;j<ts.getSizeList();j++)
            {

                A=(Float) StatLib.Zscore(ZCompare,ts.getNum(i,j));
                if(A>max)
                    max=A;
                ZCompare.add(ts.getNum(i,j));
            }
        NormalZscore.add(new ZScoreCorrelatedFeatures(ts.getFeature(i),max));
        }
    }
    @Override
    public List<AnomalyReport> detect(TimeSeries ts)
    {
        float B;
        float ZSCORE=0;
        float max=0;
        List<AnomalyReport> reports = null;
        for(int i=0; i<ts.getSizeTable();i++) {
            ZCompare.add(ts.getNum(i,0));
            for (int j = 1; j < ts.getSizeList(); j++)
            {
                B=(Float) StatLib.Zscore(ZCompare,ts.getNum(i,j));
                if(B>ZSCORE)
                    ZSCORE=B;
                if(ts.getNum(i,j)>max)
                    max=ts.getNum(i,j);

                if(ZSCORE>max)
                    reports.add();
            }
        }
       return reports;
    }
}

