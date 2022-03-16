<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Approval - MyPLS</title>
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            Discussion Groups
        </h2>
        <#if success??>
            <#if success == true>
                <div class="alert alert-dismissible alert-success" style="margin-top: -1.5%">
                    The user has successly become an admin.
                </div>
            </#if>
        </#if>
        <#if err??>
            <div class="alert alert-dismissible alert-danger" style="margin-top: -1.5%">
                <strong>Error!</strong> Something went wrong. Please try again later.
            </div>
        </#if>
        <div style="display: flex;justify-content: space-evenly">
            <div class="card" style="width: 45%; border: #78C2AD">
                <h4 class="card-header border-primary text-black-50 mb-3">
                    Requests to become a Professor
                </h4>
                <form action="/approval" method="post">
                    <div style="margin-top: 2%">
                        <table class="table">
                            <#list profs as k, v>
                                <tr>
                                    <th scope="row">
                                        <div class="text-muted">${v}</div>
                                    </th>
                                    <td>
                                        <div style="float: right; display: flex">
                                            <button type="submit" name="approve" value="${k}" class="btn btn-primary" style="margin-left: -4%">
                                                Approve
                                            </button>
                                            <button type="submit" name="deny" value="${k}" class="btn btn-primary" style="margin-left: 2%">
                                                Deny
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </#list>
                        </table>
                    </div>
                </form>
            </div>
            <div class="card" style="width: 45%; border: #78C2AD">
                <h4 class="card-header border-primary text-black-50 mb-3">
                    Add another admin
                </h4>
                <form action="/approval" method="post">
                    <div class="space-between">
                        <input required class="form-control col-5" name="searchText" type="text" placeholder="Search" value="${searchText!""}">
                        <select class="form-control col-2" name="filterBy" id="exampleSelect1">
                            <#list searchOptions as k,v>
                                <option value=${k}>${v}</option>
                            </#list>
                            <#if filterKey??>
                                <option selected value=${filterKey}>${filterVal}</option>
                            </#if>
                        </select>
                        <button class="btn btn-primary col-2" type="submit">Search</button>
                        <a class="btn btn-primary col-2"  href="/approval">Clear</a>
                    </div>
                    <div style="margin-top: 2%">
                        <table class="table">
                            <tr class="table-primary justify-content-between">
                            </tr>
                            <#if users??>
                                <#list users as k,v>
                                    <tr>
                                        <th scope="row">
                                            <div class="text-muted">${v}</div>
                                        </th>
                                        <td>
                                            <button style="float: right" class="btn btn-primary" name="admin" value="${k}" type="submit">Authorize</button>
                                        </td>
                                    </tr>
                                </#list>
                            </#if>
                            <#if noUser??>
                                <div> No results found </div>
                            </#if>
                        </table>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>