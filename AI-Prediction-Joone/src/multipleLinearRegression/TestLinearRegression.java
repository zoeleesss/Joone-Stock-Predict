package multipleLinearRegression;

import stock.util.*;
/**
 * Created by sss on 29/11/2017.
 */
public class TestLinearRegression {

    public static void main(String[] args) {

        double[] inputArray = new double[]
                {
                        3333,5555,7777,9999    };
        double t=testLinearRegression(inputArray);
        System.out.println("data forecast : "+t );


    }


    public static double testLinearRegression(double []data)
    {
        double [][]trainData=StockUtil.vectorToMatrix(data);

        double t=test(trainData);
        return t;
    }

    private static double test(double [][]trainData)
    {
        double [][]t_data=new double [trainData.length-1][trainData[0].length+2];



        for (int i=0;i<trainData.length-1;i++)
        {
            for (int j=0;j<trainData[0].length+1;j++)
            {
                if (j==0)
                {
                    //trainData的第一列全部置为1.0（feature x0）
                    //trainData last col is Target data
                        t_data[i][0]=1.0;
                }
                else  {
                    t_data[i][j] = trainData[i][j - 1];
                    //if (j==trainData[0].length)
                    t_data[i][j+1] = trainData[i + 1][j - 1];
                }
            }

        }
        StockUtil.printData(t_data);

        t_data=StockUtil.mapToOne(t_data,1);

        for (int i=0;i<t_data.length;i++)
        {
            t_data[i][0]=1.0;
        }
        StockUtil.printData(t_data);
        LinearRegression m = new LinearRegression(t_data);
        m.printTrainData();
        m.trainTheta();
        m.printTheta();
        double []parms=new double[2];
        parms[0]=1;
        parms[1]=trainData[trainData.length-1][0];
        parms[1]=StockUtil.mapToOneApply(parms,2)[1];
        StockUtil.printData(parms);
        double next=m.predict(parms);
        double n=StockUtil.MapToOrigin(StockUtil.input_settings,next);
        System.out.println("\nPredict 1\t"+parms[1]+"\ty: "+next+"\t z: "+n);
        return n;
    }

    private static void testFromDisk()
    {
        LinearRegression m = new LinearRegression("src/multipleLinearRegression/trainData.txt",0.0001,100000);
        m.printTrainData();
        m.trainTheta();
        m.printTheta();
        double []params={1,43090.00};    //target should be around 10
        System.out.println("\nPredict 1\t43090\ty: "+m.predict(params));

    }


}