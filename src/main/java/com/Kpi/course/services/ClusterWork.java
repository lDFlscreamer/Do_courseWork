package com.Kpi.course.services;

import com.Kpi.course.enities.Best;
import com.Kpi.course.enities.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * this class conteins a methods to clustering a graph
 * and use some service for calculate some array and matrix
 * like {@link Counter} and {@link Analyser}
 *
 * @version 1.0
 * @see Result
 * @see Counter
 * @see Analyser
 */
@Service
public class ClusterWork {

    private static final Logger logger = LoggerFactory.getLogger(ClusterWork.class);
    @Autowired
    private Counter counter;

    @Autowired
    private Analyser analyser;

    /**
     * combine some cluster ,rebuild matrix and combine cluster in Array
     *
     * @param previous result of previous itaration
     * @return Result of this itaration
     * @see Result
     */
    public Result clusterItarationFirstAlgorithm(Result previous) {
        //gets previous data
        int[][][] previousMatrix = previous.getMatrix();
        if (previousMatrix.length == 1) {//one cluster already
            return previous;
        }
        ArrayList<Integer> important = previous.getImportant();
        //calculatin centers
        if (previousMatrix[0].length == 2) {
            Result current = rebuildResult(previous, 0, 1);
            return current;
        }
        int[][] calculateCenter = counter.calculateCenter(previousMatrix);
        int[] rating = counter.choseCenter(calculateCenter, important); //only for important criterion

        ArrayList<Integer> centers = analyser.getMaxvalueIndex(rating);
        if (centers.isEmpty()) {
            rating = counter.choseCenter(calculateCenter);
            centers = analyser.getMaxvalueIndex(rating);
        }
        int[][] pointToPoint = counter.calculatePointForCenter(previousMatrix, important);
        int[] pair = new int[0];
        try {
            pair = getPair(previousMatrix, pointToPoint, centers);
        } catch (Exception e) {
            logger.error("pair with important criterion not found");
            pointToPoint = counter.calculatePointForCenter(previousMatrix);
            try {
                pair = getPair(previousMatrix, pointToPoint, centers);
            } catch (Exception ex) {
                logger.error("pair not found");
                pair = getPair(0, centers.get(0));
            }
        }
        int firstCluster = pair[0];
        int secondCluster = pair[1];


        //rebuild matrix and clusterName
        Result currentIteration = rebuildResult(previous, firstCluster, secondCluster);
        return currentIteration;
    }

    /**
     * combine some cluster ,rebuild matrix and combine cluster in Array
     *
     * @param previous result of previous itaration
     * @return Result of this itaration
     * @see Result
     */
    public Result clusterItarationSecondAlgorithm(Result previous) {
        //gets previous data
        int[][][] previousMatrix = previous.getMatrix();
        if (previousMatrix.length == 1) {//one cluster already
            return previous;
        }
        ArrayList<Integer> important = previous.getImportant();
        //calculatin centers
        if (previousMatrix[0].length == 2) {
            Result current = rebuildResult(previous, 0, 1);
            return current;
        }
        int[][] calculateCenter = counter.calculateCenter(previousMatrix);
        int[] rating = counter.choseCenter(calculateCenter); //only for important criterion

        ArrayList<Integer> centers = analyser.getMaxvalueIndex(rating);

        int[][] pointToPoint = counter.calculatePointForCenter(previousMatrix);
        int[] pair = new int[0];
        try {
            pair = getPair(previousMatrix, pointToPoint, centers);
        } catch (Exception e) {
            logger.error("pair not found");
            pair = getPair(0, centers.get(0));
        }
        int firstCluster = pair[0];
        int secondCluster = pair[1];


        //rebuild matrix and clusterName
        Result currentIteration = rebuildResult(previous, firstCluster, secondCluster);
        return currentIteration;
    }

    /**
     * rebuild a array of cluster Names
     *
     * @param previos array who describe a each cluster of graph
     * @param first   index  cluster to combine
     * @param second  index of another cluster
     * @return new array after combination
     * @see Result
     */
    public ArrayList<ArrayList<String>> rebuildClusterName(ArrayList<ArrayList<String>> previos, int first, int second) {
        ArrayList<ArrayList<String>> result = new ArrayList<>(previos);
        ArrayList<String> cluster = result.get(second);
        result.remove(second);
        result.get(first).addAll(cluster);
        return result;
    }

    /**
     * @param previousMatrix previous matrix to rebuild
     * @param first          index  cluster to combine
     * @param second         index of another cluster
     * @return a rebuild matrix of a graph
     * @see Result
     */
    public int[][][] rebuildAllMatrix(int[][][] previousMatrix, int first, int second) {
        int[][][] matrix = new int[previousMatrix.length][previousMatrix[0].length - 1][previousMatrix[0][0].length - 1];


        for (int i = 0; i < previousMatrix.length; i++) {
            int[][] result = rebuildPartOfMatrix(previousMatrix[i], first, second);
            matrix[i] = result;
        }
        return matrix;
    }

