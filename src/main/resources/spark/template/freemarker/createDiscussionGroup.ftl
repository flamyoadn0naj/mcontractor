<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Create Discussion Groups - MyPLS</title>
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h4 class="card-header border-primary text-black-50 mb-3">
            Create Discussion Group
        </h4>
        <#if err??>
            <div class="alert alert-dismissible alert-danger" style="margin-top: -1.5%">
                <strong>Error!</strong> Something went wrong, please try again.
            </div>
        </#if>
        <form style="display: flex;justify-content: center" method="post" action="/discussion/create">
            <div class="card" style="width: 80%; border: none; margin-top: 2%">
                <div class="form-group space-between mb-3">
                    <label class="padding2right col-3" for="1a">Name:</label>
                    <input required type="text" name="name" class="form-control col-9" id="name" value="">
                </div>
                <div class="form-group mb-3" style="display: flex">
                    <label class="padding2right col-4" for="1a">Do you want the group to be private?</label>
                    <div class="custom-control custom-radio padding2right">
                        <input type="radio" id="customRadio1" value="1" name="customRadio" class="custom-control-input" checked="">
                        <label class="custom-control-label" for="customRadio1">Yes</label>
                    </div>
                    <div class="custom-control custom-radio padding2right">
                        <input type="radio" id="customRadio2" value="0" name="customRadio" class="custom-control-input">
                        <label class="custom-control-label" for="customRadio2">No</label>
                    </div>
                </div>
                <button type="submit" name="action" value="Save" class="btn btn-primary" style="width:10rem; float:right;">
                    Submit
                </button>
                <div>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>