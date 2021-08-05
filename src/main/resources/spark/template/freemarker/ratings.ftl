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
            Ratings & Feedback
        </h2>
        <div class="card" style="width: 95%; border:none; margin-left: 2.5%">
            <#if role == "prof">
                <form style="display:flex; justify-content:center;" action="/ratings" method="post">
                    <div class="space-between">
                        <input class="form-control col-7" name="searchTextUser" type="text" placeholder="Search" value="">
                        <button class="btn btn-primary my-2 my-sm-0 col-2" type="submit">Search</button>
                    </div>
                </form>
                <div style="margin-top: 2%">
                    <table class="table table-bordered">
                        <tr class="table-primary">
                            <th scope="col">Learner</th>
                            <th scope="col">Rating</th>
                            <th scope="col">Action</th>
                        </tr>
                        <tr>
                            <th scope="row">Saad Hassan</th>
                            <td>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star large"></span>
                            </td>
                            <td>
                                <button type="button" class="btn btn-primary">
                                    <a href="/rating/individual" class="text-white">Rate</a>
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">Maheen Contractor</th>
                            <td>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star large"></span>
                                <span class="fa fa-star large"></span>
                            </td>
                            <td>
                                <button type="button" class="btn btn-primary">
                                    <a href="" class="text-white">Rate</a>
                                </button>
                            </td>
                        </tr>
                    </table>
                </div>
            <#elseif role == "learner">
                <form style="display:flex; justify-content:center;" action="/ratings" method="post">
                    <div class="space-between">
                        <input class="form-control col-7" name="searchTextUser" type="text" placeholder="Search" value="">
                        <button class="btn btn-primary my-2 my-sm-0 col-2" type="submit">Search</button>
                    </div>
                </form>
                <div style="margin-top: 2%">
                    <table class="table table-bordered">
                        <tr class="table-primary">
                            <th scope="col">Professor</th>
                            <th scope="col">Rating</th>
                            <th scope="col">Action</th>
                        </tr>
                        <tr>
                            <th scope="row">AbdulMutalib Wahaishi</th>
                            <td>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star large"></span>
                            </td>
                            <td>
                                <button type="button" class="btn btn-primary">
                                    <a href="/rating/individual" class="text-white">Rate</a>
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">Samuel Malachowsky</th>
                            <td>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star checked large"></span>
                                <span class="fa fa-star large"></span>
                                <span class="fa fa-star large"></span>
                            </td>
                            <td>
                                <button type="button" class="btn btn-primary">
                                    <a href="" class="text-white">Rate</a>
                                </button>
                            </td>
                        </tr>
                    </table>
                </div>
            <#else>
                <div style="margin-top: 1%">
                    <h3> Users </h3>
                    <form action="/ratings" method="post">
                        <div class="space-between mb-2">
                            <input class="form-control col-7" name="searchTextUser" type="text" placeholder="Search" value="">
                            <button class="btn btn-primary my-2 my-sm-0 col-2" type="submit">Search</button>
                        </div>
                    </form>
                    <form method="post" action="/ratings" class="mb-5">
                        <table class="table table-bordered" style="text-align: center">
                            <tr class="table-primary">
                                <th scope="col">Name</th>
                                <th scope="col">Role</th>
                                <th scope="col">Rating</th>
                                <th scope="col">Feedback</th>
                            </tr>
                            <#if users??>
                                <#list users as i,u>
                                    <tr>
                                        <th scope="row">${u.name}</th>
                                        <td>${u.role}</td>
                                        <td>
                                            <#if u.rating == 0>
                                                <span class="fa fa-star large"></span>
                                                <span class="fa fa-star large"></span>
                                                <span class="fa fa-star large"></span>
                                                <span class="fa fa-star large"></span>
                                                <span class="fa fa-star large"></span>
                                            <#else>
                                                <#list 1..u.rating as i>
                                                    <span class="fa fa-star checked large"></span>
                                                </#list>
                                                <#list 1..u.unchecked as j>
                                                    <span class="fa fa-star large"></span>
                                                </#list>
                                            </#if>
                                        </td>
                                        <td>
                                            <button type="submit" name="userId" value=${i} class="btn-download">
                                                <i class="fa fa-eye"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </#list>
                            </#if>
                        </table>
                    </form>
                    <h3>Courses</h3>
                    <form action="/ratings" method="post">
                        <div class="space-between mb-2">
                            <input class="form-control col-7" name="searchTextUser" type="text" placeholder="Search" value="">
                            <button class="btn btn-primary my-2 my-sm-0 col-2" type="submit">Search</button>
                        </div>
                    </form>
                    <form method="post" action="/ratings" class="mb-3">
                        <table class="table table-bordered" style="text-align: center">
                            <tr class="table-primary">
                                <th scope="col">Name</th>
                                <th scope="col">Rating</th>
                                <th scope="col">Feedback</th>
                            </tr>
                            <#if courses??>
                                <#list courses as i,c>
                                    <tr>
                                        <th scope="row">${c.name}</th>
                                        <td>
                                            <#if c.rating == 0>
                                                <span class="fa fa-star large"></span>
                                                <span class="fa fa-star large"></span>
                                                <span class="fa fa-star large"></span>
                                                <span class="fa fa-star large"></span>
                                                <span class="fa fa-star large"></span>
                                            <#else>
                                                <#list 1..c.rating as i>
                                                    <span class="fa fa-star checked large"></span>
                                                </#list>
                                                <#list 1..c.unchecked as j>
                                                    <span class="fa fa-star large"></span>
                                                </#list>
                                            </#if>
                                        </td>
                                        <td>
                                            <button type="submit" name="courseId" value=${i} class="btn-download">
                                                <i class="fa fa-eye"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </#list>
                            </#if>
                        </table>
                    </form>
                </div>
            </#if>
        </div>
</div>
</div>
</body>
</html>