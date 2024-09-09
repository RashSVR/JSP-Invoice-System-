<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Items</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h1 class="text-center mb-4">Manage Items</h1>

    <!-- Alert Div for Messages -->
    <div id="alertDiv" class="alert" role="alert" style="display: none;"></div>

    <!-- Button to Open the Add Item Modal -->
    <button type="button" class="btn btn-success mb-3" data-toggle="modal" data-target="#addItemModal">
        Add New Item
    </button>

    <!-- Item List Table -->
    <div class="card">
        <div class="card-header">
            <h4>Items</h4>
        </div>
        <div class="card-body">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody id="itemTableBody">
                <c:forEach var="item" items="${items}">
                    <tr id="item-${item.id}">
                        <td>${item.id}</td>
                        <td>${item.name}</td>
                        <td>${item.description}</td>
                        <td>${item.quantity}</td>
                        <td>${item.price}</td>
                        <td>
                            <button class="btn btn-primary btn-sm" onclick="editItem(${item.id})">Edit</button>
                            <button class="btn btn-danger btn-sm" onclick="deleteItem(${item.id})">Delete</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Add/Edit Item Modal -->
<div class="modal fade" id="addItemModal" tabindex="-1" aria-labelledby="addItemModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addItemModalLabel">Add/Edit Item</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="itemForm">
                    <input type="hidden" id="itemId" name="id">
                    <div class="form-group">
                        <label for="name">Name</label>
                        <input type="text" class="form-control" id="name" name="name" required>
                    </div>
                    <div class="form-group">
                        <label for="description">Description</label>
                        <input type="text" class="form-control" id="description" name="description">
                    </div>
                    <div class="form-group">
                        <label for="quantity">Quantity</label>
                        <input type="number" class="form-control" id="quantity" name="quantity" required>
                    </div>
                    <div class="form-group">
                        <label for="price">Price</label>
                        <input type="number" step="0.01" class="form-control" id="price" name="price" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Save Item</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    // JavaScript and AJAX code for item management

    // Add or Edit Item (submit form)
    $('#itemForm').submit(function (event) {
        event.preventDefault();

        const formData = $(this).serialize();
        const action = $('#itemId').val() ? 'edit' : 'insert';

        $.ajax({
            url: 'items',
            type: 'POST',
            data: formData + '&action=' + action,
            success: function (response) {
                // Handle success
                $('#addItemModal').modal('hide');
                $('#alertDiv').removeClass('alert-danger').addClass('alert-success').text('Item saved successfully').show();
                setTimeout(() => $('#alertDiv').hide(), 3000);

                if (action === 'insert') {
                    // Add new item to the table
                    $('#itemTableBody').append(`
                        <tr id="item-${response.id}">
                            <td>${response.id}</td>
                            <td>${response.name}</td>
                            <td>${response.description}</td>
                            <td>${response.quantity}</td>
                            <td>${response.price}</td>
                            <td>
                                <button class="btn btn-primary btn-sm" onclick="editItem(${response.id})">Edit</button>
                                <button class="btn btn-danger btn-sm" onclick="deleteItem(${response.id})">Delete</button>
                            </td>
                        </tr>
                    `);
                } else {
                    // Update the existing item in the table
                    const row = $(`#item-${response.id}`);
                    row.find('td:nth-child(2)').text(response.name);
                    row.find('td:nth-child(3)').text(response.description);
                    row.find('td:nth-child(4)').text(response.quantity);
                    row.find('td:nth-child(5)').text(response.price);
                }
            },
            error: function () {
                // Handle error
                $('#alertDiv').removeClass('alert-success').addClass('alert-danger').text('Failed to save item').show();
                setTimeout(() => $('#alertDiv').hide(), 3000);
            }
        });
    });

    // Edit item (populate modal)
    function editItem(id) {
        $.ajax({
            url: 'items',
            type: 'GET',
            data: {action: 'get', id: id},
            success: function (response) {
                $('#itemId').val(response.id);
                $('#name').val(response.name);
                $('#description').val(response.description);
                $('#quantity').val(response.quantity);
                $('#price').val(response.price);
                $('#addItemModal').modal('show');
            }
        });
    }

    // Delete item
    function deleteItem(id) {
        if (!confirm('Are you sure you want to delete this item?')) return;

        $.ajax({
            url: 'items',
            type: 'POST',
            data: {action: 'delete', id: id},
            success: function () {
                $(`#item-${id}`).remove();
                $('#alertDiv').removeClass('alert-danger').addClass('alert-success').text('Item deleted successfully').show();
                setTimeout(() => $('#alertDiv').hide(), 3000);
            },
            error: function () {
                $('#alertDiv').removeClass('alert-success').addClass('alert-danger').text('Failed to delete item').show();
                setTimeout(() => $('#alertDiv').hide(), 3000);
            }
        });
    }
</script>
</body>
</html>
