package org.example.repository;

import org.example.db.DataBase;
import org.example.dto.Card;
import org.example.enums.GeneralStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Repository
public class CardRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Card card) {

        String sql = "insert into card (card_number, exp_date,balance,status,phone,created_date) " + " values (?,?,?,?,?,?)";
        int n = jdbcTemplate.update(sql, card.getCardNumber(), card.getExpDate(), card.getBalance(), card.getStatus().name(), card.getPhone(), card.getCreatedDate());

        return n;
    }

    public Card getCardById(Integer id) {
        String sql = "Select * from Card where id=" + id;
        Card card = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Card.class));
        return card;

    }

    public Boolean rechargeBalance(Integer cardId, Double balance) {

        String sql = String.format("update card set balance = %d where id = %d", balance, cardId);
        int n = jdbcTemplate.update(sql);
        return n != 0 ? true : false;
    }

    public List<Card> getList() {
        String sql = "Select * from card";
        List<Card> cardList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Card.class));
        if(cardList.isEmpty()){
            return null;
        }
        return cardList;
    }

    public Card getCardByNumber(String number) {

        String sql = "select * from card where visible = true and card_number = '" + number + "';";
        List<Card> cardList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Card.class));
        if (cardList.isEmpty()) {
            return null;
        }
        return cardList.get(0);

    }

    public int assignPhoneToCard(String phone, String cardNum) {

        String sql = String.format("update card set phone = '%s' where card_number ='%s'", phone, cardNum);
        int n = jdbcTemplate.update(sql);
        return n;
    }

    public List<Card> getCardByProfilePhone(String phone) {

        String sql = "select * from card where visible = true and phone = '" + phone + "';";
        List<Card> cardList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Card.class));
        return cardList;
    }

    public int updateCardStatus(String cardNum, GeneralStatus status) {

        String sql = String.format("update card set status = '%s' where  card_number = '%s'", status.name(), cardNum);
        int n = jdbcTemplate.update(sql);
        return n;
    }

    public int deleteCard(String cardNumber) {
        String sql = String.format("update card set visible = false where  card_number = '%s'", cardNumber);
        int n = jdbcTemplate.update(sql);
        return n;
    }

    public int updateCard(Card card) {

        String sql = String.format("update card set exp_date = '%s' where  card_number = '%s'", card.getExpDate(), card.getCardNumber());
        int n = jdbcTemplate.update(sql);
        return n;
    }

    public int refillCard(String cardNum, Double amount) {

        String sql = String.format("update card set balance ='%s' where card_number ='%s'", amount,cardNum);
        int n = jdbcTemplate.update(sql);
        return n;
    }
}
