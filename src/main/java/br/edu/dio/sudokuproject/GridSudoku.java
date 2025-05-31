package br.edu.dio.sudokuproject;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridSudoku {

    // Static and final properties for grid dimensions
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    // Map where the key is a Coordinate object and the value is a Cell object
    private final Map<Coordinate, Cell> gridCellsMap;
    // Map where the key is a Coordinate object and the value is the subGrid associated
    private final Map<Coordinate, SudokuSubGrid> subGridMap;

    @Getter
    private final List<List<Cell>> gridCellsListForRowAndColumnChecking;

    // Integer representing the count of filled (non-empty) cells
    @Getter
    private Integer filledCellCount;


    // Boolean indicating if the game is consistent (no Sudoku rule violations)
    private Boolean isGameConsistent;

    @Getter
    private EnumGameStatus gameStatus;


    /**
     * Constructor for the GridSudoku class.
     * Initializes the maps and the game state properties.
     */
    public GridSudoku(List<Cell> initialCells) {
        this.subGridMap = new HashMap<>();
        this.gridCellsMap = new HashMap<>();
        this.filledCellCount = 0; // Starts with 0 filled cells
        this.isGameConsistent = true; // Assumes consistent until a violation is detected
        this.gridCellsListForRowAndColumnChecking = new ArrayList<>();

        // Populate the gridCells map with initially empty cells and maps the subGrids
        for (int rowIndex = 0; rowIndex < GRID_SIZE; rowIndex++) {

            ArrayList<Cell> rowList = new ArrayList<>();
            gridCellsListForRowAndColumnChecking.add(rowIndex, rowList);
            for (int columnIndex = 0; columnIndex < GRID_SIZE; columnIndex++) {
                Coordinate coordinate = new Coordinate(rowIndex, columnIndex);

                Cell newCell = new Cell(coordinate);
                rowList.add(columnIndex, newCell);

                int topLeftRow = (rowIndex / SUBGRID_SIZE) * SUBGRID_SIZE;
                int topLeftCol = (columnIndex / SUBGRID_SIZE) * SUBGRID_SIZE;
                Coordinate subGridKey = new Coordinate(topLeftRow, topLeftCol);
                SudokuSubGrid subGrid;

                if (!subGridMap.containsKey(subGridKey)) {
                    subGrid = new SudokuSubGrid(subGridKey);
                    // And add it to the map, associated with its key (top-left coordinate)
                    subGridMap.put(subGridKey, subGrid);
                }
                else subGrid = subGridMap.get(subGridKey);
                subGrid.getCells().add(newCell);
                gridCellsMap.put(coordinate, newCell);
                subGridMap.put(coordinate,subGrid);

            }
        }

        initialCells.forEach( inputCell -> {
            Coordinate coord = inputCell.getCoordinate();
            Cell existingCell = gridCellsMap.get(coord);
            existingCell.setInitialValue(inputCell.getValue());
        });

        this.gameStatus = EnumGameStatus.NOT_INITIATED;
    }

    /**
     * Sets the value of a specific cell in the grid and updates the game's state.
     * This is a simplification and does not include the full Sudoku validation logic here.
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @param value The value to set (0 for an empty cell).
     */
    public void setCellValue(int row, int col, int value) {
        Coordinate coord = new Coordinate(row, col);
        Cell cell = gridCellsMap.get(coord);
        cell.setValue(value);
        filledCellCount++;
        validateSudokuConsistency(row, col, false);
    }


    public void removeCellValue(Integer row, Integer col) throws Exception {
        Coordinate coord = new Coordinate(row, col);
        Cell cell = gridCellsMap.get(coord);
        boolean isEmpty = !cell.getIsFilled();
        if(isEmpty)
            throw new Exception("Cell is already empty");

        if (cell.getIsInitiallyFilled()) {
            throw new Exception("Initial values cannot be removed.");
        }

        validateSudokuConsistency(row, col, true);
        cell.removeValue();
        this.filledCellCount--;
        this.gameStatus = EnumGameStatus.INCOMPLETE;
    }

    private void validateSudokuConsistency(Integer rowIndex, Integer columnIndex, boolean isRemoval) {

        Coordinate changedCoordinate = new Coordinate(rowIndex, columnIndex);
        Cell lastChangedCell = gridCellsMap.get(changedCoordinate);

        Boolean consistentRow = checkRowConsistency(lastChangedCell, isRemoval);
        Boolean consistentColumn = checkColumnConsistency(lastChangedCell, isRemoval);
        Boolean consistentSubGrid = checkSubGridConsistency(lastChangedCell, isRemoval);

        Boolean cellConsistent = consistentRow && consistentColumn && consistentSubGrid;
        boolean anyOtherCellInconsistent = false;

        if (cellConsistent) {
            anyOtherCellInconsistent = gridCellsMap.values().stream().anyMatch(cell -> !cell.getConsistent());
        }

        this.isGameConsistent =  cellConsistent && !anyOtherCellInconsistent ;
    }

    private Boolean checkRowConsistency(Cell lastChangedCell, boolean isRemoval) {
        Boolean rowConsistent = true;
        Integer rowIndex = lastChangedCell.getCoordinate().getRow();
        Integer columnIndex = lastChangedCell.getCoordinate().getCol();

        List<Cell> row = this.gridCellsListForRowAndColumnChecking.get(rowIndex);
        List<Cell> repeatedCells = new ArrayList<>();
        repeatedCells.add(lastChangedCell);
        boolean conflict = false;

        for (int cIndex = 0; cIndex < GRID_SIZE; cIndex++) {
            Cell existingCell = row.get(cIndex);
            boolean repeatedValue = false;
            repeatedValue = existingCell.getValue() == lastChangedCell.getValue() && existingCell != lastChangedCell;
            conflict = conflict || repeatedValue;
            if(repeatedValue){
                lastChangedCell.setRowConsistent(false);
                existingCell.setRowConsistent(false);
                repeatedCells.add(existingCell);
            }

        }
        if(repeatedCells.size() < 3 && isRemoval){
            //this means a value was removed and had one or less duplicates in the same row
            //therefore both cells will be consistent after the change
            repeatedCells.forEach(cell -> cell.setRowConsistent(true));
            return rowConsistent;
        }

        rowConsistent = !conflict;


        return rowConsistent;
    }

    private Boolean checkColumnConsistency(Cell lastChangedCell, boolean isRemoval) {
        Boolean columnConsistent = true;
        List<Cell> repeatedCells = new ArrayList<>();
        repeatedCells.add(lastChangedCell);
        boolean conflict = false;

        for (int rIndex = 0; rIndex < GRID_SIZE; rIndex++) {
            Coordinate currentCoordinate = new Coordinate(rIndex, lastChangedCell.getCoordinate().getCol());
            Cell existingCell = gridCellsMap.get(currentCoordinate);
            boolean repeatedValue = false;
            repeatedValue = existingCell.getValue() == lastChangedCell.getValue() && existingCell != lastChangedCell;
            conflict = conflict || repeatedValue;
            if(repeatedValue){
                lastChangedCell.setColumnConsistent(false);
                existingCell.setColumnConsistent(false);
                repeatedCells.add(existingCell);
            }
        }

        if(repeatedCells.size() < 3 && isRemoval){
            //this means the changed value had one or less dupllicates in the same column
            //therefore both cells will be consistent after the change
            repeatedCells.forEach(cell -> cell.setColumnConsistent(true));
            return columnConsistent;

        }

        columnConsistent = !conflict;

        return columnConsistent;
    }

    private Boolean checkSubGridConsistency(Cell lastChangedCell, boolean isRemoval) {
        Boolean subGridConsistent = true;
        Coordinate coordinateChanged = lastChangedCell.getCoordinate();
        SudokuSubGrid subGrid = subGridMap.get(coordinateChanged);
        List<Cell> repeatedCells = new ArrayList<>();
        repeatedCells.add(lastChangedCell);
        boolean conflict = false;

        for (Cell existingCell : subGrid.getCells()) {

            boolean repeatedValue = false;

            repeatedValue = existingCell.getValue() == lastChangedCell.getValue() && existingCell != lastChangedCell;
            conflict = conflict || repeatedValue;

            if (repeatedValue){
                lastChangedCell.setSubGridConsistent(false);
                existingCell.setSubGridConsistent(false);
                repeatedCells.add(existingCell);
            }
        }
        if(repeatedCells.size() < 3 && isRemoval){
            //this means the changed value had one or less dupllicates in the same column
            //therefore both cells will be consistent after the change
            repeatedCells.forEach(cell -> cell.setColumnConsistent(true));
            return subGridConsistent;
        }
        subGridConsistent = !conflict;
        subGrid.setConsistent(subGridConsistent);

        return subGridConsistent;
    }

    public Boolean getGameConsistent() {
        return gameStatus == EnumGameStatus.NOT_INITIATED || isGameConsistent;
    }

    public void clearGame(){

        for (int rowIndex = 0; rowIndex < GRID_SIZE; rowIndex++) {
            for (int columnIndex = 0; columnIndex < GRID_SIZE; columnIndex++) {
                Coordinate coordinate = new Coordinate(rowIndex, columnIndex);
                Cell existingCell = gridCellsMap.get(coordinate);
                boolean isNotInitiallyFilled = !existingCell.getIsInitiallyFilled();
                if (isNotInitiallyFilled) {
                    existingCell.removeValue();
                }
            }
        }
        this.gameStatus = EnumGameStatus.NOT_INITIATED;

    }


    public void showGame(){
        System.out.println(" _____  _____  _____  _____  _____  _____  _____  _____  _____ ");
        for(int i = 0 ; i < 9 ; i++){
            System.out.println("|     ||     ||     ||     ||     ||     ||     ||     ||     |");
            List<Cell> row = this.gridCellsListForRowAndColumnChecking.get(i);
            StringBuilder lineBuilder = new StringBuilder();
            row.forEach(cell -> lineBuilder.append(cell.toString()));
            System.out.println(lineBuilder);
            System.out.println("|_____||_____||_____||_____||_____||_____||_____||_____||_____|");
        }
    }

    public Boolean startGame(){
        if(this.gameStatus != EnumGameStatus.NOT_INITIATED)
            return false;
        else {
            this.gameStatus = EnumGameStatus.INCOMPLETE;
            return true;
        }

    }

    public Boolean finishGame() {
        boolean isGameCompletelyFilled = (filledCellCount == (GRID_SIZE * GRID_SIZE));
        if(isGameCompletelyFilled && this.getGameConsistent()){
            gameStatus = EnumGameStatus.COMPLETE;
            return true;
        }
        return false;

    }

}
