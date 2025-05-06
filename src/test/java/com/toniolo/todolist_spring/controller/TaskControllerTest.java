package com.toniolo.todolist_spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toniolo.todolist_spring.dto.TaskRequestDTO;
import com.toniolo.todolist_spring.dto.TaskResponseDTO;
import com.toniolo.todolist_spring.infra.SecurityFilter;
import com.toniolo.todolist_spring.mapper.TaskMapper;
import com.toniolo.todolist_spring.model.TaskModel;
import com.toniolo.todolist_spring.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskMapper taskMapper;

    @Test
    @DisplayName("Deve retornar lista de tarefas")
    void testGetTasks() throws Exception {
        TaskModel task = new TaskModel(); // preencha os campos conforme seu model
        TaskResponseDTO dto = new TaskResponseDTO(); // preencha os campos conforme seu dto

        Mockito.when(taskService.getTasks()).thenReturn(Arrays.asList(task));
        Mockito.when(taskMapper.toDtoList(anyList())).thenReturn(Arrays.asList(dto));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @DisplayName("Deve retornar 204 quando não houver tarefas")
    void testGetTasksNoContent() throws Exception {
        Mockito.when(taskService.getTasks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar tarefa por ID")
    void testGetTaskById() throws Exception {
        TaskModel task = new TaskModel();
        TaskResponseDTO dto = new TaskResponseDTO();

        Mockito.when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));
        Mockito.when(taskMapper.toDto(task)).thenReturn(dto);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar tarefa inexistente")
    void testGetTaskByIdNotFound() throws Exception {
        Mockito.when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve criar nova tarefa")
    void testCreateTask() throws Exception {
        TaskRequestDTO requestDto = new TaskRequestDTO(); // preencha os campos conforme seu dto
        TaskModel taskModel = new TaskModel();
        TaskResponseDTO responseDto = new TaskResponseDTO();

        Mockito.when(taskMapper.toEntity(any(TaskRequestDTO.class))).thenReturn(taskModel);
        Mockito.when(taskService.createTask(any(TaskModel.class))).thenReturn(taskModel);
        Mockito.when(taskMapper.toDto(taskModel)).thenReturn(responseDto);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve marcar tarefa como em progresso")
    void testMarkTaskAsInProgress() throws Exception {
        TaskModel task = new TaskModel();
        TaskResponseDTO dto = new TaskResponseDTO();

        Mockito.when(taskService.markTaskAsInProgress(1L)).thenReturn(Optional.of(task));
        Mockito.when(taskMapper.toDto(task)).thenReturn(dto);

        mockMvc.perform(put("/tasks/1/status/in-progress"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 ao marcar tarefa inexistente como em progresso")
    void testMarkTaskAsInProgressNotFound() throws Exception {
        Mockito.when(taskService.markTaskAsInProgress(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/tasks/1/status/in-progress"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve marcar tarefa como concluída")
    void testMarkTaskAsCompleted() throws Exception {
        TaskModel task = new TaskModel();
        TaskResponseDTO dto = new TaskResponseDTO();

        Mockito.when(taskService.markTaskAsDone(1L)).thenReturn(Optional.of(task));
        Mockito.when(taskMapper.toDto(task)).thenReturn(dto);

        mockMvc.perform(put("/tasks/1/status/completed"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 ao marcar tarefa inexistente como concluída")
    void testMarkTaskAsCompletedNotFound() throws Exception {
        Mockito.when(taskService.markTaskAsDone(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/tasks/1/status/completed"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar tarefa")
    void testDeleteTask() throws Exception {
        Mockito.doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }
}