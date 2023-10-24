package br.com.rocketseat.todoist.task;

import br.com.rocketseat.todoist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){
        UUID idUser = (UUID) request.getAttribute("idUser");
        taskModel.setIdUser(idUser);

        LocalDateTime currentDate = LocalDateTime.now();
        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Alguma Data é invalida");
        }
        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("As datas podem estar invertidas");
        }
        TaskModel task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body("Tudo Ok");
    }

    @GetMapping("/")
    public List<TaskModel> list (HttpServletRequest request){
        return this.taskRepository.findByIdUser((UUID) request.getAttribute("idUser"));
    }
    //Update da task pelo ID dela
    @PutMapping("/{id}")
    public ResponseEntity update (@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){
        var task = this.taskRepository.findById(id).orElse(null);
        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
        } else if (!task.getIdUser().equals(request.getAttribute("idUser"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão");
        }else {
            Utils.copyNonNullProperties(taskModel, task);
            return ResponseEntity.ok().body(taskRepository.save(task));
        }
    }

}
