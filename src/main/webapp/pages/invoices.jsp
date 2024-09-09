<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Invoices</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h1 class="text-center mb-4">Manage Invoices</h1>

    <!-- Alert Div for Messages -->
    <div id="alertDiv" class="alert" role="alert" style="display: none;"></div>

    <!-- Button to Open the Add Invoice Modal -->
    <button type="button" class="btn btn-success mb-3" data-toggle="modal" data-target="#addInvoiceModal">
        Add New Invoice
    </button>

    <!-- Invoice List Table -->
    <div class="card">
        <div class="card-header">
            <h4>Invoices</h4>
        </div>
        <div class="card-body">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Date</th>
                    <th>Customer</th>
                    <th>Total</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody id="invoiceTableBody">
                <c:forEach var="invoice" items="${invoices}">
                    <tr id="invoice-${invoice.id}">
                        <td>${invoice.id}</td>
                        <td>${invoice.date}</td>
                        <td>${invoice.customer}</td>
                        <td>${invoice.total}</td>
                        <td>
                            <button class="btn btn-primary btn-sm" onclick="editInvoice(${invoice.id})">Edit</button>
                            <button class="btn btn-danger btn-sm" onclick="deleteInvoice(${invoice.id})">Delete</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Add/Edit Invoice Modal -->
<div class="modal fade" id="addInvoiceModal" tabindex="-1" aria-labelledby="addInvoiceModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addInvoiceModalLabel">Add/Edit Invoice</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="invoiceForm">
                    <input type="hidden" id="invoiceId">
                    <div class="form-group">
                        <label for="date">Date</label>
                        <input type="date" class="form-control" id="date" required>
                    </div>
                    <div class="form-group">
                        <label for="customer">Customer</label>
                        <input type="text" class="form-control" id="customer">
                    </div>
                    <div class="form-group">
                        <label for="total">Total</label>
                        <input type="number" step="0.01" class="form-control" id="total" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Save Invoice</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    // JavaScript and AJAX code for invoice management

    // Add or Edit Invoice
    $('#invoiceForm').on('submit', function(e) {
        e.preventDefault();

        let id = $('#invoiceId').val();
        let url = id ? 'invoices?action=edit' : 'invoices?action=insert';

        $.ajax({
            type: 'POST',
            url: url,
            data: $('#invoiceForm').serialize(),
            dataType: 'json',
            beforeSend: function() {
                $('#alertDiv').removeClass('alert-success alert-danger').addClass('alert-info').text('Processing...').show();
            },
            success: function(data) {
                $('#alertDiv').removeClass('alert-danger alert-info').addClass('alert-success').text('Invoice saved successfully!').show();
                $('#addInvoiceModal').modal('hide');

                if (id) {
                    $('#invoice-' + id).html(`
                        <td>${data.id}</td>
                        <td>${data.date}</td>
                        <td>${data.customer}</td>
                        <td>${data.total}</td>
                        <td>
                            <button class="btn btn-primary btn-sm" onclick="editInvoice(${data.id})">Edit</button>
                            <button class="btn btn-danger btn-sm" onclick="deleteInvoice(${data.id})">Delete</button>
                        </td>
                    `);
                } else {
                    $('#invoiceTableBody').append(`
                        <tr id="invoice-${data.id}">
                            <td>${data.id}</td>
                            <td>${data.date}</td>
                            <td>${data.customer}</td>
                            <td>${data.total}</td>
                            <td>
                                <button class="btn btn-primary btn-sm" onclick="editInvoice(${data.id})">Edit</button>
                                <button class="btn btn-danger btn-sm" onclick="deleteInvoice(${data.id})">Delete</button>
                            </td>
                        </tr>
                    `);
                }

                $('#invoiceForm')[0].reset();
                $('#invoiceId').val('');
            },
            error: function(xhr, status, error) {
                $('#alertDiv').removeClass('alert-success alert-info').addClass('alert-danger').text('Failed to save invoice.').show();
            }
        });
    });

    // Edit Invoice
    function editInvoice(id) {
        $.ajax({
            type: 'GET',
            url: 'invoices?action=edit&id=' + id,
            dataType: 'json',
            success: function(data) {
                $('#invoiceId').val(data.id);
                $('#date').val(data.date);
                $('#customer').val(data.customer);
                $('#total').val(data.total);
                $('#addInvoiceModalLabel').text('Edit Invoice');
                $('#addInvoiceModal').modal('show');
            },
            error: function(xhr, status, error) {
                console.error("Error fetching invoice:", error);
            }
        });
    }

    // Delete Invoice
    function deleteInvoice(id) {
        if (confirm('Are you sure you want to delete this invoice?')) {
            $.ajax({
                type: 'POST',
                url: 'invoices?action=delete',
                data: { id: id },
                success: function(data) {
                    $('#alertDiv').removeClass('alert-danger').addClass('alert-success').text('Invoice deleted successfully!').show();
                    $('#invoice-' + id).remove();
                },
                error: function(xhr, status, error) {
                    $('#alertDiv').removeClass('alert-success').addClass('alert-danger').text('Failed to delete invoice.').show();
                }
            });
        }
    }
</script>
</body>
</html>
