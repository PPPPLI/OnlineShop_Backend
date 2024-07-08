package back.api.web;

import back.api.business.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtEncoder jwtEncoder;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final Logger logger = Logger.getLogger(ResourceController.class.getName());

    @Autowired
    AuthController(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtEncoder = jwtEncoder;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerEndpoint(@RequestBody Map<String, String> body) {
        return userDetailsServiceImpl.registerUser(body);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginEndpoint(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            throw new BadCredentialsException("Full authentication is required");
        }

        String encoded = authHeader.substring(6);
        String decoded = new String(Base64.getDecoder().decode(encoded));
        String[] credentials = decoded.split(":");
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                credentials[0],
                credentials[1]
        );

        Authentication authentication = authenticationManager.authenticate(authToken);

        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(60, ChronoUnit.MINUTES))
                .claim("scope", authorities)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();

        return ResponseEntity.ok().body(Map.of("jwtToken", token));
    }

//    @GetMapping("/test")
//    public ResponseEntity<?> testEndpoint(@AuthenticationPrincipal Jwt jwt) {
//        System.out.println(jwt.getClaimAsMap("role"));
//        return ResponseEntity.ok().body("Hello, " + jwt.getSubject() + "!");
//    }

    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint(Authentication authentication) {
        //System.out.println(authentication.getAuthorities());
        return ResponseEntity.ok().build();
    }

}
