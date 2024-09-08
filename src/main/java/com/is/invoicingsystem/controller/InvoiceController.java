package com.is.invoicingsystem.controller;
import com.is.invoicingsystem.dao.InvoiceDao;
import com.is.invoicingsystem.dao.ItemDao;
import com.is.invoicingsystem.model.Invoice;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@WebServlet("/invoices")
public class InvoiceController extends HttpServlet {
    private InvoiceDao invoiceDao;
    private ItemDao itemDao;

    public void init() {
        invoiceDao = new InvoiceDao();
        itemDao = new ItemDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("invoices", invoiceDao.getAllInvoices());
        request.setAttribute("items", itemDao.getAllItems());
        RequestDispatcher dispatcher = request.getRequestDispatcher("pages/invoices.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("invoices");
            return;
        }

        switch (action) {
            case "add":
                addInvoice(request, response);
                break;
            case "edit":
                editInvoice(request, response);
                break;
            case "delete":
                deleteInvoice(request, response);
                break;
            default:
                response.sendRedirect("invoices");
                break;
        }
    }

    private void addInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        double total = Double.parseDouble(request.getParameter("total"));
        String[] selectedItems = request.getParameterValues("items");

        // Convert java.util.Date to java.time.LocalDate
        Date date = new Date(); // current date
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Invoice invoice = new Invoice();
        invoice.setDate(localDate); // Set LocalDate
        invoice.setTotal(BigDecimal.valueOf(total));

        invoiceDao.saveInvoice(invoice);
        response.sendRedirect("invoices");
    }

    private void editInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        double total = Double.parseDouble(request.getParameter("total"));

        // Convert java.util.Date to java.time.LocalDate
        Date date = new Date(); // current date
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setDate(localDate); // Set LocalDate
        invoice.setTotal(BigDecimal.valueOf(total));

        invoiceDao.saveInvoice(invoice);
        response.sendRedirect("invoices");
    }

    private void deleteInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        invoiceDao.deleteInvoice(id);
        response.sendRedirect("invoices");
    }
}