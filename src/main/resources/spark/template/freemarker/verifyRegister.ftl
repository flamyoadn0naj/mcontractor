<!DOCTYPE html>
<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <title>Verify Your Account - MyPLS</title>
</head>
<body>
<#--    <#assign resend = false>-->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary" style="display:flex; justify-content:space-between">
      <a class="navbar-brand" href="">My Personal Learning Space (myPLS)</a>
      <ul class="navbar-nav">
        <li class="nav-item active">
            <#if type == "send">
                <a class="nav-link" href="/register"> Register <span class="sr-only">(current)</span></a>
            <#else>
                <a class="nav-link" href="/login"> Login <span class="sr-only">(current)</span></a>
            </#if>

        </li>
      </ul>
    </nav>

    <#if resend??>
        <#if resend == true>
            <div class="alert alert-dismissible alert-success">
                An email has been sent again to your email address.
            </div>
        <#else>
            <div class="alert alert-dismissible alert-danger">
                Something went wrong. Please try again.
            </div>
        </#if>
    </#if>

    <#if type == "send">
        <div class="text-black border-primary mb-3" style="display:flex; justify-content:center; text-align:center; margin:15%">
            <h2 style="max-width:40rem; margin-bottom:2%">
                An email has been sent to you with a verification link.
                <br/><br/>
                Please click on the link to verify your email address
            </h2>
        </div>
        <form action="/verify-register/send" method="post">
            <div style="display:flex; justify-content:center">
                <button type="submit" class="btn btn-primary" style="width:20rem" name="resend" value="true", id="resend">
                    Resend Email
                </button>
            </div>
        </form>
    <#else>
        <div class="text-black border-primary mb-3" style="display:flex; justify-content:center; text-align:center; margin:15%">
            <h2 style="max-width:40rem; margin-bottom:2%">
                Your account has been confirmed.
                <br><br>
                Please <a href="/login">log in</a> to access your account.
            </h2>
        </div>
    </#if>

<#--    <#if resend == true>-->
<#--      ${toast}-->
<#--    </#if>-->
</body>
</html>
