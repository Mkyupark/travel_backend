package gs.chippo.travel.controller;


import gs.chippo.travel.config.JwtTokenProvider;
import gs.chippo.travel.dto.ResponseDTO;
import gs.chippo.travel.dto.UserDTO;
import gs.chippo.travel.entity.UserEntity;
import gs.chippo.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider tokenProvider;


    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
        try{
            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .build();
            UserEntity registeredUser = userService.create(user);
            UserDTO responseUserDTO = userDTO.builder()
                    .email(registeredUser.getEmail())
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }catch(Exception e){
            ResponseDTO responseDTO =
                    ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @PostMapping("/signin")
    public ResponseEntity<?>authenticate(@RequestBody UserDTO userDTO){
        UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(),
                passwordEncoder);

        if(user !=null){
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .id(user.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
