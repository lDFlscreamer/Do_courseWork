package com.Kpi.course.controllers;

import com.Kpi.course.enities.Result;
import com.Kpi.course.services.ClusterWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * end-pint to clustering
 * used some services like {@link ClusterWork}
 *
 * @version 1.0
 * @see ClusterWork
 */
@RestController
public class MainController {

    @Autowired
    private ClusterWork clusterWork;


    /**
     *
     * @param arg inputed state of clustering graph
     * @return result of current iteration
     */
    @PostMapping(value = "/clustering",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    Result doAglomeration(@RequestBody Result arg) {
        Result result = clusterWork.clusterItarationFirstAlgorithm(arg);
        return result;
    }

}
