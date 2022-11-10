package org.example.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
@Repository
public class DataBase {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public  void initDatabase() {

        String profile = "create table if not exists profile ( \n" +
                "             id serial primary key,\n" +
                "             name varchar(20) not null,\n" +
                "             surname varchar(20) not null,\n" +
                "             phone varchar(12),\n" +
                "             password varchar not null,\n" +
                "             created_date timestamp not null default now(),\n" +
                "             status varchar(20) not null, \n" +
                "             role varchar not null\n" + ");";


        String card = "create table if not exists card(\n" +
                "   id serial primary key,\n" +
                "   card_number varchar unique,\n" +
                "   exp_date date not null,\n" +
                "   balance numeric not null,\n" +
                "   status varchar(20) not null,\n" +
                "   phone varchar(12) ,\n" +
                "   added_date timestamp ,\n" +
                "   visible boolean default true ,\n" +
                "   created_date timestamp not null default now()\n" + ");";


        String terminal = "create table if not exists terminal( id serial primary key, " +
                "code varchar unique not null ," +
                "address varchar ," +
                "status varchar, " +
                "visible boolean default true, " +
                "created_date timestamp default now()) ;";

        String transaction = "create table if not exists transaction(" +
                " id serial primary key, " +
                "card_id int not null, " +
                "amount numeric , " +
                "terminal_id int , " +
                "type varchar , " +
                "created_date timestamp default now()," +
                " foreign key(card_id) references  card(id), " +
                " foreign key(terminal_id) references  terminal(id)) ;";
        jdbcTemplate.execute(profile);
        jdbcTemplate.execute(card);
        jdbcTemplate.execute(transaction);
        jdbcTemplate.execute(terminal);
    }




}
