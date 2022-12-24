import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;



class Player implements Runnable{
	private Thread t;
	private final static Object LOCK = new Object();
	private Socket s;
	private InputStream in;
	private OutputStream out;
	private int[][][] ship4;
	private int[][][] ship3;
	private int[][][] ship2;
	private int[][][] ship1;
	private boolean iAmReady;
	private int iAmFirst;
	private String cellAttackedStr;
	boolean cellAttacked;
	private int hisShipsDead;
	private boolean amIWin;
	private boolean infinity;
	
	
	
	public Player(Socket s, String name) {
		try {
			this.t = new Thread(this, name);
			in = s.getInputStream();
			out = s.getOutputStream();
			t.start();
		} catch (Exception e) {}
	}//Constructor
	
	{
		ship4 = new int[1][4][3];
		ship3 = new int[2][3][3];
		ship2 = new int[3][2][3];
		ship1 = new int[4][1][3];
		iAmReady = false;
		iAmFirst = 0;//0-не определено, 1-первый,2-второй
		cellAttackedStr = "";
		cellAttacked = false;
		amIWin = false;
		hisShipsDead = 0;
		infinity = true;
	}//initBlock
	
	@Override
	public void run() {
		putObject("start.");
		try {t.sleep(500);} catch (InterruptedException e) {}//sleep
		initializeMyFleet();
		while(iAmFirst == 0) {
			try {t.sleep(100);} catch (InterruptedException e) {}//sleep
		}//while
		if(iAmFirst == 2) {waiting();}
		while(infinity) {
			if(hisShipsDead == 10) {
				amIWin = true;
				putObject("won.");
				break;
			}
			putObject("giveCell.");
			String checkCellreturn = checkCell();
			if(checkCellreturn == "dead.") {
				hisShipsDead++;
			}
			putObject(checkCellreturn);//принимаю номер клетки и отвечаю
			if(checkCellreturn != "miss.") {
				try {t.sleep(200);} catch (InterruptedException e) {}
			}//если не промахнулся, то повторяешь ход
			else {
				changeReady();
				notifyPlayer();
				notifyPlayer();
				waiting();
			}
			
		}
		
		//streamClosing{
		try {in.close();out.close();} catch (IOException e) {}//streamsClosing}
	}//run
	
	
	private String checkCell() {
		String cell = getObject();
		int y = cell.charAt(0) - '0';
		int x = cell.charAt(1) - '0';
		cellAttackedStr = cell;
		cellAttacked = true;
		for (int i = 0; i < 4; i++) {
			if(i == 0) {
				for (int j = 0; j < 1; j++) {
					for(int k = 0; k < 4; k++) {
						if(ship4[j][k][0] == y &&
						   ship4[j][k][1] == x) {
							ship4[j][k][2] = 1;
							boolean isDead = true;
							for(int m = 0; m < 4; m++) {
								if(ship4[j][m][2] == 0) {
									isDead = false;
								}
							}//проверка остальных клеток(чтобы констатировать смерть)
							if (isDead) {return "dead.";}//умер
							else {return "injury.";}//просто ранен
						}//попал
					}//for k (проход по клеткам корабля)
				}//for j(проход по кораблям одного типа)
			}//четырехпалубник
			else if(i==2) {
				for (int j = 0; j < 2; j++) {
					for(int k = 0; k < 3; k++) {
						if(ship3[j][k][0] == y &&
						   ship3[j][k][1] == x) {
							ship3[j][k][2] = 1;
							boolean isDead = true;
							for(int m = 0; m < 3; m++) {
								if(ship3[j][m][2] == 0) {
									isDead = false;
								}
							}//проверка остальных клеток(чтобы констатировать смерть)
							if (isDead) {return "dead.";}//умер
							else {return "injury.";}//просто ранен
						}//попал
					}//for k (проход по клеткам корабля)
				}//for j(проход по кораблям одного типа)
			}//трехпалубники
			else if(i == 3) {
				for (int j = 0; j < 3; j++) {
					for(int k = 0; k < 2; k++) {
						if(ship2[j][k][0] == y &&
						   ship2[j][k][1] == x) {
							ship2[j][k][2] = 1;
							boolean isDead = true;
							for(int m = 0; m < 2; m++) {
								if(ship2[j][m][2] == 0) {
									isDead = false;
								}
							}//проверка остальных клеток(чтобы констатировать смерть)
							if (isDead) {return "dead.";}//умер
							else {return "injury.";}//просто ранен
						}//попал
					}//for k (проход по клеткам корабля)
				}//for j(проход по кораблям одного типа)
			}//двухпалуюники
			else {
				for (int j = 0; j < 4; j++) {
					for(int k = 0; k < 1; k++) {
						if(ship1[j][k][0] == y &&
						   ship1[j][k][1] == x) {
							ship1[j][k][2] = 1;
							boolean isDead = true;
							for(int m = 0; m < 1; m++) {
								if(ship1[j][m][2] == 0) {
									isDead = false;
								}
							}//проверка остальных клеток(чтобы констатировать смерть)
							if (isDead) {return "dead.";}//умер
							else {return "injury.";}//просто ранен
						}//попал
					}//for k (проход по клеткам корабля)
				}//for j(проход по кораблям одного типа)
			}//однопалубники
		}//for i(проход по всем кораблям)
		return "miss."; // не попал
	}//checkCell
	
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
	
	
	public void putObject(String str){
		try {	   		   		    
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);		   
			Object obj = new String(str);			            
			oos.writeObject(obj);
			byte[] bts = bos.toByteArray();
			out.write(bts);
		}	catch (Exception e){}
		
	}//PutObject
	
	public int[][][] swap(int number){
		if(number == 1) {
			return ship4;
		}else if (number == 2) {
			return ship3;
		}
		else if(number == 3) {
			return ship2;
		}else {
			return ship1;
		}
	}
	
	public void swap2(int[][][] ship, int number){
		if(number == 1) {
			ship4 = ship;
		}else if (number == 2) {
			ship3 = ship;
		}
		else if(number == 3) {
			ship2 = ship;
		}else {
			ship1 = ship;
		}
	}
	
	public void cellAtacked(String cell) {
		putObject(cell + "attacked.");
	}
	public boolean isCellAttacked() {
		return cellAttacked;
	}
	public String getAttackedCell() {
		cellAttacked = false;
		return cellAttackedStr;
	}
	
	private void initializeMyFleet() {
		for(int i = 0; i < 4; i++) {
			String ship4S = getObject();
			ship4[0][i][0] = (ship4S.charAt(0) - '0');
			ship4[0][i][1] = (ship4S.charAt(1) - '0');
			ship4[0][i][2] = 0;//ранена ли клетка
		}//четырехпалубник
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 3; j++) {
				String ship3S = getObject();
				ship3[i][j][0] = (ship3S.charAt(0) - '0');
				ship3[i][j][1] = (ship3S.charAt(1) - '0');
				ship3[i][j][2] = 0;//ранена ли клетка
			}
		}//трехпалубники
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 2; j++) {
				String ship2S = getObject();
				ship2[i][j][0] = (ship2S.charAt(0) - '0');
				ship2[i][j][1] = (ship2S.charAt(1) - '0');
				ship2[i][j][2] = 0;//ранена ли клетка
			}
		}//двухпалубники
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 1; j++) {
				String ship1S = getObject();
				ship1[i][j][0] = (ship1S.charAt(0) - '0');
				ship1[i][j][1] = (ship1S.charAt(1) - '0');
				ship1[i][j][2] = 0;//ранена ли клетка
			}
		}//однопалубники
		changeReady();
	}//initFleet
	
	
	public boolean isReady() {
		return iAmReady;
	}//isReady
	
	public void changeReady() {
		iAmReady = !iAmReady;
	}//changeReady
	
	public void youAreFirst(boolean yes) {
		if(yes) {iAmFirst = 1;}
		else {iAmFirst = 2;}
	}//first
	
	private void waiting() {
		 synchronized (LOCK) {
			 try {
				 LOCK.wait();
             } catch (InterruptedException e) {
            	 e.printStackTrace();
             }
         }
	}//waiting
	private void notifyPlayer() {
		try {t.sleep(500);} catch (InterruptedException e) {}
		synchronized (LOCK) {
			 LOCK.notifyAll();
        }
	}
	
	public boolean areYouWin() {
		return amIWin;
	}
	
	public void youAreLoser() {
		putObject("lr.");
	}
	
	public Thread getThread() {
		return t;
	}
	
	public void die() {
		infinity = false;
	}
	
	
}//Player



