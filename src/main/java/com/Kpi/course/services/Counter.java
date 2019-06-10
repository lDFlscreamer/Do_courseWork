package com.Kpi.course.services;

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
     * @param matrixDistances matrix where for each point calculate amount of connection via criterion
     *                        (result of calculateCenter)
     *                        first coordinate - point
     *                        second coordinate - criterion
     * @return array of rating each point
     * first coordinate - rating of point
     * second coordinate - isCluster
     */
    public int[] choseCenter(int[][] matrixDistances, int[] coefficient, ArrayList<Integer> important) {
        int[] result = new int[matrixDistances.length];

        for (int i = 0; i < matrixDistances.length; i++) {
            int rating = 0;
            for (int j = 0; j < matrixDistances[0].length; j++) {
                if (important.contains(j)) {
                    rating += matrixDistances[i][j] * coefficient[j];
                }
            }
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


        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix[0][0].length; j++) {
                if (i == j) { //is same cluster
                    continue;
                }
                int amount = 0;
                for (int k = 0; k < matrix.length; k++) {
                    if (!important.contains(k)) { //not important criterion
                        continue;
                    }
                    if (matrix[k][i][j] != 0) { //increment a amount of criterion
                        amount++;
                    }
                }
                result[i][j] = amount;
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


        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix[0][0].length; j++) {
                if (i == j) { //is same cluster
                    result[i][j] = 0;
                    continue;
                }
                int amount = 0;
                for (int k = 0; k < matrix.length; k++) {
                    if (matrix[k][i][j] != 0) { //increment a amount of criterion
                        amount++;
                    }
                }
                result[i][j] = amount;
            }
        }

        return result;
    }

    /**
     * calculate a function for a graph
     *
     * @param matrix      matrix of graph
     * @param coefficient coefficient  for a criterion
     * @return value of function
     */
    public long calculateFunction(int[][][] matrix, int[] coefficient, ArrayList<Integer> important) {
        int sum = 0;
        int[][] conection = calculateCenter(matrix);
        for (int i = 0; i < matrix[0].length; i++) {
            int clusterValue = 0;
            for (int j = 0; j < matrix.length; j++) {
                if (!important.contains(j)) {
                    continue;
                }//if this is not important criterion
                clusterValue += conection[i][j] * (matrix[j][i][i] * coefficient[j]);//calculate value for each cluster
            }
            sum += clusterValue;//add Cluster value
        }

        return sum;
    }

}
