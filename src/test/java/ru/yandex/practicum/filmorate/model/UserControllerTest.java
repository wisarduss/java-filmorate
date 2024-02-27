package ru.yandex.practicum.filmorate.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
<<<<<<< HEAD
=======
import ru.yandex.practicum.filmorate.model.User;
>>>>>>> f8eaca6cd03b84a8bfea41c8c04ec16b21274142

public class UserControllerTest {
    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testShouldReturnUserSuccess() {
        User user = new User();
        user.setEmail("testemail@gmail.com");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.now().minusMonths(7));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testShouldReturnUserWithFailedEmail() {
        User user = new User();
        user.setEmail("testemail");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.now().minusMonths(7));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testShouldReturnUserWithFailedLogin() {
        User user = new User();
        user.setEmail("testemail@mail.ru");
        user.setBirthday(LocalDate.now().minusMonths(7));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testShouldReturnUserWithFailedBirthday() {
        User user = new User();
        user.setEmail("testemail@mail.ru");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.now().plusMonths(7));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }
}
