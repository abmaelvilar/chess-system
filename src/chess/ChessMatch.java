package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Tower;

public class ChessMatch {

	private Board board;
	private int turn;
	private Color currentPlayer;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();	
	
	public int getTurn() {
		return turn;
	}

	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public ChessMatch() {
		board = new Board(8,8);
		currentPlayer = Color.WHITE;
		turn = 1;
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
		piecesOnTheBoard.add(piece);
	}
	
	private void initialSetup() {
		placeNewPiece('b',6,new Tower(board,Color.WHITE));
		placeNewPiece('c',8,new King(board,Color.BLACK));
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source,target);
		Piece capturedPiece = makeMove(source,target);
		nextTurn();
		return (ChessPiece)capturedPiece;
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) {
			throw new ChessException("Nao existe peça nesta na origem");
		}
		
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("A peça escolhida não é sua!");
		}
		
		
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("Você não pode mover esta peça");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("Essa peça não pode se mover para a posição escolhida");
		}
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.PlacePiece(p, target);
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;		
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE)?Color.BLACK:Color.WHITE;
	}
	
	
	
	
	
	
	
}
