package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.model.dto.InvoiceDtoExt;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.List;

@Repository
public class InvoiceTransactionDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<InvoiceDtoExt> getInvoiceDtoExt(int company_id, int category_id, InvoiceStatus invoiceStatus){

        findWithCompanyCategoryStatus(company_id,category_id,invoiceStatus);
        return null;
    }

    private String hqlInvoiceDtoExt(){
        String query = "select " +
                "inv.id " +
                ", inv.issueDate  " +
                ", inv.dueDate  " +
                ", prod.name " +
                ", inv.sum " +
                ", concat(cust.firstName,' ',cust.lastName) " +
                ", inv.status " +
                "from Invoice inv join inv.contract contr " +
                "left join contr.product prod " +
                "left join contr.customer cust " +
                "left join contr.company co "+
                "left join prod.category categ "+
                "where co.id =:co_id and categ.id=:categ_id and inv.status =:inv_status ";
        return query;
    }

    public List findWithCompanyCategoryStatus(int co_id, int categ_id, InvoiceStatus invoiceStatus){
        System.out.println(hqlInvoiceDtoExt());
        Query query= entityManager.createQuery(hqlInvoiceDtoExt());
        query.setParameter("co_id", co_id);
        query.setParameter("categ_id", categ_id);
        query.setParameter("inv_status", invoiceStatus);
        List list= query.getResultList();
        list.forEach(e-> System.out.println(e));
        return list;
    }

}
