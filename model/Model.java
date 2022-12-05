package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

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
			s = new Socket("127.0.0.1", 1113);
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
		while(infinity) {
			if(timeToSend) {
				timeToSend = false;
				putObject(strToServ);
			}
			try {
				t.sleep(50);
			} catch (InterruptedException e) {}
		}//while
		
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
			System.out.print(strFromServ + '\n');
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
				if (str.contains(".")) {
					return str;
				} else {
					Thread.currentThread().sleep(50);
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