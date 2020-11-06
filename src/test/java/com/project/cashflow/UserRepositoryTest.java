package com.project.cashflow;

import com.project.cashflow.domain.User;
import com.project.cashflow.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() {
        User user = User.builder()
                .email("allan@hotmail.com")
                .password("Allan123")
                .build();
        User save = userRepository.save(user);

        System.out.println(save.getId());
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        assertThat(optionalUser).isNotEmpty();
    }

}
