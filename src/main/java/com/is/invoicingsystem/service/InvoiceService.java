package com.is.invoicingsystem.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.is.invoicingsystem.dto.InvoiceItemObject;
import com.is.invoicingsystem.dto.InvoiceItemsResponse;
import com.is.invoicingsystem.model.Invoice;
import com.is.invoicingsystem.model.InvoiceItem;
import com.is.invoicingsystem.model.Item;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface InvoiceService {
    void getItems(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void getInvoices(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void addInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void editInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void deleteInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void printInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
