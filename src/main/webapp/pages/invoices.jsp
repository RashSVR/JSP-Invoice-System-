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
                    <th>Customer Name</th>
                    <th>Invoice Date</th>
                    <th>Total Amount</th>
                    <th>Status</th>
                    <th>Items</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody id="invoiceTableBody">
                <c:forEach var="invoice" items="${listInvoices}">
                    <tr id="invoice-${invoice.id}">
                        <td>${invoice.id}</td>
                        <td>${invoice.customerName}</td>
                        <td>${invoice.invoiceDate}</td>
                        <td>${invoice.totalAmount}</td>
                        <td>${invoice.status}</td>
                        <td>
                            <button class="btn btn-info btn-sm" data-toggle="modal" data-target="#invoiceItemsModal" onclick="showInvoiceItems(${invoice.id})">View Items</button>
                        </td>
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
                <h5 class="modal-title" id="addInvoiceModalLabel">Add New Invoice</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="invoiceForm">
                    <input type="hidden" id="invoiceId">
                    <div class="form-group">
                        <label for="customerName">Customer Name</label>
                        <input type="text" class="form-control" id="customerName" required>
                    </div>
                    <div class="form-group">
                        <label for="invoiceDate">Invoice Date</label>
                        <input type="date" class="form-control" id="invoiceDate" required>
                    </div>
                    <div class="form-group">
                        <label for="totalAmount">Total Amount</label>
                        <input type="number" step="0.01" class="form-control" id="totalAmount" required>
                    </div>
                    <div class="form-group">
                        <label for="status">Status</label>
                        <select class="form-control" id="status" required>
                            <option value="Paid">Paid</option>
                            <option value="Unpaid">Unpaid</option>
                            <option value="Pending">Pending</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Save Invoice</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Invoice Items Modal -->
<div class="modal fade" id="invoiceItemsModal" tabindex="-1" aria-labelledby="invoiceItemsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="invoiceItemsModalLabel">Invoice Items</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Description</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Total</th>
                    </tr>
                    </thead>
                    <tbody id="invoiceItemsTableBody">
                    <!-- Items will be dynamically inserted here -->
                    </tbody>
                </table>
                <button class="btn btn-success" data-toggle="modal" data-target="#addItemToInvoiceModal">Add New Item</button>
            </div>
        </div>
    </div>
</div>

<!-- Add New Item to Invoice Modal -->
<div class="modal fade" id="addItemToInvoiceModal" tabindex="-1" aria-labelledby="addItemToInvoiceModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addItemToInvoiceModalLabel">Add New Item to Invoice</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="addItemForm">
                    <input type="hidden" id="invoiceIdForItem">
                    <div class="form-group">
                        <label for="itemDescription">Description</label>
                        <input type="text" class="form-control" id="itemDescription" required>
                    </div>
                    <div class="form-group">
                        <label for="itemQuantity">Quantity</label>
                        <input type="number" class="form-control" id="itemQuantity" required>
                    </div>
                    <div class="form-group">
                        <label for="itemPrice">Price</label>
                        <input type="number" step="0.01" class="form-control" id="itemPrice" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Add Item</button>
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
        let url = id ? 'invoices?action=update' : 'invoices?action=insert';

        $.post(url, {
            id: id,
            customerName: $('#customerName').val(),
            invoiceDate: $('#invoiceDate').val(),
            totalAmount: $('#totalAmount').val(),
            status: $('#status').val()
        }, function(data) {
            location.reload();
        });
    });

    // Edit Invoice
    function editInvoice(id) {
        $.get('invoices?action=edit&id=' + id, function(data) {
            let invoice = data.invoice;
            $('#invoiceId').val(invoice.id);
            $('#customerName').val(invoice.customerName);
            $('#invoiceDate').val(invoice.invoiceDate);
            $('#totalAmount').val(invoice.totalAmount);
            $('#status').val(invoice.status);
            $('#addInvoiceModal').modal('show');
        });
    }

    // Delete Invoice
    function deleteInvoice(id) {
        if (confirm('Are you sure you want to delete this invoice?')) {
            $.post('invoices?action=delete', { id: id }, function(data) {
                location.reload();
            });
        }
    }

    // Show Invoice Items
    function showInvoiceItems(invoiceId) {
        $.get('invoices?action=viewItems&invoiceId=' + invoiceId, function(data) {
            $('#invoiceItemsTableBody').html(data);
            $('#invoiceItemsModal').modal('show');
        });
    }

    // Add New Item to Invoice
    $('#addItemForm').on('submit', function(e) {
        e.preventDefault();

        $.post('items?action=addToInvoice', {
            invoiceId: $('#invoiceIdForItem').val(),
            description: $('#itemDescription').val(),
            quantity: $('#itemQuantity').val(),
            price: $('#itemPrice').val()
        }, function(response) {
            if (response.success) {
                $('#addItemToInvoiceModal').modal('hide');
                showInvoiceItems($('#invoiceIdForItem').val());
            } else {
                alert('Failed to add item. Please try again.');
            }
        });
    });

    // Set invoice ID when showing the Add Item modal
    $('#addItemToInvoiceModal').on('show.bs.modal', function(event) {
        let button = $(event.relatedTarget);
        let invoiceId = button.data('invoice-id');
        $('#invoiceIdForItem').val(invoiceId);
    });
</script>
</body>
</html>
