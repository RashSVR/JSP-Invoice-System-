package com.is.invoicingsystem.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface InvoiceService {
    void getItems(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void getInvoices(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void addInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void editInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void deleteInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void printInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
