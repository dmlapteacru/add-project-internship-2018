package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.model.dto.InvoiceDtoExt;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
public class InvoiceTransactionDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<InvoiceDtoExt> getInvoiceDtoExt(int company_id, int category_id, InvoiceStatus invoiceStatus){

        return null;
    }

    private String hqlInvoiceDtoExt(){
        String query = "select " +
                "inv.id " +
                ", inv.issue_date  " +
                ", inv.due_date  " +
                ", prod.name " +
                ", inv.sum " +
                ", concat(cust.first_name,' ',cust.last_name) " +
                ", inv.status " +
                "from Invoice inv join inv.contract contr " +
                "left join contr.product prod " +
                "left join contr.customer cust " +
                "left join contr.company co"+
                "left join prod.category categ"+
                "where co.id =:co_id and categ.id=:categ_id and inv.status =:inv_status ";
        return query;
    }

}
