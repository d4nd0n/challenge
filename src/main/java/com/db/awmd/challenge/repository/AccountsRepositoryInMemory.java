package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transaction;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.NotAllowedTransactionException;
import com.db.awmd.challenge.service.EmailNotificationService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }
  
  // Istanza del servizio notifiche
  EmailNotificationService emailNotificationService = new EmailNotificationService();
  
  // Operazione di trasferimento sulla hashmap
  @Override
  public Transaction transferMoney(Transaction transaction) throws NotAllowedTransactionException{ 
	  
	try {
		if(transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new NotAllowedTransactionException (
					"Amount of transaction must be positive.");
		}	
		if(accounts.get(transaction.getAccountFrom()).getBalance().subtract(transaction.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
			throw new NotAllowedTransactionException (
					"Account " + transaction.getAccountFrom() + " does not have enough money.");
		} 
	} catch (NullPointerException msg) {
		throw new NotAllowedTransactionException(msg.getMessage());
	}	
	  	
	
	Account from = new Account(transaction.getAccountFrom());
	from.setBalance(accounts.get(from.getAccountId()).getBalance().subtract(transaction.getAmount()));
	
	Account to = new Account(transaction.getAccountTo());
	to.setBalance(accounts.get(to.getAccountId()).getBalance().add(transaction.getAmount()));
	
	accounts.replace(transaction.getAccountFrom(), from);
	accounts.replace(transaction.getAccountTo(), to);
	
	emailNotificationService.notifyAboutTransfer(accounts.get(transaction.getAccountFrom()),
			"Money transferred to " + transaction.getAccountTo() + " in the amount of " + transaction.getAmount().toString());
	emailNotificationService.notifyAboutTransfer(accounts.get(transaction.getAccountTo()),
			"Money transferred from " + transaction.getAccountFrom() + " in the amount of " + transaction.getAmount().toString());

	return transaction;	  
  }

}
