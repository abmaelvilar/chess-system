package application;

import boardgame.Position;
import boardgame.Board;

public class Program {

	public static void main(String[] args) {
		
		Position p = new Position(3,5);
		Board b = new Board(8,8);
		System.out.println(p);
	}

}
