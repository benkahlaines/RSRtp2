import java.io.Serializable;

public class Matrix implements Serializable {
    private double[][] data;

    public Matrix(double[][] data) {
        this.data = data;
    }

    public double[][] getData() {
        return data;
    }


    

    public static double[][] transpose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
    
        double[][] transposedMatrix = new double[cols][rows];
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }
    
        return transposedMatrix;
    }
    

  

    // nouvelle méthode pour créer une matrice à partir d'un tableau de double
    public static Matrix fromArray(double[][] array) {
        return new Matrix(array);
    }

    public static double[][] sum(double[][] mat1, double[][] mat2) {
        double[][] result = new double[mat1.length][mat1[0].length];
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat1[0].length; j++) {
                result[i][j] = mat1[i][j] + mat2[i][j];
            }
        }
        return result;
    }
    public static double[][] product(double[][] mat1, double[][] mat2) {
        int n = mat1.length;
        int m = mat2[0].length;
        int p = mat2.length;
        double[][] result = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < p; k++) {
                    result[i][j] += mat1[i][k] * mat2[k][j];
                }
            }
        }
        return result;
    }
    
    
    

    
}
