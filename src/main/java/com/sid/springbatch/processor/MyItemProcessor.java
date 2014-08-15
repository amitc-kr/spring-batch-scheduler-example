package com.sid.springbatch.processor;

import com.sid.springbatch.model.Person;
import com.sid.springbatch.model.Account;
import org.springframework.batch.item.ItemProcessor;

public class MyItemProcessor implements ItemProcessor<Account, Person> {
    @Override
    public Person process(final Account account) throws Exception {
        Person person = new Person();
        person.setFirstName(account.getLastName());
        person.setLastName(account.getFirstName());        
        return person;
    }
}
