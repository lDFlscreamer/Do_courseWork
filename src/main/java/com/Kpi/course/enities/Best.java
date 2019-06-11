package com.Kpi.course.enities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;

/**
 * class which contains best iteration
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Best {
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
    private long resultOfClustering;

    public Best() {
    }

    public Best(Result result) {
        this();
        this.setClusters(result.getClusters());
        this.setMatrix(result.getMatrix());
        this.setResultOfClustering(result.getResultOfClustering());
    }
}
