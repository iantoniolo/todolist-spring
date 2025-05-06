package com.toniolo.todolist_spring.mapper;

import com.toniolo.todolist_spring.dto.TaskRequestDTO;
import com.toniolo.todolist_spring.dto.TaskResponseDTO;
import com.toniolo.todolist_spring.dto.UserResponseDTO;
import com.toniolo.todolist_spring.model.TaskModel;
import com.toniolo.todolist_spring.model.TaskStatus;
import com.toniolo.todolist_spring.model.UserModel;
import com.toniolo.todolist_spring.model.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private final TaskMapper mapper = new TaskMapper();

    @Test
    @DisplayName("Deve mapear TaskRequestDTO para TaskModel")
    void testToEntity() {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setDescription("Nova tarefa");

        TaskModel model = mapper.toEntity(dto);

        assertNotNull(model);
        assertEquals("Nova tarefa", model.getDescription());
    }

    @Test
    @DisplayName("Deve mapear TaskModel para TaskResponseDTO com usuário")
    void testToDtoWithUser() {
        UserModel user = new UserModel("usuario", "senha", UserRole.STANDARD);
        user.setId(10L);

        TaskModel task = new TaskModel();
        task.setId(1L);
        task.setDescription("Tarefa de teste");
        task.setStatus(TaskStatus.PENDING);
        task.setUser(user);

        TaskResponseDTO dto = mapper.toDto(task);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Tarefa de teste", dto.getDescription());
        assertEquals(TaskStatus.PENDING, dto.getStatus()); // Ajustado para enum
        assertNotNull(dto.getUser());
        assertEquals(10L, dto.getUser().getId());
        assertEquals("usuario", dto.getUser().getUsername());
        assertEquals("STANDARD", dto.getUser().getRole());
    }

    @Test
    @DisplayName("Deve mapear TaskModel para TaskResponseDTO sem usuário")
    void testToDtoWithoutUser() {
        TaskModel task = new TaskModel();
        task.setId(2L);
        task.setDescription("Tarefa sem usuário");
        task.setStatus(TaskStatus.PENDING);
        task.setUser(null);

        TaskResponseDTO dto = mapper.toDto(task);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("Tarefa sem usuário", dto.getDescription());
        assertEquals(TaskStatus.PENDING, dto.getStatus()); // Ajustado para enum
        assertNull(dto.getUser());
    }

    @Test
    @DisplayName("Deve mapear lista de TaskModel para lista de TaskResponseDTO")
    void testToDtoList() {
        TaskModel task1 = new TaskModel();
        task1.setId(1L);
        task1.setDescription("Tarefa 1");
        task1.setStatus(TaskStatus.PENDING);

        TaskModel task2 = new TaskModel();
        task2.setId(2L);
        task2.setDescription("Tarefa 2");
        task2.setStatus(TaskStatus.COMPLETED);

        List<TaskModel> tasks = Arrays.asList(task1, task2);

        List<TaskResponseDTO> dtos = mapper.toDtoList(tasks);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Tarefa 1", dtos.get(0).getDescription());
        assertEquals(TaskStatus.PENDING, dtos.get(0).getStatus()); // Ajustado para enum
        assertEquals("Tarefa 2", dtos.get(1).getDescription());
        assertEquals(TaskStatus.COMPLETED, dtos.get(1).getStatus()); // Ajustado para enum
    }
}