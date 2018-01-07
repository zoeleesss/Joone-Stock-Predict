/**
 * Created by sss on 12/11/2017.
 */

import org.joone.engine.*;
import org.joone.engine.learning.*;
import org.joone.io.*;
import org.joone.net.*;
import java.util.*;
import java.io.*;
import java.math.*;
import stock.util.*;

/**
 *
 * JOONE实现
 *
 * */

public class Test_NeuralTest implements NeuralNetListener,Serializable {

    //序列号用于检查兼容性
    private static final long serialVersionUID = -3472219226214066504L;

    //Use Single Tread
    private boolean singleThreadMode = true;


    /*
     *          GETTERS     &   SETTERS
     *
     */



    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {


        Test_NeuralTest xor = new Test_NeuralTest();

        double[][] testArray = new double[][]
                {
                        /*{ 1.0, 1.0 },
                        {0.002,0.99},
                        {0.8982,0.9909},
                        {0.002,0.012},
                        {0.0003,0.899}*/
                        {29209.640625,29431.810547,29145.130859,29367.060547,29367.060547}
                };




        double[][] results= xor.test("network.net",StockUtil.mapToOne(testArray,1,5),5,1);

        int i=0;
        //results=StockUtil.MapToOrigin(settings,results,1,5);

        for (double [] result:results ){
            System.out.println("test"+i+" : "+result[0]);
            i++;
        }


    }



    /**
     * Method declaration
     */

    public double[][] test(String NNet_Path,double[][] testData,int inputDimension,int outputDimension){
        NeuralNet testBPNN = this.Get_BPNN(NNet_Path);

        System.out.println("test data len "+testData.length);

        double[][] result = new double[testData.length][outputDimension];
        if (testBPNN != null) {
            double[] temp = new double[outputDimension];

            Layer input = testBPNN.getInputLayer();
            /**采用矩阵输入测试*/
            MemoryInputSynapse inputStream = new MemoryInputSynapse();
            input.removeAllInputs();
            input.addInputSynapse(inputStream);
            inputStream.setInputArray(testData);

            /**  get column selector by invoking a tool method   **/
            String columnSelector=getColumnSelector(inputDimension);
            System.out.println("selector "+columnSelector);
            inputStream.setAdvancedColumnSelector(columnSelector);

            Layer output = testBPNN.getOutputLayer();
            MemoryOutputSynapse fileOutput = new MemoryOutputSynapse();
            output.addOutputSynapse(fileOutput);

            Monitor monitor = testBPNN.getMonitor();
            monitor.setTrainingPatterns(testData.length);
            monitor.setTotCicles(1);
            monitor.setLearning(false);
            monitor.setSingleThreadMode(singleThreadMode);
            testBPNN.go();

            for(int i = 0;i<result.length;i++){
                temp = fileOutput.getNextPattern();

                for (int j=0;j<outputDimension;j++) {

                    result[i][j] = temp[j];

                }
            }

            System.out.println("testing...");
            return result;
        }
        return result;
    }


    /**
     * Method declaration
     */



    public void cicleTerminated(NeuralNetEvent e) {
        System.out.println(e.getSource().toString());
    }

    public void errorChanged(NeuralNetEvent e) {
        Monitor mon = (Monitor) e.getSource();
        if (mon.getCurrentCicle() % 100 == 0)
            System.out.println(" Epoch:  "
                    + (mon.getTotCicles() - mon.getCurrentCicle()) + "  RMSE: "
                    + mon.getGlobalError());
    }

    public void netStarted(NeuralNetEvent e) {
        Monitor mon = (Monitor) e.getSource();
        System.out.print(" Network started for  ");
        if (mon.isLearning())
            System.out.println(" training. ");
        else
            System.out.println(" interrogation. ");
    }

    public void netStopped(NeuralNetEvent e) {
        Monitor mon = (Monitor) e.getSource();
        System.out.println(" Network stopped. Last RMSE= "
                + mon.getGlobalError());

        if (mon.isLearning()) {

            System.out.println(" training. ");


        } else System.out.println(" interrogation. ");


    }

    public void netStoppedError(NeuralNetEvent e, String error) {
        System.out.println(" Network stopped due the following error:  "
                + error);
    }



    /*
     *              tools       for     invocation
     */

    private NeuralNet Get_BPNN(String NNet_Path) {
        NeuralNetLoader loader = new NeuralNetLoader(NNet_Path);
        NeuralNet nnet = loader.getNeuralNet();
        return nnet;
    }

    private String getColumnSelector(int inputDimension){

        String str="";

        for (int i=1;i<inputDimension+1;i++)
        {

            if (i==inputDimension)
                str+=i+"";
            else str+=i+",";

        }



        return str;


    }


}


