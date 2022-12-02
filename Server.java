import java.net.*;
import java.io.*;



class Player implements Runnable{
	private Thread t;
	private Socket s;
	private InputStream in;
	private OutputStream out;
	private int[][][] ship4;
	private int[][][] ship3;
	private int[][][] ship2;
	private int[][][] ship1;
	private boolean iAmReady;
	
	
	public Player(Socket s, String name) {
		try {
			this.t = new Thread(this, name);
			in = s.getInputStream();
			out = s.getOutputStream();
			t.start();
		} catch (Exception e) {}
	}//Constructor
	
	{
		ship4 = new int[1][4][2];
		ship3= new int[2][3][2];
		ship2 = new int[3][2][2];
		ship1 = new int[4][1][2];
		iAmReady = false;
	}//initBlock
	
	@Override
	public void run() {
		
		
		
		
		
		
		
		
		//streamClosing{
		try {in.close();out.close(); s.close();} catch (IOException e) {}//streamsClosing}
	}//run
	
	
	private String getObject(){
		while(true) {
			try {	   	
				int btsAvailable = in.available();
				byte[] bts = new byte[btsAvailable];
				int btsCount =in.read(bts,0,btsAvailable);           
				ByteArrayInputStream bis =new ByteArrayInputStream(bts);
				ObjectInputStream ois = new ObjectInputStream(bis);
				Object obj =ois.readObject();
				String str = (String)obj;
				if (str.contains(".")) {
					return str;
				}
				//sleep{
				else {continue;}}	catch (Exception e) {try {Thread.currentThread().sleep(10);} catch (InterruptedException e2) {}{}}//sleep}
		}//while
	}//getObj
	
	
	public static void putObject(String str){
		try {	   		   		    
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);		   
			Object obj = new String(str);			            
			oos.writeObject(obj);
			byte[] bts = bos.toByteArray();
		}	catch (Exception e){}
		
	}//PutObject
	
	
	private void initializeMyFleet() {
		String ship4S = getObject();
		for(int i = 0; i < 4; i++) {
			ship4[0][i][0] = (ship4S.charAt(i * 2));
			ship4[0][i][1] = (ship4S.charAt(i * 2 + 1));
		}//четырехпалубник
		for(int i = 0; i < 2; i++) {
			String ship3S = getObject();
			for(int j = 0; j < 3; j++) {
				ship3[i][j][0] = (ship3S.charAt(j * 2));
				ship3[i][j][1] = (ship3S.charAt(j * 2 + 1));
			}
		}//трехпалубники
		for(int i = 0; i < 3; i++) {
			String ship2S = getObject();
			for(int j = 0; j < 2; j++) {
				ship2[i][j][0] = (ship2S.charAt(j * 2));
				ship2[i][j][1] = (ship2S.charAt(j * 2 + 1));
			}
		}//двухпалубники
		for(int i = 0; i < 4; i++) {
			String ship1S = getObject();
			for(int j = 0; j < 1; j++) {
				ship1[i][j][0] = (ship1S.charAt(j * 2));
				ship1[i][j][1] = (ship1S.charAt(j * 2 + 1));
			}
		}//однопалубники
	}//initFleet
	
	
	public boolean isReady() {
		return iAmReady;
	}
	
}//Player



class Game implements Runnable {
	private Thread t;
	private Player player1;
	private Player player2;

	public Game(Socket s, Socket s2) {
		try {
			this.t = new Thread(this, "Game");
			player1 = new Player(s, "Player 1");
			player2 = new Player(s2, "Player 2");
			t.start();
		} catch (Exception e) {}//catch
	}//Construtor

	@Override
	public void run() {
		waitPlayer(player1);
		waitPlayer(player2);
	}//run
	
	private boolean waitPlayer(Player player){
		while(true) {
			if (player.isReady()) {return true;}
			try {t.sleep(200);} catch (InterruptedException e) {}//sleep
		}//while
		
	}//wait


}//Game

class Server {
	public static void main(String args[]) {
		try {
			ServerSocket ss = new ServerSocket(1113);
			while (true) {
				Socket s = ss.accept();
				Socket s2 = ss.accept();
				new Game(s, s2);
			}/*while*/}/*try*/ catch (Exception e) {}//catch
		}//main
}//Server
