package com.Kpi.course.enities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    /**
     * arrays of cluster names
     */
    private ArrayList<ArrayList<String>> clusters;
    /**
     * matrix that explain a graph structure
     */
    private int[][][] matrix;
    /**
     * the result of function
     */
    private float resultOfClustering;
    /**
     * array  coefficients of  important
     */
    private float[] coefficient;
    /**
     * array of important criterion
     */
    private ArrayList<Integer> important;
    /**
     * best result of clustering
     */
    private Best best;

    /**
     * indicate the finish of algorithm
     */
    private boolean isFinished=false;

}
