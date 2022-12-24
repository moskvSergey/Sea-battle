package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Model implements Runnable {
	private InputStream in;
	private OutputStream out;
	private Thread t;
	private Socket s;
	private String strToServ;
	private boolean timeToSend;
	private String strFromServ;
	private boolean timeToTake;
	private Waiter waiter;
	private boolean infinity;

	public Model() {
		try {
			s = new Socket("192.168.1.57", 1113);
			in = s.getInputStream();
			out = s.getOutputStream();
			this.t = new Thread(this, "Model");
			waiter = new Waiter();
			strFromServ = "";
			strToServ = "";
			timeToSend = false;
			timeToTake = false;
			infinity = true;
			t.start();
		} catch (Exception e) {}//try

	}//Constructor
	
	@Override
	public void run() {
		putObject(user.login+".");
		try {t.sleep(1000);} catch (InterruptedException e1) {}
		putObject(user.enemy+".");
		while(infinity) {
			if(timeToSend) {
				timeToSend = false;
				putObject(strToServ);
			}
			try {
				t.sleep(50);
			} catch (InterruptedException e) {}
		}//while
		try {in.close();out.close();} catch (IOException e) {}//streamsClosing}
	}//run

	private class Waiter implements Runnable{
		private Thread waiter;
		public Waiter() {
			try {
				waiter = new Thread(this, "Waiter");
				waiter.start();
			} catch (Exception e) {}//try

		}//Constructor
		@Override
		public void run(){
			while (infinity) {
				waitServer();
			}
		}
		private void waitServer() {
			strFromServ = getObject();
			timeToTake = true;
		}
	}
	
	public void sendToServer(String str) {
		strToServ = str;
		timeToSend = true;
	}//sendToServ
	
	public boolean canITakeFromServ() {
		return timeToTake;
		
	}//canTake (возвращает готов ли ответ сервера, если да, то клиент вызывает метод takeFromServ)
	
	public String takeFromServer() {
		timeToTake = false;
		return strFromServ;
	}//takeFromServ
	
	public Thread getThread() {
		return t;
	}
	
	private String getObject() {
		while (true) {
			try {
				int btsAvailable = in.available();
				byte[] bts = new byte[btsAvailable];
				int btsCount = in.read(bts, 0, btsAvailable);
				ByteArrayInputStream bis = new ByteArrayInputStream(bts);
				ObjectInputStream ois = new ObjectInputStream(bis);
				Object obj = ois.readObject();
				String str = (String) obj;
				if (str.contains(".") && str != strFromServ) {
					return str;
				} else {
					Thread.currentThread().sleep(20);
					continue;
				}} catch (Exception e) {continue;}//catch
		}//while
	}//getObject

	private void putObject(String str) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			Object obj = new String(str);
			oos.writeObject(obj);
			byte[] bts = bos.toByteArray();
			out.write(bts);
		} catch (Exception e) {}//try
	}//putObject

	
	public void die() {
		infinity = false;
	}
}//Model