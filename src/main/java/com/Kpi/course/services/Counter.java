package com.Kpi.course.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * service for counting and calculate some info about graph
 * and cluster
 * used in {@link ClusterWork}
 *
 * @version 1.0
 * @see ClusterWork
 */
@Service
public class Counter {
    private static final Logger logger = LoggerFactory.getLogger(Counter.class);

    @Autowired
    private Analyser analyser;

    /**
     * calculate amount of each point
     * for all criterion
     *
     * @param matrix -matrix of a graph
     *               first coordinate -number of criterion
     *               second coordinate - from point
     *               thirt coordinate - to point
     * @return matrix where for each point calculate amount of connection via criterion
     * first coordinate - point
     * second coordinate - criterion
     */
    public int[][] calculateCenter(int[][][] matrix) {
        int[][] result = new int[matrix[0].length][matrix.length];

        
        /*
        set amount of conection via criterion 
         */
        for (int k = 0; k < matrix.length; k++) {
            for (int i = 0; i < matrix[0].length; i++) {
                int sum = 0;
                for (int j = 0; j < matrix[0].length; j++) {
                    if (i != j && matrix[k][i][j] != 0) {
                        sum++;
                    }
                }
                result[i][k] = sum;
            }
        }
        return result;
    }

    /**
     * calculate without coefficient
     *
     * @param matrixDistances matrix where for each point calculate amount of connection via criterion
     *                        (result of calculateCenter)
     *                        first coordinate - point
     *                        second coordinate - criterion
     * @return array of rating each point
     * first coordinate - rating of point
     * second coordinate - isCluster
     */
    public int[] choseCenter(int[][] matrixDistances) {
        int[] coef = new int[matrixDistances[0].length];
        Arrays.fill(coef, 1);

        return choseCenter(matrixDistances, coef);
    }

    /**
     * calculate with coefficient
     *
     * @param matrixDistances matrix where for each point calculate amount of connection via criterion
     *                        (result of calculateCenter)
     *                        first coordinate - point
     *                        second coordinate - criterion
     * @param coefficient     - array  coeficient of criterian
     * @return array of rating each point
     * first coordinate - rating of point
     * second coordinate - isCluster
     */
    public int[] choseCenter(int[][] matrixDistances, int[] coefficient) {
        int[] result = new int[matrixDistances.length];

        for (int i = 0; i < matrixDistances.length; i++) {
            int rating = 0;
            for (int j = 0; j < matrixDistances[0].length; j++) {
                rating += matrixDistances[i][j] * coefficient[j];
            }
            result[i] = rating;
        }

        return result;
    }

    /**
     * calculate without coefficient
     * only for a important point
     *
     * @param important       important criterion
     * @param matrixDistances matrix where for each point calculate amount of connection via criterion
     *                        (result of calculateCenter)
     *                        first coordinate - point
     *                        second coordinate - criterion
     * @return array of rating each point
     * first coordinate - rating of point
     * second coordinate - isCluster
     */
    public int[] choseCenter(int[][] matrixDistances, ArrayList<Integer> important) {
        int[] result = new int[matrixDistances.length];

        for (int i = 0; i < matrixDistances.length; i++) {
            int rating = 0;
            for (int j = 0; j < matrixDistances[0].length; j++) {
                if (important.contains(j)) {
                    rating += matrixDistances[i][j];
                }
            }
//
            result[i] = rating;
        }
        return result;
    }

    /**
     * calculate for each center amount criterion(important) to each center
     *
     * @param matrix     -matrix which explain a graph
     * @param important- important criterion
     * @return matrix with amount criterion(important) for each cluster to each cluster
     */
    public int[][] calculatePointForCenter(int[][][] matrix, ArrayList<Integer> important) {
        int[][] result = new int[matrix[0].length][matrix[0].length];

        logger.trace("\t\tcalculatePointForCenter");
        for (int i = 0; i < matrix[0][0].length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                int amount = 0;
                for (int k = 0; k < matrix.length; k++) {
                    if (!important.contains(k)) { //not important
                        continue;
                    }
                    if (matrix[k][i][j] != 0) {//if connection is exist
                        amount++;
                    }
                }
                result[i][j] = amount;//set amount of conection
                logger.trace("result[" + i + "]" + "[" + j + "]" + "=" + result[i][j]);
            }
        }
        return result;
    }

    /**
     * calculate for each cluster amount criterion to each cluster
     *
     * @param matrix -matrix which explain a graph
     * @return matrix with amount all criterion for each cluster to each cluster
     */
    public int[][] calculatePointForCenter(int[][][] matrix) {
        int[][] result = new int[matrix[0].length][matrix[0].length];

        for (int i = 0; i < matrix[0][0].length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                int amount = 0;
                for (int k = 0; k < matrix.length; k++) {
                    if (matrix[k][i][j] != 0) {//if connection is exist
                        amount++;
                    }
                }
                result[i][j] = amount;//set amount of conection
            }
        }
        return result;
    }

    /**
     * calculate a function for a graph
     *
     * @param important   important criterion
     * @param matrix      matrix of graph
     * @param coefficient coefficient  for a criterion
     * @return value of function
     */
    public float calculateFunction(int[][][] matrix, float[] coefficient, ArrayList<Integer> important) {
        float sum = 0;
        int[][] conection = calculateCenter(matrix);


        for (int i = 0; i < matrix[0].length; i++) {
            int clusterValue = 0;
            float sumCoefficient = 0;
            int sumInerValue = 0;
            for (int j = 0; j < matrix.length; j++) {
                if (!important.contains(j)) {
                    continue;
                }//if this is not important criterion
                sumInerValue += matrix[j][i][i];
                sumCoefficient += conection[i][j] * coefficient[j];
                logger.trace("clusterValue = " + clusterValue + "=" + conection[i][j] + "*" + "(" + matrix[j][i][i] + "*" + coefficient[j] + ")");
            }
            sum += sumCoefficient * sumInerValue;//add Cluster value
        }
        logger.trace("sum = " + sum);
        return sum;
    }

}
