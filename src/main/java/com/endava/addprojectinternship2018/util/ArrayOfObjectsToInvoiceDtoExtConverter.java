package com.endava.addprojectinternship2018.util;

import com.endava.addprojectinternship2018.model.dto.InvoiceDtoExt;

import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ArrayOfObjectsToInvoiceDtoExtConverter implements Converter<Object[], InvoiceDtoExt> {
    /**
     *
     "inv.id " +
     ", inv.issueDate  " +
     ", inv.dueDate  " +
     ", prod.name " +
     ", inv.sum " +
     ", concat(cust.firstName,' ',cust.lastName) " +
     ", inv.status " +
     * @param objects
     * @return
     */

    @Override
    public InvoiceDtoExt convert(Object[] objects) {
        InvoiceDtoExt invoiceDtoExt = new InvoiceDtoExt();
        if (objects[0] != null ){invoiceDtoExt.setInvoiceId((int) objects[0]);}
        if (objects[1] != null){invoiceDtoExt.setIssueDate((LocalDate) objects[1]);}
        if (objects[2] != null){invoiceDtoExt.setDueDate((LocalDate) objects[2]);}
        if (objects[3] != null){invoiceDtoExt.setProductName((String) objects[3]);}
        if (objects[4] != null){invoiceDtoExt.setInvoiceSum((double) objects[4]);}
        if (objects[5] != null){invoiceDtoExt.setCustomerFullName((String) objects[5]);}
        if (objects[6] != null){invoiceDtoExt.setInvoiceStatus((InvoiceStatus) objects[6]);}

        return invoiceDtoExt;
    }
}