    /**
     * @param matrix criterion matrix to rebuild
     * @param first  index  cluster to combine
     * @param second index of another cluster
     * @return a rebuild matrix of a criterion
     */
    public int[][] rebuildPartOfMatrix(int[][] matrix, int first, int second) {
        int[][] resulted = new int[matrix.length - 1][matrix.length - 1];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = i; j < matrix[0].length; j++) {
                if (i == second || j == second) {//this is removed cluster
                    continue;
                }
                int indexJ = j > second ? j - 1 : j;
                int indexI = i > second ? i - 1 : i;
                if (i == first) {
                    int sum = matrix[first][j] + matrix[second][j];
                    resulted[indexI][indexJ] = sum;
                    resulted[indexJ][indexI] = sum;
                }//set inner number of cluster
                else {
                    if (j == first) {
                        int sum = matrix[i][first] + matrix[i][second];
                        resulted[indexI][indexJ] = sum;
                        resulted[indexJ][indexI] = sum;
                    }
                    resulted[indexI][indexJ] = matrix[i][j];
                    resulted[indexJ][indexI] = matrix[i][j];
                }
            }
        }
        return resulted;
    }

    /**
     * get pair to combine firstly choosing a
     *
     * @param matrix                matrix of graph ,used to understand if center is cluster
     * @param pointToPointImportant matrix of connection(only important ) between vertex
     * @param centers               center to chose best pair
     * @return pair to combine
     * @throws Exception if maxValue is 0 (no best conection)
     */
    public int[] getPair(int[][][] matrix, int[][] pointToPointImportant, ArrayList<Integer> centers) throws Exception {
        int[] pair = new int[pointToPointImportant.length];
        int maxValue = 0; //max value of all graph
        int indexMaxValue = centers.get(0);
        for (int i :
                centers) {
            int[] currentLine = pointToPointImportant[i];
            //value of best point
            int bestPartner = 0;
            int bestPartnerValue = 0;
            //value of best cluster
            int bestCluster = 0;
            int bestClusterValue = 0;
            //anylyse Current line
            for (int j = 0; j < pointToPointImportant.length; j++) {
                if (!analyser.isCluster(matrix, j)) {
                    if (bestPartnerValue < currentLine[j]) {
                        bestPartner = j;
                        bestPartnerValue = currentLine[j];
                    }
                } else {
                    if (bestClusterValue < currentLine[j]) {
                        bestCluster = j;
                        bestClusterValue = currentLine[j];
                    }
                }
            }
            //diference between two best value
            int diference = bestClusterValue - bestPartnerValue;

            if (bestClusterValue > bestPartnerValue && diference > 2) {// if cluster better than poin more than 2
                pair[i] = bestCluster;
                if (maxValue < bestClusterValue) {//set best of center
                    indexMaxValue = i;
                    maxValue = bestClusterValue;
                }

            } else {
                if (maxValue < bestPartnerValue) {
                    indexMaxValue = i;
                    maxValue = bestPartnerValue;
                }
                pair[i] = bestPartner;
            }

            logger.trace("\t\t\tpoint " + i);
            logger.trace("bestCuster " + bestCluster);
            logger.trace("bestValueCuster " + bestClusterValue);
            logger.trace("bestPartner " + bestPartner);
            logger.trace("bestValuePartner " + bestPartnerValue);

        }
        if (maxValue == 0) {
            throw new Exception("empty");
        }
        logger.trace("cluster with index " + indexMaxValue + " have a " + maxValue);
        logger.trace("cluster with index " + indexMaxValue + " pair with " + pair[indexMaxValue]);
        return getPair(indexMaxValue, pair[indexMaxValue]);
    }


    /**
     * set in ascending order
     *
     * @param first  first of pair
     * @param second second from pair
     * @return return pair in ascending order
     */
    public int[] getPair(int first, int second) {
        int[] pair = new int[2];
        if (first > second) {
            pair[0] = second;
            pair[1] = first;
        } else {
            pair[1] = second;
            pair[0] = first;

        }
        return pair;
    }

    /**
     * combine in cluster  obgect with first and second index
     *
     * @param previous previous iteration
     * @param first    first index
     * @param second   second index
     * @return result of current iteration
     */
    public Result rebuildResult(Result previous, int first, int second) {
        ArrayList<ArrayList<String>> previousClusters = previous.getClusters();
        int[][][] previousMatrix = previous.getMatrix();

        int[] coefficient = previous.getCoefficient();
        ArrayList<Integer> important = previous.getImportant();

        int[][][] newMatrix = rebuildAllMatrix(previousMatrix, first, second);
        logger.trace("combine " + first + "+" + second);


        ArrayList<ArrayList<String>> newCluster = rebuildClusterName(previousClusters, first, second);
        //calculate current function
        long function = counter.calculateFunction(newMatrix, coefficient, important);
        Result currentIteration = new Result();
        currentIteration.setClusters(newCluster);
        currentIteration.setCoefficient(coefficient);
        currentIteration.setMatrix(newMatrix);
        currentIteration.setImportant(important);
        currentIteration.setResultOfClustering(function);
        //if function better than privious
        if (previous.getBest() == null || previous.getBest().getResultOfClustering() < function) {
            currentIteration.setBest(new Best(currentIteration));
        } else {
            currentIteration.setBest(previous.getBest());
        }
        return currentIteration;
    }

}
