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
            View ${fileName}
        </h2>
        <#if fileType == ".mp4">
            <video width="400" height="500" src="${filePath}" controls>
                Error loading video player.
            </video>
        <#else>
            <p>
                MyPLS does not support the viewing of this file type.
            </p>
        </#if>
    </div>
</div>
</body>
</html>