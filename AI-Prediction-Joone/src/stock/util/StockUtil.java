package stock.util;

import java.util.*;

/**
 * Created by sss on 06/01/2018.
 */
public class StockUtil {

    public static List<Double> target_settings;
    public static List<Double> input_settings;

    public StockUtil() {
    }

    public static double [][] mapToOne(double [][]data,int x,int y)
    {
        List<Double> ms=getMinNMax(data, x, y);
        double min= ms.get(0);
        double max=ms.get(1);
        double [][]mappedResult=new double[x][y];
        for (int i=0;i<x;i++)
        {
            for (int j=0;j<y;j++)
            {
                double t=data[i][j];


                mappedResult[i][j]=(t-min)/(max-min);

            }

        }
        return mappedResult;
    }

    public static double [][] mapToOne(double [][]data,int high_level)
    {
        int x=data.length;
        int y=data[0].length;
        List<Double> ms=getMinNMax(data, x, y);
        if (high_level==1) input_settings=ms;
        double min= ms.get(0);
        double max=ms.get(1);
        double [][]mappedResult=new double[x][y];
        for (int i=0;i<x;i++)
        {
            for (int j=0;j<y;j++)
            {
                double t=data[i][j];


                mappedResult[i][j]=(t-min)/(max-min);

            }

        }
        return mappedResult;
    }



    public static double [][] mapToOneApply(double [][]data,int x,int y)
    {
        List<Double> ms=input_settings;
        double min= ms.get(0);
        double max=ms.get(1);
        double [][]mappedResult=new double[x][y];
        for (int i=0;i<x;i++)
        {
            for (int j=0;j<y;j++)
            {
                double t=data[i][j];
                mappedResult[i][j]=(t-min)/(max-min);
            }
        }
        return mappedResult;
    }


    public static double [] mapToOne(double []data,int x)
    {
        List<Double> ms=getMinNMax(data, x);
        double min= ms.get(0);
        double max=ms.get(1);
        double []mappedResult=new double[x];
        for (int i=0;i<x;i++)
        {

                double t=data[i];


                mappedResult[i]=(t-min)/(max-min);


        }
        return mappedResult;
    }


    public static double [] mapToOneApply(double []data,int x)
    {
        List<Double> ms=input_settings;
        double min= ms.get(0);
        double max=ms.get(1);
        double []mappedResult=new double[x];
        for (int i=0;i<x;i++)
        {
                double t=data[i];
                mappedResult[i]=(t-min)/(max-min);
        }

        return mappedResult;
    }




    public static List<Double> getMinNMax(double [][]data,int x,int y) {

        List<Double> ms=new ArrayList<Double>();
        double min=data[0][0],max=data[0][0];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                double t = data[i][j];
                if (t>max) max=t;
                if (t<min) min=t;
            }

        }
        ms.add(min);
        ms.add(max);
        return ms;
    }

    public static List<Double> getMinNMax(double []data,int x) {

        List<Double> ms=new ArrayList<Double>();
        double min=data[0],max=data[0];

        for (int i = 0; i < x; i++) {

                double t = data[i];
                if (t>max) max=t;
                if (t<min) min=t;


        }
        ms.add(min);
        ms.add(max);
        target_settings=ms;
        return ms;

    }


    public static double [][] MapToOrigin(List<Double> settings,double [][]data, int x,int y)
    {

        double min= settings.get(0);
        double max=settings.get(1);
        double [][]mappedResult=new double[x][y];
        for (int i=0;i<x;i++)
        {
            for (int j=0;j<y;j++)
            {
                double t=data[i][j];

                mappedResult[i][j]=min+t*(max-min);

            }

        }
        return mappedResult;

    }
    public static double [] MapToOrigin(List<Double> settings,double []data, int x)
    {

        double min= settings.get(0);
        double max=settings.get(1);
        double []mappedResult=new double[x];
        for (int i=0;i<x;i++)
        {
                double t=data[i];

                mappedResult[i]=min+t*(max-min);


        }
        return mappedResult;

    }

    public static double MapToOrigin(List<Double> settings,double data)
    {

        double min= settings.get(0);
        double max=settings.get(1);
        double mappedResult;


        mappedResult=min+data*(max-min);

        return mappedResult;

    }


    public static double [][]vectorToMatrix(double []data)
    {
        double [][]d=new double[data.length][1];
        for (int i=0;i<data.length;i++)
        {
            d[i][0]=data[i];
        }
        return d;
    }

    public static double [][]getMethod2TrainData(double []data)
    {
        /**
         * 3                0
         * 4                1
         * 5    1   3,4     2
         * 6    1   4,5     3
         * 7    1   5,6     length-1
         */


        double [][]d=new double[data.length-2][2];
        for (int i=0;i<data.length-2;i++)
        {
            d[i][0]=data[i];
            d[i][1]=data[i+1];
        }
        input_settings=getMinNMax(d,data.length-2,1);
        d=mapToOne(d,data.length-2,2);
        return d;

    }

    public static double[][]getMethod2TestData(double []data)
    {
        /**
         * 3                0
         * 4                1
         * 5    1   3,4     2
         * 6    1   4,5     3
         * 7    1   5,6     length-1
         *
         * 6,7 -> next forecast
         */


        double [][]d=new double[1][2];
        d[0][0]=data[data.length-2];
        d[0][1]=data[data.length-1];
        d=mapToOneApply(d,1,2);
        return d;

    }

    public static double [][]getMethod2TargetData(double []data)
    {
        double [][]d=new double[data.length-2][1];
        for (int i = 2,index=0; i < data.length; i++,index++) {
            d[index][0]=data[i];
        }
        target_settings=getMinNMax(d,data.length-2,1);
        d=mapToOne(d,data.length-2,1);
        return d;
    }

    public static  double [][]getMethod1TrainData(double []data)
    {
        /**
         * 3              0
         * 4    1   3     1
         * 5    1   4     2
         * 6    1   5     3
         * 7    1   6     length-1
         */


        double [][]d=new double[data.length-1][1];
        for (int i=0;i<data.length-1;i++)
        {
            d[i][0]=data[i];
        }
        input_settings=getMinNMax(d,data.length-1,1);
        d=mapToOne(d,data.length-1,1);
        return d;

    }

    public static double [][]getMethod1TargetData(double []data)
    {
        double [][]d=new double[data.length-1][1];
        for (int i = 1,index=0; i < data.length; i++,index++) {
            d[index][0]=data[i];
        }
        target_settings=getMinNMax(d,data.length-1,1);
        d=mapToOne(d,data.length-1,1);
        return d;
    }

    public static double[][]getMethod1TestData(double []data)
    {

        // 7

        double [][]d=new double[1][1];
        d[0][0]=data[data.length-1];
        d=mapToOneApply(d,1,1);
        return d;
    }

    public static void printData(double [][]data,int row,int col)
    {
        System.out.println("printing data ..");
        for (int i=0;i<row;i++)
        {
            for (int j=0;i<col;j++)
            {
                System.out.print(data[i][j]+"\t");
            }
            System.out.println("\n");
        }

    }

    public static void printData(double [][]data)
    {
        System.out.println("printing data ..");
        for (int i=0;i<data.length;i++)
        {
            for (int j=0;j<data[0].length;j++)
            {
                System.out.print(data[i][j]+"\t");
            }
            System.out.println("\n");
        }

    }

    public static void printData(double []data)
    {
        System.out.println("printing data ..");
        for (int i=0;i<data.length;i++)
        {
            System.out.print(data[i]+"\t");

            System.out.println("\n");
        }

    }





}
