package br.edu.dio.sudokuproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridSudoku {

    // Static and final properties for grid dimensions
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    // Map where the key is a Coordinate object and the value is a Cell object
    private Map<Coordinate, Cell> gridCellsMap;
    // Map where the key is a Coordinate object and the value is the subGrid associated
    private Map<Coordinate, SudokuSubGrid> subGridMap;

    private List<List<Cell>> gridCellsListForRowAndColumnChecking;


    // Integer representing the count of filled (non-empty) cells
    private Integer filledCellCount;

    // Boolean indicating if the game is completely filled (all 81 cells)
    private Boolean isGameCompletelyFilled;

    // Boolean indicating if the game is consistent (no Sudoku rule violations)
    private Boolean isGameConsistent;

    /**
     * Constructor for the GridSudoku class.
     * Initializes the maps and the game state properties.
     */
    public GridSudoku() {
        this.gridCellsMap = new HashMap<>();
        this.filledCellCount = 0; // Starts with 0 filled cells
        this.isGameCompletelyFilled = false; // Starts as false
        this.isGameConsistent = true; // Assumes consistent until a violation is detected
        this.gridCellsListForRowAndColumnChecking = new ArrayList<List<Cell>>();

        // Populate the gridCells map with initially empty cells and maps the subGrids
        for (int rowIndex = 0; rowIndex < GRID_SIZE; rowIndex++) {

            ArrayList<Cell> rowList = new ArrayList<Cell>();
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

                subGridMap.put(coordinate,subGrid);
                gridCellsMap.put(coordinate, newCell);

            }
        }
    }

    // --- Getter Methods for properties ---

    public Boolean isGameCompletelyFilled() {
        return isGameCompletelyFilled;
    }

    public Boolean isGameConsistent() {
        return isGameConsistent;
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


        boolean consistentRow = false;
        //TODO

        boolean consistentColumn = false;
        //TODO

        boolean consistentSubGrid = false;
        //TODO

        this.isGameConsistent =  ( consistentRow  && consistentColumn && consistentSubGrid);


        // Logic to update isGameCompletelyFilled
        isGameCompletelyFilled = (filledCellCount == (GRID_SIZE * GRID_SIZE));


    }


    public void removeCellValue(int row, int col) {
        Coordinate coord = new Coordinate(row, col);
        Cell cell = gridCellsMap.get(coord);


        if (cell.getIsInitiallyFilled()) {
            throw new RuntimeException("Célula originalmente preenchida não pode ser apagada");
        }

        cell.removeValue();
        filledCellCount--;

        boolean consistentRow = false;
        //TODO

        boolean consistentColumn = false;
        //TODO

        boolean consistentSubGrid = false;
        //TODO

        this.isGameConsistent =  ( consistentRow  && consistentColumn && consistentSubGrid);

        // Logic to update isGameCompletelyFilled
        isGameCompletelyFilled = (filledCellCount == (GRID_SIZE * GRID_SIZE));


    }


}
