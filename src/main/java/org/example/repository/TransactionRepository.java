package org.example.repository;

import org.example.container.ComponentContainer;
import org.example.dto.Card;
import org.example.dto.Profile;
import org.example.dto.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Target;
import java.time.LocalDate;
import java.util.List;

import static org.example.container.ComponentContainer.currentProfile;
import static org.example.enums.TransactionType.Payment;

@Repository
public class TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public int createTransaction(Transaction transaction) {

           String sql= "insert into transaction(card_id,terminal_id,amount,type,created_date) " +
                            "values (?,?,?,?,?)";
           int n= jdbcTemplate.update(sql,transaction.getCardId(),transaction.getTerminalId(),transaction.getAmount(),
                transaction.getTransactionType().name(),
                transaction.getCreatedDate());
            return n;
    }


    public List<Transaction> transactionByDayList(LocalDate date) {

            String sql="SELECT * from transaction\n" +
                "where created_date between (Select '"+date+"'::Timestamp)\n" +
                "and (Select'"+date+"'::Timestamp+'24 hours')";
            List<Transaction>transactionList=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Transaction.class));
            return transactionList;
    }

    public List<Transaction> getTodayTransactionList() {

        String sql="select * from transaction\n" +
                "where created_date between current_date and current_timestamp\n" +
                "order by created_date desc";
        List<Transaction>transactionList=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Transaction.class));
        return transactionList;
    }

    public List<Transaction> transactionBetweenDay(LocalDate startD, LocalDate endD) {

            String sql=String.format("SELECT * from transaction where created_date between '%s' and '%s' order by created_date desc",startD,endD);
            List<Transaction> transactionList=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Transaction.class));
            return transactionList;
    }

    public double showBalance() {
        String sql="Select * from transaction where type='"+Payment+"'";
         List<Transaction> transactionList=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Transaction.class));
         if(transactionList.isEmpty()){
             return 0.;
         }
         Double sum=0.;
        for (Transaction tr:transactionList) {
            sum=sum+tr.getAmount();
        }
            return sum;

    }

    public List<Transaction> transactionByTerminal(String code) {

                   String sql="Select * from transaction \n" +
                    "where terminal_id = (Select id from terminal \n" +
                    "                     where code = '" + code + "');";
                   List<Transaction>transactionList=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Transaction.class));
                   return transactionList;
    }

    public List<Transaction> transactionByCard(Card card) {

        String sql=String.format("Select * from transaction where card_id in  (Select id from card where card_number = '" +card.getCardNumber()+ "')");

        List<Transaction>transactionList=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Transaction.class));
        if(transactionList.isEmpty()){
         return null;
        }else {
            return transactionList;
        }
    }

    public List<Transaction> getTransactionList() {
           String sql="select * from transaction order by created_date desc";
           List<Transaction>transactionList=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Transaction.class));
           return transactionList;
    }

    public List<Transaction> profileTransactionList() {

            String sql="Select * from transaction \n" +
                    "where card_id in (Select id from card\n" +
                    "where phone = '" + currentProfile.getPhone() + "');";
        List<Transaction>transactionList=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Transaction.class));
        if(transactionList.isEmpty()){
            return null;
        }
        return transactionList;
    }

    public void makePayment() {

        if (checkBalance() <= 1400) {
            System.out.println("Not enough money");
            return;
        }

        String query = String.format("Update card set balance = balance - 1400 where phone = '%s'", currentProfile.getPhone());

        int result = jdbcTemplate.update(query);

        if (result == 0) {
            System.out.println("ERROR");
            return;
        } else {
            System.out.println("SUCCESS");
        }
    }

   /* private double checkBalance() {
        try (Connection connection = DataBase.getConnection()) {
            Statement statement = connection.createStatement();

            String query = String.format("Select balance from card where phone = '%s'", ComponentContainer.currentProfile.getPhone());

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return 0;
    }*/
   private Double checkBalance(){
       String sql=String.format("Select * from card where phone = '%s'", ComponentContainer.currentProfile.getPhone());
       List<Card> cardList=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Card.class));
       if(cardList.isEmpty()){
           return null;
       }
       return cardList.get(0).getBalance();
   }
}


