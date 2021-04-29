package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Server {

	public interface ClientHandler
	{
		 public void start();
		public void openDio(InputStream in, OutputStream out);

	}
	private int clientLimit;
	volatile boolean stop;
	public Server() {
		stop=false;
	}

	private void startServer(int port, ClientHandler ch)
	{
		while(!this.stop)
		{

			try {
				ServerSocket server = new ServerSocket(port);
				try {
					server.setSoTimeout(1000);

					Socket client = server.accept();

					InputStream in = client.getInputStream();
					OutputStream out = client.getOutputStream();
					ch.openDio(in,out);
					ch.start();

					client.close();

				} catch (SocketTimeoutException e) {}

				server.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
	}
	
	// runs the server in its own thread
	public void start(int port, ClientHandler ch) {
		new Thread(()->startServer(port,ch)).start();
	}
	
	public void stop() {
		stop=true;
	}
}
