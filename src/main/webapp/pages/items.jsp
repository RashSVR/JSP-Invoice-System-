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
    <h1 class="text-center mb-4">Manage Invoice Items</h1>

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
                    <th>Description</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Total</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody id="itemTableBody">
                <c:forEach var="item" items="${listItems}">
                    <tr id="item-${item.id}">
                        <td>${item.id}</td>
                        <td>${item.description}</td>
                        <td>${item.quantity}</td>
                        <td>${item.price}</td>
                        <td>${item.total}</td>
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
                    <input type="hidden" id="itemId">
                    <div class="form-group">
                        <label for="description">Description</label>
                        <input type="text" class="form-control" id="description" required>
                    </div>
                    <div class="form-group">
                        <label for="quantity">Quantity</label>
                        <input type="number" class="form-control" id="quantity" required>
                    </div>
                    <div class="form-group">
                        <label for="price">Price</label>
                        <input type="number" step="0.01" class="form-control" id="price" required>
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

    // Add or Edit Item
    $('#itemForm').on('submit', function(e) {
        e.preventDefault();
        let id = $('#itemId').val();
        let url = id ? 'items?action=update' : 'items?action=insert';

        $.post(url, {
            id: id,
            description: $('#description').val(),
            quantity: $('#quantity').val(),
            price: $('#price').val()
        }, function(data) {
            location.reload();
        });
    });

    // Edit Item
    function editItem(id) {
        $.get('items?action=edit&id=' + id, function(data) {
            let item = data.item;
            $('#itemId').val(item.id);
            $('#description').val(item.description);
            $('#quantity').val(item.quantity);
            $('#price').val(item.price);
            $('#addItemModal').modal('show');
        });
    }

    // Delete Item
    function deleteItem(id) {
        if (confirm('Are you sure you want to delete this item?')) {
            $.post('items?action=delete', { id: id }, function(data) {
                location.reload();
            });
        }
    }
</script>
</body>
</html>
