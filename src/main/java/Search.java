
import java.util.*;

public class Search {

    public static final int horizontalGridSize = getHorizontalGridSize();
    public static final int verticalGridSize = getVerticalGridSizeGridSize();
    public static int count = 0;

    // why is the horizontalGridSize only 5, and the verticalGridSize only 6?
    // why are there only 6 input characters? \/
    //  public static final char[] input = { 'W', 'Y', 'I', 'T', 'Z', 'L', 'P', 'X', 'F', 'V', 'N', 'U'};
// 5x6
//public static final char[] input = { 'F', 'L', 'P'};
// 5x5
    public static final char[] input = getInput();
    // Static UI class to display the board
    public static UI ui = new UI(horizontalGridSize, verticalGridSize, 50);

    // Helper function which starts the brute force algorithm
    public static void search() {
        // Initialize an empty board
        int[][] field = new int[horizontalGridSize][verticalGridSize];

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                // -1 in the state matrix corresponds to empty square
                // Any positive number identifies the ID of the pentomino
                field[i][j] = -1;
            }
        }
        //Start brute force
        //bruteForce(field);
        iWantBcDegreeForFreePlz(field, input);
    }

    private static int characterToID(char character) {
        int pentID = -1;
        if (character == 'X' || character == 'x') {
            pentID = 0;
        } else if (character == 'I' || character == 'i') {
            pentID = 1;
        } else if (character == 'Z' || character == 'z') {
            pentID = 2;
        } else if (character == 'T' || character == 't') {
            pentID = 3;
        } else if (character == 'U' || character == 'u') {
            pentID = 4;
        } else if (character == 'V' || character == 'v') {
            pentID = 5;
        } else if (character == 'W' || character == 'w') {
            pentID = 6;
        } else if (character == 'Y' || character == 'y') {
            pentID = 7;
        } else if (character == 'L' || character == 'l') {
            pentID = 8;
        } else if (character == 'P' || character == 'p') {
            pentID = 9;
        } else if (character == 'N' || character == 'n') {
            pentID = 10;
        } else if (character == 'F' || character == 'f') {
            pentID = 11;
        }
        count++;
        return pentID;
    }

    private static boolean iWantBcDegreeForFreePlz(int[][] field, char[] restOfInput) {
        if (restOfInput.length > 0) {
            int localInput = restOfInput.length;
            restOfInput = Arrays.copyOf(restOfInput, restOfInput.length - 1);

            //System.out.println(localInput);
            int pentID = characterToID(input[localInput - 1]);
            for (int i = 0; i < PentominoDatabase.data[pentID].length; i++) {

                //***********
                int[][] pieceToPlace = PentominoDatabase.data[pentID][i];

                //Randomly generate a position to put the pentomino on the board
                int x = -1;
                int y = -1;
                if (horizontalGridSize < pieceToPlace.length) {
                    //this particular rotation of the piece is too long for the field
                    x = -1;
                } else {
                    //there are multiple possibilities where to place the piece without leaving the field

                    for (int j = 0; j < horizontalGridSize - pieceToPlace.length + 1; j++) {
                        x = j;
                        //there are multiple possibilities where to place the piece without leaving the field
                        if (verticalGridSize < pieceToPlace[0].length) {
                            y = -1;
                        } else {
                            for (int k = 0; k < verticalGridSize - pieceToPlace[0].length + 1; k++) {
                                y = k;
                                //If there is a possibility to place the piece on the field, do it
                                if (x >= 0 && y >= 0) {
                                    addPiece(field, pieceToPlace, pentID, x, y);
                                    if (iWantBcDegreeForFreePlz(field, restOfInput)) {
                                        ui.setState(field);
                                        System.out.println("Solution found");
                                    } else {
                                        for (int l = 0; l < field.length; l++) {
                                            for (int m = 0; m < field[l].length; m++) {
                                                field[l][m] = -1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Boolean solutionFound = true;
            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[i].length; j++) {
                    if (field[i][j] == -1) {
                        solutionFound = false;
                    }
                }
            }

            if (solutionFound) {
                //display the field
                return true;
            } else {
                return false;
            }
        }
        System.err.println("No solution found and will not be found!");
        return false;
    }

    private static void bruteForce(int[][] field) {
        Random random = new Random();
        boolean solutionFound = false;

        while (!solutionFound) {
            solutionFound = true;

            //Empty board again to find a solution
            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[i].length; j++) {
                    field[i][j] = -1;
                }
            }

            //Put all pentominoes with random rotation/inversion on a random position on the board
            for (int i = 0; i < input.length; i++) {

                //Choose a pentomino and randomly rotate/inverse it
                int pentID = characterToID(input[i]);
                int mutation = random.nextInt(PentominoDatabase.data[pentID].length);
                int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];

                //Randomly generate a position to put the pentomino on the board
                int x;
                int y;
                if (horizontalGridSize < pieceToPlace.length) {
                    //this particular rotation of the piece is too long for the field
                    x = -1;
                } else if (horizontalGridSize == pieceToPlace.length) {
                    //this particular rotation of the piece fits perfectly into the width of the field
                    x = 0;
                } else {
                    //there are multiple possibilities where to place the piece without leaving the field
                    x = random.nextInt(horizontalGridSize - pieceToPlace.length + 1);
                }

                if (verticalGridSize < pieceToPlace[0].length) {
                    //this particular rotation of the piece is too high for the field
                    y = -1;
                } else if (verticalGridSize == pieceToPlace[0].length) {
                    //this particular rotation of the piece fits perfectly into the height of the field
                    y = 0;
                } else {
                    //there are multiple possibilities where to place the piece without leaving the field
                    y = random.nextInt(verticalGridSize - pieceToPlace[0].length + 1);
                }

                //If there is a possibility to place the piece on the field, do it
                if (x >= 0 && y >= 0) {
                    addPiece(field, pieceToPlace, pentID, x, y);
                }
            }
            //		int[][] filledField = new int[field.length][field[0].length];
            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[i].length; j++) {
                    if (field[i][j] == -1) {
                        solutionFound = false;
                    }
                }
            }

            if (solutionFound) {
                //display the field
                ui.setState(field);
                System.out.println("Solution found");
                break;
            }
        }
    }

    // Adds a pentomino to the position on the field (overriding current board at that position)
    public static void addPiece(int[][] field, int[][] piece, int pieceID, int x, int y) {
        for (int i = 0; i < piece.length; i++) // loop over x position of pentomino
        {
            for (int j = 0; j < piece[i].length; j++) // loop over y position of pentomino
            {
                if (piece[i][j] == 1) {
                    // Add the ID of the pentomino to the board if the pentomino occupies this square
                    field[x + i][y + j] = pieceID;
                }
            }
        }
    }

    private static int getHorizontalGridSize() {
        System.out.println("Enter the horizontal grid size");
        Scanner in = new Scanner(System.in);
        return in.nextInt();
    }

    private static int getVerticalGridSizeGridSize() {
        System.out.println("Enter the vertical grid size");
        Scanner in = new Scanner(System.in);
        return in.nextInt();
    }

    private static char[] getInput() {
        int numberOfPentominoes = 0;
        int vertical = verticalGridSize;
        int horizontal = horizontalGridSize;
        char[] input1 = new char[numberOfPentominoes];
        if ((vertical * horizontal) % 5 != 0) {
            System.out.println("Invalid dimentions of the field");
        } else {
            if ((vertical * horizontal) > 60) {
                //the maximum area 12 pentominoes can cover
                System.out.println("The field is too large");
            } else {
                numberOfPentominoes = (vertical * horizontal) / 5;

                Scanner in = new Scanner(System.in);
                System.out.println("Type " + numberOfPentominoes + " letters representing the pentonimoes, without a space");
                String word = in.nextLine();

                input1 = word.toCharArray();
            }
        }
        return input1;
    }

    // Main function. Needs to be executed to start the brute force algorithm
    public static void main(String[] args) {
        long startTime = 0;
        long endTime = 0;
        long timeElapsed = 0;
        startTime = System.currentTimeMillis();
        search();
        endTime = System.currentTimeMillis();
        timeElapsed = endTime - startTime;
        System.out.println("The code took " + (timeElapsed * 0.001) + " seconds to find a solution");
        System.out.println("The program used " + count + " steps to get a solution.");
        return;
    }

}
