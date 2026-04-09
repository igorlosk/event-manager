package dev.sorokin.eventmanager.users;

import dev.sorokin.eventmanager.security.jwt.JwtAuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    private final static Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    private final JwtAuthenticationService authenticationService;

    private final UserToDtoMapper userToDtoMapper;

    public UsersController(
            UserService userService,
            JwtAuthenticationService authenticationService,
            UserToDtoMapper userToDtoMapper) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.userToDtoMapper = userToDtoMapper;
    }

    @PostMapping()
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid SingUpRequest singUpRequest) {
        LOGGER.info("Get request for singUp: login={}", singUpRequest.login());
        var user = userService.registerUser(singUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserDto(
                        user.id(),
                        user.login()
                ));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @RequestBody @Valid SignInRequest signInRequest
    ) {
        LOGGER.info("Get request for sign-in: login={}", signInRequest.login());
        var token = authenticationService.authenticateUser(signInRequest);

        return ResponseEntity.status(HttpStatus.OK).body(new JwtTokenResponse(token));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) {
        LOGGER.info("Get user by id={}", id);
        return userToDtoMapper.userToDto(userService.findById(id));
    }
}
