package com.github.vkvish19.avro.generic;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

public class GenericRecordExamples
{
    private static final String CUSTOMER_GENERIC_FILE_NAME = "customer-generic.avro";
    public static void main(String[] args)
    {
        //step 0: define schema
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse("{\n" +
                "\t\"type\": \"record\",\n" +
                "\t\"namespace\": \"com.github.vkvish19\",\n" +
                "\t\"name\": \"Customer\",\n" +
                "\t\"doc\": \"Avro Schema for Customer\",\n" +
                "\t\"fields\": [\n" +
                "\t\t{\"name\": \"first_name\", \"type\": \"string\", \"doc\": \"First Name of the customer\"},\n" +
                "\t\t{\"name\": \"last_name\", \"type\": \"string\", \"doc\": \"Last Name of the customer\"},\n" +
                "\t\t{\"name\": \"age\", \"type\": \"int\", \"doc\": \"Age of the customer\"},\n" +
                "\t\t{\"name\": \"height\", \"type\": \"float\", \"doc\": \"Height in cms of the customer\"},\n" +
                "\t\t{\"name\": \"weight\", \"type\": \"float\", \"doc\": \"Weight in kgs of the customer\"},\n" +
                "\t\t{\"name\": \"automated_email\", \"type\": \"boolean\", \"default\": true, \"doc\": \"true if the customer wants marketing emails\"}\n" +
                "\t]\n" +
                "}");
        
        
        //step 1: create a generic record
        GenericRecordBuilder customerBuilder = new GenericRecordBuilder(schema);
        customerBuilder.set("first_name", "John");
        customerBuilder.set("last_name", "Doe");
        customerBuilder.set("age", 25);
        customerBuilder.set("height", 170f);
        customerBuilder.set("weight", 80.5f);
        customerBuilder.set("automated_email", false);
        GenericData.Record customer = customerBuilder.build();
        System.out.println("customer = " + customer);
    
        GenericRecordBuilder customerBuilderWithDefault = new GenericRecordBuilder(schema);
        customerBuilderWithDefault.set("first_name", "Gary");
        customerBuilderWithDefault.set("last_name", "Doe");
        customerBuilderWithDefault.set("age", 27);
        customerBuilderWithDefault.set("height", 171f);
        customerBuilderWithDefault.set("weight", 74.1f);
        GenericData.Record customerWithDefault = customerBuilderWithDefault.build();
        System.out.println("customerWithDefault = " + customerWithDefault);
    
        //step 2: write that generic record to a file
        final DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        try(DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter))
        {
            dataFileWriter.create(customer.getSchema(), new File(CUSTOMER_GENERIC_FILE_NAME));
            dataFileWriter.append(customer);
            dataFileWriter.append(customerWithDefault);
            System.out.println("Written " + CUSTOMER_GENERIC_FILE_NAME);
        }
        catch(IOException e)
        {
            System.out.println("Couldn't write file...");
            e.printStackTrace();
        }
        
        //step 3: read a generic record from a file
        final File file = new File(CUSTOMER_GENERIC_FILE_NAME);
        final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
        GenericRecord customerRead;
        try(DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader))
        {
            while(dataFileReader.hasNext())
            {
                customerRead = dataFileReader.next();
                //step 4: interpret the record
//                System.out.println("Successfully read the avro file : " + CUSTOMER_GENERIC_FILE_NAME);
                System.out.println(customerRead.toString());
    
                //get the data from the generic record
                System.out.println(String.format("First Name : %s ## Age : %s", customerRead.get("first_name"), customerRead.get("age")));
    
                //read a non existent field
                System.out.println("Non existent field : " + customerRead.get("not_here"));
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
