package test;

import java.util.List;

public class ZScoreAnomalyDetector implements TimeSeriesAnomalyDetector {
    private List<ZScoreCorrelatedFeatures> NormalZscore;


    @Override
    public void learnNormal(TimeSeries ts) {
        float A;

        for(int i=0;i<ts.getSizeTable();i++) {
            for(int j=0;j<ts.getSizeTable();j++)
            {
                //A=/*(Float)StatLib.Zscore(*/ts.getArrayColumns(i),ts.getNum(i,j));
                A = 0;
                NormalZscore.add(new ZScoreCorrelatedFeatures(ts.getFeature(i),A));
            }
        }

    }

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        return null;
    }
}

