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
            Upload File Test
        </h2>
        <form action="/upload" method="post" enctype="multipart/form-data">
            <label for="myfile">Select a file</label>
            <input type="file" id="myfile" name="myfile"/>
            <input type="submit" id="buttonUpload" value="Upload"/>
            <br>
            <p>Result:&nbsp;:<span id="result"></span></p>
        </form>
    </div>
</div>
</body>
</html>