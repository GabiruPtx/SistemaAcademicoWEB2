package Service;

import dao.UserDAO;
import model.User;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO(); // Instância do DAO
    }

    public boolean authenticate(String email, String password, String role) {
        User user = userDAO.getUserByEmailAndRole(email, role);
        return user != null && user.getPassword().equals(password);
    }
}
