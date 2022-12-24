package controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.Model;



class ControllerToModel implements Runnable {
	private Thread t;
    private Model model;
    private Controller controller;
    private int[][] cells;
    private int[][] hisCells;
    private boolean canIAttack;
	private int done;//подсчет сколько клеток уже расставленно
	//
	private int clickedButtonY, clickedButtonX;
	private boolean buttonIsClicked;
	//нужно для того чтобы ЭТОТ поток запускал функции, а не javafxAp
	private String ship;

	public ControllerToModel(Controller controller) {
		try {
			this.t = new Thread(this, "ControllerToModel");
		    model = new Model();
		    this.controller = controller;
			t.start();
		} catch (Exception e) {}
	}//Constructor
	
	{
		cells = new int[10][10];
		hisCells = new int[10][10];
		done = 0;
		ship = "";
		clickedButtonY = 0;
		clickedButtonX = 0;
		canIAttack = false;
		buttonIsClicked = false;
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				hisCells[i][j] = 0;
			}
		}
	}//initializeBlock
	
	@Override
	public void run() {
		while(true) {
			if (model.canITakeFromServ()) {
				String servString = model.takeFromServer();
				if (servString.contains("start.")) {
					break;
				}
			}
			else {
				try {
					t.sleep(20);
				} catch (InterruptedException e) {}
			}
			
		}	
		updateButtons();
		while(done != 20) {
			if(buttonIsClicked) {buttonClicked();}
			try {
				t.sleep(20);
			} catch (InterruptedException e) {}
		}
		while (true) {
			if (model.canITakeFromServ()) {
				String servString = model.takeFromServer();
				if (servString.contains("won.")) {
					winner();
					break;
				}
				if (servString.contains("lr.")) {
					loser();
					break;
				}
				else if(servString.contains("attacked.")) {
					if(cells[servString.charAt(0)-'0'][servString.charAt(1)-'0'] == 2) {
						cells[servString.charAt(0)-'0'][servString.charAt(1)-'0'] = 4;
					}
					else {
						cells[servString.charAt(0)-'0'][servString.charAt(1)-'0'] = 5;
					}
				}
				else if(servString.contains("giveCell.")) {
					canIAttack = true;
				}else if(servString.contains("miss.")) {
					hisCells[clickedButtonY][clickedButtonX] = 1;
				}
				else if(servString.contains("injury.")) {
					hisCells[clickedButtonY][clickedButtonX] = 2;
				}
				else if(servString.contains("dead.")) {
					hisCells[clickedButtonY][clickedButtonX] = 3;
				}
				updateButtons();
			}
			if (buttonIsClicked) {buttonClicked();}
		}
	}//run
	
	public void clickedButtonNumber(int y, int x) {
		clickedButtonY = y;
		clickedButtonX = x;
		buttonIsClicked = true;
	}
	
	private void attackCell() {
		buttonIsClicked = false;
		model.sendToServer(Integer.toString(clickedButtonY) + 
				Integer.toString(clickedButtonX) + ".");
		canIAttack = false;
	}
	
	private void buttonClicked() {
		buttonIsClicked = false;
		if(done != 20) {placeMyFleet(clickedButtonY, clickedButtonX);}
		else {attackCell();}
		updateButtons();
	}
	
	private void updateButtons() {
		Platform.runLater(new Runnable() { 
			public void run() {
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						if(cells[i][j] == 2) {
							getMyButton(i, j).setStyle("-fx-border-color: black; -fx-font-size: 16px;");
							getMyButton(i, j).setStyle("-fx-color: green; -fx-font-size: 16px;");
							getMyButton(i, j).setDisable(true);
						}
						else if(cells[i][j] == 4) {
							getMyButton(i, j).setStyle("-fx-color: red; -fx-font-size: 16px;");
						}
						else if(cells[i][j] == 5) {
							getMyButton(i, j).setStyle("-fx-color: gray; -fx-font-size: 16px;");
							getMyButton(i, j).setText(".");
						}
						else if (done == 20) {
							getMyButton(i, j).setDisable(true);
						}
						else if(cells[i][j] == 0) {
							getMyButton(i, j).setDisable(false);
						}
						
						else {
							getMyButton(i, j).setDisable(true);
						}//обновление моих полей
						
						if(canIAttack) {
							getHisButton(i, j).setDisable(false);
						}
						else if(!canIAttack) {
							getHisButton(i, j).setDisable(true);
						}
						if(hisCells[i][j] == 1) {
							getHisButton(i, j).setText(".");
							getHisButton(i, j).setStyle("-fx-color: gray; -fx-font-size: 16px;");
						}
						else if(hisCells[i][j] == 2) {
							getHisButton(i, j).setText("#");
							getHisButton(i, j).setStyle("-fx-color: pink; -fx-font-size: 16px;");
						}else if(hisCells[i][j] == 3) {
							getHisButton(i, j).setText("X");
							getHisButton(i, j).setStyle("-fx-color: red; -fx-font-size: 16px;");
						}
						
					}
				}
			}
		});
		
	}
	private Button getMyButton(int y, int x) {
		if(y == 0 && x == 0) {return controller.cell00;}
		else if(y == 0 && x == 1) {return controller.cell01;}
		else if(y == 0 && x == 2) {return controller.cell02;}
		else if(y == 0 && x == 3) {return controller.cell03;}
		else if(y == 0 && x == 4) {return controller.cell04;}
		else if(y == 0 && x == 5) {return controller.cell05;}
		else if(y == 0 && x == 6) {return controller.cell06;}
		else if(y == 0 && x == 7) {return controller.cell07;}
		else if(y == 0 && x == 8) {return controller.cell08;}
		else if(y == 0 && x == 9) {return controller.cell09;}
		else if(y == 1 && x == 0) {return controller.cell10;}
		else if(y == 1 && x == 1) {return controller.cell11;}
		else if(y == 1 && x == 2) {return controller.cell12;}
		else if(y == 1 && x == 3) {return controller.cell13;}
		else if(y == 1 && x == 4) {return controller.cell14;}
		else if(y == 1 && x == 5) {return controller.cell15;}
		else if(y == 1 && x == 6) {return controller.cell16;}
		else if(y == 1 && x == 7) {return controller.cell17;}
		else if(y == 1 && x == 8) {return controller.cell18;}
		else if(y == 1 && x == 9) {return controller.cell19;}
		else if(y == 2 && x == 0) {return controller.cell20;}
		else if(y == 2 && x == 1) {return controller.cell21;}
		else if(y == 2 && x == 2) {return controller.cell22;}
		else if(y == 2 && x == 3) {return controller.cell23;}
		else if(y == 2 && x == 4) {return controller.cell24;}
		else if(y == 2 && x == 5) {return controller.cell25;}
		else if(y == 2 && x == 6) {return controller.cell26;}
		else if(y == 2 && x == 7) {return controller.cell27;}
		else if(y == 2 && x == 8) {return controller.cell28;}
		else if(y == 2 && x == 9) {return controller.cell29;}
		else if(y == 3 && x == 0) {return controller.cell30;}
		else if(y == 3 && x == 1) {return controller.cell31;}
		else if(y == 3 && x == 2) {return controller.cell32;}
		else if(y == 3 && x == 3) {return controller.cell33;}
		else if(y == 3 && x == 4) {return controller.cell34;}
		else if(y == 3 && x == 5) {return controller.cell35;}
		else if(y == 3 && x == 6) {return controller.cell36;}
		else if(y == 3 && x == 7) {return controller.cell37;}
		else if(y == 3 && x == 8) {return controller.cell38;}
		else if(y == 3 && x == 9) {return controller.cell39;}
		else if(y == 4 && x == 0) {return controller.cell40;}
		else if(y == 4 && x == 1) {return controller.cell41;}
		else if(y == 4 && x == 2) {return controller.cell42;}
		else if(y == 4 && x == 3) {return controller.cell43;}
		else if(y == 4 && x == 4) {return controller.cell44;}
		else if(y == 4 && x == 5) {return controller.cell45;}
		else if(y == 4 && x == 6) {return controller.cell46;}
		else if(y == 4 && x == 7) {return controller.cell47;}
		else if(y == 4 && x == 8) {return controller.cell48;}
		else if(y == 4 && x == 9) {return controller.cell49;}
		else if(y == 5 && x == 0) {return controller.cell50;}
		else if(y == 5 && x == 1) {return controller.cell51;}
		else if(y == 5 && x == 2) {return controller.cell52;}
		else if(y == 5 && x == 3) {return controller.cell53;}
		else if(y == 5 && x == 4) {return controller.cell54;}
		else if(y == 5 && x == 5) {return controller.cell55;}
		else if(y == 5 && x == 6) {return controller.cell56;}
		else if(y == 5 && x == 7) {return controller.cell57;}
		else if(y == 5 && x == 8) {return controller.cell58;}
		else if(y == 5 && x == 9) {return controller.cell59;}
		else if(y == 6 && x == 0) {return controller.cell60;}
		else if(y == 6 && x == 1) {return controller.cell61;}
		else if(y == 6 && x == 2) {return controller.cell62;}
		else if(y == 6 && x == 3) {return controller.cell63;}
		else if(y == 6 && x == 4) {return controller.cell64;}
		else if(y == 6 && x == 5) {return controller.cell65;}
		else if(y == 6 && x == 6) {return controller.cell66;}
		else if(y == 6 && x == 7) {return controller.cell67;}
		else if(y == 6 && x == 8) {return controller.cell68;}
		else if(y == 6 && x == 9) {return controller.cell69;}
		else if(y == 7 && x == 0) {return controller.cell70;}
		else if(y == 7 && x == 1) {return controller.cell71;}
		else if(y == 7 && x == 2) {return controller.cell72;}
		else if(y == 7 && x == 3) {return controller.cell73;}
		else if(y == 7 && x == 4) {return controller.cell74;}
		else if(y == 7 && x == 5) {return controller.cell75;}
		else if(y == 7 && x == 6) {return controller.cell76;}
		else if(y == 7 && x == 7) {return controller.cell77;}
		else if(y == 7 && x == 8) {return controller.cell78;}
		else if(y == 7 && x == 9) {return controller.cell79;}
		else if(y == 8 && x == 0) {return controller.cell80;}
		else if(y == 8 && x == 1) {return controller.cell81;}
		else if(y == 8 && x == 2) {return controller.cell82;}
		else if(y == 8 && x == 3) {return controller.cell83;}
		else if(y == 8 && x == 4) {return controller.cell84;}
		else if(y == 8 && x == 5) {return controller.cell85;}
		else if(y == 8 && x == 6) {return controller.cell86;}
		else if(y == 8 && x == 7) {return controller.cell87;}
		else if(y == 8 && x == 8) {return controller.cell88;}
		else if(y == 8 && x == 9) {return controller.cell89;}
		else if(y == 9 && x == 0) {return controller.cell90;}
		else if(y == 9 && x == 1) {return controller.cell91;}
		else if(y == 9 && x == 2) {return controller.cell92;}
		else if(y == 9 && x == 3) {return controller.cell93;}
		else if(y == 9 && x == 4) {return controller.cell94;}
		else if(y == 9 && x == 5) {return controller.cell95;}
		else if(y == 9 && x == 6) {return controller.cell96;}
		else if(y == 9 && x == 7) {return controller.cell97;}
		else if(y == 9 && x == 8) {return controller.cell98;}
		else {return controller.cell99;}
		
	}

	private Button getHisButton(int y, int x) {
		if(y == 0 && x == 0) {return controller.cell001;}
		else if(y == 0 && x == 1) {return controller.cell011;}
		else if(y == 0 && x == 2) {return controller.cell021;}
		else if(y == 0 && x == 3) {return controller.cell031;}
		else if(y == 0 && x == 4) {return controller.cell041;}
		else if(y == 0 && x == 5) {return controller.cell051;}
		else if(y == 0 && x == 6) {return controller.cell061;}
		else if(y == 0 && x == 7) {return controller.cell071;}
		else if(y == 0 && x == 8) {return controller.cell081;}
		else if(y == 0 && x == 9) {return controller.cell091;}
		else if(y == 1 && x == 0) {return controller.cell101;}
		else if(y == 1 && x == 1) {return controller.cell111;}
		else if(y == 1 && x == 2) {return controller.cell121;}
		else if(y == 1 && x == 3) {return controller.cell131;}
		else if(y == 1 && x == 4) {return controller.cell141;}
		else if(y == 1 && x == 5) {return controller.cell151;}
		else if(y == 1 && x == 6) {return controller.cell161;}
		else if(y == 1 && x == 7) {return controller.cell171;}
		else if(y == 1 && x == 8) {return controller.cell181;}
		else if(y == 1 && x == 9) {return controller.cell191;}
		else if(y == 2 && x == 0) {return controller.cell201;}
		else if(y == 2 && x == 1) {return controller.cell211;}
		else if(y == 2 && x == 2) {return controller.cell221;}
		else if(y == 2 && x == 3) {return controller.cell231;}
		else if(y == 2 && x == 4) {return controller.cell241;}
		else if(y == 2 && x == 5) {return controller.cell251;}
		else if(y == 2 && x == 6) {return controller.cell261;}
		else if(y == 2 && x == 7) {return controller.cell271;}
		else if(y == 2 && x == 8) {return controller.cell281;}
		else if(y == 2 && x == 9) {return controller.cell291;}
		else if(y == 3 && x == 0) {return controller.cell301;}
		else if(y == 3 && x == 1) {return controller.cell311;}
		else if(y == 3 && x == 2) {return controller.cell321;}
		else if(y == 3 && x == 3) {return controller.cell331;}
		else if(y == 3 && x == 4) {return controller.cell341;}
		else if(y == 3 && x == 5) {return controller.cell351;}
		else if(y == 3 && x == 6) {return controller.cell361;}
		else if(y == 3 && x == 7) {return controller.cell371;}
		else if(y == 3 && x == 8) {return controller.cell381;}
		else if(y == 3 && x == 9) {return controller.cell391;}
		else if(y == 4 && x == 0) {return controller.cell401;}
		else if(y == 4 && x == 1) {return controller.cell411;}
		else if(y == 4 && x == 2) {return controller.cell421;}
		else if(y == 4 && x == 3) {return controller.cell431;}
		else if(y == 4 && x == 4) {return controller.cell441;}
		else if(y == 4 && x == 5) {return controller.cell451;}
		else if(y == 4 && x == 6) {return controller.cell461;}
		else if(y == 4 && x == 7) {return controller.cell471;}
		else if(y == 4 && x == 8) {return controller.cell481;}
		else if(y == 4 && x == 9) {return controller.cell491;}
		else if(y == 5 && x == 0) {return controller.cell501;}
		else if(y == 5 && x == 1) {return controller.cell511;}
		else if(y == 5 && x == 2) {return controller.cell521;}
		else if(y == 5 && x == 3) {return controller.cell531;}
		else if(y == 5 && x == 4) {return controller.cell541;}
		else if(y == 5 && x == 5) {return controller.cell551;}
		else if(y == 5 && x == 6) {return controller.cell561;}
		else if(y == 5 && x == 7) {return controller.cell571;}
		else if(y == 5 && x == 8) {return controller.cell581;}
		else if(y == 5 && x == 9) {return controller.cell591;}
		else if(y == 6 && x == 0) {return controller.cell601;}
		else if(y == 6 && x == 1) {return controller.cell611;}
		else if(y == 6 && x == 2) {return controller.cell621;}
		else if(y == 6 && x == 3) {return controller.cell631;}
		else if(y == 6 && x == 4) {return controller.cell641;}
		else if(y == 6 && x == 5) {return controller.cell651;}
		else if(y == 6 && x == 6) {return controller.cell661;}
		else if(y == 6 && x == 7) {return controller.cell671;}
		else if(y == 6 && x == 8) {return controller.cell681;}
		else if(y == 6 && x == 9) {return controller.cell691;}
		else if(y == 7 && x == 0) {return controller.cell701;}
		else if(y == 7 && x == 1) {return controller.cell711;}
		else if(y == 7 && x == 2) {return controller.cell721;}
		else if(y == 7 && x == 3) {return controller.cell731;}
		else if(y == 7 && x == 4) {return controller.cell741;}
		else if(y == 7 && x == 5) {return controller.cell751;}
		else if(y == 7 && x == 6) {return controller.cell761;}
		else if(y == 7 && x == 7) {return controller.cell771;}
		else if(y == 7 && x == 8) {return controller.cell781;}
		else if(y == 7 && x == 9) {return controller.cell791;}
		else if(y == 8 && x == 0) {return controller.cell801;}
		else if(y == 8 && x == 1) {return controller.cell811;}
		else if(y == 8 && x == 2) {return controller.cell821;}
		else if(y == 8 && x == 3) {return controller.cell831;}
		else if(y == 8 && x == 4) {return controller.cell841;}
		else if(y == 8 && x == 5) {return controller.cell851;}
		else if(y == 8 && x == 6) {return controller.cell861;}
		else if(y == 8 && x == 7) {return controller.cell871;}
		else if(y == 8 && x == 8) {return controller.cell881;}
		else if(y == 8 && x == 9) {return controller.cell891;}
		else if(y == 9 && x == 0) {return controller.cell901;}
		else if(y == 9 && x == 1) {return controller.cell911;}
		else if(y == 9 && x == 2) {return controller.cell921;}
		else if(y == 9 && x == 3) {return controller.cell931;}
		else if(y == 9 && x == 4) {return controller.cell941;}
		else if(y == 9 && x == 5) {return controller.cell951;}
		else if(y == 9 && x == 6) {return controller.cell961;}
		else if(y == 9 && x == 7) {return controller.cell971;}
		else if(y == 9 && x == 8) {return controller.cell981;}
		else {return controller.cell991;}
		
	}
	////////////////////////////////////////Снизу все для расстановки кораблей в начале
	private void placeMyFleet(int y, int x) {
		closeCell(y, x);
		done++;
		if (done < 5) {		
			if (done == 4) {setBarriers();}
		}//четырехпалубник
		else if(done > 4 && done < 11) {
			if (done == 7) {setBarriers();}// model.sendToServer(ship); ship = "";}
			if (done == 10) {setBarriers();}// model.sendToServer(ship); ship = "";}
		}//трехпалубники
		else if(done > 10 && done < 17) {
			if (done == 12) {setBarriers();}// model.sendToServer(ship); ship = "";}
			if (done == 14) {setBarriers();}// model.sendToServer(ship); ship = "";}
			if (done == 16) {setBarriers();}// model.sendToServer(ship); ship = "";}
		}//двухпалубники
		else {
			if (done == 17) {setBarriers();}// model.sendToServer(ship); ship = "";}
			if (done == 18) {setBarriers();}// model.sendToServer(ship); ship = "";}
			if (done == 19) {setBarriers();}// model.sendToServer(ship); ship = "";}
			if (done == 20) {setBarriers();}// model.sendToServer(ship); ship = "";}
		}//однопалубники
		
		
	}//placeMyFleet
	
	private void closeCell(int y, int x) {
		/*Закрывает все клетки, кроме окружения только что поставленной клетки*/
		clearCells();
		ship += Integer.toString(y) + Integer.toString(x)+".";
		model.sendToServer(ship); ship = "";
		cells[y][x] = 2;
		if ((y-1>-1 && y+1<10)) {
			if(cells[y-1][x] == 2 || cells[y+1][x] == 2){
				if(x-1 > -1) {cells[y][x-1] = 1;}
				if(x+1 < 10) {cells[y][x+1] = 1;}
			}
		}
		if (x-1>-1 && x+1<10) {
			if(cells[y][x-1] == 2 || cells[y][x+1] == 2) {
				if(y-1 > -1) {cells[y-1][x] = 1;}
				if(y+1 < 10) {cells[y+1][x] = 1;}
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (!((i == y-1 && j == x-1) || (i == y-1 && j == x+1) ||
						(i == y+1 && j == x-1) || (i == y+1 && j == x+1)) &&
					((i > y - 2 && i < y + 2) && (j > x - 2 && j < x + 2)) || cells[i][j] == 2) {continue;}
				cells[i][j] = 1;

			}//for j
		}//for i
	}//checkCell

	private void clearCells() {
		/*Открывает все клетки, кроме уже занятых*/
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (cells[i][j] == 2 || cells[i][j] == 3) {continue;}
				if(done == 20) {cells[i][j] = 1;}
				else{cells[i][j] = 0;}
			}
		}//сделать кнопки кликабельными
	}//clearCElls
	
	private void setBarriers() {
		/*После того как корабль расставлен окружает его прямоугольником, чтобы нельзя было поставить два корабля впритык*/
		clearCells();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if(i == 0 || i ==9 || j ==0 || j == 9) {
					if(i == 0 && j == 0) {
						if ((cells[i+1][j]==2 || cells[i][j+1]==2) && cells[i][j]!=2) {
							cells[i][j] = 3;
						}
					}else if(i == 9 && j == 0) {
						if ((cells[i-1][j]==2 || cells[i][j+1]==2) && cells[i][j]!=2) {
							cells[i][j] = 3;
						}
					}else if(i == 0 && j == 9) {
						if ((cells[i+1][j]==2 || cells[i][j-1]==2) && cells[i][j]!=2) {
							cells[i][j] = 3;
						}
					}else if(i == 9 && j == 9) {
						if ((cells[i-1][j]==2 || cells[i][j-1]==2) && cells[i][j]!=2) {
							cells[i][j] = 3;
						}
					}
					else if(i == 0 && cells[i][j]!=2) {
						if ((cells[i][j-1]==2 || cells[i][j+1]==2 || cells[i+1][j]==2
								|| cells[i+1][j-1] == 2 || cells[i+1][j+1] == 2
								|| cells[i+1][j+1] == 2) && cells[i][j]!=2) {
								cells[i][j] = 3;
						}	
					}else if(i == 9 && cells[i][j]!=2) {
						if ((cells[i][j-1]==2 || cells[i][j+1]==2 || cells[i-1][j]==2
								|| cells[i-1][j-1] == 2 || cells[i-1][j+1] == 2) && cells[i][j]!=2) {
							cells[i][j] = 3;
						}	
					}else if(j == 0 && cells[i][j]!=2) {
						if ((cells[i][j+1]==2 || cells[i+1][j]==2 || cells[i-1][j]==2
								|| cells[i-1][j+1] == 2|| cells[i+1][j+1] == 2) && cells[i][j]!=2) {
							cells[i][j] = 3;
						}	
					}
					else if(j == 9 && cells[i][j]!=2) {
						if ((cells[i][j-1]==2 || cells[i-1][j]==2 || cells[i+1][j]==2
								|| cells[i+1][j-1] == 2 || cells[i-1][j-1] == 2) && cells[i][j]!=2) {
								cells[i][j] = 3;
						}	
					}
					continue;
				}
				if ((cells[i+1][j] == 2 || cells[i-1][j] == 2 ||
						cells[i][j+1] == 2 || cells[i][j-1] == 2 ||
						cells[i+1][j+1] == 2 || cells[i-1][j-1] == 2
						|| cells[i+1][j-1] == 2 || cells[i-1][j+1] == 2)
						&& cells[i][j] != 2) {
						cells[i][j] = 3;
					}
			}
		}//окружить корабли барьером
		

	}//setBarriers
	
	private void winner() {
		Platform.runLater(new Runnable() { 
			public void run() {
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						if (i == 5) {
							if(j == 2) {
								getMyButton(i, j).setText("W");
								getMyButton(i, j).setStyle("-fx-text-fill: green");
								getHisButton(i, j).setText("L");
								getHisButton(i, j).setStyle("-fx-text-fill: red");
								
							}else if(j == 3) {
								getMyButton(i, j).setText("I");
								getMyButton(i, j).setStyle("-fx-text-fill: green");
								getHisButton(i, j).setText("O");
								getHisButton(i, j).setStyle("-fx-text-fill: red");
							}
							else if(j == 4) {
								getMyButton(i, j).setText("N");
								getMyButton(i, j).setStyle("-fx-text-fill: green");
								getHisButton(i, j).setText("S");
								getHisButton(i, j).setStyle("-fx-text-fill: red");
							}
							else if(j == 5) {
								getMyButton(i, j).setText("N");
								getMyButton(i, j).setStyle("-fx-text-fill: green");
								getHisButton(i, j).setText("E");
								getHisButton(i, j).setStyle("-fx-text-fill: red");
							}
							else if(j == 6) {
								getMyButton(i, j).setText("E");
								getMyButton(i, j).setStyle("-fx-text-fill: green");
								getHisButton(i, j).setText("R");
								getHisButton(i, j).setStyle("-fx-text-fill: red");
							}
							else if(j == 7) {
								getMyButton(i, j).setText("R");
								getMyButton(i, j).setStyle("-fx-text-fill: green");
							}
						}}}
			}
		});
		model.die();
	}	
		
	
	private void loser() {
		Platform.runLater(new Runnable() { 
			public void run() {
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						if (i == 5) {
							if(j == 2) {
								getHisButton(i, j).setText("W");
								getHisButton(i, j).setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
								getMyButton(i, j).setText("L");
								getMyButton(i, j).setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
							}else if(j == 3) {
								getHisButton(i, j).setText("I");
								getHisButton(i, j).setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
								getMyButton(i, j).setText("O");
								getMyButton(i, j).setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
							}
							else if(j == 4) {
								getHisButton(i, j).setText("N");
								getHisButton(i, j).setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
								getMyButton(i, j).setText("S");
								getMyButton(i, j).setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
							}
							else if(j == 5) {
								getHisButton(i, j).setText("N");
								getHisButton(i, j).setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
								getMyButton(i, j).setText("E");
								getMyButton(i, j).setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
							}
							else if(j == 6) {
								getHisButton(i, j).setText("E");
								getHisButton(i, j).setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
								getMyButton(i, j).setText("R");
								getMyButton(i, j).setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
							}
							else if(j == 7) {
								getHisButton(i, j).setText("R");
								getHisButton(i, j).setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
							}
						}
						}}}});
		model.die();
	}
	
}//ControllerToModel


