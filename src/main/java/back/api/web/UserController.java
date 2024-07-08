package back.api.web;

import back.api.business.UserService;
import back.api.persistence.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @PostMapping("/block/{username}")
    public void blockUser(@PathVariable String username){
        userService.blockUser(username);
    }

    @PostMapping("/unblock/{username}")
    public void unblockUser(@PathVariable String username){
        userService.unblockUser(username);
    }
}
