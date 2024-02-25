package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private MpaDbStorage mpaDbStorage;

    @BeforeEach
    public void setUp() {
        mpaDbStorage = new MpaDbStorage(jdbcTemplate);
    }

    @Test
    public void testShouldReturnGetByIdMpa() {
        Mpa mpa = mpaDbStorage.getMpaById(3);
        assertNotNull(mpa);
        assertEquals(mpa.getName(), "PG-13");
    }

    @Test
    public void testShouldReturnFindAllMpa() {
        List<Mpa> ratings = mpaDbStorage.findMpa();
        assertEquals(ratings.size(), 5);
        assertEquals(ratings.get(2).getName(), "PG-13");
    }
}