public class Controller {
	private ControllerToModel conToMod = new ControllerToModel(this);
	
	@FXML
    public Button cell00;

    @FXML
    public Button cell001;

    @FXML
    public Button cell01;

    @FXML
    public Button cell011;

    @FXML
    public Button cell02;

    @FXML
    public Button cell021;

    @FXML
    public Button cell03;

    @FXML
    public Button cell031;

    @FXML
    public Button cell04;

    @FXML
    public Button cell041;

    @FXML
    public Button cell05;

    @FXML
    public Button cell051;

    @FXML
    public Button cell06;

    @FXML
    public Button cell061;

    @FXML
    public Button cell07;

    @FXML
    public Button cell071;

    @FXML
    public Button cell08;

    @FXML
    public Button cell081;

    @FXML
    public Button cell09;

    @FXML
    public Button cell091;

    @FXML
    public Button cell10;

    @FXML
    public Button cell101;

    @FXML
    public Button cell11;

    @FXML
    public Button cell111;

    @FXML
    public Button cell12;

    @FXML
    public Button cell121;

    @FXML
    public Button cell13;

    @FXML
    public Button cell131;

    @FXML
    public Button cell14;

    @FXML
    public Button cell141;

    @FXML
    public Button cell15;

    @FXML
    public Button cell151;

