package prateek.assignment.userdata.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import prateek.assignment.userdata.entity.User;
import prateek.assignment.userdata.interfaces.UserDao;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import static prateek.assignment.userdata.utils.Constants.USERNAME;

@Repository
@Transactional
public class UserRepository implements UserDao {

    @Autowired
    EntityManager entityManager;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // REST API
    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public void createUser(User user) {

        try{
            entityManager.persist(user);
        }catch(ConstraintViolationException ex) {
            throw ex;
        }catch(Exception ex) {
            throw ex;
        }

    }

    @Override
    public Map<String, String> deleteUser(int id) {
        User user = findUserById(id);
        Map<String, String> map = new HashMap<>();
        if (user != null) {
            entityManager.remove(user);
            map.put("message", "User deleted");
        } else {
            map.put("message", "User does not exist");
        }
        return map;
    }

    @Override
    public User findUserByEmail(String email) {
        TypedQuery<User> query = entityManager
                .createQuery("select u from User u where u.email=:email", User.class)
                .setParameter("email", email);

        if(query.getResultList().isEmpty()) {
            return null;
        }else{
            return query.getSingleResult();
        }
    }

    @Override
    public User findUserByUsername(String username){
        TypedQuery<User> query = entityManager
                .createQuery("select u from User u where u.username=:username", User.class)
                .setParameter(USERNAME, username);

        if(query.getResultList().isEmpty()){
            return null;
        }else{
            return query.getSingleResult();
        }
    }

    @Override
    public void updateUserInfo(User user) {

        try{
            entityManager.merge(user);
        }catch (ConstraintViolationException ex) {
            throw ex;
        }catch (Exception ex) {
            throw ex;
        }

    }

    @Override
    public User findUserById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public boolean usernameExists(String username) {
        TypedQuery<User> query = entityManager
                .createQuery("select u from User u where u.username=:username", User.class)
                .setParameter(USERNAME, username);

        return !query.getResultList().isEmpty();
    }

    @Override
    public boolean emailExists(String email) {
        TypedQuery<User> query = entityManager
                .createQuery("select u from User u where u.email=:email", User.class)
                .setParameter("email", email);
        return !query.getResultList().isEmpty();
    }

    @Override
    public User loginUser(String username, String password) {
        logger.info("Username -> {}", username);
        TypedQuery<User> query = entityManager
                .createQuery(
                        "select u from User u where u.username= :username and u.password=:password",
                        User.class
                ).setParameter(USERNAME, username)
                .setParameter("password", password);

        User user;
        if(query.getResultList().isEmpty()) {
            return null;
        }
        user = query.getSingleResult();
        return user;
    }

}
