package com.toniolo.todolist_spring.service;

import com.toniolo.todolist_spring.model.TaskModel;
import com.toniolo.todolist_spring.model.TaskStatus;
import com.toniolo.todolist_spring.model.UserModel;
import com.toniolo.todolist_spring.model.UserRole;
import com.toniolo.todolist_spring.repository.TaskRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskAuthorizationService authorizationService;
    private TaskService taskService;

    private UserModel admin;
    private UserModel user;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        authorizationService = mock(TaskAuthorizationService.class);
        taskService = new TaskService();
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
        ReflectionTestUtils.setField(taskService, "authorizationService", authorizationService);

        admin = new UserModel("admin", "senha", UserRole.ADMIN);
        admin.setId(1L);
        user = new UserModel("user", "senha", UserRole.STANDARD);
        user.setId(2L);
    }

    private void mockCurrentUser(UserModel currentUser) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUser);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Admin deve ver todas as tasks")
    void testGetTasksAsAdmin() {
        mockCurrentUser(admin);
        List<TaskModel> allTasks = Arrays.asList(new TaskModel(), new TaskModel());
        when(taskRepository.findAll()).thenReturn(allTasks);

        List<TaskModel> result = taskService.getTasks();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Usuário comum deve ver apenas suas tasks")
    void testGetTasksAsUser() {
        mockCurrentUser(user);
        List<TaskModel> userTasks = Arrays.asList(new TaskModel());
        when(taskRepository.findByUserId(user.getId())).thenReturn(userTasks);

        List<TaskModel> result = taskService.getTasks();
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Deve retornar task se autorizado")
    void testGetTaskByIdAuthorized() {
        mockCurrentUser(user);
        TaskModel task = new TaskModel();
        task.setUser(user);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(authorizationService.isAdminOrOwner(user, task)).thenReturn(true);

        Optional<TaskModel> result = taskService.getTaskById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Deve lançar AccessDeniedException se não autorizado ao buscar task")
    void testGetTaskByIdNotAuthorized() {
        mockCurrentUser(user);
        TaskModel task = new TaskModel();
        task.setUser(new UserModel("other", "senha", UserRole.STANDARD));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(authorizationService.isAdminOrOwner(user, task)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    @DisplayName("Deve criar task com usuário atual e status PENDING")
    void testCreateTask() {
        mockCurrentUser(user);
        TaskModel task = new TaskModel();
        when(taskRepository.save(any(TaskModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskModel result = taskService.createTask(task);

        assertEquals(user, result.getUser());
        assertEquals(TaskStatus.PENDING, result.getStatus());
    }

    @Test
    @DisplayName("Deve atualizar status para IN_PROGRESS se autorizado")
    void testMarkTaskAsInProgressAuthorized() {
        mockCurrentUser(user);
        TaskModel task = new TaskModel();
        task.setUser(user);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(authorizationService.isAdminOrOwner(user, task)).thenReturn(true);
        when(taskRepository.save(any(TaskModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<TaskModel> result = taskService.markTaskAsInProgress(1L);

        assertTrue(result.isPresent());
        assertEquals(TaskStatus.IN_PROGRESS, result.get().getStatus());
    }

    @Test
    @DisplayName("Deve lançar AccessDeniedException ao tentar atualizar status sem permissão")
    void testMarkTaskAsInProgressNotAuthorized() {
        mockCurrentUser(user);
        TaskModel task = new TaskModel();
        task.setUser(new UserModel("other", "senha", UserRole.STANDARD));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(authorizationService.isAdminOrOwner(user, task)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> taskService.markTaskAsInProgress(1L));
    }

    @Test
    @DisplayName("Deve deletar task se autorizado")
    void testDeleteTaskAuthorized() {
        mockCurrentUser(user);
        TaskModel task = new TaskModel();
        task.setUser(user);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(authorizationService.isAdminOrOwner(user, task)).thenReturn(true);

        doNothing().when(taskRepository).deleteById(1L);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar AccessDeniedException ao tentar deletar sem permissão")
    void testDeleteTaskNotAuthorized() {
        mockCurrentUser(user);
        TaskModel task = new TaskModel();
        task.setUser(new UserModel("other", "senha", UserRole.STANDARD));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(authorizationService.isAdminOrOwner(user, task)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> taskService.deleteTask(1L));
    }
}