    @FXML
    public Button cell16;

    @FXML
    public Button cell161;

    @FXML
    public Button cell17;

    @FXML
    public Button cell171;

    @FXML
    public Button cell18;

    @FXML
    public Button cell181;

    @FXML
    public Button cell19;

    @FXML
    public Button cell191;

    @FXML
    public Button cell20;

    @FXML
    public Button cell201;

    @FXML
    public Button cell21;

    @FXML
    public Button cell211;

    @FXML
    public Button cell22;

    @FXML
    public Button cell221;

    @FXML
    public Button cell23;

    @FXML
    public Button cell231;

    @FXML
    public Button cell24;

    @FXML
    public Button cell241;

    @FXML
    public Button cell25;

    @FXML
    public Button cell251;

    @FXML
    public Button cell26;

    @FXML
    public Button cell261;

    @FXML
    public Button cell27;

    @FXML
    public Button cell271;

    @FXML
    public Button cell28;

    @FXML
    public Button cell281;

    @FXML
    public Button cell29;

    @FXML
    public Button cell291;

    @FXML
    public Button cell30;

    @FXML
    public Button cell301;

    @FXML
    public Button cell31;

    @FXML
    public Button cell311;

    @FXML
    public Button cell32;

    @FXML
    public Button cell321;

    @FXML
    public Button cell33;

