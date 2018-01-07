
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


/**
 *
 * JOONE实现
 *
 * */
public class Train_NeuralNet implements NeuralNetListener,Serializable
{

    //序列号用于检查兼容性
    private static final long serialVersionUID = -3472219226214066504L;

    private NeuralNet nnet = null;
    private FileInputSynapse inputSynapse, desiredOutputSynapse;
    private LinearLayer input;
    private SigmoidLayer hidden, output;
    boolean singleThreadMode = true;
    private String columnOfFactors;
    private String columnOfResults;
    private int nodesOfInputLayer;
    private int nodesOfOutputLayer;
    private int nodesOfHiddenLayer;
    private String inputFileString,desiredOutputFileString;
    private String savedNetFile;
    private int totalEpoches=1000;
    private double leaningRate=1;
    private double momentum=0.1;




    /**
     *          GETTERS     &   SETTERS
     *
     **/

    public Train_NeuralNet(int nodesOfInputLayer, int nodesOfOutputLayer, String inputFileString, String desiredOutputFileString, String savedNetFile) {

        this.nodesOfInputLayer = nodesOfInputLayer;
        this.nodesOfOutputLayer = nodesOfOutputLayer;

        this.columnOfFactors = getColumnSelector(nodesOfInputLayer);
        this.columnOfResults = getColumnSelector(nodesOfOutputLayer);

        this.inputFileString = inputFileString;
        this.desiredOutputFileString = desiredOutputFileString;
        this.savedNetFile = savedNetFile;
    }

    public Train_NeuralNet() {
    }

    public int getNodesOfInputLayer() {
        return nodesOfInputLayer;
    }

    public void setNodesOfInputLayer(int nodesOfInputLayer) {
        this.nodesOfInputLayer = nodesOfInputLayer;
    }

    public int getNodesOfOutputLayer() {
        return nodesOfOutputLayer;
    }

    public void setNodesOfOutputLayer(int nodesOfOutputLayer) {
        this.nodesOfOutputLayer = nodesOfOutputLayer;
    }

    public String getSavedNetFile() {
        return savedNetFile;
    }

    public void setSavedNetFile(String savedNetFile) {
        this.savedNetFile = savedNetFile;
    }

    public int getTotalEpoches() {
        return totalEpoches;
    }

    public void setTotalEpoches(int totalEpoches) {
        this.totalEpoches = totalEpoches;
    }

    public double getLeaningRate() {
        return leaningRate;
    }

    public void setLeaningRate(double leaningRate) {
        this.leaningRate = leaningRate;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public String getColumnOfFactors() {
        return columnOfFactors;
    }

    public void setColumnOfFactors(String columnOfFactors) {
        this.columnOfFactors = columnOfFactors;
    }

    public String getColumnOfResults() {
        return columnOfResults;
    }

    public void setColumnOfResults(String columnOfResults) {
        this.columnOfResults = columnOfResults;
    }

    public void setInputFileString(String inputFileString) {
        this.inputFileString = inputFileString;
    }

    public void setDesiredOutputFileString(String desiredOutputFileString) {
        this.desiredOutputFileString = desiredOutputFileString;
    }



    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String args[])
    {




        Train_NeuralNet xor = new Train_NeuralNet(4,1,"src/utilities/savedData","src/utilities/savedTarget","network.net");

        xor.initNeuralNet();
        xor.train();
    }



    /**

     * Method declaration
     */




    public void train()
    {


        // set the inputs
        inputSynapse.setInputFile(new File(inputFileString));

        inputSynapse.setAdvancedColumnSelector(columnOfFactors);
        //inputSynapse.setFirstCol(1);
        //inputSynapse.setLastCol(2);


        // set the desired outputs
        desiredOutputSynapse.setInputFile(new File(desiredOutputFileString));

        desiredOutputSynapse.setAdvancedColumnSelector(columnOfResults);
        //desiredOutputSynapse.setFirstCol(1);
        //desiredOutputSynapse.setLastCol(1);


        // get the monitor object to train or feed forward
        Monitor monitor = nnet.getMonitor();

        // set the monitor parameters
        monitor.setLearningRate(leaningRate);
        monitor.setMomentum(momentum);

        int lines_of_file=getTextLines(inputFileString);
        System.out.println(" lines "+lines_of_file);

       // monitor.setTrainingPatterns((int)((new File(inputFileString)).length()+1));
        monitor.setTrainingPatterns(lines_of_file);
        monitor.setTotCicles(totalEpoches);
        monitor.setLearning(true);

        long initms = System.currentTimeMillis();
        // Run the network in single-thread, synchronized mode
        nnet.getMonitor().setSingleThreadMode(singleThreadMode);
        nnet.go(true);
        System.out.println(" Total time=  "
                + (System.currentTimeMillis() - initms) + "  ms ");

    }


    /**
     * Method declaration
     */
    protected void initNeuralNet()
    {

        System.out.println(" init=   ");
        //set the nodes of hidden layer
        nodesOfHiddenLayer=(int)(Math.sqrt(nodesOfInputLayer+nodesOfOutputLayer)+2);

        // First create the three layers
        input = new LinearLayer();
        hidden = new SigmoidLayer();
        output = new SigmoidLayer();

        // set the dimensions of the layers
        input.setRows(nodesOfInputLayer);
        hidden.setRows(nodesOfHiddenLayer);
        output.setRows(nodesOfOutputLayer);

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
        inputSynapse = new FileInputSynapse();

        input.addInputSynapse(inputSynapse);

        // The Trainer and its desired output
        desiredOutputSynapse = new FileInputSynapse();

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
        //System.out.println(e.toString());
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

        if (mon.isLearning()) {

            System.out.println(" training. ");


            try {
                FileOutputStream stream = new FileOutputStream(savedNetFile);
                ObjectOutputStream out = new ObjectOutputStream(stream);
                out.writeObject(nnet);// 写入nnet对象
                out.close();
                stream.close();
                System.out.println("Save in " + savedNetFile);
            } catch (Exception excp) {
                excp.printStackTrace();
            }
        }

        else System.out.println(" interrogation. ");



    }

    public void netStoppedError(NeuralNetEvent e, String error)
    {
        System.out.println(" Network stopped due the following error:  "
                + error);
    }


    private int getTextLines(String filename){
        int x = 0;   // 用于统计行数，从0开始
        try {
            String path = filename;// 定义文件路径
            FileReader fr = new FileReader(path);   //这里定义一个字符流的输入流的节点流，用于读取文件（一个字符一个字符的读取）
            BufferedReader br = new BufferedReader(fr);  // 在定义好的流基础上套接一个处理流，用于更加效率的读取文件（一行一行的读取）

            while (br.readLine() != null) { //  readLine()方法是按行读的，返回值是这行的内容
                x++;   // 每读一行，则变量x累加1
            }
            br.close();
            fr.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

            return x;  //返回总的行数

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

