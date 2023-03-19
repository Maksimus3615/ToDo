package com.shpp.services;

import com.shpp.models.Status;
import com.shpp.models.Task;
import com.shpp.repo.TaskRepo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class ApiSpringTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TaskRepo repo;
    Task task1 = new Task()
            .setName("task1")
            .setPlannedTerm(LocalDate.of(2099, 8, 8))
            .setLastStatusChange(LocalDate.of(2023, 1, 1))
            .setStatus(Status.CANCELLED)
            .setStatusTranslated("СКАСОВАНО");
    Task task2 = new Task()
            .setName("task2")
            .setPlannedTerm(LocalDate.of(3099, 9, 9))
            .setLastStatusChange(LocalDate.of(2023, 1, 1))
            .setStatus(Status.WORK_IN_PROGRESS)
            .setStatusTranslated("В_РОБОТІ");
    Task task3 = new Task()
            .setName("task3")
            .setPlannedTerm(LocalDate.of(4099, 10, 10))
            .setLastStatusChange(LocalDate.of(2023, 1, 1))
            .setStatus(Status.DONE)
            .setStatusTranslated("ВИКОНАНО");
    private final Logger log = LoggerFactory.getLogger(ApiSpringTest.class);

    @WithUserDetails("user")
    @Test
    void iTestGetAllTasks() {
        repo.save(task1);
        repo.save(task2);
        repo.save(task3);
        RequestBuilder requestBuilder = get("/tasks/");
        try {
            this.mockMvc.perform(requestBuilder).andExpectAll(
                    status().isOk(),
                    content().contentType(MediaType.APPLICATION_JSON),
                    content().json("[" +
                            "{" +
                            "\"id\":1," +
                            "\"task_name\":\"task1\"," +
                            "\"planned_term\":\"2099-08-08\"," +
                            "\"last_status_change\":\"2023-01-01\"," +
                            "\"status\":\"CANCELLED\"," +
                            "\"status_translated\":\"СКАСОВАНО\"" +
                            "}," +
                            "{" +
                            "\"id\":2," +
                            "\"task_name\":\"task2\"," +
                            "\"planned_term\":\"3099-09-09\"," +
                            "\"last_status_change\":\"2023-01-01\"," +
                            "\"status\":\"WORK_IN_PROGRESS\"," +
                            "\"status_translated\":\"В_РОБОТІ\"" +
                            "}," +
                            "{" +
                            "\"id\":3," +
                            "\"task_name\":\"task3\"," +
                            "\"planned_term\":\"4099-10-10\"," +
                            "\"last_status_change\":\"2023-01-01\"," +
                            "\"status\":\"DONE\"," +
                            "\"status_translated\":\"ВИКОНАНО\"" +
                            "}]"));
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Test
    @WithUserDetails("admin")
    void iTestAddTask() {
        RequestBuilder requestBuilder = post("/tasks/?plannedTerm=2044-10-11&taskName=task1")
                .header("Accept-Language", "ukr");
        String date = LocalDate.now().toString();
        try {
            this.mockMvc.perform(requestBuilder).andExpectAll(
                    status().isCreated(),
                    content().contentType(MediaType.APPLICATION_JSON),
                    content().json("{" +
                            "\"id\":1," +
                            "\"task_name\":\"task1\"," +
                            "\"planned_term\":\"2044-10-11\"," +
                            "\"last_status_change\":\"" + date + "\"," +
                            "\"status\":\"PLANNED\"," +
                            "\"status_translated\":\"ЗАПЛАНОВАНО\"}"));
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
    @Test
    @WithUserDetails("admin")
    void iTestPutNextStatus() {
        repo.save(task1);
        repo.save(task2);
        repo.save(task3);
        RequestBuilder requestBuilder = put("/tasks/put_next_status/2")
                .header("Accept-Language", "ukr");
        try {
            this.mockMvc.perform(requestBuilder).andExpectAll(
                    status().isCreated(),
                    content().contentType(MediaType.APPLICATION_JSON),
                    content().json("{" +
                            "\"id\":2," +
                            "\"task_name\":\"task2\"," +
                            "\"planned_term\":\"3099-09-09\"," +
                            "\"last_status_change\":\"2023-01-01\"," +
                            "\"status\":\"DONE\"," +
                            "\"status_translated\":\"ВИКОНАНО\"}"));

            this.mockMvc.perform(requestBuilder).andExpectAll(
                    status().isBadRequest(),
                    content().contentType(MediaType.APPLICATION_JSON),
                    content().json("{\"error\": \"Поточний статус завдання не може бути змінен\"}"));

        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