    @FXML
    public Button cell331;

    @FXML
    public Button cell34;

    @FXML
    public Button cell341;

    @FXML
    public Button cell35;

    @FXML
    public Button cell351;

    @FXML
    public Button cell36;

    @FXML
    public Button cell361;

    @FXML
    public Button cell37;

    @FXML
    public Button cell371;

    @FXML
    public Button cell38;

    @FXML
    public Button cell381;

    @FXML
    public Button cell39;

    @FXML
    public Button cell391;

    @FXML
    public Button cell40;

    @FXML
    public Button cell401;

    @FXML
    public Button cell41;

    @FXML
    public Button cell411;

    @FXML
    public Button cell42;

    @FXML
    public Button cell421;

    @FXML
    public Button cell43;

    @FXML
    public Button cell431;

    @FXML
    public Button cell44;

    @FXML
    public Button cell441;

    @FXML
    public Button cell45;

    @FXML
    public Button cell451;

    @FXML
    public Button cell46;

    @FXML
    public Button cell461;

    @FXML
    public Button cell47;

    @FXML
    public Button cell471;

    @FXML
    public Button cell48;

    @FXML
    public Button cell481;

    @FXML
    public Button cell49;

    @FXML
    public Button cell491;

    @FXML
    public Button cell50;

    @FXML
    public Button cell501;

