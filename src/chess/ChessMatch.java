package chess;

import boardgame.Board;
import chess.pieces.King;
import chess.pieces.Tower;

public class ChessMatch {

	private Board board;
	
	public ChessMatch() {
		board = new Board(8,8);
		initialSetup();
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i,j);
			}
		}
		return mat;			
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece){
		board.PlacePiece(piece, new ChessPosition(column,row).toPosition());
	}
	
	private void initialSetup() {
		placeNewPiece('b',6,new Tower(board,Color.WHITE));
		placeNewPiece('c',8,new King(board,Color.WHITE));
	}
}