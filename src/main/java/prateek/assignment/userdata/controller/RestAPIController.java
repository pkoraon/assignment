package prateek.assignment.userdata.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;
import prateek.assignment.userdata.entity.User;
import prateek.assignment.userdata.interfaces.UserService;
import prateek.assignment.userdata.model.Login;
import prateek.assignment.userdata.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RestAPIController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/rest/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @PostMapping("/rest/login")
    public ResponseEntity<Map<String, Object>> getUser(@RequestBody Login login) {
        Map<String, Object> map = userService.login(login);
        if (map.containsKey("message")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @PostMapping("/rest")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody User user) {
        Map<String, String> map = new HashMap<>();
        try{
            userService.createUser(user, map);
        } catch (UnexpectedRollbackException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(map);
    }

    @DeleteMapping("/rest")
    public ResponseEntity<Map<String, String>> deleteUser(@RequestParam int id) {
        Map<String, String> map = userService .deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @PutMapping("/rest")
    public ResponseEntity<Map<String, String>> updateUser(@RequestParam int id, @RequestBody User user) {
        Map<String, String> map = userService.updateUserData(id, user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(map);
    }

}
