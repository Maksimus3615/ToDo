package com.shpp.services;

import com.shpp.models.ApiError;
import com.shpp.models.Status;
import com.shpp.models.Task;
import com.shpp.models.TaskDTO;
import com.shpp.repo.TaskRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceSpringTest {
    TaskDTO taskDTO = new TaskDTO(
            LocalDate.of(3000, 12, 12),
            "task_dto");
    TaskDTO newDTO = new TaskDTO(
            LocalDate.of(5999, 1, 1),
            "new_name");
    Task task = new Task()
            .setName(taskDTO.getTaskName())
            .setPlannedTerm(taskDTO.getPlannedTerm())
            .setLastStatusChange(LocalDate.now())
            .setStatus(Status.PLANNED)
            .setStatusTranslated("ЗАПЛАНОВАНО");
    Task task1 = new Task()
            .setName(task.getName())
            .setPlannedTerm(task.getPlannedTerm())
            .setLastStatusChange(LocalDate.now())
            .setStatus(Status.CANCELLED)
            .setStatusTranslated("СКАСОВАНО");
    Task task2 = new Task()
            .setName(task.getName())
            .setPlannedTerm(task.getPlannedTerm())
            .setLastStatusChange(LocalDate.now())
            .setStatus(Status.WORK_IN_PROGRESS)
            .setStatusTranslated("В_РОБОТІ");
    Task newTask2 = new Task()
            .setName(newDTO.getTaskName())
            .setPlannedTerm(newDTO.getPlannedTerm())
            .setLastStatusChange(LocalDate.now())
            .setStatus(task2.getStatus())
            .setStatusTranslated(task2.getStatusTranslated());
    Task task3 = new Task()
            .setName("task3")
            .setPlannedTerm(LocalDate.of(2099, 10, 10))
            .setLastStatusChange(LocalDate.now())
            .setStatus(Status.DONE)
            .setStatusTranslated("ВИКОНАНО");

    Long id = 99L;
    @Mock
    TaskRepo taskRepo;
    @Mock
    MessageSource messageSource;
    @Mock
    BindingResult result;
    @Mock
    StatusService statusService;
    @InjectMocks
    TaskService service;
    ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("ukr"));
    private final Logger log = LoggerFactory.getLogger(TaskServiceSpringTest.class);

    @Test
    void testGetAllTasks() {
        List<Task> list = new ArrayList<>();
        list.add(task1);
        list.add(task2);
        list.add(task3);
        when(taskRepo.findAll()).thenReturn(list);
        assertEquals(service.getAllTasks(), list);
        log.info("*****************************************************************************");
    }

    @Test
    void testAddTask() {
        int index = 0;
        when(result.hasErrors()).thenReturn(false);
        when(statusService.getStatusByIndex(index)).thenReturn(getStatusByIndex(index));

        i18nStatusByIndex(index);

        ResponseEntity<Object> o1 = service.addTask(taskDTO, result);
        ResponseEntity<Object> o2 = ResponseEntity
                .status(HttpStatus.CREATED)
                .body(task);

        assertEquals(o1.toString(), o2.toString());
        log.info("*****************************************************************************");
    }

    @Test
    void testAddTaskHasErrors() {
        when(result.hasErrors()).thenReturn(true);

        String keyError = "not_future_term.error";
        String translateError = bundle.getString(keyError);
        when(service.getTranslatedError(keyError)).thenReturn(translateError);

        ResponseEntity<Object> o1 = service.addTask(taskDTO, result);
        ResponseEntity<Object> o2 = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(translateError));

        assertEquals(o1.toString(), o2.toString());
        log.info("*****************************************************************************");
    }

    private void i18nErrorByKey(String keyError) {

    }

    @Test
    void testCancelTask() {
        int index = Status.CANCELLED.getIndex();
        when(taskRepo.findTaskById(anyLong())).thenReturn(task);

        i18nStatusByIndex(index);

        ResponseEntity<Object> o1 = service.cancelTask(id);
        ResponseEntity<Object> o2 = ResponseEntity
                .status(HttpStatus.CREATED)
                .body(task1);

        assertEquals(o1.toString(), o2.toString());
        log.info("*****************************************************************************");
    }

    @Test
    void testCancelTaskHasError() {
        when(taskRepo.findTaskById(anyLong())).thenReturn(null);

        String keyError = "not_found.error";
        String translateError = bundle.getString(keyError);
        when(service.getTranslatedError(keyError)).thenReturn(translateError);

        ResponseEntity<Object> o1 = service.cancelTask(id);
        ResponseEntity<Object> o2 = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(translateError));

        assertEquals(o1.toString(), o2.toString());
        log.info("*****************************************************************************");
    }

    @Test
    void testPutNextStatus() {
        int index = task.getStatus().getIndex() + 1;

        when(taskRepo.findTaskById(anyLong())).thenReturn(task);
        when(statusService.getStatusByIndex(index)).thenReturn(getStatusByIndex(index));

        i18nStatusByIndex(index);

        ResponseEntity<Object> o1 = service.putNextStatus(id);
        ResponseEntity<Object> o2 = ResponseEntity
                .status(HttpStatus.CREATED)
                .body(task2);

        assertEquals(o1.toString(), o2.toString());
        log.info("*****************************************************************************");
    }

    @Test
    void testPutNextStatusHasError() {
        when(taskRepo.findTaskById(anyLong())).thenReturn(task3);

        String keyError = "change_status.error";
        String translateError = bundle.getString(keyError);
        when(service.getTranslatedError(keyError)).thenReturn(translateError);

        ResponseEntity<Object> o1 = service.putNextStatus(id);
        ResponseEntity<Object> o2 = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(translateError));

        assertEquals(o1.toString(), o2.toString());
        log.info("*****************************************************************************");
    }

    @Test
    void testChangeTask() {
        int index = task2.getStatus().getIndex();

        when(result.hasErrors()).thenReturn(false);
        when(taskRepo.findTaskById(anyLong())).thenReturn(task2);

        i18nStatusByIndex(index);

        ResponseEntity<Object> o1 = service.changeTask(id, newDTO, result);
        ResponseEntity<Object> o2 = ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newTask2);

        assertEquals(o1.toString(), o2.toString());
        log.info("*****************************************************************************");
    }

    private void i18nStatusByIndex(int index) {
        String keyStatus = "status." + index;
        String translateStatus = bundle.getString(keyStatus);
        when(service.getTranslatedStatus(index)).thenReturn(translateStatus);
    }

    @Test
    void testDelTask() {
        when(taskRepo.findTaskById(anyLong())).thenReturn(task1);

        ResponseEntity<Object> o1 = service.delTask(id);
        ResponseEntity<Object> o2 = ResponseEntity.ok().build();

        assertEquals(o1.toString(), o2.toString());
        log.info("*****************************************************************************");
    }

    @Test
    void testGetTask() {
        when(taskRepo.findTaskById(anyLong())).thenReturn(task);

        ResponseEntity<Object> o1 = service.getTask(id);
        ResponseEntity<Object> o2 = ResponseEntity.ok(task);

        assertEquals(o1.toString(), o2.toString());
        log.info("*****************************************************************************");
    }

    private Status getStatusByIndex(int index) {
        StatusService ss = new StatusService();
        return ss.getStatusByIndex(index);
    }
}