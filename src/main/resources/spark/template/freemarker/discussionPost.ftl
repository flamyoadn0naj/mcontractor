<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            Introduction to Software Engineering
        </h2>
        <h4 class="card-header border-primary text-black-50 mb-3" style="margin-top: -1.5%">
            Create Post
        </h4>
        <form style="display:flex; justify-content:center;">
            <div class="card" style="width: 80%">
                <div class="card-header" id="headingOne">
                    <h6 class="space-between">
                        <span class="text-primary mb-3" style="margin-right: 2%">
                            Name:
                        </span>
                        <input required type="text" name="name" class="form-control" id="name">
                    </h6>

                    <div class="form-group space-between">
                        <input required type="text" name="content" class="form-control" id="content" placeholder="Please add Content here">
                    </div>
                    <div class="space-between">
                        <label class="padding2right" for="1b">Upload Files: (max 5)</label>
                        <div>
                            <button type="button" class="btn-download" style="float: right">
                                <i class="fa fa-upload"></i>
                            </button>
                            <input type="file" class="small" id="inputGroupFile02" style="width: 50%; float: right; margin-left: 2%;">
                        </div>
                    </div>
                    <div class="mb-3" style="display:grid; justify-content: end">
                        <span style="float: right">
                            <button type="button" class="btn-download" style="float: right">
                                <i class="fa fa-upload"></i>
                            </button>
                            <input type="file" class="small" id="inputGroupFile02" style="width: 50%; float: right; margin-left: 2%;">
                        </span>
                        <span style="float: right">
                            <button type="button" class="btn-download" style="float: right">
                                <i class="fa fa-upload"></i>
                            </button>
                            <input type="file" class="small" id="inputGroupFile02" style="width: 50%; float: right; margin-left: 2%;">
                        </span>
                        <span style="float: right">
                            <button type="button" class="btn-download" style="float: right">
                                <i class="fa fa-upload"></i>
                            </button>
                            <input type="file" class="small" id="inputGroupFile02" style="width: 50%; float: right; margin-left: 2%;">
                        </span>
                        <span style="float: right">
                            <button type="button" class="btn-download" style="float: right">
                                <i class="fa fa-upload"></i>
                            </button>
                            <input type="file" class="small" id="inputGroupFile02" style="width: 50%; float: right; margin-left: 2%;">
                        </span>
                    </div>

                    <button type="submit" name="action" value="Save" class="btn btn-primary" style="width:10rem; float:right;">
                        Save
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>