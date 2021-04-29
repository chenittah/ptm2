package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Commands {
	
	// Default IO interface
	public interface DefaultIO{
		public String readText();
		public void write(String text);
		public float readVal();

		public default void UploadFile(String fileName) {
			try {
				PrintWriter printFile = new PrintWriter(new FileWriter(fileName));
				String line;
				while(!(line = readText()).equals("done")) {
					printFile.println(line);
				}
				printFile.close();
			}

			catch (IOException e) {
				e.printStackTrace();
			}

		}

		void write(float val);
	}
	
	// the default IO to be used in all commands
	DefaultIO dio;
	public Commands(DefaultIO dio) {
		this.dio=dio;
	}
	
	// you may add other helper classes here
	
	
	
	// the shared state of all commands
	private class SharedState{
		public TimeSeries anomalyTest;
		public TimeSeries anomalyTrain;
		public SimpleAnomalyDetector anomalyDetector;
		public List<AnomalyReport> reportList;
	}
	
	private  SharedState sharedState=new SharedState();

	// Command abstract class
	public abstract class Command{
		protected String description;
		public Command(String description) {
			this.description=description;
		}
		
		public abstract void execute();
	}


	public class UploadCommand extends Command{

		public UploadCommand() {
			super("1. upload a time series csv file");
		}

		@Override
		public void execute() {
			dio.write(	"Please upload your local train CSV file.\n");
			dio.UploadFile("anomalyTrain.csv");
			dio.write(	"Upload complete.\n");
			sharedState.anomalyTrain = new TimeSeries("anomalyTrain.csv");

			dio.write(	"Please upload your local test CSV file.\n");
			dio.UploadFile("anomalyTest.csv");
			dio.write(	"Upload complete.\n");
			sharedState.anomalyTest = new TimeSeries("anomalyTest.csv");
		}
	}

	public class SettingsCommand extends Command{
		public SettingsCommand() {
			super("2. algorithm settings");
		}

		@Override
		public void execute() {
			float threshold = 0;
			dio.write("The current correlation threshold is " + SimpleAnomalyDetector.threshold + "\n");
			dio.write("type a new threshold\n");
			threshold = dio.readVal();
			dio.readText();

			while(threshold<0 || threshold>1){
				dio.write("please choose a value between 0 and 1\n");
				dio.write("The current correlation threshold is " + SimpleAnomalyDetector.threshold + "\n");
				dio.write("type a new threshold\n");
				threshold = dio.readVal();
				dio.readText();

			}
			SimpleAnomalyDetector.threshold = threshold;
		}
	}

	public class AnomalyDetactionCommand extends Command{

		public AnomalyDetactionCommand() {
			super("3. detect anomalies");
		}

		@Override
		public void execute()
		{
			sharedState.anomalyDetector = new SimpleAnomalyDetector();
			sharedState.anomalyDetector.learnNormal(sharedState.anomalyTrain);
			sharedState.reportList = sharedState.anomalyDetector.detect(sharedState.anomalyTest);
			dio.write("anomaly detection complete.\n");
		}
	}


	public class Result extends Command{

		public Result() {

			super("4. display results");

		}

		@Override
		public void execute()
		{
			for(AnomalyReport ar: sharedState.reportList){
				dio.write(ar.timeStep+"	"+ar.description +"\n");
			}
			dio.write("Done.\n");

		}
	}


	public class AnallizingCommand extends Command{
		public AnallizingCommand() {
			super("5. upload anomalies and analyze results");
		}

		@Override
		public void execute() {
			int p=0;
			int n=sharedState.anomalyTest.getSizeList();
			String line;
			String[] lineSplit ;
			float TP=0;
			float FP=0;
			float B=0;
			float count =0;
			List<String> listT = new ArrayList<>();
			dio.write("Please upload your local anomalies file.\n");
			while (!(line = dio.readText()).equals("done")) {
					lineSplit = line.split(",");
					n = n - (parseInt(lineSplit[1]) - parseInt(lineSplit[0]) + 1);
					p++;

					for (AnomalyReport ar : sharedState.reportList) {
						if ((ar.timeStep >= Integer.parseInt(lineSplit[0])) && (ar.timeStep <= Integer.parseInt(lineSplit[1]))) {
							if ((B + 1) == ar.timeStep) {
								B = ar.timeStep;
								continue;
							} else {
								if (listT.contains(ar.description)) {
									listT.remove(ar.description);
									FP--;
								} else {
									TP++;
									B = ar.timeStep;
									listT.add(ar.description);
								}
							}
						}
						else {
							if (listT.contains(ar.description)) {
								continue;
							} else {
								if ((count + 1) == ar.timeStep) {
									count =  ar.timeStep;
									continue;
								}
								else {
									FP++;
									listT.add(ar.description);
									count = ar.timeStep;
								}
							}
						}
					}
				}

			dio.write("Upload complete.\n");
			dio.write("True Positive Rate: " + Math.floor(TP/p*1000)/1000 +"\n");
			dio.write("False Positive Rate: " + Math.floor(FP/n*1000)/1000+"\n");


		}

		public long[] getRange(int index) {
			long[] arr = new long[3];
			arr[0] = sharedState.reportList.get(index).timeStep;
			arr[1]=sharedState.reportList.get(index).timeStep;
			arr[2]=index +1;

			for(int i=index+1 ;i<sharedState.reportList.size();i++) {
				if(((sharedState.reportList.get(i-1).timeStep+1)!=(sharedState.reportList.get(i).timeStep))&&
						(!sharedState.reportList.get(i-1).description.equals(sharedState.reportList.get(i).description))) {
					arr[1]=sharedState.reportList.get(i-1).timeStep;
					arr[2]=i;
					break;
				}
			}
			return arr;
		}
	}


	public class ExitCommand extends Command{
		public ExitCommand() {
			super("6. exit");
		}

		@Override
		public void execute() {
			//null
		}
	}
}
