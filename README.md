This project aims to fulfill a challenge proposed in a DIO course, according to the statement below. I tried to apply a significantly different logic than what was demonstrated.This was for practice purposes and to intentionally complexify the solution, showcasing different strategies and algorithms and giving opportunity to explain the trade-off between memory consumption and processing.

Sudoku Game
https://en.wikipedia.org/wiki/Sudoku

Requirements
You must have an interactive menu where we can choose from the following options:

    1 - Start a new game: The initial game board should be displayed on the screen, with spaces only filled by the initial numbers (use the main method's arguments to provide the initial numbers and their respective positions);

    2 - Place a new number: The following information should be requested from the player (number to be placed, horizontal index, and vertical index of the number). It should not be allowed to place a number in a position that is already filled (whether it's a fixed number or one entered by the player);

    3 - Remove a number: The vertical and horizontal indices of the number to be removed should be requested (if the number is a fixed game number, a message should be displayed informing that the number cannot be removed);

    4 - View game: Visualize the current state of the game;

    5 - Check game status: The current status of the game should be checked (not started, incomplete, and complete) and whether it contains errors or not (the game is incorrect when numbers are in conflicting positions). All game statuses can contain errors or not, except for the "not started" status, which is always error-free;

    6 - Clear game: Removes all numbers entered by the user and keeps the fixed numbers of the game;

    7 - Finish game: If the game has all spaces filled in a valid way, the game ends. Otherwise, inform the user that they must fill all spaces with their respective numbers;
