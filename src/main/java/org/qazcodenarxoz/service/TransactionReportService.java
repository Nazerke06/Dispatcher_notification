package org.qazcodenarxoz.service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import org.qazcodenarxoz.dto.PageResponse;
import org.qazcodenarxoz.dto.TransactionFilter;
import org.qazcodenarxoz.entity.TransactionReport;

import java.util.ArrayList;
import java.util.List;

public class TransactionReportService {

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public PageResponse<TransactionReport> getReports(TransactionFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransactionReport> cq = cb.createQuery(TransactionReport.class);
        Root<TransactionReport> root = cq.from(TransactionReport.class);

        List<Predicate> predicates = buildPredicates(filter, cb, root);
        cq.where(predicates.toArray(new Predicate[0]));

        applySorting(filter, cb, cq, root);

        TypedQuery<TransactionReport> query = entityManager.createQuery(cq);
        query.setFirstResult(filter.getPage() * filter.getSize());
        query.setMaxResults(filter.getSize());

        List<TransactionReport> content = query.getResultList();

        // count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<TransactionReport> countRoot = countQuery.from(TransactionReport.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(buildPredicates(filter, cb, countRoot).toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageResponse<>(
                filter.getPage(),
                filter.getSize(),
                total,
                content
        );
    }

    private void applySorting(TransactionFilter filter, CriteriaBuilder cb,
                              CriteriaQuery<TransactionReport> cq, Root<TransactionReport> root) {
        if (filter.getSort() != null && !filter.getSort().isEmpty()) {
            String[] parts = filter.getSort().split(",");
            String sortField = parts[0];
            boolean ascending = parts.length > 1 && "asc".equalsIgnoreCase(parts[1]);
            Path<Object> sortPath = root.get(sortField);
            if (ascending) {
                cq.orderBy(cb.asc(sortPath));
            } else {
                cq.orderBy(cb.desc(sortPath));
            }
        } else {
            cq.orderBy(cb.desc(root.get("createdAt")));
        }
    }

    private List<Predicate> buildPredicates(TransactionFilter filter, CriteriaBuilder cb, Root<?> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getFrom()));
        }
        if (filter.getTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getTo()));
        }
        if (isNotBlank(filter.getMerchantCode())) {
            predicates.add(cb.equal(root.get("merchantCode"), filter.getMerchantCode()));
        }
        if (isNotBlank(filter.getAgentCode())) {
            predicates.add(cb.equal(root.get("agentCode"), filter.getAgentCode()));
        }
        if (isNotBlank(filter.getProductCode())) {
            predicates.add(cb.equal(root.get("productCode"), filter.getProductCode()));
        }
        if (isNotBlank(filter.getBuyerEmail())) {
            predicates.add(cb.equal(root.get("buyerEmail"), filter.getBuyerEmail()));
        }
        if (filter.getSuccess() != null) {
            predicates.add(cb.equal(root.get("success"), filter.getSuccess()));
        }
        if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
            predicates.add(root.get("status").in(filter.getStatus()));
        }
        if (filter.getResultCode() != null && !filter.getResultCode().isEmpty()) {
            predicates.add(root.get("resultCode").in(filter.getResultCode()));
        }
        if (filter.getMinAmount() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), filter.getMinAmount()));
        }
        if (filter.getMaxAmount() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("amount"), filter.getMaxAmount()));
        }
        if (isNotBlank(filter.getCurrency())) {
            predicates.add(cb.equal(root.get("currency"), filter.getCurrency()));
        }
        if (filter.getMinCommission() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("commissionAmount"), filter.getMinCommission()));
        }
        if (filter.getMaxCommission() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("commissionAmount"), filter.getMaxCommission()));
        }
        if (filter.getHasFeeRule() != null) {
            if (filter.getHasFeeRule()) {
                predicates.add(cb.isNotNull(root.get("commissionAmount")));
            } else {
                predicates.add(cb.isNull(root.get("commissionAmount")));
            }
        }

        return predicates;
    }

    private boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}