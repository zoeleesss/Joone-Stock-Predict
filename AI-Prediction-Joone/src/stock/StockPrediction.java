/**
 * Created by sss on 12/11/2017.
 */
package stock;
import org.joone.engine.*;
import org.joone.engine.learning.*;
import org.joone.io.*;
import org.joone.net.*;
import stock.util.StockUtil;

import java.util.*;

/*
 *
 * JOONE实现
 *
 * */
public class StockPrediction implements NeuralNetListener {
    private NeuralNet nnet = null;
    private MemoryInputSynapse inputSynapse, desiredOutputSynapse;
    LinearLayer input;
    SigmoidLayer hidden, output;
    boolean singleThreadMode = true;
    private double []savedPattern;
    private List<Double> settings;


    // XOR input
    /*private
*/
    // XOR desired output
    /*private
*/
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        // 2 methods
        // method 1 , use only one last ,
        // method 2 , use two last results


        double[] inputArray = new double[]
                {
                        3333,5555,2222,6666,9999,4444    };
        System.out.println("strategy 1: "+strategy1(inputArray));
        System.out.println("strategy 2: "+strategy2(inputArray));

    }

    public static double strategy1(double []data)
    {
        StockPrediction stock = new StockPrediction();
        double next=stock.method1(data);
        System.out.println(" next "+next);
        return next;
    }

    public static double strategy2(double []data)
    {
        StockPrediction stock = new StockPrediction();
        double next=stock.method2(data);
        System.out.println(" next "+next);
        return next;
    }


    private double method1(double []data)
    {

        double [][]input=StockUtil.getMethod1TrainData(data);
        double [][]target=StockUtil.getMethod1TargetData(data);
        double [][]test=StockUtil.getMethod1TestData(data);
        initNeuralNet(1);
        String selector="1";
        train(input,target,selector);
        savedPattern=new double[2];
        interrogate(test,selector);
        double pattern=savedPattern[0];
        return pattern;

    }

    private double method2(double []data)
    {
        double [][]input=StockUtil.getMethod2TrainData(data);
        double [][]target=StockUtil.getMethod2TargetData(data);
        double [][]test=StockUtil.getMethod2TestData(data);
        initNeuralNet(2);
        String selector="1,2";
        train(input,target,selector);
        savedPattern=new double[3];
        interrogate(test,selector);
        double pattern=savedPattern[0];
        return pattern;

    }



    /**
     * Method declaration
     */
    public void train(double [][]input,double [][]target,String selector)
    {

        // set the inputs



        inputSynapse.setInputArray(input);
        inputSynapse.setAdvancedColumnSelector(selector);
        // set the desired outputs
        desiredOutputSynapse.setInputArray(target);
        desiredOutputSynapse.setAdvancedColumnSelector("1");

        // get the monitor object to train or feed forward
        Monitor monitor = nnet.getMonitor();

        // set the monitor parameters
        monitor.setLearningRate(0.9);
        monitor.setMomentum(0.05);
        monitor.setTrainingPatterns(input.length);
        monitor.setTotCicles(5000);
        monitor.setLearning(true);

        long initms = System.currentTimeMillis();
        // Run the network in single-thread, synchronized mode
        nnet.getMonitor().setSingleThreadMode(singleThreadMode);
        nnet.go(true);
        System.out.println(" Total time=  "
                + (System.currentTimeMillis() - initms) + "  ms ");
    }

    private void interrogate(double [][]testData,String selector)
    {

        for (int i=0;i<testData.length;i++)
        {
            System.out.println(" d "+testData[i][0]);

        }



        inputSynapse.setInputArray(testData);
        inputSynapse.setAdvancedColumnSelector(selector);
        Monitor monitor = nnet.getMonitor();


        //Question !!!            HERE            !!!
        //
        // If the following parameter is set to 5,
        // Only 4 patterns could be produced.
        // So set the parameter to ! LENGTH+1 !

        monitor.setTrainingPatterns(testData.length+1);

        //Question ABOVE ^^^^^^^



        monitor.setTotCicles(1);
        monitor.setLearning(false);
        MemoryOutputSynapse memOut = new MemoryOutputSynapse();
        // set the output synapse to write the output of the net

        System.out.println(" length of array: "+testData.length);


        if (nnet != null)
        {
            nnet.addOutputSynapse(memOut);
            System.out.println(nnet.check());
            nnet.getMonitor().setSingleThreadMode(singleThreadMode);
            nnet.go();

            //Vector patterns= memOut.getAllPatterns();

            for (int i = 0; i <testData.length; i++)
            {
                System.out.println(" Trial # " + (i + 1));
                double[] pattern = memOut.getNextPattern();
                //double[] pattern = (double[])patterns.get(i);

                System.out.println(" Output pattern # " + (i + 1) + " = "
                        + StockUtil.MapToOrigin(StockUtil.target_settings,pattern[0]));

                savedPattern[i]=StockUtil.MapToOrigin(StockUtil.target_settings,pattern[0]);
            }

            System.out.println(" Interrogating Finished ");
        }
    }

    /**
     * Method declaration
     */
    protected void initNeuralNet(int input_dimension)
    {

        // First create the three layers
        input = new LinearLayer();
        hidden = new SigmoidLayer();
        output = new SigmoidLayer();

        // set the dimensions of the layers
        input.setRows(input_dimension);
        hidden.setRows(5);
        output.setRows(1);

        input.setLayerName(" L.input ");
        hidden.setLayerName(" L.hidden ");
        output.setLayerName(" L.output ");

        // Now create the two Synapses
        FullSynapse synapse_IH = new FullSynapse(); /* input -> hidden conn. */
        FullSynapse synapse_HO = new FullSynapse(); /* hidden -> output conn. */

        // Connect the input layer whit the hidden layer
        input.addOutputSynapse(synapse_IH);
        hidden.addInputSynapse(synapse_IH);

        // Connect the hidden layer whit the output layer
        hidden.addOutputSynapse(synapse_HO);
        output.addInputSynapse(synapse_HO);

        // the input to the neural net
        inputSynapse = new MemoryInputSynapse();

        input.addInputSynapse(inputSynapse);

        // The Trainer and its desired output
        desiredOutputSynapse = new MemoryInputSynapse();

        TeachingSynapse trainer = new TeachingSynapse();

        trainer.setDesired(desiredOutputSynapse);

        // Now we add this structure to a NeuralNet object
        nnet = new NeuralNet();

        nnet.addLayer(input, NeuralNet.INPUT_LAYER);
        nnet.addLayer(hidden, NeuralNet.HIDDEN_LAYER);
        nnet.addLayer(output, NeuralNet.OUTPUT_LAYER);
        nnet.setTeacher(trainer);
        output.addOutputSynapse(trainer);
        nnet.addNeuralNetListener(this);
    }

    public void cicleTerminated(NeuralNetEvent e)
    {
    }

    public void errorChanged(NeuralNetEvent e)
    {
        Monitor mon = (Monitor) e.getSource();
        if (mon.getCurrentCicle() % 100 == 0)
            System.out.println(" Epoch:  "
                    + (mon.getTotCicles() - mon.getCurrentCicle()) + "  RMSE: "
                    + mon.getGlobalError());
    }

    public void netStarted(NeuralNetEvent e)
    {
        Monitor mon = (Monitor) e.getSource();
        System.out.print(" Network started for  ");
        if (mon.isLearning())
            System.out.println(" training. ");
        else
            System.out.println(" interrogation. ");
    }

    public void netStopped(NeuralNetEvent e)
    {
        Monitor mon = (Monitor) e.getSource();
        System.out.println(" Network stopped. Last RMSE= "
                + mon.getGlobalError());
    }

    public void netStoppedError(NeuralNetEvent e, String error)
    {
        System.out.println(" Network stopped due the following error:  "
                + error);
    }
}

