package com.github.vkvish19.avro.evolution;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import com.github.vkvish19.CustomerV1;
import com.github.vkvish19.CustomerV2;

public class SchemaEvolutionExamples
{
    private static final String CUSTOMER_V1_AVRO = "customer-v1.avro";
    private static final String CUSTOMER_V2_AVRO = "customer-v2.avro";
    
    public static void main(String[] args)
    {
        //*********************************************************************************************************************//
        //****************************test backward compatible read************************************************************//
        //*********************************************************************************************************************//

        //deal with V1 of customer
        CustomerV1 customerV1 = CustomerV1.newBuilder()
                .setAge(24)
                .setFirstName("John")
                .setLastName("Delp")
                .setWeight(78.2f)
                .setHeight(171f)
                .setAutomatedEmail(false)
                .build();
        System.out.println("customerV1 = " + customerV1.toString());
        
        //write it to file
        final DatumWriter<CustomerV1> datumWriter = new SpecificDatumWriter<>(CustomerV1.class);
        try(final DataFileWriter<CustomerV1> dataFileWriter = new DataFileWriter<>(datumWriter))
        {
            dataFileWriter.create(customerV1.getSchema(), new File(CUSTOMER_V1_AVRO));
            dataFileWriter.append(customerV1);
            System.out.println("successfully wrote to file : " + CUSTOMER_V1_AVRO);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        //read it with v2 schema
        System.out.println("reading " + CUSTOMER_V1_AVRO + " with v2 schema...");
        final File file = new File(CUSTOMER_V1_AVRO);
        final DatumReader<CustomerV2> datumReaderv2 = new SpecificDatumReader<>(CustomerV2.class);
        try(final DataFileReader<CustomerV2> dataFileReaderV2 = new DataFileReader<>(file, datumReaderv2))
        {
            while(dataFileReaderV2.hasNext())
            {
                CustomerV2 customerV2 = dataFileReaderV2.next();
                System.out.println("customerV2 = " + customerV2.toString());
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    
        System.out.println("BACKWARD SCHEMA EVOLUTION SUCCESSFUL!!!");
        System.out.println("*********************************************************************************************************************");
        
        //*********************************************************************************************************************//
        //****************************test forward compatible read************************************************************//
        //*********************************************************************************************************************//
    
        //deal with V1 of customer
        CustomerV2 customerV2 = CustomerV2.newBuilder()
                .setAge(27)
                .setFirstName("Vishu")
                .setLastName("Kulkarni")
                .setWeight(80.2f)
                .setHeight(172f)
                .setPhoneNumber("1234567898")
                .setEmail("abc@xyz.com")
                .build();
        System.out.println("customerV2 = " + customerV2.toString());
    
        //write it to file
        final DatumWriter<CustomerV2> datumWriterV2 = new SpecificDatumWriter<>(CustomerV2.class);
        try(final DataFileWriter<CustomerV2> dataFileWriterV2 = new DataFileWriter<>(datumWriterV2))
        {
            dataFileWriterV2.create(customerV2.getSchema(), new File(CUSTOMER_V2_AVRO));
            dataFileWriterV2.append(customerV2);
            System.out.println("successfully wrote to file : " + CUSTOMER_V2_AVRO);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    
        //read it with v1 schema
        System.out.println("reading " + CUSTOMER_V2_AVRO + " with v1 schema...");
        final File file2 = new File(CUSTOMER_V2_AVRO);
        final DatumReader<CustomerV1> datumReaderv1 = new SpecificDatumReader<>(CustomerV1.class);
        try(final DataFileReader<CustomerV1> dataFileReaderV1 = new DataFileReader<>(file2, datumReaderv1))
        {
            while(dataFileReaderV1.hasNext())
            {
                CustomerV1 customerV11 = dataFileReaderV1.next();
                System.out.println("customerV2 = " + customerV11.toString());
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    
        System.out.println("FORWARD SCHEMA EVOLUTION SUCCESSFUL!!!");
    }
}
