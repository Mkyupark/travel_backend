package gs.chippo.travel.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import gs.chippo.travel.config.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureWebMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @DisplayName("유저 회원가입")
    public void testRegisterUser() throws Exception {
        UserDTO userDTO = UserDTO.builder().email("test@example.com").username("testuser").password("password").build();

        UserEntity userEntity = UserEntity.builder().email("test@example.com").username("testuser").password(passwordEncoder.encode("password")).build();

        when(userService.create(any(UserEntity.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        String userDTOJson = new ObjectMapper().writeValueAsString(userDTO);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJson)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF 토큰 포함
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(userService, times(1)).create(any(UserEntity.class));
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    @DisplayName("유저 로그인")
    public void testAuthenticate() throws Exception {
        UserDTO userDTO = UserDTO.builder()
                .email("user01@gmail.com")
                .password("12345")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .email("user01@gmail.com")
                .password(passwordEncoder.encode("12345"))
                .build();

        when(userService.getByCredentials(eq("user01@gmail.com"), eq("12345"), any(PasswordEncoder.class)))
                .thenReturn(userEntity);
        when(tokenProvider.create(userEntity)).thenReturn("dummyToken");

        String userDTOJson = new ObjectMapper().writeValueAsString(userDTO);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJson)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF 토큰 포함
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user01@gmail.com"))
                .andExpect(jsonPath("$.token").value("dummyToken"));

        verify(userService, times(1)).getByCredentials(eq("user01@gmail.com"), eq("12345"), any(PasswordEncoder.class));
        verify(tokenProvider, times(1)).create(userEntity);
    }

    @Test
    public void testGetUserId() throws Exception {
        String token = "dummyToken";
        String userId = "testuser";

        when(tokenProvider.getUserId(token)).thenReturn(userId);

        mockMvc.perform(post("/auth/user")
                        .header("Authorization", "Bearer " + token)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF 토큰 포함
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("testuser"));

        verify(tokenProvider, times(1)).getUserId(token);
    }

    @Test
    public void testGetUserId_InvalidToken() throws Exception {
        String token = "invalidToken";

        when(tokenProvider.getUserId(token)).thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(post("/auth/user")
                        .header("Authorization", "Bearer " + token)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF 토큰 포함
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("토큰으로 id 찾지 못함"));

        verify(tokenProvider, times(1)).getUserId(token);
    }
}
