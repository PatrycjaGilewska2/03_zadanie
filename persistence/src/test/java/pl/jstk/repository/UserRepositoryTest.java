package pl.jstk.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Optional;

import pl.jstk.PersistenceModuleConfig;
import pl.jstk.entity.UserEntity;
import pl.jstk.repository.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersistenceModuleConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testShouldFindUserById() {
        // given
        final long userId = 1;
        // when
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        // then
        assertNotNull(userEntity);
        assertEquals("admin", userEntity.get().getUserName());
    }

    @Test
    public void testShouldFindUsersByTitle() {
        // given
        final String userName = "admin";
        // when
        List<UserEntity> usersEntity = userRepository.findUsersByName(userName);
        // then
        assertNotNull(usersEntity);
        assertFalse(usersEntity.isEmpty());
        assertEquals(userName, usersEntity.get(0).getUserName());
    }
}
