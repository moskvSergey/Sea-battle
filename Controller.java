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
    private int[][] cantPlaceHere;
	private int done;//подсчет сколько клеток уже расставленно
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
		cantPlaceHere = new int[10][10];
		done = 0;
		ship = "";
	}//initializeBlock
	
	@Override
	public void run() {
		while(true) {
			
		}
	}//run
	
	public void clickedButtonNumber(int y, int x) {
		placeMyFleet(y, x);
		updateMyButtons();
	}
	
	private void placeMyFleet(int y, int x) {
		closeCell(y, x);
		done++;
		if (done < 5) {		
			if (done == 4) {setBarriers();}// model.sendToServer(ship); ship = "";}
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
		ship += Integer.toString(y) + Integer.toString(x);
		cantPlaceHere[y][x] = 2;
		if ((y-1>-1 && y+1<10)) {
			if(cantPlaceHere[y-1][x] == 2 || cantPlaceHere[y+1][x] == 2){
				if(x-1 > -1) {cantPlaceHere[y][x-1] = 1;}
				if(x+1 < 10) {cantPlaceHere[y][x+1] = 1;}
			}
		}
		if (x-1>-1 && x+1<10) {
			if(cantPlaceHere[y][x-1] == 2 || cantPlaceHere[y][x+1] == 2) {
				if(y-1 > -1) {cantPlaceHere[y-1][x] = 1;}
				if(y+1 < 10) {cantPlaceHere[y+1][x] = 1;}
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (!((i == y-1 && j == x-1) || (i == y-1 && j == x+1) ||
						(i == y+1 && j == x-1) || (i == y+1 && j == x+1)) &&
					((i > y - 2 && i < y + 2) && (j > x - 2 && j < x + 2)) || cantPlaceHere[i][j] == 2) {continue;}
				cantPlaceHere[i][j] = 1;

			}//for j
		}//for i
	}//checkCell

	private void clearCells() {
		/*Открывает все клетки, кроме уже занятых*/
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (cantPlaceHere[i][j] == 2 || cantPlaceHere[i][j] == 3) {continue;}
				cantPlaceHere[i][j] = 0;
			}
		}//сделать кнопки кликабельными
	}//clearCElls
	
	private void setBarriers() {
		/*После того как корабль расставлен окружает его прямоугольником, чтобы нельзя было поставить два корабля впритык*/
		clearCells();
		for (int i = 1; i < 9; i++) {
			for (int j = 1; j < 9; j++) {
				if ((cantPlaceHere[i+1][j] == 2 || cantPlaceHere[i-1][j] == 2 ||
					cantPlaceHere[i][j+1] == 2 || cantPlaceHere[i][j-1] == 2 ||
					cantPlaceHere[i+1][j+1] == 2 || cantPlaceHere[i-1][j-1] == 2
					|| cantPlaceHere[i+1][j-1] == 2 || cantPlaceHere[i-1][j+1] == 2)
					&& cantPlaceHere[i][j] != 2) {
					cantPlaceHere[i][j] = 3;
				}
			}
		}//окружить корабли барьером
		

	}//setBarriers
	
	private void updateMyButtons() {
		if(cantPlaceHere[0][0] == 0) {controller.cell00.setDisable(false);}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell00.setDisable(true);}});}
		if(cantPlaceHere[0][1] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell01.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell01.setDisable(true);}});}
		if(cantPlaceHere[0][2] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell02.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell02.setDisable(true);}});}
		if(cantPlaceHere[0][3] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell03.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell03.setDisable(true);}});}
		if(cantPlaceHere[0][4] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell04.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell04.setDisable(true);}});}
		if(cantPlaceHere[0][5] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell05.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell05.setDisable(true);}});}
		if(cantPlaceHere[0][6] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell06.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell06.setDisable(true);}});}
		if(cantPlaceHere[0][7] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell07.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell07.setDisable(true);}});}
		if(cantPlaceHere[0][8] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell08.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell08.setDisable(true);}});}
		if(cantPlaceHere[0][9] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell09.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell09.setDisable(true);}});}
		if(cantPlaceHere[1][0] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell10.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell10.setDisable(true);}});}
		if(cantPlaceHere[1][1] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell11.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell11.setDisable(true);}});}
		if(cantPlaceHere[1][2] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell12.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell12.setDisable(true);}});}
		if(cantPlaceHere[1][3] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell13.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell13.setDisable(true);}});}
		if(cantPlaceHere[1][4] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell14.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell14.setDisable(true);}});}
		if(cantPlaceHere[1][5] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell15.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell15.setDisable(true);}});}
		if(cantPlaceHere[1][6] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell16.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell16.setDisable(true);}});}
		if(cantPlaceHere[1][7] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell17.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell17.setDisable(true);}});}
		if(cantPlaceHere[1][8] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell18.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell18.setDisable(true);}});}
		if(cantPlaceHere[1][9] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell19.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell19.setDisable(true);}});}
		if(cantPlaceHere[2][0] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell20.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell20.setDisable(true);}});}
		if(cantPlaceHere[2][1] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell21.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell21.setDisable(true);}});}
		if(cantPlaceHere[2][2] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell22.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell22.setDisable(true);}});}
		if(cantPlaceHere[2][3] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell23.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell23.setDisable(true);}});}
		if(cantPlaceHere[2][4] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell24.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell24.setDisable(true);}});}
		if(cantPlaceHere[2][5] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell25.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell25.setDisable(true);}});}
		if(cantPlaceHere[2][6] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell26.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell26.setDisable(true);}});}
		if(cantPlaceHere[2][7] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell27.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell27.setDisable(true);}});}
		if(cantPlaceHere[2][8] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell28.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell28.setDisable(true);}});}
		if(cantPlaceHere[2][9] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell29.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell29.setDisable(true);}});}
		if(cantPlaceHere[3][0] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell30.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell30.setDisable(true);}});}
		if(cantPlaceHere[3][1] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell31.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell31.setDisable(true);}});}
		if(cantPlaceHere[3][2] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell32.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell32.setDisable(true);}});}
		if(cantPlaceHere[3][3] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell33.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell33.setDisable(true);}});}
		if(cantPlaceHere[3][4] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell34.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell34.setDisable(true);}});}
		if(cantPlaceHere[3][5] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell35.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell35.setDisable(true);}});}
		if(cantPlaceHere[3][6] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell36.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell36.setDisable(true);}});}
		if(cantPlaceHere[3][7] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell37.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell37.setDisable(true);}});}
		if(cantPlaceHere[3][8] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell38.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell38.setDisable(true);}});}
		if(cantPlaceHere[3][9] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell39.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell39.setDisable(true);}});}
		if(cantPlaceHere[4][0] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell40.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell40.setDisable(true);}});}
		if(cantPlaceHere[4][1] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell41.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell41.setDisable(true);}});}
		if(cantPlaceHere[4][2] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell42.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell42.setDisable(true);}});}
		if(cantPlaceHere[4][3] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell43.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell43.setDisable(true);}});}
		if(cantPlaceHere[4][4] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell44.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell44.setDisable(true);}});}
		if(cantPlaceHere[4][5] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell45.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell45.setDisable(true);}});}
		if(cantPlaceHere[4][6] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell46.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell46.setDisable(true);}});}
		if(cantPlaceHere[4][7] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell47.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell47.setDisable(true);}});}
		if(cantPlaceHere[4][8] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell48.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell48.setDisable(true);}});}
		if(cantPlaceHere[4][9] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell49.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell49.setDisable(true);}});}
		if(cantPlaceHere[5][0] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell50.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell50.setDisable(true);}});}
		if(cantPlaceHere[5][1] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell51.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell51.setDisable(true);}});}
		if(cantPlaceHere[5][2] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell52.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell52.setDisable(true);}});}
		if(cantPlaceHere[5][3] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell53.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell53.setDisable(true);}});}
		if(cantPlaceHere[5][4] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell54.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell54.setDisable(true);}});}
		if(cantPlaceHere[5][5] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell55.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell55.setDisable(true);}});}
		if(cantPlaceHere[5][6] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell56.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell56.setDisable(true);}});}
		if(cantPlaceHere[5][7] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell57.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell57.setDisable(true);}});}
		if(cantPlaceHere[5][8] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell58.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell58.setDisable(true);}});}
		if(cantPlaceHere[5][9] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell59.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell59.setDisable(true);}});}
		if(cantPlaceHere[6][0] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell60.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell60.setDisable(true);}});}
		if(cantPlaceHere[6][1] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell61.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell61.setDisable(true);}});}
		if(cantPlaceHere[6][2] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell62.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell62.setDisable(true);}});}
		if(cantPlaceHere[6][3] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell63.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell63.setDisable(true);}});}
		if(cantPlaceHere[6][4] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell64.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell64.setDisable(true);}});}
		if(cantPlaceHere[6][5] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell65.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell65.setDisable(true);}});}
		if(cantPlaceHere[6][6] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell66.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell66.setDisable(true);}});}
		if(cantPlaceHere[6][7] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell67.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell67.setDisable(true);}});}
		if(cantPlaceHere[6][8] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell68.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell68.setDisable(true);}});}
		if(cantPlaceHere[6][9] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell69.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell69.setDisable(true);}});}
		if(cantPlaceHere[7][0] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell70.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell70.setDisable(true);}});}
		if(cantPlaceHere[7][1] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell71.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell71.setDisable(true);}});}
		if(cantPlaceHere[7][2] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell72.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell72.setDisable(true);}});}
		if(cantPlaceHere[7][3] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell73.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell73.setDisable(true);}});}
		if(cantPlaceHere[7][4] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell74.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell74.setDisable(true);}});}
		if(cantPlaceHere[7][5] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell75.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell75.setDisable(true);}});}
		if(cantPlaceHere[7][6] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell76.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell76.setDisable(true);}});}
		if(cantPlaceHere[7][7] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell77.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell77.setDisable(true);}});}
		if(cantPlaceHere[7][8] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell78.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell78.setDisable(true);}});}
		if(cantPlaceHere[7][9] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell79.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell79.setDisable(true);}});}
		if(cantPlaceHere[8][0] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell80.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell80.setDisable(true);}});}
		if(cantPlaceHere[8][1] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell81.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell81.setDisable(true);}});}
		if(cantPlaceHere[8][2] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell82.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell82.setDisable(true);}});}
		if(cantPlaceHere[8][3] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell83.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell83.setDisable(true);}});}
		if(cantPlaceHere[8][4] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell84.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell84.setDisable(true);}});}
		if(cantPlaceHere[8][5] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell85.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell85.setDisable(true);}});}
		if(cantPlaceHere[8][6] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell86.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell86.setDisable(true);}});}
		if(cantPlaceHere[8][7] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell87.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell87.setDisable(true);}});}
		if(cantPlaceHere[8][8] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell88.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell88.setDisable(true);}});}
		if(cantPlaceHere[8][9] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell89.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell89.setDisable(true);}});}
		if(cantPlaceHere[9][0] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell90.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell90.setDisable(true);}});}
		if(cantPlaceHere[9][1] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell91.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell91.setDisable(true);}});}
		if(cantPlaceHere[9][2] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell92.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell92.setDisable(true);}});}
		if(cantPlaceHere[9][3] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell93.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell93.setDisable(true);}});}
		if(cantPlaceHere[9][4] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell94.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell94.setDisable(true);}});}
		if(cantPlaceHere[9][5] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell95.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell95.setDisable(true);}});}
		if(cantPlaceHere[9][6] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell96.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell96.setDisable(true);}});}
		if(cantPlaceHere[9][7] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell97.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell97.setDisable(true);}});}
		if(cantPlaceHere[9][8] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell98.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell98.setDisable(true);}});}
		if(cantPlaceHere[9][9] == 0) {Platform.runLater(new Runnable() { public void run() {controller.cell99.setDisable(false);}});}
		else {Platform.runLater(new Runnable() { public void run() {controller.cell99.setDisable(true);}});}
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