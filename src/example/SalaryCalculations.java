package example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SalaryCalculations {


    public static File createFile(String path) throws Exception {
        File file = new File(path);
        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());
        } else {
            System.out.println("File already exists.");
        }
        return file;
    }

    public static void createFinancialAccountsFile() throws Exception {
        List<PaymentDetails> paymentDetails = new ArrayList();

        paymentDetails.add(createPayment("debtor", "1.10.100.1", 1000));
        paymentDetails.add(createPayment("creditor", "1.10.100.2", 700));
        paymentDetails.add(createPayment("creditor", "1.10.100.3", 300));
        FileWriter myWriter = new FileWriter(createFile("D:\\project\\javaFirst\\folder\\paymentFile.txt"));
        for (PaymentDetails info : paymentDetails) {
            myWriter.write(info.getDebtorOrCreditor() + "\t" + info.getDepositNumber() + "\t" + info.getAmount() + "\n");
        }
        myWriter.close();
    }

    public static void createDebtorFile(PaymentDetails info, boolean valid) throws Exception {
        PaymentDetails debtor = info;
        String url = "";
        if (valid) {
            url = "D:\\project\\javaFirst\\folder\\debtorFileAfterPayment.txt";
        } else {
            url = "D:\\project\\javaFirst\\folder\\debtorFile.txt";
        }
        FileWriter myWriter = new FileWriter(createFile(url));
        myWriter.write(debtor.getDebtorOrCreditor() + "\t" + debtor.getDepositNumber() + "\t" + debtor.getAmount() + "\n");
        myWriter.close();
    }

    public static void createTransactionFile(List info) throws Exception {
        int i = 0;
        List<PaymentDetails> creditors = info;
        FileWriter myWriter = new FileWriter(createFile("D:\\project\\javaFirst\\folder\\transactionFile.txt"));
        for (PaymentDetails creditor : creditors) {
            myWriter.write(i + "\t" + creditor.getDepositNumber() + "\t" + creditor.getAmount() + "\n");
            i++;
        }
        myWriter.close();
    }

    public static void paySalaries() throws Exception {
        Scanner reader = new Scanner(createFile("D:\\project\\javaFirst\\folder\\paymentFile.txt"));
        List<PaymentDetails> list = changeStringToObj(reader);
        PaymentDetails debtor = null;
        List<PaymentDetails> creditor = new ArrayList();
        int creditorAmount = 0;

        for (PaymentDetails info : list) {
            if (info.getDebtorOrCreditor().equals("debtor")) {
                debtor = info;
            } else {
                creditor.add(createPayment(info.getDebtorOrCreditor(), info.getDepositNumber(), info.getAmount()));
                creditorAmount += info.getAmount();
            }
        }

        if (debtor.getAmount() >= creditorAmount) {

            PaymentDetails info = createPayment(debtor.getDebtorOrCreditor(), debtor.getDepositNumber(), debtor.getAmount() - creditorAmount);
            createDebtorFile(info, true);
            createTransactionFile(creditor);

        } else {
            System.out.println("پول نیست...");
        }

        reader.close();

    }

    public static PaymentDetails createPayment(String debtorOrCreditor, String depositNumber, Integer amount) {
        PaymentDetails Payment = new PaymentDetails();
        Payment.setAmount(amount);
        Payment.setDepositNumber(depositNumber);
        Payment.setDebtorOrCreditor(debtorOrCreditor);

        return Payment;
    }

    public static List changeStringToObj(Scanner reader) {
        List<PaymentDetails> list = new ArrayList();
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            String[] arrOfStr = data.split("\t", 3);
            list.add(createPayment(arrOfStr[0], arrOfStr[1], Integer.parseInt(arrOfStr[2])));
        }
        System.out.println(list);
        return list;
    }

    public static void createFirstDebtorFile() throws Exception {
        Scanner reader = new Scanner(createFile("D:\\project\\javaFirst\\folder\\paymentFile.txt"));
        List<PaymentDetails> list = changeStringToObj(reader);
        for (PaymentDetails info : list) {
            if (info.getDebtorOrCreditor().equals("debtor")) {
                createDebtorFile(info, false);
                reader.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        createFinancialAccountsFile();
        createFirstDebtorFile();
        paySalaries();
    }

}