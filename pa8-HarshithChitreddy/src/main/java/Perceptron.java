public class Perceptron {
    private double[] weightVector;
    private double biasTerm;
    private static final int NUMBER_OF_BINS = 64;
    private static final double LEARNING_RATE = 1.0;

    public Perceptron() {
        weightVector = new double[NUMBER_OF_BINS];
        biasTerm = 0.0;
    }

    public void train(double[] inputHistogram, int targetLabel) {
        double predictedValue = evaluate(inputHistogram);
        double updateValue = (targetLabel - predictedValue) * LEARNING_RATE;
        for (int i = 0; i < NUMBER_OF_BINS; i++) {
            weightVector[i] += updateValue * inputHistogram[i];
        }
        biasTerm += updateValue;
    }

    public double evaluate(double[] inputHistogram) {
        double weightedSum = biasTerm;
        for (int i = 0; i < NUMBER_OF_BINS; i++) {
            weightedSum += weightVector[i] * inputHistogram[i];
        }
        return weightedSum;
    }

    public double[] getWeights() {
        return weightVector;
    }

    public double getBias() {
        return biasTerm;
    }
}
