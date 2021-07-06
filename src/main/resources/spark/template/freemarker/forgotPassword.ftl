<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary" style="display:flex; justify-content:space-between">
    <a class="navbar-brand" href="">My Personal Learning Space (myPLS)</a>
    <ul class="navbar-nav">
        <li class="nav-item active">
            <a class="nav-link" href="/login">Login <span class="sr-only">(current)</span></a>
        </li>
    </ul>
</nav>
<#if success??>
    <#if success == "true">
        <div class="alert alert-dismissible alert-success">
           ${succMsg}
        </div>
    </#if>
</#if>

<#if errorLink??>
    <div class="alert alert-dismissible alert-danger">
        <strong>Error!</strong> Incorrect link, please try clicking the link again or
        <a href="/forgot-password/email">send yourself another email to reset your password</a>
    </div>
</#if>

<div style="display: flex;justify-content: center">
    <div class="card text-black border-primary mb-3" style="width:35%; margin-top:5%">
        <h2 class="card-header text-white bg-primary mb-3" style="padding:10pt">
            Forgot Your Password?
        </h2>
        <#if pageType == "password">
            <div class="invalid-feedback" style="${errorPassMatch}">
                The passwords do not match or must be at least 6 characters. Try again.
            </div>
        <#else>
            <div class="invalid-feedback" style="${errorEmail}">Email must be in the form _@rit.edu</div>
        </#if>

        <div class="card-body">
            <#if pageType == "email">
                <p class="card-text">
                    Please enter your email address. We will email you a link to reset your password.
                </p>
            </#if>

            <form style="padding:20pt" action=${actionLink} method="post">
                <#if pageType == "email">
                    <div class="form-group">
                        <input required type="email" class="form-control" name="email" id="email" aria-describedby="emailHelp" placeholder="rit@rit.edu" value="">
                    </div>
                <#else>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input required type="email" class="form-control" name="email" id="email" aria-describedby="emailHelp" placeholder="rit@rit.edu" value="">
                    </div>
                    <div class="form-group">
                        <label for="confirmationCode">Confirmation Code</label>
                        <input required type="number" class="form-control" name="confirmCode" id="confirmCode" placeholder="4 Digit Code" maxlength="4">
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input required type="password" class="form-control" name="pass" id="password" placeholder="Password">
                    </div>
                    <div class="form-group">
                        <label for="retypePassword">Retype Password</label>
                        <input required type="password" class="form-control" name="retPass" id="retypePassword" placeholder="Retype Password">
                    </div>
                </#if>
                <div class="form-group" style="padding-top: 35pt; display: flex; justify-content: center">
                    <button type="submit" class="btn btn-primary"  style="width:100%">
                        <#if pageType == "email">
                            Send Email
                        <#else>
                            Change Password
                        </#if>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>

</html>