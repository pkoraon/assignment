package prateek.assignment.userdata.interfaces;

import prateek.assignment.userdata.entity.User;
import prateek.assignment.userdata.model.Login;
import java.util.List;
import java.util.Map;

public interface UserService {

    public List<User> getAllUsers();

    public Map<String, Object> login(Login login);

    public Map<String, String> deleteUser(int id);

    public Map<String, String> updateUserData(int id, User user);

    public void createUser(User user, Map<String, String> map);

}
