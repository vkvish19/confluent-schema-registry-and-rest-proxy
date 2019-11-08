package com.github.vkvish19.avro.specific;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import com.github.vkvish19.Customer;

public class SpecificRecordExamples
{
    private static final String CUSTOMER_SPECIFIC_FILE_NAME = "customer-specific.avro";
    
    public static void main(String[] args)
    {
        //define schema
        Customer.Builder customerBuilder = Customer.newBuilder();
        customerBuilder.setAge(25);
        customerBuilder.setFirstName("John");
        customerBuilder.setLastName("Doe");
        customerBuilder.setHeight(170f);
        customerBuilder.setWeight(80.5f);
        customerBuilder.setAutomatedEmail(false);
        
        Customer customer = customerBuilder.build();
        System.out.println("customer = " + customer);
        
        //write to file
        final DatumWriter<Customer> datumWriter = new SpecificDatumWriter<>(Customer.class);
        try(DataFileWriter<Customer> dataFileWriter = new DataFileWriter<>(datumWriter))
        {
            dataFileWriter.create(customer.getSchema(), new File(CUSTOMER_SPECIFIC_FILE_NAME));
            dataFileWriter.append(customer);
            System.out.println("Successfully wrote to file : " + CUSTOMER_SPECIFIC_FILE_NAME);
        }
        catch(IOException e)
        {
            System.out.println("Couldn't write file...");
            e.printStackTrace();
        }
        
        //read from file
        final File file = new File(CUSTOMER_SPECIFIC_FILE_NAME);
        DatumReader<Customer> datumReader = new SpecificDatumReader<>(Customer.class);
        Customer customerRead;
        try(final DataFileReader<Customer> dataFileReader = new DataFileReader<>(file, datumReader))
        {
            while(dataFileReader.hasNext())
            {
                customerRead = dataFileReader.next();
                //interpret
                System.out.println(String.format("First Name : %s ## Height (in cms) : %s", customerRead.getFirstName(), Float.toString(customerRead.getHeight())));
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
