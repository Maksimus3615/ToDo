package com.shpp.services;

import com.shpp.models.ApiError;
import com.shpp.models.Status;
import com.shpp.models.Task;
import com.shpp.models.TaskDTO;
import com.shpp.repo.TaskRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepo taskRepo;
    private final StatusService statusService;
    private final MessageSource messageSource;

    public TaskService(TaskRepo taskRepo,
                       StatusService statusService,
                       MessageSource messageSource) {
        this.taskRepo = taskRepo;
        this.statusService = statusService;
        this.messageSource = messageSource;
    }

    public List<Task> getAllTasks() {
        log.info("Starts new @GetMapping (get all tasks)...");
        return taskRepo.findAll();
    }

    public ResponseEntity<Object> addTask(TaskDTO dto,
                                          BindingResult result) {
        log.info("Starts new @PostMapping...");
        if (result.hasErrors())
            return getNotFutureDateErrorResponse();
        Task task = new Task()
                .setName(dto.getTaskName())
                .setPlannedTerm(dto.getPlannedTerm())
                .setLastStatusChange(LocalDate.now())
                .setStatus(statusService.getStatusByIndex(0));
        task.setStatusTranslated(getTranslatedStatus(task.getStatus().getIndex()));
        taskRepo.save(task);
        log.info("New task is saved...");
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    public ResponseEntity<Object> cancelTask(Long id) {
        log.info("Starts new @PutMapping (cancel task)...");
        Task task = taskRepo.findTaskById(id);
        if (task == null)
            return getNotFoundErrorResponse(id);
        task.setStatus(Status.CANCELLED);
        task.setStatusTranslated(getTranslatedStatus(task.getStatus().getIndex()));
        taskRepo.save(task);
        log.info("The task was cancelled...");
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    public ResponseEntity<Object> putNextStatus(Long id) {
        log.info("Starts new @PutMapping (next status)...");
        Task task = taskRepo.findTaskById(id);
        if (task == null)
            return getNotFoundErrorResponse(id);
        Status status = task.getStatus();
        int index = status.getIndex();
        if (!status.equals(Status.DONE) && !status.equals(Status.CANCELLED)) {
            task.setStatus(statusService.getStatusByIndex(index + 1));
            task.setStatusTranslated(getTranslatedStatus(task.getStatus().getIndex()));
            taskRepo.save(task);
            log.info("The task status was changed...");
            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        } else
            return getChangeStatusErrorResponse(id);
    }

    public ResponseEntity<Object> changeTask(Long id,
                                             TaskDTO dto,
                                             BindingResult result) {
        log.info("Starts new @PutMapping...");
        if (result.hasErrors())
            return getNotFutureDateErrorResponse();
        Task task = taskRepo.findTaskById(id);
        if (task == null)
            return getNotFoundErrorResponse(id);
        task.setPlannedTerm(dto.getPlannedTerm()).setName(dto.getTaskName());
        task.setStatusTranslated(getTranslatedStatus(task.getStatus().getIndex()));
        taskRepo.save(task);
        log.info("The task with id={} was changed...", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    public ResponseEntity<Object> delTask(Long id) {
        log.info("Starts new @DeleteMapping...");
        Task task = taskRepo.findTaskById(id);
        if (task == null)
            return getNotFoundErrorResponse(id);
        taskRepo.delete(task);
        log.info("The task with id={} was deleted...", id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> getTask(Long id) {
        log.info("Starts new @GetMapping (get one task by id)...");
        Task task = taskRepo.findTaskById(id);
        if (task == null)
            return getNotFoundErrorResponse(id);
        log.info("The task was found: {}", task);
        return ResponseEntity.ok(task);
    }

    private ResponseEntity<Object> getNotFoundErrorResponse(long id) {
        log.info("The task with id={} was not found...", id);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(getTranslatedError("not_found.error")));
    }

    private ResponseEntity<Object> getChangeStatusErrorResponse(long id) {
        log.info("The task with id={} has final status...", id);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(getTranslatedError("change_status.error")));
    }

    private ResponseEntity<Object> getNotFutureDateErrorResponse() {
        log.info("The deadline of task must contain a date that has not yet arrived...");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(getTranslatedError("not_future_term.error")));
    }

    public String getTranslatedError(String errorName) {
        return messageSource.getMessage(
                errorName,
                null, LocaleContextHolder.getLocale());
    }

    public String getTranslatedStatus(int index) {
        return messageSource.getMessage(
                "status." + index,
                null, LocaleContextHolder.getLocale());
        //null, new Locale("ukr"));
    }
}