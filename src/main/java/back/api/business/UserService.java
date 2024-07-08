package back.api.business;

import back.api.persistence.models.User;
import back.api.persistence.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public Optional<User> findByUsername(String username){
        return usersRepository.findByUsername(username);
    }

    public List<User> getUsers(){
        return usersRepository.findAll();
    }

    public void blockUser(String username){
        User user = usersRepository.findByUsername(username).orElseThrow();
        user.setAccountNonLocked(false);
        usersRepository.save(user);
    }

    public void unblockUser(String username){
        User user = usersRepository.findByUsername(username).orElseThrow();
        user.setAccountNonLocked(true);
        usersRepository.save(user);
    }
}
