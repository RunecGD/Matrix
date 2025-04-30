package LenAl;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Matrix matrix = inputMatrix(scanner);

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Умножение матрицы");
            System.out.println("2. Вывести матрицу");
            System.out.println("3. Найти обратную матрицу");
            System.out.println("4. Найти матрицу перехода");
            System.out.println("5. Найти ранг матрицы");
            System.out.println("6. Найти ядро матрицы");
            System.out.println("7. Найти образ матрицы");
            System.out.println("8. Выход");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    Matrix matrix2 = inputMatrix(scanner);
                    try {
                        Matrix result = matrix.multiply(matrix2);
                        System.out.println("Результат умножения матриц:");
                        result.print();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Ваша матрица:");
                    matrix.print();
                    break;
                case 3:
                    try {
                        Matrix inverse = matrix.inverse();
                        System.out.println("Обратная матрица:");
                        inverse.print();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        Matrix transition = matrix.transitionMatrix();
                        System.out.println("Матрица перехода:");
                        transition.print();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    int rank = matrix.rank();
                    System.out.println("Ранг матрицы: " + rank);
                    break;
                case 6:
                    List<Fraction[]> kernel = matrix.kernel();
                    System.out.println("Ядро матрицы:");
                    for (Fraction[] vector : kernel) {
                        for (Fraction value : vector) {
                            System.out.print(value + "\t");
                        }
                        System.out.println();
                    }
                    break;
                case 7:
                    List<Fraction[]> image = matrix.image();
                    System.out.println("Образ матрицы:");
                    for (Fraction[] vector : image) {
                        for (Fraction value : vector) {
                            System.out.print(value + "\t");
                        }
                        System.out.println();
                    }
                    break;
                case 8:
                    continueRunning = false;
                    break;
                default:
                    System.out.println("Некорректный выбор. Пожалуйста, попробуйте снова.");
            }
        }

        scanner.close();
    }

    private static Matrix inputMatrix(Scanner scanner) {
        System.out.print("Введите количество строк матрицы: ");
        int rows = scanner.nextInt();
        System.out.print("Введите количество столбцов матрицы: ");
        int cols = scanner.nextInt();
        Matrix matrix = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print("Введите элемент [" + (i + 1) + "][" + (j + 1) + "] в виде 'числитель/знаменатель' или 'целое': ");
                String input = scanner.next();
                if (input.contains("/")) {
                    String[] parts = input.split("/");
                    int numerator = Integer.parseInt(parts[0]);
                    int denominator = Integer.parseInt(parts[1]);
                    matrix.setElement(i, j, new Fraction(numerator, denominator));
                } else {
                    int value = Integer.parseInt(input);
                    matrix.setElement(i, j, new Fraction(value, 1));
                }
            }
        }
        return matrix;
    }
}