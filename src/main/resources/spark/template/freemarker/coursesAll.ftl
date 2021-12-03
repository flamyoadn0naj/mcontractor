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
            Courses
        </h2>
        <div style="width: 90%; margin-left: 2%">
            <ul class="nav nav-tabs mb-3">
                <li class="nav-item">
                    <a class="nav-link active" data-toggle="tab" href="/courses/all">All Courses</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/courses">My Courses</a>
                </li>
            </ul>
            <form method="post" action="/courses/all">
                <div style="display: flex">
                    <div style="width: 50%">
                        <#if role == "admin">
                            <button class="btn btn-primary text-white" style="float:left">
                                <a href="/courses/create-course" class="text-white">Create Course</a>
                            </button>
                        </#if>
                    </div>
                    <div class="mb-3" style="width: 50%; display: flex; justify-content: flex-end">
                        <b class="col-2" style="margin-top: 2%" >Filter By:</b>
                        <select class="form-control col-4" name="filterBy" id="exampleSelect1" style="margin-right: 2%">
                            <#list filterOptions as op>
                                <option>${op}</option>
                            </#list>
                            <#if filterStatus??>
                                <option selected>${filterStatus}</option>
                            </#if>
                        </select>
                        <button class="btn btn-primary col-3" type="submit">Search</button>
                        <a class="col-3" href="/courses/all">
                            <button class="btn btn-primary col-12" type="button">Clear</button>
                        </a>
                    </div>
                </div>
            </form>
            <table class="table table-bordered">
                <tr class="table-primary">
                    <th scope="col">Name</th>
                    <th scope="col">Professor</th>
                    <th scope="col">Prerequisites</th>
                    <th scope="col">Start Date</th>
                    <th scope="col">End Date</th>
                    <th scope="col">Status</th>
                    <th scope="col">Actions</th>
                </tr>
                <#if courses??>
                    <#list courses as c>
                        <tr>
                            <th scope="row"><div class="text-muted">${c.name}</div></th>
                            <td>${c.prof}</td>
                            <td style="text-align: center">${c.prereq}</td>
                            <td>${c.startDate}</td>
                            <td>${c.endDate}</td>
                            <td>${c.status}</td>
                            <#if c.status == "Upcoming">
                                <td>
                                    <a class="btn-download padding2right" href="/courses/all?edit=${c.id}"><i class="fa fa-edit"></i></a>
                                    <a class="btn-download" href="/courses/all?del=${c.id}"><i class="fa fa-trash"></i></a>
                                </td>
                            <#elseif c.status == "Current">
                                <td>
                                    <a class="btn-download padding2right" href="/courses/all?edit=+${c.id}"><i class="fa fa-edit"></i></a>
                                </td>
                            <#else >
                                <td></td>
                            </#if>
                        </tr>
                    </#list>
                </#if>
            </table>
            <#if !courses??>
                <div style="margin-left: 2%"> No Results Found</div>
            </#if>
        </div>
    </div>
</div>
</body>
</html>