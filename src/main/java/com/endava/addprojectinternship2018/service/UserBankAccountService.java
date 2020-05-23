package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.*;
import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.*;
import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserBankAccountService {

    @Autowired
    private UserUtil userUtil;

    private final BankAccountDao bankAccountDao;

    private final CompanyDao companyDao;
    private final CustomerDao customerDao;
    private final InvoiceDao invoiceDao;
    private final TransactionDao transactionDao;


    @Autowired
    public UserBankAccountService(BankAccountDao bankAccountDao, CompanyDao companyDao, CustomerDao customerDao, InvoiceDao invoiceDao, TransactionDao transactionDao) {
        this.bankAccountDao = bankAccountDao;
        this.companyDao = companyDao;
        this.customerDao = customerDao;
        this.invoiceDao = invoiceDao;
        this.transactionDao = transactionDao;
    }

    @Transactional
    public void save(UserBankAccountDto userBankAccountDto) {

        if (userUtil.getCurrentUser().getRole() == Role.COMPANY) {
            Company currentCompany = userUtil.getCurrentCompany();
            if (currentCompany.getCountNumber() == null && currentCompany.getAccessKey() == null) {
                currentCompany.setCountNumber(userBankAccountDto.getCountNumber());
                currentCompany.setAccessKey(userBankAccountDto.getAccessKey());
                companyDao.save(currentCompany);
            }
        } else {
            Customer currentCustomer = userUtil.getCurrentCustomer();
            if (currentCustomer.getCountNumber() == null && currentCustomer.getAccessKey() == null) {
                currentCustomer.setCountNumber(userBankAccountDto.getCountNumber());
                currentCustomer.setAccessKey(userBankAccountDto.getAccessKey());
                customerDao.save(currentCustomer);
            }
        }
    }

    @Transactional
    public void createAccount() {
        if (userUtil.getCurrentUser().getRole() == Role.COMPANY) {
            Company currentCompany = userUtil.getCurrentCompany();
            if (null == currentCompany.getBankAccount() || null == currentCompany.getBankAccount().getAccountNumber()) {
                BankAccount bankAccount = bankAccountDao.save(new BankAccount());
                currentCompany.setBankAccount(bankAccount);
                companyDao.save(currentCompany);
            }
        } else {
            Customer currentCustomer = userUtil.getCurrentCustomer();
            if (null == currentCustomer.getBankAccount() || null == currentCustomer.getBankAccount().getAccountNumber()) {
                BankAccount bankAccount = bankAccountDao.save(new BankAccount());
                currentCustomer.setBankAccount(bankAccount);
                customerDao.save(currentCustomer);
            }
        }
    }

    public double getBalance() {
        if (userUtil.getCurrentUser().getRole() == Role.COMPANY) {
            Company currentCompany = userUtil.getCurrentCompany();
            if (null != currentCompany.getBankAccount()) {
                return currentCompany.getBankAccount().getBalance();
            }
        } else {
            Customer currentCustomer = userUtil.getCurrentCustomer();
            if (null != currentCustomer.getBankAccount()) {
                return currentCustomer.getBankAccount().getBalance();
            }
        }
        return 0.0;
    }

    public void addMoney(double sum) {
        BankAccount bankAccount = getBankAccount();
        if (null != bankAccount) {
            Transaction transaction = new Transaction(bankAccount, bankAccount, bankAccount.getBalance(), sum, "Add funds");
            transactionDao.save(transaction);
            bankAccount.setBalance(transaction.getCurrentBalance());
            bankAccountDao.save(bankAccount);
        }
    }

    public StatementDto getStatement(StatementDateReqDto statementRequestDto) {
        BankAccount bankAccount = getBankAccount();
        List<Transaction> transactions = transactionDao.findAllByBankAccountPayer_AccountNumberAndDateBetweenOrderByIdAsc(
                bankAccount.getAccountNumber(),
                LocalDate.parse(statementRequestDto.getDate()),
                LocalDate.parse(statementRequestDto.getDateTo())
        );

        double balanceBefore;

        if (!transactions.isEmpty()) {
            balanceBefore = transactions.get(0).getBalanceBefore();
            List<TransactionRequestDto> transactionRequestDtoList = transactions.stream()
                    .map(a -> new TransactionRequestDto(a.getDate().toString(),
                            a.getBankAccountPayer().getAccountNumber(),
                            a.getBankAccountReceiver().getAccountNumber(),
                            a.getSum(),
                            a.getDescription()))
                    .collect(Collectors.toList());

            return createStatementDto(balanceBefore, transactionRequestDtoList);
        }
        return new StatementDto();
    }

    public void sendMoney(PaymentDto paymentDto) throws Exception {
        BankAccount bankAccount = getBankAccount();
        if (null != bankAccount) {
            double amount = paymentDto.getSum();
            BankAccount bankAccountReceiver =
                    bankAccountDao.findByAccountNumber(paymentDto.getCorrespondentCount()).orElseThrow(() -> new Exception("Nu such account"));
            Transaction transactionPayer = new Transaction(bankAccount,
                    bankAccountReceiver,
                    bankAccount.getBalance(),
                    -amount,
                    paymentDto.getDescription());
            transactionDao.save(transactionPayer);

            Transaction transactionReceiver = new Transaction(bankAccountReceiver,
                    bankAccount,
                    bankAccountReceiver.getBalance(),
                    amount,
                    paymentDto.getDescription());

            transactionDao.save(transactionPayer);
            transactionDao.save(transactionReceiver);

            bankAccount.reduceBalance(amount);
            bankAccountReceiver.increaseBalance(amount);

            bankAccountDao.save(bankAccount);
            bankAccountDao.save(bankAccountReceiver);
        }
    }

    public void sendMoney(List<PaymentDto> paymentDtoList) throws Exception {
        for (PaymentDto paymentDto : paymentDtoList) {
            sendMoney(paymentDto);
        }
    }

    private BankAccount getBankAccount() {
        if (userUtil.getCurrentUser().getRole() == Role.COMPANY) {
            Company currentCompany = userUtil.getCurrentCompany();
            if (null != currentCompany.getBankAccount()) {
                return currentCompany.getBankAccount();
            }
        } else {
            Customer currentCustomer = userUtil.getCurrentCustomer();
            if (null != currentCustomer.getBankAccount()) {
                return currentCustomer.getBankAccount();
            }
        }
        return null;
    }

    public StatementDto createStatementDto(double balanceBefore, List<TransactionRequestDto> transactionRequestDtoList) {

        boolean isCompany = (userUtil.getCurrentUser().getRole() == Role.COMPANY);

        List<TransactionDto> transactionDtoList = new LinkedList<>();
        double balanceAfter = balanceBefore;
        for (TransactionRequestDto dto : transactionRequestDtoList) {
            TransactionDto transactionDto = new TransactionDto();
            if (isCompany) {
                Optional<Customer> optionalCustomer = customerDao.findByBankAccountAccountNumber(dto.getCorrespondentCount());
                if (optionalCustomer.isPresent()) {
                    transactionDto.setPartnerName(optionalCustomer.get().getFullName());
                } else {
                    transactionDto.setPartnerName(companyDao.findByBankAccountAccountNumber(dto.getMainCount()).get().getName());
                }
            } else {
                Optional<Company> optionalCompany = companyDao.findByBankAccountAccountNumber(dto.getCorrespondentCount());
                if (optionalCompany.isPresent()) {
                    transactionDto.setPartnerName(optionalCompany.get().getName());
                } else {
                    transactionDto.setPartnerName(customerDao.findByBankAccountAccountNumber(dto.getMainCount()).get().getFullName());
                }
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String fullDescription;
            try {
                String[] str = dto.getDescription().split("=");
                int invoiceId = Integer.valueOf(str[1]);
                Invoice currentInvoice = invoiceDao.findById(invoiceId).get();
                StringBuilder sb = new StringBuilder();
                sb.append("Invoice period: ");
                sb.append(currentInvoice.getServiceStartDate().format(formatter));
                sb.append("-");
                sb.append(currentInvoice.getServiceEndDate().format(formatter));
                sb.append(". Service: ");
                sb.append(currentInvoice.getContract().getProduct().getName());
                fullDescription = sb.toString();
            } catch (Exception ex) {
                fullDescription = dto.getDescription();
            }
            balanceAfter += dto.getSum();
            transactionDto.setDate(dto.getDate());
            transactionDto.setDescription(fullDescription);
            transactionDto.setSum(dto.getSum());
            transactionDto.setCurrentBalance(balanceAfter);
            transactionDtoList.add(transactionDto);
        }

        StatementDto result = new StatementDto();
        result.setStartBalance(balanceBefore);
        result.setEndBalance(balanceAfter);
        result.setListOfTransactions(transactionDtoList);

        return result;
    }

}
