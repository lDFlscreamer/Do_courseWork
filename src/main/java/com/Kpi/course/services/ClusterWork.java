package com.Kpi.course.services;

import com.Kpi.course.enities.Best;
import com.Kpi.course.enities.Result;
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
        ArrayList<ArrayList<String>> previousClusters = previous.getClusters();
        int[][][] previousMatrix = previous.getMatrix();
        if (previousMatrix.length == 1) {//one cluster already
            return previous;
        }
        int[] coefficient = previous.getCoefficient();
        ArrayList<Integer> important = previous.getImportant();
        //calculatin centers
        int[][] calculateCenter = counter.calculateCenter(previousMatrix);
        int[] rating = counter.choseCenter(calculateCenter, coefficient, important);
        ArrayList<Integer> center = analyser.getMaxvalueIndex(rating);
        //if with important criterion center dont exist
        if (center.isEmpty()) {
            rating = counter.choseCenter(calculateCenter, coefficient);
            center = analyser.getMaxvalueIndex(rating);
        }
        //get pair of cluster to combine
        int[] pair = getPair(center, previousMatrix, important);
        int firstCluster = pair[0];
        int secondCluster = pair[1];
        //rebuild matrix and clusterName
        int[][][] newMatrix = rebuildAllMatrix(previousMatrix, firstCluster, secondCluster);
        ArrayList<ArrayList<String>> newCluster = rebuildClusterName(previousClusters, firstCluster, secondCluster);
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
                int indexI = j > second ? j - 1 : j;
                int indexJ = i > second ? i - 1 : i;
                if (i == first || j == second || j == first) {

                    int sum = matrix[i][j] + matrix[second][j];
                    resulted[indexJ][indexI] = sum;
                    resulted[indexI][indexJ] = sum;
                }//set inner number of cluster
                else {
                    resulted[indexJ][indexI] = matrix[i][j];
                    resulted[indexI][indexJ] = matrix[i][j];
                }
            }
        }
        return resulted;
    }

    /**
     * find a pair to combine
     *
     * @param center    center wich find in
     * @param matrix
     * @param important
     * @return pair to combine
     */
    public int[] getPair(ArrayList<Integer> center, int[][][] matrix, ArrayList<Integer> important) {
        //max[0]=value of max element
        //max[1]= index of center
        //rating of cluster for each center
        int[][] pointForCenter = counter.calculatePointForCenter(matrix, important);
        int[][] raiting = analyser.getRaiting(pointForCenter, center, matrix);
        ArrayList<Integer> indexSecond = analyser.getMaxvalueIndex(raiting[1]);
        //if conection with important criterion
        if (indexSecond.isEmpty()) {
            pointForCenter = counter.calculatePointForCenter(matrix);
            raiting = analyser.getRaiting(pointForCenter, center, matrix);
            indexSecond = analyser.getMaxvalueIndex(raiting[1]);
        }
        int first = indexSecond.get(0).intValue();
        int second = raiting[first][0];

        return getPair(first, second);
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


}