    @FXML
    public Button cell51;

    @FXML
    public Button cell511;

    @FXML
    public Button cell52;

    @FXML
    public Button cell521;

    @FXML
    public Button cell53;

    @FXML
    public Button cell531;

    @FXML
    public Button cell54;

    @FXML
    public Button cell541;

    @FXML
    public Button cell55;

    @FXML
    public Button cell551;

    @FXML
    public Button cell56;

    @FXML
    public Button cell561;

    @FXML
    public Button cell57;

    @FXML
    public Button cell571;

    @FXML
    public Button cell58;

    @FXML
    public Button cell581;

    @FXML
    public Button cell59;

    @FXML
    public Button cell591;

    @FXML
    public Button cell60;

    @FXML
    public Button cell601;

    @FXML
    public Button cell61;

    @FXML
    public Button cell611;

    @FXML
    public Button cell62;

    @FXML
    public Button cell621;

    @FXML
    public Button cell63;

    @FXML
    public Button cell631;

    @FXML
    public Button cell64;

    @FXML
    public Button cell641;

    @FXML
    public Button cell65;

    @FXML
    public Button cell651;

    @FXML
    public Button cell66;

    @FXML
    public Button cell661;

    @FXML
    public Button cell67;

    @FXML
    public Button cell671;

    @FXML
    public Button cell68;

