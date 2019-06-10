package com.Kpi.course.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalInt;

/**
 * class for analyse info like  choose best value in array
 * used in {@link ClusterWork}
 *
 * @version 1.0
 * @see ClusterWork
 */
@Service
public class Analyser {

    /**
     * get index of max value in array
     *
     * @param array - array where need to get a index of max elements
     * @return array of indexes (if  array have a few elements with max value)
     */
    public ArrayList<Integer> getMaxvalueIndex(int[] array) {
        ArrayList<Integer> indexsOfMax = new ArrayList<>();
        OptionalInt max = Arrays.stream(array).max();
        if (!max.isPresent() || max.getAsInt() == 0) {
            return indexsOfMax;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == max.getAsInt()) {
                indexsOfMax.add(i);
            }
        }

        return indexsOfMax;
    }

    /**
     * indicate if cluster contain only one point
     *
     * @param matrix         matrix of grap
     * @param indexOfCluster index of analysing vertex
     * @return false  if cluster with   indexOfCluster contains one point
     */
    public boolean isCluster(int[][][] matrix, int indexOfCluster) {
        boolean moreThanzero = false;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][indexOfCluster][indexOfCluster] > 0) {
                moreThanzero = true;
                break;
            }
        }
        return moreThanzero;
    }

    /**
     * get a rating  for all conection with center
     * and help to choose best pair to combine
     *
     * @param pointForCenter amount criterion to each cluster calculated by {@link Counter}
     * @param center         center of graph
     * @param matrix         matrix that  explain a graph
     * @return best variable for each center (index of best cluster to combine ,and value)
     * @see Counter
     */
    public int[][] getRaiting(int[][] pointForCenter, ArrayList<Integer> center, int[][][] matrix) {
        int[][] pair = new int[2][pointForCenter.length];

        for (int i = 0; i < pointForCenter[0].length; i++) {
            //calculate only for center
            if (!center.contains(i)) {
                continue;
            }
            int[] current = pointForCenter[i];
            //get index of max value
            ArrayList<Integer> maxvalueIndex = getMaxvalueIndex(current);
            //if max value is dont exist or 0
            if (maxvalueIndex.isEmpty()) {
                pair[0][i] = 0;
                pair[1][i] = 0;
            }
            //set value index
            pair[0][i] = maxvalueIndex.get(0);
            //set value
            OptionalInt max = Arrays.stream(current).max();
            pair[1][i] = isCluster(matrix, i) ? max.getAsInt() - 1 : max.getAsInt();
        }
        return pair;
    }

}
