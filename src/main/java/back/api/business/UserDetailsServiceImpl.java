package back.api.business;


import back.api.configuration.authentification.UserDetailsImpl;
import back.api.exceptions.BadRequestException;
import back.api.exceptions.UserAlreadyExistsException;
import back.api.persistence.models.User;
import back.api.persistence.models.enums.Role;
import back.api.persistence.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Autowired
    public UserDetailsServiceImpl(UsersRepository userRepository){
        this.usersRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsImpl user = usersRepository.findByUsername(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.isAccountNonLocked()) throw new IllegalArgumentException("User is locked");

        return user;
    }

    @Transactional
    public ResponseEntity<?> registerUser(Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if(username == null || password == null) {
            throw new BadRequestException("Username and password are required");
        }

        if (usersRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        usersRepository.save(new User(username, password, Role.USER));
        return ResponseEntity.ok().body(Map.of("message", "User registered successfully"));
    }
}
