package test;


import test.Commands.DefaultIO;
import test.Server.ClientHandler;

import java.io.*;
import java.util.Scanner;

public class AnomalyDetectionHandler implements ClientHandler{

	SocketIO sio;
	public class SocketIO implements DefaultIO {
		Scanner in;
		PrintWriter out;

		public SocketIO(InputStream in, OutputStream out) {
			this.in = new Scanner(new InputStreamReader(in));
			this.out =  new PrintWriter(out);
		}

		@Override
		public String readText()
		{
			return in.nextLine();
		}

		@Override
		public void write(String text)
		{
			out.print(text);
			out.flush();
		}

		@Override
		public float readVal()
		{
			return in.nextFloat();
		}

		@Override
		public void write(float val) {
			out.print(val);
			out.flush();
		}
		public void close() {
			in.close();
			out.close();
		}
	}
	public void start()
	{
		CLI cli=new CLI(this.sio);
		cli.start();
	}
	public void openDio(InputStream in, OutputStream out){this.sio= new SocketIO(in,out);}

}
