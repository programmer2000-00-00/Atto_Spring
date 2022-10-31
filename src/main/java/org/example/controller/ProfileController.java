package org.example.controller;

import org.example.container.ComponentContainer;
import org.example.dto.Profile;
import org.example.service.CardService;
import org.example.service.TransactionService;
import org.example.util.ScannerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Scanner;
@Controller
public class ProfileController {
    @Autowired
    private CardService cardService ;
    @Autowired
    private TransactionService transactionService;

    public void start() {
        boolean b = true;

        while (b) {
            menu();
            int operation = ScannerUtil.getAction();
            switch (operation) {
                case 1:
                    addCard();
                    break;
                case 2:
                    cardList();
                    break;
                case 3:
                    changeCardStatus();
                    break;
                case 4:
                    deleteCard();
                    break;
                case 5:
                    refill();
                    break;
                case 6:
                    transactionList();
                    break;
                case 7:
                    payment();
                    break;
                case 0:
                    b = false;
                    break;
                default:
                    b = false;
                    System.out.println("Wrong operation number");
            }
        }
    }


    public void menu() {
        System.out.println("1. Add Card");
        System.out.println("2. Card List ");
        System.out.println("3. Card Change Status");
        System.out.println("4. Delete Card");
        System.out.println("5. ReFill ");
        System.out.println("6. Transaction List");
        System.out.println("7. Make Payment");
        System.out.println("0. Log out");
    }

    /**
     * Card
     */

    private void addCard() {
        System.out.print("Enter card number: ");

        Scanner scanner = new Scanner(System.in);
        String cardNumber = scanner.nextLine();
        Profile profile = ComponentContainer.currentProfile;
        cardService.addCardToProfile(profile.getPhone(), cardNumber);
    }

    private void cardList( ) {
        System.out.print("--- Card List ---");
        Profile profile = ComponentContainer.currentProfile;
        cardService.profileCardList(profile.getPhone());
    }

    private void changeCardStatus( ) {
        System.out.print("Enter card number: ");

        Scanner scanner = new Scanner(System.in);
        String cardNumber = scanner.nextLine();
        Profile profile = ComponentContainer.currentProfile;
        cardService.userChangeCardStatus(profile.getPhone(), cardNumber);
    }

    private void deleteCard( ) {
        System.out.print("Enter card number: ");
        Scanner scanner = new Scanner(System.in);
        String cardNumber = scanner.nextLine();

        Profile profile = ComponentContainer.currentProfile;
        cardService.userDeleteCard(profile.getPhone(), cardNumber);
    }

    private void refill( ) {
        System.out.print("Enter card number: ");
        Scanner scanner = new Scanner(System.in);
        String cardNumber = scanner.nextLine();

        System.out.print("Enter amount: ");
        Double amount = scanner.nextDouble();
        Profile profile = ComponentContainer.currentProfile;
        cardService.userRefillCard(profile.getPhone(), cardNumber, amount);
    }


    /**
     * Transaction
     */
    private void transactionList() {
        System.out.println("\t\tTransaction List\t\t");
        transactionService.profileTransactionList();
    }

    private void payment() {
        System.out.print("Enter card number: ");
        Scanner scanner = new Scanner(System.in);
        String cardNumber = scanner.nextLine();

        System.out.print("Enter terminal number: ");
        String terminalCode = scanner.nextLine();

        transactionService.payment(cardNumber, terminalCode);

    }

}
