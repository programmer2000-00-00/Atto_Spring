package org.example.service;

import org.example.container.ComponentContainer;
import org.example.dto.Card;
import org.example.dto.Terminal;
import org.example.dto.Transaction;
import org.example.enums.TransactionType;
import org.example.repository.CardRepository;
import org.example.repository.TerminalRepository;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
public class TransactionService {
        @Autowired
        private TransactionRepository transactionRepository;
        @Autowired
        private TerminalRepository terminalRepository;
        @Autowired
        private CardRepository cardRepository;


    public void createTransaction(Integer cardId, Integer terminalId, Double amount, TransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setCardId(cardId);
        transaction.setTerminalId(terminalId);
        transaction.setAmount(amount);
        transaction.setTransactionType(type);
        transaction.setCreatedDate(LocalDateTime.now());

        transactionRepository.createTransaction(transaction);
    }


      public void todayTransactionList() {
        List<Transaction> transactionList =transactionRepository.getTodayTransactionList();

        if(transactionList.isEmpty()){
            System.out.println("transaction is not exists");
            return;
        }
        for (Transaction transaction : transactionList) {
            System.out.println(transaction);

        }
    }


    public void transactionByday(String day) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
        LocalDate date = LocalDate.parse(day, formatter);

        List<Transaction> transactionList1 =transactionRepository.transactionByDayList(date);

        if(transactionList1.isEmpty()){
            System.out.println("transaction is not exists");
            return;
        }
        for (Transaction transaction : transactionList1) {
            System.out.println(transaction);
        }

    }

    public void transactionBetweenDays(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
        LocalDate startD = LocalDate.parse(startDate, formatter);
        LocalDate endD = LocalDate.parse(endDate, formatter);


        List<Transaction> transactionList =transactionRepository.transactionBetweenDay(startD,endD);

        if(transactionList.isEmpty()){
            System.out.println("transaction is not exists");
            return;
        }
        for (Transaction transaction : transactionList) {
            System.out.println(transaction);
        }
    }

    public void totalBalance() {
        double balance=transactionRepository.showBalance();

        System.out.println("balance:"+balance);
    }

    public void transactionByTerminal(String code) {

        Terminal terminal=terminalRepository.getTerminalByCode(code);

        if(terminal==null){
            System.out.println("Terminal not found");
            return;
        }

      List<Transaction> transactionList=transactionRepository.transactionByTerminal(code);

        if(transactionList==null){
            System.out.println("transaction is not exists");return;
        }
        for (Transaction transaction : transactionList) {
            System.out.println(transaction);

        }
    }
    public void transactionByCard(String code) {
       Card card=cardRepository.getCardByNumber(code);

        if(card==null){
            System.out.println("card not found");
            return;
        }


        List<Transaction> transactionList=transactionRepository.transactionByCard(card);

        if(transactionList==null){
            System.out.println("transaction is not exists");return;
        }
        for (Transaction transaction : transactionList) {
            System.out.println(transaction);

        }
    }
    public void transactionList() {
        List<Transaction> transactionList = transactionRepository.getTransactionList();
        if (transactionList.isEmpty()){
            System.out.println("Transaction not exist");
            return;
        }

        for (Transaction transaction : transactionList) {
            System.out.println(transaction);
        }
    }

    public void profileTransactionList() {
        List<Transaction> transactionList = transactionRepository.profileTransactionList();
        if (transactionList.isEmpty()){
            System.out.println("Transaction not exist");
            return;
        }

        for (Transaction transaction : transactionList) {
            System.out.println(transaction);
        }
    }

    public void payment(String cardNumber, String terminalCode) {
        Card card = cardRepository.getCardByNumber(cardNumber);
        Terminal terminal = terminalRepository.getTerminalByCode(terminalCode);
        if(card==null || terminal==null){
            System.out.println("Card or Terminal not found");
            return;
        }

        if (card.getPhone() == null || !card.getPhone().equals(ComponentContainer.currentProfile.getPhone())) {
            System.out.println("Mazgi card not belongs to you.");
            return;
        }


        transactionRepository.makePayment();
        Transaction transaction = new Transaction();

        transaction.setCardId(card.getId());
        transaction.setAmount(1400D);
        transaction.setTerminalId(terminal.getId());
        transaction.setTransactionType(TransactionType.Payment);
        transaction.setCreatedDate(LocalDateTime.now());
        transactionRepository.createTransaction(transaction);

    }
}
