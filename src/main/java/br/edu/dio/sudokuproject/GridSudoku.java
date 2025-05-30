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
    private EnumGameStatus statusJogo;


    /**
     * Constructor for the GridSudoku class.
     * Initializes the maps and the game state properties.
     */
    public GridSudoku() {
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

        this.statusJogo = EnumGameStatus.NOT_INITIATED;
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
        validateSudokuConsistency(row, col, value);
        // Logic to update isGameCompletelyFilled
        boolean isGameCompletelyFilled = (filledCellCount == (GRID_SIZE * GRID_SIZE));
        if(isGameCompletelyFilled)
            statusJogo = EnumGameStatus.COMPLETE;
    }


    public void removeCellValue(Integer row, Integer col) {
        Coordinate coord = new Coordinate(row, col);
        Cell cell = gridCellsMap.get(coord);
        if (cell.getIsInitiallyFilled()) {
            throw new RuntimeException("Célula originalmente preenchida não pode ser apagada");
        }

        validateSudokuConsistency(row, col, null);
        cell.removeValue();
        this.filledCellCount--;
        this.statusJogo = EnumGameStatus.INCOMPLETE;
    }

    private void validateSudokuConsistency(Integer rowIndex, Integer columnIndex, Integer value) {

        Coordinate changedCoordinate = new Coordinate(rowIndex, columnIndex);
        Cell lastChangedCell = gridCellsMap.get(changedCoordinate);

        Boolean consistentRow = checkRowConsistency(lastChangedCell, value);
        Boolean consistentColumn = checkColumnConsistency(lastChangedCell, value);
        Boolean consistentSubGrid = checkSubGridConsistency(lastChangedCell, value);
        this.isGameConsistent =  (consistentRow && consistentColumn && consistentSubGrid);
    }

    private Boolean checkRowConsistency(Cell lastChangedCell, Integer valueInserted) {
        Boolean rowConsistent = true;
        Integer rowIndex = lastChangedCell.getCoordinate().getRow();
        Integer columnIndex = lastChangedCell.getCoordinate().getCol();

        List<Cell> row = this.gridCellsListForRowAndColumnChecking.get(rowIndex);

        for (int cIndex = 0; cIndex < GRID_SIZE; cIndex++) {
            Cell existingCell = row.get(columnIndex);

            boolean repeatedValue = existingCell.getValue() == lastChangedCell.getValue();
            if(repeatedValue){
                rowConsistent = valueInserted != null;
                existingCell.setRowConsistent(rowConsistent);
                lastChangedCell.setRowConsistent(rowConsistent);
            }
        }
        return rowConsistent;
    }

    private Boolean checkColumnConsistency(Cell lastChangedCell, Integer valueInserted) {
        Boolean columnConsistent = true;

        for (int rIndex = 0; rIndex < GRID_SIZE; rIndex++) {
            Coordinate currentCoordinate = new Coordinate(rIndex, lastChangedCell.getCoordinate().getCol());
            Cell existingCell = gridCellsMap.get(currentCoordinate);

            boolean repeatedValue = existingCell.getValue() == lastChangedCell.getValue();
            if(repeatedValue){
                columnConsistent = valueInserted != null;
                existingCell.setColumnConsistent(columnConsistent);
                lastChangedCell.setColumnConsistent(columnConsistent);
            }
        }
        return columnConsistent;
    }

    private Boolean checkSubGridConsistency(Cell lastChangedCell, Integer valueInserted) {
        Boolean subGridConsistent = true;
        Coordinate coordinateChanged = lastChangedCell.getCoordinate();
        SudokuSubGrid subGrid = subGridMap.get(coordinateChanged);
        for (Cell existingCell : subGrid.getCells()) {
            boolean repeatedValue = existingCell.getValue() == lastChangedCell.getValue();
            if (repeatedValue)
                subGridConsistent = valueInserted != null;
                existingCell.setSubGridConsistent(subGridConsistent);
                lastChangedCell.setSubGridConsistent(subGridConsistent);
                subGrid.setConsistent(subGridConsistent);
        }
        return subGridConsistent;
    }

    public Boolean getGameConsistent() {
        return statusJogo == EnumGameStatus.NOT_INITIATED || isGameConsistent;
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
        this.statusJogo = EnumGameStatus.NOT_INITIATED;

    }

}
