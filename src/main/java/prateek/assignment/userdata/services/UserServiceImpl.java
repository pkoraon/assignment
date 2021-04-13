package prateek.assignment.userdata.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static prateek.assignment.userdata.utils.Constants.*;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
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
    public void createUser(JsonNode paylod, Map<String, String> map) {
        logger.info("UserServiceImpl.createUser() called with payload {}", paylod);
        User user = new Gson().fromJson(paylod.toString(),User.class);
        if(userRepository.emailExists(paylod.get("email").toString())){
            user.setEmailExists(true);
            map.put(MESSAGE, "Email already exists.");
        }else if(userRepository.usernameExists(user.getUsername())){
            user.setUsernameExists(true);
            map.put(MESSAGE, "Username already exist. Try something else.");
        }

        if(!user.getUsernameExists() &&  !user.getEmailExists()) {
            try{
                userRepository.createUser(user);
                map.put(MESSAGE, "User created");
            }catch(ConstraintViolationException ex){
                Set<ConstraintViolation<?>> exceptions = ex.getConstraintViolations();
                for (ConstraintViolation<?> exception : exceptions) {
                    map.put(
                            exception.getPropertyPath().toString(),
                            exception.getMessageTemplate()
                    );
                }
            }catch(Exception ex) {
                map.put(MESSAGE, "Could not create new user.");
            }
        }
    }

    @Override
    @Transactional
    public Map<String,String> updateUserData(int id, JsonNode payload) {

        Map<String, String> map = new HashMap<>();

        if (id != 0) {
            User user = new Gson().fromJson(payload.toString(), User.class);
            User retrieved = userRepository.findUserById(id);

            if(retrieved == null){
                map.put(MESSAGE, "User does not exist.");
            }else{
                retrieved = userRepository.findUserByEmail(user.getEmail());
                if (retrieved != null && retrieved.getId() != id) {
                    user.setEmailExists(true);
                    map.put(MESSAGE, "Email id exists");
                }

                retrieved = userRepository.findUserByUsername(user.getUsername());
                if(retrieved != null && retrieved.getId() != id) {
                    user.setUsernameExists(true);
                    map.put(MESSAGE, "Username already exists. Try something else.");
                }

                if(user.getUsernameExists() || user.getEmailExists()) {
                    return map;
                }else{
                    user.setId(id);
                    try{
                        userRepository.updateUserInfo(user);
                        logger.info("User {}", user.toMap());
                        map.put(MESSAGE, "Successfully updated");
                    }catch (ConstraintViolationException ex) {
                        logger.error("ConstraintViolationException");
                        Set<ConstraintViolation<?>> exceptions = ex.getConstraintViolations();
                        for (ConstraintViolation<?> exception : exceptions) {
                            map.put(
                                    exception.getPropertyPath().toString(),
                                    exception.getMessageTemplate()
                            );
                        }
                    }catch (Exception ex) {

                        map.put(MESSAGE, "Some Error Occurred.");
                    }
                }
            }
        }else{
            map.put(MESSAGE, "Invalid ID");
        }

        return map;
    }

}
