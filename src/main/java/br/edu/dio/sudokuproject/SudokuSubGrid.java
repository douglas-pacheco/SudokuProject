package br.edu.dio.sudokuproject;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
class SudokuSubGrid {


    private final List<Cell> cells;
    private final Coordinate subGridKey; // Top-left cell coordinate of this subgrid

    @Setter
    private Boolean consistent;




    public SudokuSubGrid(Coordinate subGridKey) {
        this.subGridKey = subGridKey;
        this.cells = new ArrayList<>();

    }

}