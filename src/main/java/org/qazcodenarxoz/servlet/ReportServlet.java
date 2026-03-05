package org.qazcodenarxoz.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.qazcodenarxoz.config.AppContext;
import org.qazcodenarxoz.dto.PageResponse;
import org.qazcodenarxoz.dto.TransactionFilter;
import org.qazcodenarxoz.entity.TransactionReport;
import org.qazcodenarxoz.service.TransactionReportService;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/reports/transactions")
public class ReportServlet extends HttpServlet {

    private final ObjectMapper objectMapper;

    public ReportServlet() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = AppContext.createEntityManager();
        try {
            TransactionReportService service = new TransactionReportService();
            service.setEntityManager(em);

            TransactionFilter filter = parseFilter(req);
            PageResponse<TransactionReport> pageResponse = service.getReports(filter);

            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getWriter(), pageResponse);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    private TransactionFilter parseFilter(HttpServletRequest req) {
        TransactionFilter filter = new TransactionFilter();

        filter.setFrom(parseOffsetDateTime(req.getParameter("from")));
        filter.setTo(parseOffsetDateTime(req.getParameter("to")));
        filter.setMerchantCode(req.getParameter("merchantCode"));
        filter.setAgentCode(req.getParameter("agentCode"));
        filter.setProductCode(req.getParameter("productCode"));
        filter.setBuyerEmail(req.getParameter("buyerEmail"));
        filter.setSuccess(parseBoolean(req.getParameter("success")));
        filter.setStatus(parseList(req.getParameter("status")));
        filter.setResultCode(parseList(req.getParameter("resultCode")));
        filter.setMinAmount(parseBigDecimal(req.getParameter("minAmount")));
        filter.setMaxAmount(parseBigDecimal(req.getParameter("maxAmount")));
        filter.setCurrency(req.getParameter("currency"));
        filter.setMinCommission(parseBigDecimal(req.getParameter("minCommission")));
        filter.setMaxCommission(parseBigDecimal(req.getParameter("maxCommission")));
        filter.setHasFeeRule(parseBoolean(req.getParameter("hasFeeRule")));
        filter.setBuyerExternalId(req.getParameter("buyerExternalId"));

        int page = parseInt(req.getParameter("page"), 0);
        int size = parseInt(req.getParameter("size"), 20);
        if (size > 200) size = 200;
        filter.setPage(page);
        filter.setSize(size);

        String sort = req.getParameter("sort");
        if (sort != null && !sort.isEmpty()) {
            filter.setSort(sort);
        }

        return filter;
    }

    private OffsetDateTime parseOffsetDateTime(String value) {
        if (value == null) return null;
        try {
            return OffsetDateTime.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private Boolean parseBoolean(String value) {
        if (value == null) return null;
        return Boolean.valueOf(value);
    }

    private List<String> parseList(String value) {
        if (value == null) return null;
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null) return null;
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
