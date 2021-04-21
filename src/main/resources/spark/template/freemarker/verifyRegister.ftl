<!DOCTYPE html>
<head>
<#--    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>-->
<#--    <meta http-equiv="refresh" content="10">-->
    <title>Title</title>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<#--    <#assign resend = false>-->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary" style="display:flex; justify-content:space-between">
      <a class="navbar-brand" href="">My Personal Learning Space (myPLS)</a>
      <ul class="navbar-nav">
        <li class="nav-item active">
            <a class="nav-link" href="/register"> Register <span class="sr-only">(current)</span></a>
        </li>
      </ul>
    </nav>
    <div class="text-black border-primary mb-3" style="display:flex; justify-content:center; text-align:center; margin:15%">
      <h2 style="max-width:40rem; margin-bottom:2%">
        An email has been sent to you with a verification link.
        <br/><br/>
        Please click on the link to verify your email address
      </h2>
    </div>
    <div style="display:flex; justify-content:center">
      <button type="submit" class="btn btn-primary" style="width:20rem">
        Resend Email
      </button>
    </div>
<#--    <#if resend == true>-->
<#--      ${toast}-->
<#--    </#if>-->
</body>
</html>
