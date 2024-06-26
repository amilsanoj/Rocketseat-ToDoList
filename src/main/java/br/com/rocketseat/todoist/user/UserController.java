package br.com.rocketseat.todoist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")

public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create (@RequestBody UserModel userModel){
        UserModel user = this.userRepository.findByUsername(userModel.getUsername());
        if(user != null){
            System.out.println("Usuário já existe na base");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário Já existe");
        }
        //Cifrando a senha
        String  pwHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(pwHashred);

        UserModel userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

}
