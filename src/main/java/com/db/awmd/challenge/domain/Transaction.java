package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

// Classe della transazione di denaro tra accounts
@Data
public class Transaction {
	
	@NotNull
	@Min(value = 0, message = "Amount of transaction must be positive.")
	BigDecimal amount;
	@NotNull(message = "The account can't be null")
	@NotEmpty
	String accountFrom;
	@NotNull(message = "The account can't be null")
	@NotEmpty
	String accountTo;
	
	@JsonCreator
	public Transaction(@JsonProperty("amount") BigDecimal amount,
	    @JsonProperty("accountFrom") String accountFrom,
	    @JsonProperty("accountTo") String accountTo) {
	    this.amount = amount;
	    this.accountFrom = accountFrom;
	    this.accountTo = accountTo;
	}
	  
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}	
	public String getAccountFrom() {
		return accountFrom;
	}	public void setAccountFrom(String account) {
		this.accountFrom = account;
	}	
	public String getAccountTo() {
		return accountTo;
	}
	public void setAccountTo(String account) {
		this.accountTo = account;
	}


	
}
