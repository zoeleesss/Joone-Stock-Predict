package utilities;

/**
 * Created by sss on 24/12/2017.
 */
import java.io.*;
import stock.util.*;

public class ProcessInputDataInFile {



    private static int getRowNumber(String fileName)
    {
        int count =0;
       File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString=null;
            while ( (tempString=reader.readLine()) != null) {

                // null | volume = 0 omitted

                if (!(tempString.split("\t")[1]).equals("null")  && !(tempString.split("\t")[6].equals("0")))
                count++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        return count;

    }


    /*

    0,1 -> time ,open . omitted

    HIGH,LOW,CLOSE,ADJ,VOLUME
    2,3,4,5,6

    e


     */


    public static void main(String[] args) {

        double data[][];

        String f="src/multipleLinearRegression/trainData.txt";
        int row = getRowNumber(f);



        //for (int i = 0; i < row; i++)//data的第一列全部置为1.0（feature x0）
          //  data[i][0] = 1.0;

        int col=4;

        data = new double[row][4];
        double []target=new double[row];


        File file = new File(f);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int counter = 0;
            while ((counter < row) && (tempString = reader.readLine()) != null) {

                /*String tData0=tempString.split("\t")[0];
                String tData1=tempString.split("\t")[1];
                String tData2=tempString.split("\t")[2];*/
                String[] tempData = tempString.split("\t");
                //{tData0,tData1,tData2};
                //System.out.println(counter);
                if (tempData[1].equals("null")) continue;
                if (tempData[6].equals("0")) continue;

                for (int i=0;i<col;i++)
                {
                    data[counter][i]=Double.parseDouble(tempData[i+2]);
                }

                 target[counter]= (int)Double.parseDouble(tempData[4]);


                counter++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }



System.out.println(row);

        //printData(row,data);

        saveIn2Files("src/utilities/savedData","src/utilities/savedTarget",data,target,row,col);

    }


    public static void printTrainData(int row,double [][]data)
    {
        System.out.println("Train Data:\n");
        for(int i=0;i<6;i++)
            System.out.printf("%10s","x"+i+" ");
        System.out.printf("%10s","y"+" \n");
        for(int i=0;i<row;i++)
        {
            for(int j=0;j<2;j++)
            {
                System.out.printf("%10s",data[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printData(int row,double [][]data)
    {
        for(int i=0;i<row;i++)
        {
            for(int j=0;j<5;j++)
            {
                System.out.println(data[i][j]+" ");
            }
            System.out.println();
        }
    }

    public static void saveIn2Files(String inputF,String targetF,double [][]data,double []target,int row,int col)

    {

        saveInFile(3900,inputF,data,row,col);


        //target F
        saveTargetFile(3900,targetF,target,row);


    }



    public static void saveInFile(int start,String filename,double [][]data,int row,int col)
    {
        File f=new File(filename);
        String out="";

        //System.out.println("r "+row+" s "+(row-start)+" c "+col);

        double [][]t=new double[row-start][col];
        for (int i=start,index=0;i<row;i++,index++)
        {
            for (int j=0;j<col;j++)
            {
                //if (j<col-1)
                //out+=data[i][j]+";";
                    t[index][j]=data[i][j];

                //else out+=desiredData[i][j];
            }
            //out+="\n";
            //System.out.println(" sf: "+i);
        }

        double [][]desiredData=StockUtil.mapToOne(t,row-start,col);
        for (int i=0;i<row-start;i++)
        {
            for (int j=0;j<col;j++)
            {
                if (j<col-1)
                    out+=desiredData[i][j]+";";
                else out+=desiredData[i][j];
            }
            if (i<row-start-1) out+="\n";
            //System.out.println(" sf: "+i);
        }


        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
            //System.out.println(out);
            bufferedWriter.write(out);

            bufferedWriter.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public static void saveTargetFile(int start,String filename,double []data,int row) {
        File f = new File(filename);
        String out = "";



        double []t=new double[row-start];

        for (int i = start,index=0; i < row; i++,index++) {
             t[index]= data[i];
            //out += "\n";
        }
        double []desiredData=StockUtil.mapToOne(t,row-start);
        for (int i = 0; i < row-start; i++) {
            if (i<row-1)
                out+=desiredData[i]+"\n";
            else out+=desiredData[i];
        }
        double []d={0.02783946674416568};

        System.out.println("test "+StockUtil.MapToOrigin(StockUtil.target_settings,d,1)[0]);

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
            //System.out.println(out);
            bufferedWriter.write(out);

            bufferedWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

