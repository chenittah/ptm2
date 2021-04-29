package test;

public class AnomalyDetector {

    public void learnNormal(TimeSeries ts) {

        float tmpF = 0;
        float correlation = 0;
        float max = 0;
        int index = 0;

        SimpleAnomalyDetector linearAlgo = new SimpleAnomalyDetector();
        ZScoreAnomalyDetector ZScoreAlgo = new ZScoreAnomalyDetector();
        HybridAnomalyDetector HybridAlgo = new HybridAnomalyDetector();

        for (int i = 0; i < ts.getSizeTable(); i++) {
            for (int j = 0; j < ts.getSizeTable(); j++) {
                if (i != j) {
                    tmpF = Math.abs(StatLib.pearson(ts.getArrayColumns(i), ts.getArrayColumns(j)));
                    if (tmpF > correlation) {
                        index = j;
                        correlation = tmpF;
                    }
                }
            }
            if (correlation >= 0.95)
                linearAlgo.learnNormal(ts);
            else if (correlation <0.5)
                ZScoreAlgo.learnNormal(ts);
            else
                HybridAlgo.learnNormal(ts);

        }
    }
}
