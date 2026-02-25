package org.qazcodenarxoz.filter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class StatusExposingResponseWrapper extends HttpServletResponseWrapper {
    private int httpStatus = SC_OK;

    public StatusExposingResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void setStatus(int sc) {
        this.httpStatus = sc;
        super.setStatus(sc);
    }

    @Override
    public int getStatus() {
        return httpStatus;
    }
}
