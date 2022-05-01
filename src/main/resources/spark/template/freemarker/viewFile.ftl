<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>View File - MyPLS</title>
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            <p style="float: left;">View ${fileName}</p>
            <form method="get" action="/course/learnMat/${courseNumber}">
                <button type="submit" class="btn btn-primary" style="float: right; margin-right: 2%">Back</button>
            </form>
        </h2>
        <div style="width:80%; margin:auto; text-align:center;">
        <#if fileType == ".mp4">
            <video src="${filePath}" controls>
                Error loading video player.
            </video>
        <#else>
            <p>
                MyPLS does not support the viewing of this file type.
            </p>
        </#if>
        </div>
    </div>
</div>
</body>
</html>