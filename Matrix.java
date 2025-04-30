package LenAl;

import java.util.ArrayList;
import java.util.List;

class Matrix {
    private Fraction[][] data;

    public Matrix(int rows, int cols) {
        data = new Fraction[rows][cols];
    }

    public void setElement(int row, int col, Fraction value) {
        data[row][col] = value;
    }

    public Fraction getElement(int row, int col) {
        return data[row][col];
    }

    public int getRows() {
        return data.length;
    }

    public int getCols() {
        return data[0].length;
    }

    public Matrix inverse() {
        if (getRows() != getCols()) {
            throw new IllegalArgumentException("Matrix must be square to find the inverse.");
        }

        int n = getRows();
        Matrix augmented = new Matrix(n, 2 * n);

        // Создание расширенной матрицы
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmented.setElement(i, j, this.getElement(i, j));
                augmented.setElement(i, j + n, (i == j) ? new Fraction(1, 1) : new Fraction(0, 1));
            }
        }

        // Применение метода Гаусса
        for (int i = 0; i < n; i++) {
            // Найти ненулевой пивот и обменять строки, если необходимо
            if (augmented.getElement(i, i).numerator == 0) {
                boolean swapped = false;
                for (int k = i + 1; k < n; k++) {
                    if (augmented.getElement(k, i).numerator != 0) {
                        // Обмен строк
                        for (int j = 0; j < 2 * n; j++) {
                            Fraction temp = augmented.getElement(i, j);
                            augmented.setElement(i, j, augmented.getElement(k, j));
                            augmented.setElement(k, j, temp);
                        }
                        swapped = true;
                        break;
                    }
                }
                if (!swapped) {
                    throw new IllegalArgumentException("Matrix is singular and cannot be inverted.");
                }
            }

            // Приведение к единичной матрице
            Fraction pivot = augmented.getElement(i, i);
            for (int j = 0; j < 2 * n; j++) {
                augmented.setElement(i, j, augmented.getElement(i, j).divide(pivot));
            }

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    Fraction factor = augmented.getElement(j, i);
                    for (int k = 0; k < 2 * n; k++) {
                        augmented.setElement(j, k, augmented.getElement(j, k).subtract(augmented.getElement(i, k).multiply(factor)));
                    }
                }
            }
        }

        // Извлечение правой части как обратной матрицы
        Matrix inverse = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse.setElement(i, j, augmented.getElement(i, j + n));
            }
        }
        return inverse;
    }

    public Matrix transitionMatrix() {
        if (getRows() == 0 || getCols() == 0) {
            throw new IllegalArgumentException("Matrix must not be empty to calculate transition matrix.");
        }

        Matrix transition = new Matrix(getRows(), getCols());

        for (int j = 0; j < getCols(); j++) {
            Fraction columnSum = new Fraction(0, 1);

            // Суммируем все элементы в текущем столбце
            for (int i = 0; i < getRows(); i++) {
                columnSum = columnSum.add(getElement(i, j));
            }

            // Проверяем, если сумма столбца равна нулю
            if (columnSum.numerator == 0) {
                // Если сумма равна нулю, устанавливаем все элементы столбца в 0
                for (int i = 0; i < getRows(); i++) {
                    transition.setElement(i, j, new Fraction(0, 1));
                }
            } else {
                // Нормализуем элементы в столбце
                for (int i = 0; i < getRows(); i++) {
                    transition.setElement(i, j, getElement(i, j).divide(columnSum));
                }
            }
        }
        return transition;
    }

    public Matrix multiply(Matrix other) {
        if (this.getCols() != other.getRows())
            throw new IllegalArgumentException("Invalid matrix dimensions for multiplication.");
        Matrix result = new Matrix(this.getRows(), other.getCols());
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < other.getCols(); j++) {
                Fraction sum = new Fraction(0, 1);
                for (int k = 0; k < this.getCols(); k++) {
                    sum = sum.add(this.getElement(i, k).multiply(other.getElement(k, j)));
                }
                result.setElement(i, j, sum);
            }
        }
        return result;
    }


    public int rank() {
        int rank = 0;
        Matrix temp = this.copy();
        for (int i = 0; i < temp.getRows(); i++) {
            for (int j = 0; j < temp.getCols(); j++) {
                if (temp.getElement(i, j).numerator != 0) {
                    rank++;
                    for (int k = 0; k < temp.getRows(); k++) {
                        if (k != i) {
                            Fraction factor = temp.getElement(k, j).divide(temp.getElement(i, j));
                            for (int l = 0; l < temp.getCols(); l++) {
                                temp.setElement(k, l, temp.getElement(k, l).subtract(temp.getElement(i, l).multiply(factor)));
                            }
                        }
                    }
                    break; // Переход к следующей строке
                }
            }
        }
        return rank;
    }

    public List<Fraction[]> kernel() {
        int n = getCols();
        int m = getRows();
        Matrix augmented = new Matrix(m, n + 1);

        // Заполнение расширенной матрицы
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                augmented.setElement(i, j, getElement(i, j));
            }
            augmented.setElement(i, n, new Fraction(0, 1)); // Прибавляем нулевой столбец
        }

        // Применение метода Гаусса
        for (int i = 0; i < m; i++) {
            // Найти ненулевой пивот
            if (augmented.getElement(i, i).numerator == 0) {
                boolean swapped = false;
                for (int k = i + 1; k < m; k++) {
                    if (augmented.getElement(k, i).numerator != 0) {
                        // Обмен строк
                        for (int j = 0; j <= n; j++) {
                            Fraction temp = augmented.getElement(i, j);
                            augmented.setElement(i, j, augmented.getElement(k, j));
                            augmented.setElement(k, j, temp);
                        }
                        swapped = true;
                        break;
                    }
                }
                if (!swapped) {
                    continue; // Переход к следующей строке
                }
            }

            // Нормализация строки
            Fraction pivot = augmented.getElement(i, i);
            for (int j = 0; j <= n; j++) {
                augmented.setElement(i, j, augmented.getElement(i, j).divide(pivot));
            }

            // Обнуление остальных строк
            for (int j = 0; j < m; j++) {
                if (j != i) {
                    Fraction factor = augmented.getElement(j, i);
                    for (int k = 0; k <= n; k++) {
                        augmented.setElement(j, k, augmented.getElement(j, k).subtract(augmented.getElement(i, k).multiply(factor)));
                    }
                }
            }
        }

        // Сбор ядра
        List<Fraction[]> kernelVectors = new ArrayList<>();
        boolean[] isFree = new boolean[n]; // Массив для отслеживания свободных переменных
        for (int i = 0; i < n; i++) {
            isFree[i] = true; // Изначально считаем все переменные свободными
        }

        // Определение свободных переменных
        for (int i = 0; i < m; i++) {
            boolean isZeroRow = true;
            for (int j = 0; j < n; j++) {
                if (augmented.getElement(i, j).numerator != 0) {
                    isZeroRow = false;
                    isFree[j] = false; // Если есть ненулевой элемент, это не свободная переменная
                    break;
                }
            }
            if (isZeroRow) {
                continue; // Пустая строка
            }
        }

        // Создание векторов ядра
        for (int i = 0; i < n; i++) {
            if (isFree[i]) {
                Fraction[] vector = new Fraction[n];
                for (int j = 0; j < n; j++) {
                    vector[j] = (j == i) ? new Fraction(1, 1) : new Fraction(0, 1); // Задаем свободные переменные
                }
                kernelVectors.add(vector);
            }
        }

        return kernelVectors;
    }
    public List<Fraction[]> image() {
        List<Fraction[]> imageVectors = new ArrayList<>();
        for (int j = 0; j < getCols(); j++) {
            Fraction[] columnVector = new Fraction[getRows()];
            for (int i = 0; i < getRows(); i++) {
                columnVector[i] = getElement(i, j);
            }
            imageVectors.add(columnVector);
        }
        return imageVectors;
    }

    public Matrix copy() {
        Matrix copy = new Matrix(getRows(), getCols());
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                copy.setElement(i, j, this.getElement(i, j));
            }
        }
        return copy;
    }

    public void print() {
        for (Fraction[] row : data) {
            for (Fraction value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }
}