package test;

import java.util.ArrayList;

import test.Commands.Command;
import test.Commands.DefaultIO;

public class CLI {

	ArrayList<Command> commands;
	DefaultIO dio;
	test.Commands c;
	
	public CLI(DefaultIO dio) {
		this.dio=dio;
		c=new test.Commands(dio);
		commands=new ArrayList<>();
		commands.add(c.new UploadCommand());
		commands.add(c.new SettingsCommand());
		commands.add(c.new AnomalyDetactionCommand());
		commands.add(c.new Result());
		commands.add(c.new AnallizingCommand());
		commands.add(c.new ExitCommand());
	}
	
	public void start() {
		int num =0;
		while(num!=6) {
			dio.write("Welcome to the Anomaly Detection Server.\n"+  "Please choose an option:\n");
			for(Command c:commands) {
				dio.write(c.description + "\n");
			}

			if((num = (int)dio.readVal())>0 &&num<7){
				dio.readText();
				commands.get(num-1).execute();
			}

		}
	}
}
