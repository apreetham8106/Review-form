package com.example.test.generator;
import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;


import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.util.Date;
import org.hibernate.HibernateException;

public class FileIDGenerator implements IdentifierGenerator{
		
		 private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
		    private static final AtomicInteger SEQUENCE = new AtomicInteger(1);
		    
		    @Override
		    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		        String prefix = "File01-";
		        String currentDate = DATE_FORMAT.format(new Date());
		        String numericPart = generateNumericPart();
		        return prefix + currentDate + "-" + numericPart;
		    }

		    private synchronized String  generateNumericPart() {
		        // Generate a 4-digit sequence number with leading zeros
		        int sequenceNumber = SEQUENCE.getAndIncrement();
		        return String.format("%04d", sequenceNumber);
		    }
	

}
