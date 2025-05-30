package br.edu.dio.sudokuproject;

import lombok.Getter;

class Cell {

    @Getter
    private Integer value;
    @Getter
    private Boolean isInitiallyFilled; // Indicates if the value was part of the initial puzzle
    @Getter
    private Boolean isFilled;
    @Getter
	private final Coordinate coordinate;
    @Getter
    private Boolean consistent;

    private Boolean rowConsistent;
    private Boolean columnConsistent;
    private Boolean subGridConsistent;


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


    public void setInitialValue(Integer value){
		this.value = value;
		this.isFilled = true;
		this.isInitiallyFilled = true;
    }


    public void setValue(int newValue) throws RuntimeException{
        if ( newValue < 1 || newValue > 9) throw new RuntimeException("Valor Inválido: " + newValue);
		if ( value != 0 ) throw new RuntimeException("Célula já preenchida");
        this.value = newValue;
        this.isFilled = true;
    }
	
	
	public void removeValue() throws RuntimeException{
		this.value = 0;
		this.isFilled = false;
        this.setRowConsistent(true);
        this.setColumnConsistent(true);
        this.setSubGridConsistent(true);
    }

    public void setRowConsistent(Boolean rowConsistent) {
        this.rowConsistent = rowConsistent;
        this.consistent = this.rowConsistent && this.columnConsistent && this.subGridConsistent;
    }

    public void setColumnConsistent(Boolean columnConsistent) {
        this.columnConsistent = columnConsistent;
        this.consistent = this.rowConsistent && this.columnConsistent && this.subGridConsistent;
    }

    public void setSubGridConsistent(Boolean subGridConsistent) {
        this.subGridConsistent = subGridConsistent;
        this.consistent = this.rowConsistent && this.columnConsistent && this.subGridConsistent;
    }
    
    @Override
    public String toString() {
        return "|  " + ((value != 0) ? value : " ") + "  |";
    }
}
