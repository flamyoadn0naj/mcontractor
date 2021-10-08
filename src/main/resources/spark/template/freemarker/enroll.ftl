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
            Enroll
        </h2>
        <form style="display:flex; justify-content:center;" action="/enroll" method="post">
            <div class="card" style="width: 95%; border: none">
                <div class="space-between">
                    <input class="form-control col-7" name="searchText" type="text" placeholder="Search" value="software">
                    <select class="form-control col-2" name="filterBy" id="exampleSelect1">
                        <option>All</option>
                        <option>Open</option>
                        <option>Closed</option>
                    </select>
                    <button disabled class="btn btn-primary my-2 my-sm-0 col-2" type="submit">Search</button>
                </div>

                <div style="margin-top: 2%">
                    <table class="table table-bordered">
                        <tr class="table-primary">
                            <th scope="col">Course Name</th>
                            <th scope="col">Professor</th>
                            <th scope="col">Status</th>
                            <th scope="col">Credits</th>
                            <th scope="col">Meeting Day</th>
                            <th scope="col">Meeting Time</th>
                            <th scope="col">Rating</th>
                        </tr>
                        <#if courses??>
                            <#list courses as c>
                                <tr>
                                    <th scope="row"><a class="text-muted" href="enroll/about/${c.id}">${c.name}</a></th>
                                    <td>${c.prof}</td>
                                    <td>${c.state}</td>
                                    <td>${c.credits}</td>
                                    <td>${c.meeting_days}</td>
                                    <td>${c.startTimeF} - ${c.endTimeF}</td>
                                    <td>
                                        <#if c.rating??>
                                            <#if c.rating == 0>
                                                No Rating Available
                                            <#else>
                                                <#list 1..c.rating as i>
                                                    <span class="fa fa-star checked large"></span>
                                                </#list>
                                                <#list 1..c.unchecked as j>
                                                    <span class="fa fa-star large"></span>
                                                </#list>
                                            </#if>
                                            <#else>
                                            No Rating Available
                                        </#if>
                                    </td>
                                </tr>
                            </#list>
                        </#if>
                        <tr>
                            <th scope="row">Software Process and Management</th>
                            <td>Samuel Malachowsky</td>
                            <td>Closed</td>
                            <td>3</td>
                            <td>Monday, Wednesday</td>
                            <td>1:25 am - 2:15 am</td>
                            <td>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star large"></span>
                                <span class="fa fa-star large"></span>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>