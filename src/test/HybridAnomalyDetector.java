package test;

import java.util.List;

public class HybridAnomalyDetector implements TimeSeriesAnomalyDetector{


    @Override
    public void learnNormal(TimeSeries ts) {
        Point[] pointarray;
        double sumMinDis=0;
        double sumDis=0;
        double sumMaxDis = 0;
        double maxDis = 0;

        for (int i = 0; i < ts.getSizeTable(); i++) {
            pointarray = TimeSeries.toArrayPoint(ts.getArrayColumns(i), ts.getArrayColumns(i));
            for (Point p : pointarray) {
                for (Point p1:pointarray) {
                    sumDis += StatLib.distance(p,p1);
                    if (sumDis>maxDis)
                        maxDis = sumDis;
                }
                if (sumDis<sumMinDis) {
                    sumMinDis = sumDis;
                    sumMaxDis = maxDis;
                }

                SmallestCircle(p,sumMaxDis);

            }
        }
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        return null;
    }

    public void SmallestCircle(Point p, double radius){
        
    }
}