    @FXML
    public Button cell681;

    @FXML
    public Button cell69;

    @FXML
    public Button cell691;

    @FXML
    public Button cell70;

    @FXML
    public Button cell701;

    @FXML
    public Button cell71;

    @FXML
    public Button cell711;

    @FXML
    public Button cell72;

    @FXML
    public Button cell721;

    @FXML
    public Button cell73;

    @FXML
    public Button cell731;

    @FXML
    public Button cell74;

    @FXML
    public Button cell741;

    @FXML
    public Button cell75;

    @FXML
    public Button cell751;

    @FXML
    public Button cell76;

    @FXML
    public Button cell761;

    @FXML
    public Button cell77;

    @FXML
    public Button cell771;

    @FXML
    public Button cell78;

    @FXML
    public Button cell781;

    @FXML
    public Button cell79;

    @FXML
    public Button cell791;

    @FXML
    public Button cell80;

    @FXML
    public Button cell801;

    @FXML
    public Button cell81;

    @FXML
    public Button cell811;

    @FXML
    public Button cell82;

    @FXML
    public Button cell821;

    @FXML
    public Button cell83;

    @FXML
    public Button cell831;

    @FXML
    public Button cell84;

    @FXML
    public Button cell841;

    @FXML
    public Button cell85;

    @FXML
    public Button cell851;

    @FXML
    public Button cell86;

    @FXML
    public Button cell861;

    @FXML
    public Button cell87;

    @FXML
    public Button cell871;

    @FXML
    public Button cell88;

    @FXML
    public Button cell881;

    @FXML
    public Button cell89;

    @FXML
    public Button cell891;

    @FXML
    public Button cell90;

    @FXML
    public Button cell901;

    @FXML
    public Button cell91;

    @FXML
    public Button cell911;

    @FXML
    public Button cell92;

    @FXML
    public Button cell921;

    @FXML
    public Button cell93;

    @FXML
    public Button cell931;

    @FXML
    public Button cell94;

    @FXML
    public Button cell941;

    @FXML
    public Button cell95;

    @FXML
    public Button cell951;

    @FXML
    public Button cell96;

    @FXML
    public Button cell961;

    @FXML
    public Button cell97;

    @FXML
    public Button cell971;

    @FXML
    public Button cell98;

    @FXML
    public Button cell981;

    @FXML
    public Button cell99;

    @FXML
    public Button cell991;

    @FXML
    void clickCell00(ActionEvent event) {
    	conToMod.clickedButtonNumber(0, 0);
    }

    @FXML
    void clickCell01(ActionEvent event) {
    	conToMod.clickedButtonNumber(0, 1);
    }

    @FXML
    void clickCell02(ActionEvent event) {
    	conToMod.clickedButtonNumber(0, 2);
    }

    @FXML
    void clickCell03(ActionEvent event) {
    	conToMod.clickedButtonNumber(0, 3);
    }

    @FXML
    void clickCell04(ActionEvent event) {
    	conToMod.clickedButtonNumber(0, 4);
    }

    @FXML
    void clickCell05(ActionEvent event) {
    	conToMod.clickedButtonNumber(0, 5);
    }

    @FXML
    void clickCell06(ActionEvent event) {
    	conToMod.clickedButtonNumber(0, 6);
    }

