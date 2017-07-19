import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sina on 7/1/2017.
 */
public class LocalSearchOperation {

    Instances data;
    double arrayData[][];
    int[] S;
    int[] D;
    double[][] C;

    double miu;
    int delta, zi;
    double means[];
    static double[][] Cor;

    double alpha;
    static int numAttributes;


    public LocalSearchOperation(Instances data, double miu, int numSelectedFeature) {
        this.data = data;
        this.miu = miu;
        delta = rond(miu * numSelectedFeature);
        zi = rond((1 - miu) * numSelectedFeature);

        numAttributes = data.numAttributes() - 1;
        convertToArray();
        means = new double[numAttributes];
        calcMean();

    }

    private int rond(double v) {

        int t1 = (int) v;
        double t2 = v - t1;
        if (t2 >= 0.5) {
            return t1 + 1;
        } else {
            return t1;
        }
    }

    public int[] toIntArray(int[] x) {
        int numOfOnes = numOfOnes(x);
        int temp[] = new int[numOfOnes];
        int counter = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 1) {
                temp[counter] = i;
                counter++;
            }

        }
        return temp;
    }

    private int numOfOnes(int[] x) {
        int counter = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 1) {
                counter++;
            }

        }
        return counter;
    }


    public int[] lso(int[] x) {

        int[] X = toIntArray(x);
        ArrayList<Integer> Xd = new ArrayList<Integer>();
        for (int i = 0; i < D.length; i++) {
            for (int j = 0; j < X.length; j++) {
                if (D[i] == X[j]) {
                    Xd.add(D[i]);
                }
            }
        }

        ArrayList<Integer> Xs = new ArrayList<Integer>();
        for (int i = 0; i < S.length; i++) {
            for (int j = 0; j < X.length; j++) {
                if (S[i] == X[j]) {
                    Xs.add(S[i]);
                }
            }
        }

        if (delta > Xd.size()) {
            for (int i = 0, j = 0; j < delta - Xd.size() & i < D.length; i++) {
                //addToXd
                Boolean exist = false;
                for (int k = 0; k < Xd.size(); k++) {
                    if (D[i] == Xd.get(k)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    Xd.add(D[i]);
                    j++;
                }

            }
        }

        if (delta < Xd.size()) {
            for (int i = 0, j = Xd.size() - 1; j > 0 & i < Xd.size() - delta; j--, i++) {
                //delFromXd
                Xd.remove(j);
            }
        }

        if (zi > Xs.size()) {
            for (int i = 0, j = 0; j < zi - Xs.size() & i < S.length; i++) {
                //addToXs
                Boolean exist = false;
                for (int k = 0; k < Xs.size(); k++) {
                    if (S[i] == Xs.get(k)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    Xs.add(S[i]);
                    j++;
                }

            }
        }

        if (zi < Xs.size()) {
            for (int i = 0, j = Xs.size() - 1; j > 0 & i < Xs.size() - zi; j--, i++) {
                //delFromXs
                Xs.remove(j);
            }
        }


        for (int i = 0; i < x.length; i++) {
            x[i] = 0;
        }
        for (int i = 0; i < Xd.size(); i++) {
            x[Xd.get(i)] = 1;
        }
        for (int i = 0; i < Xs.size(); i++) {
            x[Xs.get(i)] = 1;
        }

        return x;

    }

    public void computeCorrelation() {
        computeC();
        Cor = new double[numAttributes][2];
        for (int i = 0; i < numAttributes; i++) {
            double sum = 0;
            for (int j = 0; j < numAttributes; j++) {
                sum += Math.abs(C[i][j]);
            }
            Cor[i][0] = sum / (numAttributes);
            Cor[i][1] = i;
        }


        Arrays.sort(Cor, (double[] a, double[] b) -> Double.compare(a[0], b[0]));

        D = new int[numAttributes / 2];
        S = new int[numAttributes / 2];
        for (int i = 0; i < numAttributes / 2; i++) {
            D[i] = (int) Cor[i][1];
        }
        int k = 0;
        for (int i = (numAttributes + 1) / 2; i < numAttributes; i++) {
            S[k] = (int) (Cor[i][1]);
            k++;
        }

        for (int i = 0; i < S.length; i++) {
            System.out.print(S[i] + ",");
        }
        System.out.println();
        System.out.println("d");
        for (int i = 0; i < D.length; i++) {
            System.out.print(D[i] + ",");
        }
    }


    private void computeC() {
        C = new double[numAttributes][numAttributes];
        for (int i = 0; i < numAttributes; i++) {
            for (int j = 0; j < numAttributes; j++) {
                double sum = 0;
                double xi = 0;
                double xj = 0;

                for (int k = 0; k < data.numInstances(); k++) {
                    xi = (arrayData[k][i] - means[i]);
                    xj = (arrayData[k][j] - means[j]);
                    sum += xi * xj;
                }
                xj = 0;
                xi = 0;
                for (int k = 0; k < data.numInstances(); k++) {
                    xi += Math.pow((arrayData[k][i] - means[i]), 2);
                    xj += Math.pow((arrayData[k][j] - means[j]), 2);
                }
                double denominator = Math.sqrt(xi) * Math.sqrt(xj);
                C[i][j] = sum / denominator;

            }

        }

    }


    private void calcMean() {
        double sum = 0;
        for (int i = 0; i < numAttributes; i++) {
            for (int j = 0; j < data.numInstances(); j++) {
                sum += arrayData[j][i];
            }
            sum /= data.numInstances();
            means[i] = sum;
        }


    }

    private void convertToArray() {
        arrayData = new double[data.numInstances()][numAttributes];
        for (int i = 0; i < data.numInstances(); i++) {
            for (int j = 0; j < numAttributes; j++) {
                arrayData[i][j] = data.instance(i).value(j);
            }
        }
    }

    public int[] getS() {
        return S;
    }

    public int[] getD() {
        return D;
    }

    public double getAlpha() {
        int sum = 0;
        for (int i = 0; i < C.length; i++) {

        }
        return alpha;
    }

    public static double getSumOfCor() {
        double sum = 0;
        for (int i = 0; i < numAttributes; i++) {

            sum += Cor[i][0];
        }
        return 1 / sum;
    }
}
