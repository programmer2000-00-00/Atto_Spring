package org.example.repository;

import org.example.db.DataBase;
import org.example.dto.Card;
import org.example.dto.Profile;
import org.example.enums.GeneralStatus;
import org.example.enums.ProfileRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Repository
public class ProfileRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Profile getProfileByPhoneAndPassword(String phone, String password) {

        String sql = String.format("Select  * from profile where phone= '%s' and password = '%s'", phone, password);
        List<Profile> profileList= jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Profile.class));
        if(profileList.isEmpty()){
            return null;
        }
        return profileList.get(0);
    }

    public Profile getProfileByPhone(String phone) {

            String sql ="Select  * from profile where phone= '"+phone+"'";
            Profile profile= jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Profile.class));
            return profile;

    }

    public Boolean isPhoneExist(String phone) {

            String sql = String.format("Select  id from profile where phone= '%s';", phone);
           List<Profile> profileList= jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Profile.class));
            return !profileList.isEmpty() ? true : false;
    }

    public Integer saveProfile(Profile profile) {

            String sql ="insert into profile(name,surname,phone,password,role,status,created_date) " +
                    "values (?,?,?,?,?,?,?)";
            int n= jdbcTemplate.update(sql, profile.getName(), profile.getSurname(), profile.getPhone(), profile.getPassword() ,
                    profile.getRole().name(), profile.getStatus().name(), profile.getCreatedDate());
            return n;
    }


    public List<Profile> getProfileList() {

             String sql="Select * from profile";
             List<Profile> profileList=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Profile.class));
             return profileList;
    }

    public Integer changeProfileStatus(String phone, GeneralStatus status) {
        String sql = String.format("update profile set status = '%s' where phone = '%s'", status.name(), phone);
        int n = jdbcTemplate.update(sql);
        return n;
}
}