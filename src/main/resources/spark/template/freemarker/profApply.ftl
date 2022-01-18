<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Approvals - MyPLS</title>
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            Apply to be a Professor
        </h2>
        <#if success??>
            <#if success == true>
                <div class="alert alert-dismissible alert-success" style="margin-top: -1.5%">
                    Your request has been sucessfully sent to the administration for approval.
                </div>
            </#if>
        </#if>

        <#if errMsg??>
            <div class="alert alert-dismissible alert-danger" style="margin-top: -1.5%">
                <strong>Error!</strong> ${errMsg}
            </div>
        </#if>
        <form style="display: flex;justify-content: center" method="post" action="/apply-prof">
            <div class="card" style="width: 80%; border: none; margin-top: 2%">
                <#if nameErr??>
                    <div class="invalid-feedback mb-3" style="display:block;margin-left:1%">
                        Your signature is incorrect. Please write down your Full Name.
                    </div>
                </#if>
                <div class="form-group mb-3" style="display: flex">
                    <label class="padding2right col-4" for="1a">Are you sure you want to be a Professor?</label>
                    <div class="custom-control custom-radio padding2right">
                        <input type="radio" id="customRadio1" value="yes" name="customRadio" class="custom-control-input" checked="">
                        <label class="custom-control-label" for="customRadio1">Yes</label>
                    </div>
                    <div class="custom-control custom-radio padding2right">
                        <input type="radio" id="customRadio2" value="no" name="customRadio" class="custom-control-input">
                        <label class="custom-control-label" for="customRadio2">No</label>
                    </div>
                </div>
                <div class="form-group mb-3" style="display: flex">
                    <label class="col-4" for="1d">Please write your name as a signature</label>
                    <input required type="text" name="name" class="form-control col-8" id="name"  value="">
                </div>
                <#if disable??>
                    <button disabled type="submit" name="action" value="Save" class="btn btn-primary" style="width:10rem; float:right;">
                        Submit
                    </button>
                <#else>
                    <button type="submit" name="action" value="Save" class="btn btn-primary" style="width:10rem; float:right;">
                        Submit
                    </button>
                </#if>
                <div>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>