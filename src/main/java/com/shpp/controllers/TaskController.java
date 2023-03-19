package com.shpp.controllers;

import com.shpp.models.Task;
import com.shpp.models.TaskDTO;
import com.shpp.services.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/tasks", produces = "application/json")
@Api(tags = "Controller API")
@Tag(name = "Controller API", description = "This controller manages the list of current TASKS...")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/")
    @ApiOperation("this method adds new task in db... ")
    @ApiImplicitParam(
            name = "Accept-language", allowEmptyValue = true,
            paramType = "header", dataTypeClass = String.class,
            example = "ukr")
    public ResponseEntity<Object> addTask(@Valid
                                          TaskDTO dto,
                                          BindingResult result) {
        return taskService.addTask(dto, result);
    }

    @PutMapping("/put_status_cancelled/{id}")
    @ApiOperation("this method cancels task by id... ")
    @ApiImplicitParam(
            name = "Accept-language", allowEmptyValue = true,
            paramType = "header", dataTypeClass = String.class,
            example = "ukr")
    public ResponseEntity<Object> cancelTask(@PathVariable Long id) {
        return taskService.cancelTask(id);
    }

    @PutMapping("/put_next_status/{id}")
    @ApiOperation("this method changes task status by id... ")
    @ApiImplicitParam(
            name = "Accept-language", allowEmptyValue = true,
            paramType = "header", dataTypeClass = String.class,
            example = "ukr")
    public ResponseEntity<Object> putNextStatus(@PathVariable Long id) {
        return taskService.putNextStatus(id);
    }

    @PutMapping("/change_task/{id}")
    @ApiOperation("this method changes task by id... ")
    @ApiImplicitParam(
            name = "Accept-language", allowEmptyValue = true,
            paramType = "header", dataTypeClass = String.class,
            example = "ukr")
    public ResponseEntity<Object> changeTask(@PathVariable Long id,
                                             @Valid
                                             TaskDTO dto,
                                             BindingResult result) {
        return taskService.changeTask(id, dto, result);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("this method deletes a task by id... ")
    @ApiImplicitParam(
            name = "Accept-language", allowEmptyValue = true,
            paramType = "header", dataTypeClass = String.class,
            example = "ukr")
    public ResponseEntity<Object> delTask(@PathVariable Long id) {
        return taskService.delTask(id);
    }

    @GetMapping("/")
    @ApiOperation("this method finds all tasks in db... ")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    @ApiOperation("this method finds a task by id... ")
    @ApiImplicitParam(
            name = "Accept-language", allowEmptyValue = true,
            paramType = "header", dataTypeClass = String.class,
            example = "ukr")
    public ResponseEntity<Object> getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }
}
