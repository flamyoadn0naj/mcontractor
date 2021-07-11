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
            Apply to be a Professor
        </h2>
        <form style="display: flex;justify-content: center" method="post" action="/applyProf">
            <div class="card" style="width: 80%; border: none; margin-top: 2%">
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
                    <input required type="text" name="Q1D" class="form-control col-8" id="name"  value="">
                </div>
                <div>
                    <button type="submit" name="action" value="Save" class="btn btn-primary" style="width:10rem; float:right;">
                        Submit
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>