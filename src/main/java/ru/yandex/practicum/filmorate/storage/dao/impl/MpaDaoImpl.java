package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.util.List;

@Component("MpaDaoImpl")
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> allMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa",
                (o1, o2) -> new MPA(o1.getInt("id"), o1.getString("mpa")));
    }

    @Override
    public MPA mpa(int id) {
        return  jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE id = ?",
                (o1, o2) -> new MPA(o1.getInt("id"), o1.getString("mpa")), id);
    }

}
