package prateek.assignment.userdata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import prateek.assignment.userdata.entity.User;
import prateek.assignment.userdata.repository.UserRepository;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

// To launch spring boot context
@ExtendWith(SpringExtension.class)
// Spring Boot Application Class
@SpringBootTest(classes = UserDataApplication.class)
class UserDataApplicationTests {

    @Autowired
    UserRepository userDao;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    void contextLoads() {
        assertNotNull(userDao);
    }

    @Test
    void GetAllUsersTest() {
        List<User> users = userDao.getAllUsers();
        assertNotNull(users);
    }

}
