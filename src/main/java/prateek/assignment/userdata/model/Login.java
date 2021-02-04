package prateek.assignment.userdata.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Login {

    @NotNull(message = "required field")
    @Size(min = 1, message = "required field")
    private String username;

    @NotNull(message = "required field")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Login{" +
                "name='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
