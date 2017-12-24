package multipleLinearRegression;

/**
 * Created by sss on 29/11/2017.
 */
public class TestLinearRegression {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        LinearRegression m = new LinearRegression("src/multipleLinearRegression/trainData.txt",0.0001,100000);
        m.printTrainData();
        m.trainTheta();
        m.printTheta();
        double []params={1,300};    //target should be around 10
        System.out.println("\nPredict 1\t300\ty: "+m.predict(params));
    }

}