    @FXML
    void clickCell07(ActionEvent event) {
    	conToMod.clickedButtonNumber(0, 7);
    }

    @FXML
    void clickCell08(ActionEvent event) {
    	conToMod.clickedButtonNumber(0, 8);
    }

    @FXML
    void clickCell09(ActionEvent event) {
    	conToMod.clickedButtonNumber(0, 9);
    }

    @FXML
    void clickCell10(ActionEvent event) {
    	conToMod.clickedButtonNumber(1, 0);
    }

    @FXML
    void clickCell11(ActionEvent event) {
    	conToMod.clickedButtonNumber(1, 1);
    }

    @FXML
    void clickCell12(ActionEvent event) {
    	conToMod.clickedButtonNumber(1, 2);
    }

    @FXML
    void clickCell13(ActionEvent event) {
    	conToMod.clickedButtonNumber(1, 3);
    }

    @FXML
    void clickCell14(ActionEvent event) {
    	conToMod.clickedButtonNumber(1, 4);
    }

    @FXML
    void clickCell15(ActionEvent event) {
    	conToMod.clickedButtonNumber(1, 5);
    }

    @FXML
    void clickCell16(ActionEvent event) {
    	conToMod.clickedButtonNumber(1, 6);
    }

    @FXML
    void clickCell17(ActionEvent event) {
    	conToMod.clickedButtonNumber(1, 7);
    }

    @FXML
    void clickCell18(ActionEvent event) {
    	conToMod.clickedButtonNumber(1, 8);
    }

    @FXML
    void clickCell19(ActionEvent event) {
    	conToMod.clickedButtonNumber(1, 9);
    }

    @FXML
    void clickCell20(ActionEvent event) {
    	conToMod.clickedButtonNumber(2, 0);
    }

    @FXML
    void clickCell21(ActionEvent event) {
    	conToMod.clickedButtonNumber(2, 1);
    }

    @FXML
    void clickCell22(ActionEvent event) {
    	conToMod.clickedButtonNumber(2, 2);
    }

    @FXML
    void clickCell23(ActionEvent event) {
    	conToMod.clickedButtonNumber(2, 3);
    }

    @FXML
    void clickCell24(ActionEvent event) {
    	conToMod.clickedButtonNumber(2, 4);
    }

    @FXML
    void clickCell25(ActionEvent event) {
    	conToMod.clickedButtonNumber(2, 5);
    }

    @FXML
    void clickCell26(ActionEvent event) {
    	conToMod.clickedButtonNumber(2, 6);
    }

    @FXML
    void clickCell27(ActionEvent event) {
    	conToMod.clickedButtonNumber(2, 7);
    }

    @FXML
    void clickCell28(ActionEvent event) {
    	conToMod.clickedButtonNumber(2, 8);
    }

    @FXML
    void clickCell29(ActionEvent event) {
    	conToMod.clickedButtonNumber(2, 9);
    }

    @FXML
    void clickCell30(ActionEvent event) {
    	conToMod.clickedButtonNumber(3, 0);
    }

    @FXML
    void clickCell31(ActionEvent event) {
    	conToMod.clickedButtonNumber(3, 1);
    }

    @FXML
    void clickCell32(ActionEvent event) {
    	conToMod.clickedButtonNumber(3, 2);
    }

    @FXML
    void clickCell33(ActionEvent event) {
    	conToMod.clickedButtonNumber(3, 3);
    }

    @FXML
    void clickCell34(ActionEvent event) {
    	conToMod.clickedButtonNumber(3, 4);
    }

    @FXML
    void clickCell35(ActionEvent event) {
    	conToMod.clickedButtonNumber(3, 5);
    }

    @FXML
    void clickCell36(ActionEvent event) {
    	conToMod.clickedButtonNumber(3, 6);
    }

    @FXML
    void clickCell37(ActionEvent event) {
    	conToMod.clickedButtonNumber(3, 7);
    }

    @FXML
    void clickCell38(ActionEvent event) {
    	conToMod.clickedButtonNumber(3, 8);
    }

    @FXML
    void clickCell39(ActionEvent event) {
    	conToMod.clickedButtonNumber(3, 9);
    }

    @FXML
    void clickCell40(ActionEvent event) {
    	conToMod.clickedButtonNumber(4, 0);
    }

    @FXML
    void clickCell41(ActionEvent event) {
    	conToMod.clickedButtonNumber(4, 1);
    }

    @FXML
    void clickCell42(ActionEvent event) {
    	conToMod.clickedButtonNumber(4, 2);
    }

    @FXML
    void clickCell43(ActionEvent event) {
    	conToMod.clickedButtonNumber(4, 3);
    }

    @FXML
    void clickCell44(ActionEvent event) {
    	conToMod.clickedButtonNumber(4, 4);
    }

    @FXML
    void clickCell45(ActionEvent event) {
    	conToMod.clickedButtonNumber(4, 5);
    }

    @FXML
    void clickCell46(ActionEvent event) {
    	conToMod.clickedButtonNumber(4, 6);
    }

    @FXML
    void clickCell47(ActionEvent event) {
    	conToMod.clickedButtonNumber(4, 7);
    }

    @FXML
    void clickCell48(ActionEvent event) {
    	conToMod.clickedButtonNumber(4, 8);
    }

    @FXML
    void clickCell49(ActionEvent event) {
    	conToMod.clickedButtonNumber(4, 9);
    }

    @FXML
    void clickCell50(ActionEvent event) {
    	conToMod.clickedButtonNumber(5, 0);
    }

    @FXML
    void clickCell51(ActionEvent event) {
    	conToMod.clickedButtonNumber(5, 1);
    }

    @FXML
    void clickCell52(ActionEvent event) {
    	conToMod.clickedButtonNumber(5, 2);
    }

