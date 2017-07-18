import java.util.Random;

/**
 * Created by sina on 7/14/2017.
 */
public class BGWO2 {


    int X[][];
    int numFeatures;
    double fitness[];
    int a;
    int alpha, beta, delta;
    GWOFitCalculator gwoFitCalculator;

    public void startGWO2() throws Exception {
        String path = "C:\\Users\\sina\\Desktop\\LBS-Kurdestan\\BGWO1\\data\\hepatit.csv";
        gwoFitCalculator = new GWOFitCalculator(path);
        numFeatures = gwoFitCalculator.getNumFeatures();
        init();
        run();
    }

    private void init() throws Exception {
        X = new int[10][numFeatures];
        fitness = new double[10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < numFeatures; j++) {


                X[i][j] = random();
            }
            fitness[i] = fit(X[i]);

        }

        double alphaTemp = Double.MAX_VALUE,
                betaTemp = Double.MAX_VALUE,
                deltaTemp = Double.MAX_VALUE;


        for (int i = 0; i < fitness.length; i++) {
            if (fitness[i] < alphaTemp) {
                alphaTemp = fitness[i];
                alpha = i;
            }

        }
        for (int i = 0; i < fitness.length; i++) {
            if (fitness[i] < betaTemp && i != alpha) {
                betaTemp = fitness[i];
                beta = i;
            }

        }
        for (int i = 0; i < fitness.length; i++) {
            if (fitness[i] < deltaTemp && i != alpha && i != beta) {
                deltaTemp = fitness[i];
                delta = i;
            }

        }

    }

    private int random() {
        Random rand = new Random();
        double r = rand.nextDouble();
        if (r < 0.5) {
            return 0;
        } else {
            return 1;
        }

    }


    private void run() throws Exception {

        for (int i = 0; i < 20; i++) {
            a = 2 - (i * 2) / 20;
            for (int j = 0; j < X.length; j++) {
                for (int k = 0; k < numFeatures; k++) {


                    double A1, A2, A3;
                    Random random = new Random();
                    double r11 = random.nextDouble();
                    double r12 = random.nextDouble();
                    double r13 = random.nextDouble();
                    double r21 = random.nextDouble();
                    double r22 = random.nextDouble();
                    double r23 = random.nextDouble();
                    double D_alpha, D_beta, D_delta;
                    double C1, C2, C3;
                    C1 = 2 * r21;
                    C2 = 2 * r22;
                    C3 = 2 * r23;
                    D_alpha = Math.abs(C1 * X[alpha][k] - X[j][k]);
                    D_beta = Math.abs(C2 * X[beta][k] - X[j][k]);
                    D_delta = Math.abs(C3 * X[delta][k] - X[j][k]);
                    A1 = (2 * a * r11) - a;
                    A2 = (2 * a * r12) - a;
                    A3 = (2 * a * r13) - a;
                    double X1, X2, X3;
                    X1 = Math.abs(X[alpha][k] - A1 * D_alpha);
                    X2 = Math.abs(X[beta][k] - A2 * D_beta);
                    X3 = Math.abs(X[delta][k] - A3 * D_delta);
                    X[j][k] = sigmoid((X1 + X2 + X3) / 3);
                }
                X[j] = refine(X[j]);
            }


            for (int j = 0; j < X.length; j++) {
                fitness[j] = fit(X[j]);

            }
            double alphaTemp = Double.MAX_VALUE,
                    betaTemp = Double.MAX_VALUE,
                    deltaTemp = Double.MAX_VALUE;


            for (int j = 0; j < fitness.length; j++) {
                if (fitness[j] < alphaTemp) {
                    alphaTemp = fitness[j];
                    alpha = j;
                }

            }
            for (int j = 0; j < fitness.length; j++) {
                if (fitness[j] < betaTemp && j != alpha) {
                    betaTemp = fitness[j];
                    beta = j;
                }

            }
            for (int j = 0; j < fitness.length; j++) {
                if (fitness[j] < deltaTemp && j != alpha && j != beta) {
                    deltaTemp = fitness[j];
                    delta = j;
                }

            }


        }
        System.out.println("====");
        for (int i = 0; i < numFeatures; i++) {

            System.out.print(X[alpha][i] + ",");
        }
        System.out.println();
        System.out.println(fit(X[alpha]));
    }

    private int[] refine(int[] x) {

    }

    private int sigmoid(double v) {


        double s = 1 / (1 + (Math.pow(Math.E, -10 * (v - 0.5))));
        Random random = new Random();
        double r = random.nextDouble();
        if (r >= s) {
            return 1;
        } else {
            return 0;
        }

    }


    private double fit(int[] x) throws Exception {
        String s = toBinaryString(x);
        return gwoFitCalculator.remove(s);

    }

    private String toBinaryString(int[] x) {
        String s = "";
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 1) {
                s += (i + 1) + ",";
            }

        }
        return s.substring(0, s.length() - 1);
    }

}
