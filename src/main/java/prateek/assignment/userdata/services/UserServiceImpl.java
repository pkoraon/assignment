package prateek.assignment.userdata.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prateek.assignment.userdata.entity.User;
import prateek.assignment.userdata.interfaces.UserDao;
import prateek.assignment.userdata.interfaces.UserService;
import prateek.assignment.userdata.model.Login;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    @Transactional
    public Map<String, Object> login(Login login) {

        Map<String, Object> map = new HashMap<>();

        User user =  userRepository.loginUser(login.getUsername(), login.getPassword());
        if (user == null) {
            map.put("message", "Wrong credentials.");
            return map;
        }

        return user.toMap();

    }

    @Override
    @Transactional
    public Map<String, String> deleteUser(int id) {
        return userRepository.deleteUser(id);
    }

    @Override
    @Transactional
    public void createUser(User user, Map<String, String> map) {

        if(userRepository.emailExists(user.getEmail())){
            user.setEmailExists(true);
            map.put("message", "Email already exists.");
        }else if(userRepository.usernameExists(user.getUsername())){
            user.setUsernameExists(true);
            map.put("message", "Username already exist. Try something else.");
        }

        if(user.getUsernameExists() || user.getEmailExists()) {
            return;
        }else{
            try{
                userRepository.createUser(user);
                map.put("message", "User created");
            }catch(ConstraintViolationException ex){
                Set<ConstraintViolation<?>> exceptions = ex.getConstraintViolations();
                for (ConstraintViolation<?> exception : exceptions) {
                    map.put(
                            exception.getPropertyPath().toString(),
                            exception.getMessageTemplate()
                    );
                }
            }catch(Exception ex) {
                map.put("message", "Could not create new user.");
            }
        }
    }

    @Override
    @Transactional
    public Map<String,String> updateUserData(int id, User user) {

        Map<String, String> map = new HashMap<>();

        if (id != 0) {
            User retrieved = userRepository.findUserById(id);

            if(retrieved == null){
                map.put("message", "User does not exist.");
            }else{
                retrieved = userRepository.findUserByEmail(user.getEmail());
                if (retrieved != null) {
                    if(id != retrieved.getId()) {
                        user.setEmailExists(true);
                        map.put("message", "Email id exists");
                    }
                }

                retrieved = userRepository.findUserByUsername(user.getUsername());
                if(retrieved != null) {
                    if(id != retrieved.getId()){
                        user.setUsernameExists(true);
                        map.put("message", "Username already exists. Try something else.");
                    }
                }

                if(user.getUsernameExists() || user.getEmailExists()) {
                    return map;
                }else{
                    user.setId(id);
                    try{
                        userRepository.updateUserInfo(user);
                        map.put("message", "Successfully updated");
                    }catch (ConstraintViolationException ex) {
                        Set<ConstraintViolation<?>> exceptions = ex.getConstraintViolations();
                        for (ConstraintViolation<?> exception : exceptions) {
                            map.put(
                                    exception.getPropertyPath().toString(),
                                    exception.getMessageTemplate()
                            );
                        }
                    }catch (Exception ex) {
                        map.put("message", "Some Error Occurred.");
                    }
                }
            }
        }else{
            map.put("message", "Invalid ID");
        }

        return map;
    }

}
