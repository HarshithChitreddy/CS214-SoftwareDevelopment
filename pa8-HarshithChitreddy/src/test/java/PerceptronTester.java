import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PerceptronTester {

    @Test
    public void testInitialization() {
        Perceptron perceptronModel = new Perceptron();
        double[] weightVector = perceptronModel.getWeights();
        assertEquals(64, weightVector.length);
        for (double weight : weightVector) {
            assertEquals(0.0, weight, 1e-6);
        }
        assertEquals(0.0, perceptronModel.getBias(), 1e-6);
    }

    @Test
    public void testEvaluate() {
        Perceptron perceptronModel = new Perceptron();
        double[] inputHistogram = new double[64];
        inputHistogram[0] = 1.0;
        inputHistogram[1] = 0.5;
        assertEquals(0.0, perceptronModel.evaluate(inputHistogram), 1e-6);
    }

    @Test
    public void testEvaluateAfterTraining() {
        Perceptron perceptronModel = new Perceptron();
        double[] inputHistogram = new double[64];
        inputHistogram[0] = 1.0;
        
        perceptronModel.train(inputHistogram, 1);
        double result = perceptronModel.evaluate(inputHistogram);
        assertTrue(result > 0, "Evaluation should be positive after training with positive label");
    }
}
