package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.Horse;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Tower;

public class ChessMatch {

	private Board board;
	private int turn;
	private Color currentPlayer;
	private boolean check;
	private boolean checkMate;
		
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();	
	
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public int getTurn() {
		return turn;
	}

	public boolean getCheck() {
		return check;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public ChessMatch() {
		board = new Board(8,8);
		currentPlayer = Color.WHITE;
		turn = 1;
		check = false;
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
		placeNewPiece('a',1,new Tower(board,Color.WHITE));
		placeNewPiece('b',1,new Horse(board,Color.WHITE));
		placeNewPiece('c',1,new Bishop(board,Color.WHITE));
		placeNewPiece('d',1,new Queen(board,Color.WHITE));
		placeNewPiece('e',1,new King(board,Color.WHITE,this));
		placeNewPiece('f',1,new Bishop(board,Color.WHITE));
		placeNewPiece('g',1,new Horse(board,Color.WHITE));
		placeNewPiece('h',1,new Tower(board,Color.WHITE));		
		placeNewPiece('a',2,new Pawn(board,Color.WHITE));
		placeNewPiece('b',2,new Pawn(board,Color.WHITE));
		placeNewPiece('c',2,new Pawn(board,Color.WHITE));
		placeNewPiece('d',2,new Pawn(board,Color.WHITE));
		placeNewPiece('e',2,new Pawn(board,Color.WHITE));
		placeNewPiece('f',2,new Pawn(board,Color.WHITE));
		placeNewPiece('g',2,new Pawn(board,Color.WHITE));
		placeNewPiece('h',2,new Pawn(board,Color.WHITE));
		
		
		placeNewPiece('a',8,new Tower(board,Color.BLACK));
		placeNewPiece('b',8,new Horse(board,Color.BLACK));
		placeNewPiece('c',8,new Bishop(board,Color.BLACK));
		placeNewPiece('d',8,new Queen(board,Color.BLACK));
		placeNewPiece('e',8,new King(board,Color.BLACK,this));
		placeNewPiece('f',8,new Bishop(board,Color.BLACK));
		placeNewPiece('g',8,new Horse(board,Color.BLACK));
		placeNewPiece('h',8,new Tower(board,Color.BLACK));		
		placeNewPiece('a',7,new Pawn(board,Color.BLACK));
		placeNewPiece('b',7,new Pawn(board,Color.BLACK));
		placeNewPiece('c',7,new Pawn(board,Color.BLACK));
		placeNewPiece('d',7,new Pawn(board,Color.BLACK));
		placeNewPiece('e',7,new Pawn(board,Color.BLACK));
		placeNewPiece('f',7,new Pawn(board,Color.BLACK));
		placeNewPiece('g',7,new Pawn(board,Color.BLACK));
		placeNewPiece('h',7,new Pawn(board,Color.BLACK));		
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source,target);
		Piece capturedPiece = makeMove(source,target);
		if(testCheck(currentPlayer)) {
			undoMove(source,target,capturedPiece);
			throw new ChessException("Você não pode se colocar em check!");
		}
		
		check = (testCheck(opponent(currentPlayer)))?true:false;
		
		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
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
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.PlacePiece(p, target);
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		//little rock
		if(p instanceof King && target.getColumn()==source.getColumn()+2) {
			Position sourceT = new Position(source.getRow(),source.getColumn()+3);
			Position targetT = new Position(source.getRow(),source.getColumn()+1);
			ChessPiece tower = (ChessPiece)board.removePiece(sourceT);
			board.PlacePiece(tower, targetT);
			tower.increaseMoveCount();			
			
		}
		//greater rock
		if(p instanceof King && target.getColumn()==source.getColumn()-2) {
			Position sourceT = new Position(source.getRow(),source.getColumn()-4);
			Position targetT = new Position(source.getRow(),source.getColumn()-1);
			ChessPiece tower = (ChessPiece)board.removePiece(sourceT);
			board.PlacePiece(tower, targetT);
			tower.increaseMoveCount();			
			
		}		
		
		return capturedPiece;		
	}
	
	
	private void undoMove(Position source, Position target, Piece captured) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.PlacePiece(p, source);
		if(captured != null) {
			board.PlacePiece(captured, target);
			capturedPieces.remove(captured);
			piecesOnTheBoard.add(captured);
		}
		
		//little rock
		if(p instanceof King && target.getColumn()==source.getColumn()+2) {
			Position sourceT = new Position(source.getRow(),source.getColumn()+3);
			Position targetT = new Position(source.getRow(),source.getColumn()+1);
			ChessPiece tower = (ChessPiece)board.removePiece(targetT);
			board.PlacePiece(tower, sourceT);
			tower.decreaseMoveCount();			
			
		}
		//greater rock
		if(p instanceof King && target.getColumn()==source.getColumn()-2) {
			Position sourceT = new Position(source.getRow(),source.getColumn()-4);
			Position targetT = new Position(source.getRow(),source.getColumn()-1);
			ChessPiece tower = (ChessPiece)board.removePiece(targetT);
			board.PlacePiece(tower, sourceT);
			tower.decreaseMoveCount();					
		}	
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
	
	private Color opponent(Color color) {
		return (color == Color.WHITE)?Color.BLACK:Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor()==color).collect(Collectors.toList());
		for(Piece p : list){	
			if(p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("O rei de cor" + color + "não está no tabuleiro");	
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor()==opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][]	mat=p.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color color){
		if(!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor()==color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][]	mat=p.possibleMoves();
			for(int i=0;i<board.getRows();i++) {
				for(int j=0;j<board.getColumns();j++) {
					if(mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i,j);
						Piece capturedPiece = makeMove(source,target);
						boolean testCheck = testCheck(color);
						undoMove(source,target,capturedPiece);
						if(!testCheck) {
							return false;
						}
					}
				}		
			}		
		}
		return true;
	} 	
}
