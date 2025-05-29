package br.edu.dio.sudokuproject;

import lombok.Getter;

class Cell {

    @Getter
    private Integer value;
    private Boolean isInitiallyFilled; // Indicates if the value was part of the initial puzzle
	private Boolean isFilled;
	private final Coordinate coordinate;


    /**
     * Constructs a new Cell.
     */
    public Cell(Coordinate coordinate) {
        this.value = 0;
		this.isInitiallyFilled = false;
		this.isFilled = false;
		this.coordinate = coordinate;
		this.coordinate.setCell(this);
    }


    public void setInitialValue(int value){
		this.value = value;
		this.isFilled = true;
		this.isInitiallyFilled = true;
    }


    public void setValue(int newValue) throws RuntimeException{
        if ( value < 1 || value > 9) throw new RuntimeException("Valor Inválido: " + value);
		if ( value != 0 ) throw new RuntimeException("Célula já preenchida");
		this.value = newValue;
		this.isFilled = true;
    }
	
	
	public void removeValue() throws RuntimeException{
		this.value = 0;
		this.isFilled = false;
    }


	public Boolean getIsInitiallyFilled() {
		return isInitiallyFilled;
	}


    @Override
    public String toString() {
        return "|  " + ((value != 0) ? value : " ") + "  |";
    }
}
