package org.example.repository;

import org.example.db.DataBase;
import org.example.dto.Profile;
import org.example.dto.Terminal;
import org.example.enums.GeneralStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.temporal.Temporal;
import java.util.LinkedList;
import java.util.List;
@Repository
public class TerminalRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Terminal terminal) {
        String sql = "insert into terminal(code,address,created_date,status) values (?,?,?,?) ";
        int n = jdbcTemplate.update(sql, terminal.getCode(), terminal.getAddress(), terminal.getCreatedDate(), terminal.getStatus().name());
        return n;
    }

    public Terminal getTerminalByCode(String code) {
        String sql = String.format("select * from terminal where visible = true and code='%s'",code);
        List<Terminal> terminalList= jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Terminal.class));
       if(terminalList.isEmpty()){
           return null;
       }else {
           return terminalList.get(0);
       }
    }

    public List<Terminal> getTerminalList() {

        String sql = String.format("select * from terminal where visible = true");
        List<Terminal> terminalLIst = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Terminal.class));
        return terminalLIst;

    }

    public int updateTerminal(Terminal terminal) {
        String sql = "update terminal set address=? where code=? ;";
        int n = jdbcTemplate.update(sql, terminal.getAddress(), terminal.getCode());
        return n;
    }

    public int changeTerminalStatus(String code, GeneralStatus status) {
        String sql = String.format("update terminal set status='%s' where code='%s'",status,code);
        int n = jdbcTemplate.update(sql);
        return n;
    }

    public int deleteTerminal(String code) {

        String sql = "update terminal set visible=false where code=?";
        int n = jdbcTemplate.update(sql, code);
        return n;
    }
}
