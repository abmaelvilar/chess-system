package boardgame;


public class Board {

	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	public Board(int rows, int columns) {
		if(rows<1 || columns<1) {
			throw new BoardException("Erro: O Numero de linhas ou colunas deve ser pelomenos 1!");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];			
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}	
	
	public Piece piece(int row, int column) {
		if(!positionExists(row,column)) {
			throw new BoardException("Erro: A posição indicada não existe!");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position position) {
		if(!positionExists(position)) {
			throw new BoardException("Erro: A posição indicada não existe no tabuleiro!");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void PlacePiece(Piece piece,Position position) {
		if(thereIsAPiece(position)) {
			throw new BoardException("Erro: Já existe uma peça na posição "+position);			
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}
	
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(),position.getColumn());
	}
	
	public boolean positionExists(int row, int column) {
		return row>=0 && row<rows && column>=0 && column<columns;
	}
	
	public boolean thereIsAPiece(Position position) {
		if(!positionExists(position)) {
			throw new BoardException("Erro: A posição indicada não existe!");
		}
		return piece(position) != null;
	}
	
	
}















