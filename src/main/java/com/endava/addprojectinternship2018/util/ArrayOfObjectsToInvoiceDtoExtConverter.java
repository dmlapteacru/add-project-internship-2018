package com.endava.addprojectinternship2018.util;

import com.endava.addprojectinternship2018.model.dto.InvoiceDtoExt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArrayOfObjectsToInvoiceDtoExtConverter implements Converter<Object[], InvoiceDtoExt> {


    @Override
    public InvoiceDtoExt convert(Object[] objects) {
        InvoiceDtoExt invoiceDtoExt = new InvoiceDtoExt();

        for (Object obj : objects
             ) {
            if (obj != null){

            }
        }
        return null;
    }
}
