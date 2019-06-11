package com.Kpi.course.controllers;

import com.Kpi.course.enities.Result;
import com.Kpi.course.services.ClusterWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class AlgorithmController {
    private static final Logger logger = LoggerFactory.getLogger(AlgorithmController.class);

    @Autowired
    private ClusterWork clusterWork;

    /**
     * do a first algorithm with a important criterion
     * @param arg inputed state of clustering graph
     * @return result of current iteration
     */
    @PostMapping(value = "/ClusteringByFirstAlgorithm",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    Result doFirstAlgorithm(@RequestBody Result arg) {
        long startTime = System.nanoTime();
        Result result=arg;
        do{
            result = clusterWork.clusterItarationFirstAlgorithm(result);
        }while(!result.isFinished());

        long endTime   = System.nanoTime();

        long totalTime = endTime - startTime;
        logger.warn("wasted time on first algorithm : "+totalTime);
        return result;
    }

    /**
     * do a first algorithm with a important criterion
     * @param arg inputed state of clustering graph
     * @return result of current iteration
     */
    @PostMapping(value = "/ClusteringBySecondAlgorithm",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    Result doSecondAlgorithm(@RequestBody Result arg) {
        long startTime = System.nanoTime();
        Result result=arg;
        do{
            result = clusterWork.clusterItarationSecondAlgorithm(result);
        }while(!result.isFinished());

        long endTime   = System.nanoTime();

        long totalTime = endTime - startTime;
        logger.warn("wasted time on second  algorithm : "+totalTime);
        return result;
    }

}
