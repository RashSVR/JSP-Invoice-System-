package com.is.invoicingsystem.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.is.invoicingsystem.dao.InvoiceDao;
import com.is.invoicingsystem.dao.InvoiceItemDao;
import com.is.invoicingsystem.dao.ItemDao;
import com.is.invoicingsystem.dto.InvoiceItemObject;
import com.is.invoicingsystem.dto.InvoiceItemsResponse;
import com.is.invoicingsystem.dto.PrintInvoiceObject;
import com.is.invoicingsystem.dto.PrintInvoiceResponse;
import com.is.invoicingsystem.model.Invoice;
import com.is.invoicingsystem.model.InvoiceItem;
import com.is.invoicingsystem.model.Item;
import com.is.invoicingsystem.service.InvoiceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class InvoiceServiceImpl implements InvoiceService {

    private InvoiceDao invoiceDao = new InvoiceDao();
    private ItemDao itemDao = new ItemDao();
    private ObjectMapper objectMapper = new ObjectMapper();
    private InvoiceItemDao invoiceItemDao = new InvoiceItemDao();
    private Gson gson = new Gson();


    public void getItems(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        Long id = 0L;

        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                id = Long.parseLong(idParam);
            }
            if (id != 0) {
                Invoice invoice = invoiceDao.getInvoiceById(id);
                if (invoice != null) {
                    InvoiceItemsResponse invoicesItemsResponse = new InvoiceItemsResponse();
                    invoicesItemsResponse.setInvoice(invoice);
                    List<InvoiceItemObject> invoiceItemObjects = new ArrayList<>();
                    invoiceItemDao.getInvoiceById(invoice).forEach(e->{
                        InvoiceItemObject invoiceItemObject = new InvoiceItemObject();
                        invoiceItemObject.setItem(e.getItem());
                        invoiceItemObject.setQuantity(e.getQuantity());
                        invoiceItemObjects.add(invoiceItemObject);
                    });
                    invoicesItemsResponse.setInvoiceItems(invoiceItemObjects);
                    String itemJson = gson.toJson(invoicesItemsResponse);
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().print(itemJson);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item with ID " + id + " not found.");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid 'id' parameter: must be a valid number.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
            e.printStackTrace();
        }
    }

    public void getInvoices(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Invoice> invoices = new ArrayList<>();
        String idParam = request.getParameter("id");
        Long id = 0L;

        try {
            // Validate id parameter
            if (idParam != null && !idParam.trim().isEmpty()) {
                id = Long.parseLong(idParam);
            }

            // Fetch item by id if present
            if (id != 0) {
                Invoice invoice = invoiceDao.getInvoiceById(id);
                if (invoice != null) {
                    invoices.add(invoice);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item with ID " + id + " not found.");
                    return;
                }
            } else {
                invoices = invoiceDao.getAllInvoices();
            }

            // Return the items as JSON
            String itemJson = gson.toJson(invoices);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(itemJson);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid 'id' parameter: must be a valid number.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
            e.printStackTrace();
        }
    }

    public void addInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        double total = Double.parseDouble(request.getParameter("total"));
        List<Map<String, Long>> selectedItems = objectMapper.readValue(
                request.getParameter("selectedItems"),
                new TypeReference<List<Map<String, Long>>>() {});
        Map<Long, Long> resultMap = selectedItems.stream()
                .collect(Collectors.toMap(obj -> obj.get("id"), obj -> obj.get("quantity")));
        Invoice invoice = new Invoice();
        invoice.setDate(new Date()); // Set LocalDate
        invoice.setTotal(BigDecimal.valueOf(total));
        invoiceDao.saveInvoice(invoice);
        String invoiceJson = gson.toJson(invoice);
        resultMap.forEach((k,v)->{
            Item item = itemDao.getItemById(k);
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoice(invoice);
            invoiceItem.setItem(item);
            invoiceItem.setQuantity(v);
            invoiceItemDao.saveInvoiceItame(invoiceItem);
        });
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write(invoiceJson);
    }

    public void editInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        double total = Double.parseDouble(request.getParameter("total"));

        Invoice invoice = invoiceDao.getInvoiceById(id);
        invoice.setTotal(BigDecimal.valueOf(total));
        invoiceDao.saveInvoice(invoice);
        invoiceItemDao.deleteByInvoiceId(invoice);

        List<Map<String, Long>> selectedItems = objectMapper.readValue(
                request.getParameter("selectedItems"),
                new TypeReference<List<Map<String, Long>>>() {});
        Map<Long, Long> resultMap = selectedItems.stream()
                .collect(Collectors.toMap(obj -> obj.get("id"), obj -> obj.get("quantity")));

        resultMap.forEach((k,v)->{
            Item item = itemDao.getItemById(k);
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoice(invoice);
            invoiceItem.setItem(item);
            invoiceItem.setQuantity(v);
            invoiceItemDao.saveInvoiceItame(invoiceItem);
        });
        String invoiceJson = gson.toJson(invoice);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write(invoiceJson);
    }

    public void deleteInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        invoiceDao.deleteInvoice(id);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void printInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String idParam = request.getParameter("id");
        Long id = 0L;

        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                id = Long.parseLong(idParam);
            }
            if (id != 0) {
                Invoice invoice = invoiceDao.getInvoiceById(id);
                if (invoice != null) {
                    List<PrintInvoiceObject> PrintInvoiceObject = new ArrayList<>();

                    invoiceItemDao.getInvoiceById(invoice).forEach(e->{
                        PrintInvoiceObject printInvoiceObject = new PrintInvoiceObject();
                        printInvoiceObject.setItemName(e.getItem().getName());
                        printInvoiceObject.setItemQuantity(e.getQuantity().toString());
                        printInvoiceObject.setItemPricePerUnit(e.getItem().getPrice().toString());
                        printInvoiceObject.setItemTotal( (String.valueOf(((e.getQuantity()) * (e.getItem().getPrice().doubleValue()))))  );
                        PrintInvoiceObject.add(printInvoiceObject);
                    });
                   HashMap<String, Object> invoicePara = new HashMap<>();
                    invoicePara.put("invoiceDate", invoice.getDate().toString() );
                    invoicePara.put("invoiceId", invoice.getId().toString() );
                    invoicePara.put("invoiceTotal", invoice.getTotal().toString() );

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    InputStream stream = InvoiceServiceImpl.class.getClassLoader().getResourceAsStream("report/invoiceReport.jrxml");

                    JasperReport jasperReport = JasperCompileManager.compileReport(stream);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, invoicePara, new JRBeanCollectionDataSource(PrintInvoiceObject));

                    JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                    outputStream.close();
                    PrintInvoiceResponse printInvoiceResponse = new PrintInvoiceResponse();
                    printInvoiceResponse.setInvoice(Base64.getEncoder().encodeToString(outputStream.toByteArray()));

                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(gson.toJson(printInvoiceResponse));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item with ID " + id + " not found.");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid 'id' parameter: must be a valid number.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
            e.printStackTrace();
        }
}
}
