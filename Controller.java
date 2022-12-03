package controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

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
		
	}//run
	
	private void placeMyFleet(int y, int x) {
		closeCell(y, x);
		done++;
		if (done < 5) {		
			if (done == 4) {setBarriers(); model.sendToServer(ship); ship = "";}
		}//четырехпалубник
		else if(done > 4 && done < 11) {
			if (done == 7) {setBarriers(); model.sendToServer(ship); ship = "";}
			if (done == 10) {setBarriers(); model.sendToServer(ship); ship = "";}
		}//трехпалубники
		else if(done > 10 && done < 17) {
			if (done == 12) {setBarriers(); model.sendToServer(ship); ship = "";}
			if (done == 14) {setBarriers(); model.sendToServer(ship); ship = "";}
			if (done == 16) {setBarriers(); model.sendToServer(ship); ship = "";}
		}//двухпалубники
		else {
			if (done == 17) {setBarriers(); model.sendToServer(ship); ship = "";}
			if (done == 18) {setBarriers(); model.sendToServer(ship); ship = "";}
			if (done == 19) {setBarriers(); model.sendToServer(ship); ship = "";}
			if (done == 20) {setBarriers(); model.sendToServer(ship); ship = "";}
		}//однопалубники
		
	}//placeMyFleet
	
	private void closeCell(int y, int x) {
		/*Закрывает все клетки, кроме окружения только что поставленной клетки*/
		clearCells();
		ship += Integer.toString(y) + Integer.toString(x);
		cantPlaceHere[y][x] = 2;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (((i > y - 1 || i < y + 1) && (j > x - 1 || j < x + 1)) || cantPlaceHere[i][j] == 2) {continue;}
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
		for (int i = 0; i < 9; i++) {
			for (int j = 1; j < 9; j++) {
				if (cantPlaceHere[i+1][j] == 2 || cantPlaceHere[i-1][j] == 2 ||
					cantPlaceHere[i][j+1] == 2 || cantPlaceHere[i][j-1] == 2 ||
					cantPlaceHere[i+1][j+1] == 2 || cantPlaceHere[i-1][j-1] == 2) {
					cantPlaceHere[i][j] = 3;
				}
			}
		}//окружить корабли барьером
	}//setBarriers
	
		
}//ControllerToModel


public class Controller {

}//Controller