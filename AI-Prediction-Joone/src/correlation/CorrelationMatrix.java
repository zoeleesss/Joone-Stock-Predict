package correlation;

/**
 * Created by sss on 07/01/2018.
 */
public class CorrelationMatrix {


    private double[][]data;

    public CorrelationMatrix(double[][] data) {
        this.data = data;
    }

    public CorrelationMatrix() {
    }

    public static void main(String[] args) {
        double [][] array = {
                {11.11, 14.34, 5.10, 20.41},
                {13.63, 17.15, 4.90, 26.94},
                {16.99, 17.45, 5.06, 25.75},
                {21.12, 18.05, 5.32, 25.98},
                {24.86, 20.42, 7.48, 16.18},
                {25.68, 23.60, 10.10, 4.18},
                {25.77, 23.42, 13.31, 2.43},
                {25.88, 22.09, 9.49, 6.50},
                {27.43, 21.43, 11.09, 25.78},
                {29.95, 24.96, 14.48, 28.16},
                {33.53, 28.37, 16.97, 24.26},
                {37.31, 42.57, 20.16, 30.18},
                {41.16, 45.16, 26.39, 17.08},
                {45.73, 52.46, 27.04, 7.39},
                {50.59, 45.30, 23.08, 3.88},
                {58.82, 46.80, 24.46, 10.53},
                {65.28, 51.11, 33.82, 20.09},
                {71.25, 53.29, 33.57, 21.22},
                {73.37, 55.36, 39.59, 12.63},
                {76.68, 45.00, 48.49, 11.17}};


        print(getCorrelationMatrix(array));

    }


    public static double [][]getCorrelationMatrix(double [][]array)
    {

        double [][]d=getStandard(array);
        //print(d);
        d=getCorrelation(d);
        //print(d);
        return d;
    }



    // 计算协方差矩阵--方阵
    private static double[][] getCovMatrix(double[][] array) {
        int h = array.length;
        int l = array[0].length;
        double[] average = new double[l];// 每个变量的均值
        double[][] temp = new double[h][l];// 保存计算协方差的中间矩阵
        double[][] result = new double[l][l];
        // 求均值
        for (int i = 0; i < l; i++) {
            double t = 0.0;
            for (int j = 0; j < h; j++) {
                t += array[j][i];
            }

            average[i] = t / h;
        }
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < l; j++) {
                temp[i][j] = array[i][j] - average[j];
            }
        }
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                double t = 0;
                for (int k = 0; k < h; k++) {
                    t += temp[k][i] * temp[k][j];
                }
                result[i][j] = t / (h - 1);
            }
        }

        System.out.println("协方差矩阵：");

        return result;
    }

    // 计算期望与标准差,标准化数据
    private static double[][] getStandard(double[][] array) {
        int h = array.length; // 行号--h
        int l = array[0].length;// 列号--l
        double[] average = new double[l];// 每个变量的均值
        double[][] result = new double[h][l];// 标准化后的向量
        double[] standardDevition = new double[l];// 每个变量的标准差
        // 求均值
        for (int i = 0; i < l; i++) {
            double temp = 0.0;
            for (int j = 0; j < h; j++) {
                temp += array[j][i];
            }
            average[i] = temp / h;
        }
        // 求标准差
        for (int i = 0; i < l; i++) {
            double temp = 0.0;
            for (int j = 0; j < h; j++) {
                temp += Math.pow((array[j][i] - average[i]), 2);
            }
            standardDevition[i] = Math.sqrt(temp / (h - 1));
        }
        // 求标准化后的矩阵
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < l; j++) {
                result[i][j] = (array[i][j] - average[j]) / standardDevition[j];
            }
        }
        CorrelationMatrix m = new CorrelationMatrix(result);
        //System.out.println("标准化后的矩阵：");
        //m.print(6, 5);
        return result;
    }


    // 计算相关矩阵---方阵
    private static double[][] getCorrelation(double[][] array) {
        int h = array.length;
        int l = array[0].length;
        double[][] result = new double[l][l];
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                double temp = 0;
                for (int k = 0; k < h; k++) {
                    temp += array[k][i] * array[k][j];
                }
                result[i][j] = temp / (h - 1);
            }
        }
        CorrelationMatrix m = new CorrelationMatrix(result);
        System.out.println("相关矩阵：");
        //m.print(6, 5);
        return result;
    }


    public static void print(double [][]array)
    {
        int h = array.length;
        int l = array[0].length;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < l; j++) {

                System.out.print(array[i][j]+"\t");

            }
            System.out.println("\n");
        }


    }
}
