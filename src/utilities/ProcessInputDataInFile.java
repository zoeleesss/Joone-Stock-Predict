package utilities;

/**
 * Created by sss on 24/12/2017.
 */
import java.io.*;

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
                if (!(tempString.split("\t")[1]).equals("null"))
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


    public static void main(String[] args) {

        double data[][];

        String f="src/multipleLinearRegression/trainData.txt";
        int row = getRowNumber(f);
        data = new double[row][2];

        //for (int i = 0; i < row; i++)//data的第一列全部置为1.0（feature x0）
          //  data[i][0] = 1.0;

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

                for (int i = 0; i < 2; i++) {
                    //System.out.println(i+" "+tempData[i]);
                    if (i == 0) {

                        data[counter][i ] = Double.parseDouble(tempData[i]) - 31776;
                        //data[counter][i + 1] = Double.parseDouble(tempData[2]);
                    }//else if (i==1)
                    //{
                    //   data[counter][i + 1] = Double.parseDouble(tempData[2]);
                    //}*/
                    else {
                        data[counter][i ] = (int) (Double.parseDouble(tempData[i])) + 0.0;
                    }
                }

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



        //printTrainData(row,data);
        saveInFile("src/utilities/savedData",data,row,2);

    }

    public static void printTrainData(int row,double [][]data)
    {
        System.out.println("Train Data:\n");
        for(int i=0;i<1;i++)
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

    public static void saveInFile(String filename,double [][]data,int row,int col)
    {
        File f=new File(filename);
        String out="";
        for (int i=0;i<row;i++)
        {
            for (int j=0;j<col;j++)
            {
                if (j<col-1)
                out+=data[i][j]+";";
                else out+=data[i][j];
            }
            out+="\n";
        }


        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
            System.out.println(out);
            bufferedWriter.write(out);

        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }

}

