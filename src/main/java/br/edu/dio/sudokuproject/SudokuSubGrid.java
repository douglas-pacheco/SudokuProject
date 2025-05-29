package br.edu.dio.sudokuproject;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

class SudokuSubGrid {


    @Getter
    private List<Cell> cells;
    @Getter
    private final Coordinate subGridKey; // Top-left cell coordinate of this subgrid
    private Boolean consistent;




    public SudokuSubGrid(Coordinate subGridKey) {
        this.subGridKey = subGridKey;
        this.cells = new ArrayList<>();

    }

}