class Game implements Runnable {
	private Thread t;
	private Player player1;
	private Player player2;
	private boolean turn;
	private boolean infinity;

	public Game(Socket s, Socket s2) {
		try {
			this.t = new Thread(this, "Game");
			player1 = new Player(s, "Player 1");
			player2 = new Player(s2, "Player 2");
			turn = true;
			infinity = true;
			t.start();
		} catch (Exception e) {}//catch
	}//Construtor

	@Override
	public void run() {
		waitPlayer(player1);
		waitPlayer(player2);
		int[][][] swap = new int[1][4][3];
		swap = player1.swap(1);
		player1.swap2(player2.swap(1),1);
		player2.swap2(swap, 1);
		swap = new int[2][3][3];
		swap = player1.swap(2);
		player1.swap2(player2.swap(2),2);
		player2.swap2(swap, 2);
		swap = new int[3][2][3];
		swap = player1.swap(3);
		player1.swap2(player2.swap(3),3);
		player2.swap2(swap, 3);
		swap = new int[4][1][3];
		swap = player1.swap(4);
		player1.swap2(player2.swap(4),4);
		player2.swap2(swap, 4);
		player1.changeReady();
		player2.changeReady();
		player1.youAreFirst(true);
		player2.youAreFirst(false);
		while(infinity) {
			if(turn) {
				waitPlayer(player1);
				player1.changeReady();
			}
			else {
				waitPlayer(player2);
				player2.changeReady();
			}//ждем пока плеер ошибется
			turn = !turn;
		}
		
	}//run
	
	
	private boolean waitPlayer(Player player){
		while(true) {
			if(turn) {
				if(player1.cellAttacked) {
					player2.cellAtacked(player1.getAttackedCell());
				}
				if(player1.areYouWin()) {
					player2.youAreLoser();
					player2.die();
				}
			}else {
				if(player2.cellAttacked) {
					player1.cellAtacked(player2.getAttackedCell());
				}
				if(player2.areYouWin()) {
					player1.youAreLoser();
					player1.die();
				}
			}
			if (player.isReady()) {return true;}
			try {t.sleep(200);} catch (InterruptedException e) {}//sleep
		}//while
		
	}//wait

	
	private void die() {
		infinity = false;
	}
	
}//Game



