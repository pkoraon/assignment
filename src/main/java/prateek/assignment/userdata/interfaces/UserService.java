package prateek.assignment.userdata.interfaces;

import com.fasterxml.jackson.databind.JsonNode;
import prateek.assignment.userdata.entity.User;
import prateek.assignment.userdata.model.Login;
import java.util.List;
import java.util.Map;

public interface UserService {

    public List<User> getAllUsers();

    public Map<String, Object> login(Login login);

    public Map<String, String> deleteUser(int id);

    public Map<String, String> updateUserData(int id, JsonNode payload);

    public void createUser(JsonNode payload, Map<String, String> map);

}
