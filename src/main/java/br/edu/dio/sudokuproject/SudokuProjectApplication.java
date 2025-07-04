package br.edu.dio.sudokuproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SudokuProjectApplication {

    private final static Scanner scanner = new Scanner(System.in);

    private final static int TAMANHO_INPUT_REMOCAO_VALOR = 2;
    private final static int TAMANHO_INPUT_INSERCAO_VALOR = 3;

    public static void main(String[] args) {

        System.out.println("*** Sudoku Game ***");

        List<Cell> inputCells = new ArrayList<Cell>();
        Arrays.stream(args).forEach( inputCell -> {

                    boolean invalidInput = !isCoordinatesAndValueInputValid(inputCell);
                    if(invalidInput){
                        System.out.println("Invalid Initial Arguments. Aborting");
                        System.exit(0);
                    }
                    String[] coordinates = inputCell.split(",");

                    int value = Integer.parseInt(coordinates[0]);
                    int rowIndex = Integer.parseInt(coordinates[1]) -1; // convert input to index number in the range [0-8]
                    int columnIndex = Integer.parseInt(coordinates[2]) -1; // convert input to index number in the range [0-8]

                    Coordinate coordinate = new Coordinate(rowIndex, columnIndex);
                    Cell newCell = new Cell(coordinate);
                    newCell.setInitialValue(value);
                    inputCells.add(newCell);
            }
        );
        GridSudoku currentGame = new GridSudoku(inputCells);

        int option = -1;

        System.out.println("Press Enter to continue...");

        while (true) {
            String lineBreak = scanner.nextLine();

            System.out.println("Select 1 of the actions below and type the respective number");
            System.out.println("1 - Start Game");
            System.out.println("2 - Input Number");
            System.out.println("3 - Remove Number");
            System.out.println("4 - Show Current Game");
            System.out.println("5 - Check Game Status");
            System.out.println("6 - Clear Game");
            System.out.println("7 - Finish Game");
            System.out.println("8 - Exit");


            String input = scanner.nextLine();

            try {
                // Attempt to convert the input to an integer
                option = Integer.parseInt(input.trim()); // .trim() removes leading/trailing spaces

            } catch (NumberFormatException e) {
                option = -1;
            }

            switch (option) {
                case 1 -> {
                    Boolean result = currentGame.startGame();
                    System.out.println((result) ? "The game has been started" : "The game was already started");
                    System.out.println("Press Enter to continue...");
                }
                case 2 -> {

                    if (currentGame.getGameStatus() == EnumGameStatus.NOT_INITIATED) {
                        System.out.println("Please start the game first using Option (1)");
                        System.out.println("Press Enter to continue...");
                        continue;
                    }

                    System.out.println("Enter value and coordinates (range from 1 to 9) in the following format:");
                    System.out.println("new value, row number,column number");
                    String coordinatesInput = scanner.nextLine();
                    boolean invalidInput = !isCoordinatesAndValueInputValid(coordinatesInput);
                    if(invalidInput){
                        System.out.println("Invalid input. Returning to main Menu");
                        System.out.println("Press Enter to continue...");
                        continue;
                    }
                    String[] coordinates = coordinatesInput.split(",");

                    int value = Integer.parseInt(coordinates[0]);
                    int rowIndex = Integer.parseInt(coordinates[1]) -1; // convert input to index number in the range [0-8]
                    int columnIndex = Integer.parseInt(coordinates[2]) -1; // convert input to index number in the range [0-8]

                    try {
                        currentGame.setCellValue(rowIndex, columnIndex,value);
                        System.out.println("Value added");
                        System.out.println("Press Enter to continue...");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Press Enter to continue...");
                        continue;
                    }
                }
                case 3 -> {

                    if (currentGame.getGameStatus() == EnumGameStatus.NOT_INITIATED) {
                        System.out.println("Please start the game first using Option (1)");
                        System.out.println("Press Enter to continue...");
                        continue;
                    }
                    System.out.println("Insert the coordinates from which to remove value (from 1 to 9) in the following format:");
                    System.out.println("row number,column number");
                    String coordinatesInput = scanner.nextLine();

                    boolean invalidInput = !isCoordinatesInputValid(coordinatesInput);
                    if(invalidInput){
                        System.out.println("Invalid input. Returning to main Menu");
                        System.out.println("Press Enter to continue...");
                        continue;
                    }
                    String[] coordinates = coordinatesInput.split(",");

                    Integer rowIndex = Integer.parseInt(coordinates[0])-1;
                    Integer columnIndex = Integer.parseInt(coordinates[1])-1;
                    try {
                        currentGame.removeCellValue(rowIndex, columnIndex);
                        System.out.println("Value removed");
                        System.out.println("Press Enter to continue...");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Press Enter to continue...");

                        continue;
                    }
                }
                case 4 -> {
                    currentGame.showGame();
                    System.out.println("Press Enter to continue...");
                }


                case 5 -> {
                    String consistencyString = " and " + ((currentGame.getGameConsistent()) ? "Consistent" : "Inconsistent");
                    System.out.println("Game Status:" + currentGame.getGameStatus() + consistencyString );
                    System.out.println("Press Enter to continue...");
                }
                case 6 -> {
                    if (currentGame.getGameStatus() == EnumGameStatus.NOT_INITIATED) {
                        System.out.println("The game already is in its initial state");
                        System.out.println("Press Enter to continue...");
                        continue;
                    }
                    currentGame.clearGame();
                    System.out.println("The game has been cleared");
                    System.out.println("Press Enter to continue...");
                }
                case 7 -> {
                    boolean gameFinished = currentGame.finishGame();
                    if(gameFinished){
                        System.out.println("The game has been finished");
                        break;
                    }
                    System.out.println("You must fill all spaces with their respective numbers");
                    System.out.println("Press Enter to continue...");
                    continue;
                }
                case 8 -> System.exit(0);
                default -> {
                    System.out.println("Invalid option, select one of the menu options");
                    System.out.println("Press Enter to continue...");
                }
            }

        }
    }



    public static boolean isCoordinatesInputValid(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        String[] parts = input.trim().split(",");

        if (parts.length != TAMANHO_INPUT_REMOCAO_VALOR) {
            return false;
        }

        try {
            int num1 = Integer.parseInt(parts[0].trim());
            int num2 = Integer.parseInt(parts[1].trim());

            boolean isNum1Valid = (num1 > 0 && num1 < 10);
            boolean isNum2Valid = (num2 > 0 && num2 < 10);

            return isNum1Valid && isNum2Valid;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static boolean isCoordinatesAndValueInputValid(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        String[] parts = input.trim().split(",");

        if (parts.length != TAMANHO_INPUT_INSERCAO_VALOR) {
            return false;
        }

        try {
            int num1 = Integer.parseInt(parts[0].trim());
            int num2 = Integer.parseInt(parts[1].trim());
            int num3 = Integer.parseInt(parts[2].trim());

            boolean isNum1Valid = (num1 > 0 && num1 < 10);
            boolean isNum2Valid = (num2 > 0 && num2 < 10);
            boolean isNum3Valid = (num3 > 0 && num3 < 10);

            return isNum1Valid && isNum2Valid && isNum3Valid;

        } catch (NumberFormatException e) {
            return false;
        }
    }


}
