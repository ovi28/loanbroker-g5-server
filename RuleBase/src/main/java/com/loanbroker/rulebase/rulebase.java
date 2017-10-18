/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loanbroker.rulebase;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Rumi
 */
@WebService(serviceName = "rulebase")
public class rulebase {

  
    /**
     * Gets list of banks that can work with the given credit score
     */
    @WebMethod(operationName = "GetBanks")
    public java.util.ArrayList<String> GetBanks(@WebParam(name = "CreditScore") Integer CreditScore) {
        ArrayList<String> banksInJson = new ArrayList<String>();
        
        // check if credit score is real
        if (CreditScore > 850 || CreditScore <= 0){
            return banksInJson;
        }
        //make banks
        ArrayList<Bank> listOfBanks = new ArrayList<Bank>();
        listOfBanks.add(new Bank("cphbusiness.bankJSON", "keyBankJSON",550));
        listOfBanks.add(new Bank("cphbusiness.bankXML", "keyBankXML",650));
        listOfBanks.add(new Bank("RabbitBankJson", "RabbitBankJsonKey", 0));
        listOfBanks.add(new Bank("SoapBankXML", "SoapBankXMLKey", 350));
        
        List<Bank> ruleBaseBanks = findScoreForBank(CreditScore, listOfBanks);
        if(!ruleBaseBanks.isEmpty())
            banksInJson = banksToJson(ruleBaseBanks);
        
        return banksInJson;
    }
    
    private static List<Bank> findScoreForBank(Integer creditScore, List<Bank> banks ){
        List<Bank> listOfBanks = new ArrayList<Bank>();
        for (Bank bank : banks) {
            if(bank.getMinScore() <= creditScore){
                listOfBanks.add(bank);
            }
        }
        return listOfBanks;
    }

    //populate Bank objects
    private static ArrayList<String> banksToJson(List<Bank> banks){
        Gson gson = new Gson();
        ArrayList<String> listOfBanksInJson = new ArrayList<String>();
        for (Bank bank : banks) {
            listOfBanksInJson.add(gson.toJson(new ExistingBank(bank.getName(), bank.getRoutingKey())));
        }
        
        return listOfBanksInJson;
    }
    
}