class Waiter implements Runnable{
	private Thread t;
	private Socket s;
	private InputStream in;
	private int numb;
	private String myName;
	private String against;
	
	public Waiter(Socket s, int numb) {
		try {
			this.t = new Thread(this, "Waiter");
			in = s.getInputStream();
			this.numb = numb;
			t.start();
		} catch (Exception e) {}
	}//Constructor
	
	{
	}//init
	
	@Override
	public void run() {
		myName = getObject();
		against = getObject();
		Server.users[numb] = myName;
		Server.against[numb] = against;
		boolean flag = true;
		while(flag) {
			for(int i = 0; i < 20; i++) {
				try {
					if(Server.slots[numb] == 0) {flag = false;break;}
					if(i == numb) {continue;}
					if(Server.slots[i] == 1) {
						if (against.contains("none.") && Server.against[i].contains("none")) {
							Server.startGame(numb, i);
							flag = false;
							break;
						}
						else if (Server.users[i].contains(against) && 
								(Server.against[i].contains("none.") || 
								Server.against[i].contains(myName))) {
							Server.startGame(numb, i);
							flag = false;
							break;
						}
						if (Server.against[i].contains(myName) && 
								(against.contains("none.") || 
								against.contains(Server.against[i]))) {
							Server.startGame(numb, i);
							flag = false;
							break;
						}
					}
				}catch(Throwable e) {continue;}
			}
		}//while
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
}//Waiter


public class Server {
	public static Socket[] sockets;
	public static String[] users;
	public static String[] against;
	public static int[] slots;
	public static int usersNumber;
	public static int yourNumberIs;
	
	
	public static void main(String args[]) {
		sockets = new Socket[20];
		users = new String[20];
		against = new String[20];
		slots = new int[20];
		usersNumber = 0;
		yourNumberIs = 0;
		try {
			ServerSocket ss = new ServerSocket(1113);
			while (true) {
				Socket s = ss.accept();
				for (int i = 0; i < 20; i++) {
					if (slots[i] == 0) {
						sockets[i] = s;
						slots[i] = 1;
						new Waiter(s, i);
						break;
					}
				}
			}/*while*/}/*try*/ catch (Exception e) {}//catch
		}//main
	
	public static void startGame(int player1, int player2) {
		synchronized(slots) {
			if (slots[player1] != 0 && slots[player2] != 0) {
				new Game(sockets[player1], sockets[player2]);
				slots[player1] = 0;
				slots[player2] = 0;
			}	
		}
		
		
	}
}//Server

