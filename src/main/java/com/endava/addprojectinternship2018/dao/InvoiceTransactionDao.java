package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.model.dto.InvoiceDtoExt;
import com.endava.addprojectinternship2018.util.ArrayOfObjectsToInvoiceDtoExtConverter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InvoiceTransactionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<InvoiceDtoExt> getInvoiceDtoExt(Integer company_id, Integer category_id, InvoiceStatus invoiceStatus) {

        List<Object[]> list = findByCompanyCategoryStatus(company_id, category_id, invoiceStatus);
        List<InvoiceDtoExt> result = new ArrayList<>();
        ArrayOfObjectsToInvoiceDtoExtConverter converter = new ArrayOfObjectsToInvoiceDtoExtConverter();
        for (Object[] li : list) {
            result.add(converter.convert(li));
        }

        return result;
    }

    private String hqlInvoiceDtoExtByAllParametersWtihBuilder(int co_id, int categ_id, InvoiceStatus invoiceStatus) {
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
                "left join contr.company co " +
                "left join prod.category categ " +
                "where co.id=:co_id ";

        StringBuilder builder = new StringBuilder(query);

        if (categ_id != 0) {
            builder.append(" and categ.id=:categ_id ");
        }
        if (invoiceStatus != null) {
            builder.append(" and inv.status=:inv_status ");
        }

        String result = builder.toString();

        return result;
    }

    public List<Object[]> findByCompanyCategoryStatus(Integer co_id, Integer categ_id, InvoiceStatus invoiceStatus) {

        Query query = entityManager.createQuery(hqlInvoiceDtoExtByAllParametersWtihBuilder(co_id, categ_id, invoiceStatus));
        if (categ_id != null) {
            query.setParameter("categ_id", categ_id);
        }
        if (categ_id != null) {
            query.setParameter("inv_status", invoiceStatus);
        }
        List<Object[]> list = query.getResultList();

        return list;
    }

    public String checkInvoiceByPeriod(String set_date, int contract_id, String status){
        String query  = "select inv.id " +
                "from Invoice inv " +
                "where set_date >= inv.issueDate " +
                "and set_date<=inv.dueDate " +
                "and inv.status=:status " +
                "and inv.contract_id=:contract_id ";
        return query;
    }

    public List<Invoice> getInvoiceByPeriodContractStatus(String date, int contract_id, String status){

        Query query = entityManager.createQuery(checkInvoiceByPeriod(date, contract_id, status));
        query.setParameter("set_date", date);
        query.setParameter("contract_id", contract_id);
        query.setParameter("status", status);

        List<Invoice> list = query.getResultList();

        return list;
    }


}

