package com.Kpi.course.controllers;

import com.Kpi.course.CourseWork;
import com.Kpi.course.enities.Result;
import com.Kpi.course.services.ClusterWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * end-pint from some functions
 * used some services like {@link ClusterWork}
 *
 * @version 1.0
 */
@RestController
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);



    /**
     * get a Default task body
     * @param arg not required and dont use
     * @return Default task body
     */
    @PostMapping(value = "/DefaultClustering",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    Result getDefault(@RequestBody(required = false) Result arg) {
        ArrayList<ArrayList<String>> first = new ArrayList<ArrayList<String>>() {{
            add(new ArrayList<String>() {{
                add("A");
            }});
            add(new ArrayList<String>() {{
                add("B");
            }});
            add(new ArrayList<String>() {{
                add("C");
            }});
            add(new ArrayList<String>() {{
                add("D");
            }});
            add(new ArrayList<String>() {{
                add("E");
            }});
            add(new ArrayList<String>() {{
                add("F");
            }});
        }};
        int[][][] matrix = new int[][][]{
                {       {0, 1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 0, 0},
                        {0, 0, 0, 1, 1, 0},
                        {0, 0, 1, 0, 0, 0},
                        {0, 0, 1, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0},
                },
                {       {0, 1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 0, 1},
                        {0, 0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0, 1},
                        {0, 0, 0, 0, 0, 0},
                        {1, 1, 0, 1, 0, 0},
                },
                {       {0, 0, 0, 0, 0, 1},
                        {0, 0, 1, 0, 0, 1},
                        {0, 1, 0, 0, 0, 0},
                        {0, 0, 0, 0, 1, 1},
                        {0, 0, 0, 1, 0, 0},
                        {1, 1, 0, 1, 0, 0},
                },
                {       {0, 0, 0, 0, 0, 0},
                        {0, 0, 1, 0, 0, 0},
                        {0, 1, 0, 1, 0, 0},
                        {0, 0, 1, 0, 1, 0},
                        {0, 0, 0, 1, 0, 0},
                        {0, 0, 0, 0, 0, 0},
                },
                {       {0, 0, 0, 0, 0, 0},
                        {0, 0, 1, 0, 0, 0},
                        {0, 1, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0, 0},
                        {0, 0, 0, 0, 0, 1},
                        {0, 0, 0, 0, 1, 0},
                }
        };
        int[] creterion = new int[]{
                1, 1, 0, 1, 0
        };
        ArrayList<Integer> important = new ArrayList<Integer>() {{
            add(0);
            add(1);
            add(3);
        }};

        Result result = new Result();
        result.setCoefficient(creterion);
        result.setImportant(important);
        result.setMatrix(matrix);
        result.setClusters(first);
        return result;
    }

}