    @FXML
    void clickCell53(ActionEvent event) {
    	conToMod.clickedButtonNumber(5, 3);
    }

    @FXML
    void clickCell54(ActionEvent event) {
    	conToMod.clickedButtonNumber(5, 4);
    }

    @FXML
    void clickCell55(ActionEvent event) {
    	conToMod.clickedButtonNumber(5, 5);
    }

    @FXML
    void clickCell56(ActionEvent event) {
    	conToMod.clickedButtonNumber(5, 6);
    }

    @FXML
    void clickCell57(ActionEvent event) {
    	conToMod.clickedButtonNumber(5, 7);
    }

    @FXML
    void clickCell58(ActionEvent event) {
    	conToMod.clickedButtonNumber(5, 8);
    }

    @FXML
    void clickCell59(ActionEvent event) {
    	conToMod.clickedButtonNumber(5, 9);
    }

    @FXML
    void clickCell60(ActionEvent event) {
    	conToMod.clickedButtonNumber(6, 0);
    }

    @FXML
    void clickCell61(ActionEvent event) {
    	conToMod.clickedButtonNumber(6, 1);
    }

    @FXML
    void clickCell62(ActionEvent event) {
    	conToMod.clickedButtonNumber(6, 2);
    }

    @FXML
    void clickCell63(ActionEvent event) {
    	conToMod.clickedButtonNumber(6, 3);
    }

    @FXML
    void clickCell64(ActionEvent event) {
    	conToMod.clickedButtonNumber(6, 4);
    }

    @FXML
    void clickCell65(ActionEvent event) {
    	conToMod.clickedButtonNumber(6, 5);
    }

    @FXML
    void clickCell66(ActionEvent event) {
    	conToMod.clickedButtonNumber(6, 6);
    }

    @FXML
    void clickCell67(ActionEvent event) {
    	conToMod.clickedButtonNumber(6, 7);
    }

    @FXML
    void clickCell68(ActionEvent event) {
    	conToMod.clickedButtonNumber(6, 8);
    }

    @FXML
    void clickCell69(ActionEvent event) {
    	conToMod.clickedButtonNumber(6, 9);
    }

    @FXML
    void clickCell70(ActionEvent event) {
    	conToMod.clickedButtonNumber(7, 0);
    }

    @FXML
    void clickCell71(ActionEvent event) {
    	conToMod.clickedButtonNumber(7, 1);
    }

    @FXML
    void clickCell72(ActionEvent event) {
    	conToMod.clickedButtonNumber(7, 2);
    }

    @FXML
    void clickCell73(ActionEvent event) {
    	conToMod.clickedButtonNumber(7, 3);
    }

    @FXML
    void clickCell74(ActionEvent event) {
    	conToMod.clickedButtonNumber(7, 4);
    }

    @FXML
    void clickCell75(ActionEvent event) {
    	conToMod.clickedButtonNumber(7, 5);
    }

    @FXML
    void clickCell76(ActionEvent event) {
    	conToMod.clickedButtonNumber(7, 6);
    }

    @FXML
    void clickCell77(ActionEvent event) {
    	conToMod.clickedButtonNumber(7, 7);
    }

    @FXML
    void clickCell78(ActionEvent event) {
    	conToMod.clickedButtonNumber(7, 8);
    }

    @FXML
    void clickCell79(ActionEvent event) {
    	conToMod.clickedButtonNumber(7, 9);
    }

    @FXML
    void clickCell80(ActionEvent event) {
    	conToMod.clickedButtonNumber(8, 0);
    }
    
    @FXML
    void clickCell81(ActionEvent event) {
    	conToMod.clickedButtonNumber(8, 1);
    }

    @FXML
    void clickCell82(ActionEvent event) {
    	conToMod.clickedButtonNumber(8, 2);
    }

    @FXML
    void clickCell83(ActionEvent event) {
    	conToMod.clickedButtonNumber(8, 3);
    }

    @FXML
    void clickCell84(ActionEvent event) {
    	conToMod.clickedButtonNumber(8, 4);
    }

    @FXML
    void clickCell85(ActionEvent event) {
    	conToMod.clickedButtonNumber(8, 5);
    }

    @FXML
    void clickCell86(ActionEvent event) {
    	conToMod.clickedButtonNumber(8, 6);
    }

    @FXML
    void clickCell87(ActionEvent event) {
    	conToMod.clickedButtonNumber(8, 7);
    }

    @FXML
    void clickCell88(ActionEvent event) {
    	conToMod.clickedButtonNumber(8, 8);
    }

    @FXML
    void clickCell89(ActionEvent event) {
    	conToMod.clickedButtonNumber(8, 9);
    }

    @FXML
    void clickCell90(ActionEvent event) {
    	conToMod.clickedButtonNumber(9, 0);
    }

    @FXML
    void clickCell91(ActionEvent event) {
    	conToMod.clickedButtonNumber(9, 1);
    }

    @FXML
    void clickCell92(ActionEvent event) {
    	conToMod.clickedButtonNumber(9, 2);
    }

    @FXML
    void clickCell93(ActionEvent event) {
    	conToMod.clickedButtonNumber(9, 3);
    }

    @FXML
    void clickCell94(ActionEvent event) {
    	conToMod.clickedButtonNumber(9, 4);
    }

    @FXML
    void clickCell95(ActionEvent event) {
    	conToMod.clickedButtonNumber(9, 5);
    }

    @FXML
    void clickCell96(ActionEvent event) {
    	conToMod.clickedButtonNumber(9, 6);
    }

    @FXML
    void clickCell97(ActionEvent event) {
    	conToMod.clickedButtonNumber(9, 7);
    }

    @FXML
    void clickCell98(ActionEvent event) {
    	conToMod.clickedButtonNumber(9, 8);
    }

    @FXML
    void clickCell99(ActionEvent event) {
    	conToMod.clickedButtonNumber(9, 9);
    }

}//Controller