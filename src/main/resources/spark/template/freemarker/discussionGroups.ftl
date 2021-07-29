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
            Discussion Groups
        </h2>

        <div style="display: flex;justify-content: space-evenly">
            <div class="card" style="width: 40%; border: #78C2AD">
                <h4 class="card-header border-primary text-black-50 mb-3">
                    My Groups
                </h4>
                <div style="margin-top: 2%">
                    <table class="table">
                        <tr class="table-primary justify-content-between">
                        </tr>
                        <#if groups??>
                            <#list groups as g>
                                <tr>
                                    <th scope="row">
                                        <a class="text-muted" href="/discussion/group-desc">${g.name}</a>
                                        <#if g.privacy??>
                                            <button type="button" class="btn-download" style="margin-left: 2%">
                                                <i class="fa fa-lock"></i>
                                            </button>
                                        </#if>
                                        <#if g.course??>
                                            <span class="badge badge-primary" style="margin-left: 2%">Course</span>
                                        </#if>
                                    </th>
                                    <td>
                                        <button style="float: right" class="btn btn-primary" type="submit">Leave</button>
                                    </td>
                                </tr>
                            </#list>
                        </#if>

                        <tr>
                            <th scope="row">
                                <a class="text-muted" href="">Added Value</a>
                            </th>
                            <td>
                                <button style="float: right" class="btn btn-primary" type="submit">Leave</button>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="card" style="width: 55%; border: #78C2AD">
                <h4 class="card-header border-primary text-black-50 mb-3">
                    Join new Groups
                </h4>
                <form action="/discussion-groups" method="post">

                    <div class="space-between">
                        <input class="form-control col-4" name="searchText" type="text" placeholder="Search" value="intro">
                        <select class="form-control col-2" name="filterBy" id="exampleSelect1">
                            <option>All</option>
                            <option>Public</option>
                            <option>Private</option>
                        </select>
                        <button class="btn btn-primary col-2"  type="submit">Search</button>
                        <button class="btn btn-primary col-3" style="float:right;" type="submit">Create Group</button>
                    </div>
                    <div style="margin-top: 2%">
                        <table class="table">
                            <tr class="table-primary justify-content-between">
                            </tr>
                            <tr>
                                <th scope="row">
                                    <a class="text-muted" href="">Introduction to Programming</a>
                                </th>
                                <td>
                                    <button style="float: right" class="btn btn-primary" type="submit">Join</button>
                                </td>
                            </tr>
                            <tr>
                                <th scope="row">
                                    <a class="text-muted" href="">Introduction to Human Computer Interaction</a>
                                    <button type="button" class="btn-download" style="margin-left: 2%">
                                        <i class="fa fa-lock"></i>
                                    </button>
                                </th>
                                <td>
                                    <button style="float: right" class="btn btn-primary" type="submit">Request</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>