package prateek.assignment.userdata.interfaces;

import prateek.assignment.userdata.entity.User;
import java.util.List;
import java.util.Map;

public interface UserDao {

    public List<User> getAllUsers();

    public User findUserById(int id);

    public User findUserByEmail(String email);

    public User findUserByUsername(String username);

    public void createUser(User user);

    public Map<String, String> deleteUser(int id);

    public void updateUserInfo(User user);

    public boolean usernameExists(String username);

    public boolean emailExists(String email);

    public User loginUser(String email, String password);

}
