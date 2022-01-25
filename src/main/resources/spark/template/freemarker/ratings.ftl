<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Ratings - MyPLS</title>
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            Ratings & Feedback
        </h2>
        <#if success??>
            <#if success == "true">
                <div class="alert alert-dismissible alert-success" style="margin-top: -1.5%">
                    Your rating has successfully been recorded.
                </div>
            </#if>
        </#if>

        <#if errorLink??>
            <div class="alert alert-dismissible alert-danger" style="margin-top: -1.5%">
                <strong>Error!</strong> Something went wrong, please try again.</a>
            </div>
        </#if>
        <div class="card" style="width: 95%; border:none; margin-left: 2.5%">
            <#if ratings??>
                <#if role == "prof">
                    <form action="/ratings" method="post">
                        <div class="space-between mb-2">
                            <input required class="form-control col-7" name="searchText" type="text" placeholder="Search" value="${searchText!""}">
                            <button class="btn btn-primary col-2" id="user" name="search" value="user" type="submit">Search</button>
                            <a class="text-white btn btn-primary my-2 my-sm-0 col-2" href="/ratings">
                                Clear
                            </a>
                        </div>
                    </form>
                    <form method="post" action="/ratings" class="mb-5">
                        <div style="margin-top: 2%">
                            <table class="table table-bordered">
                                <tr class="table-primary">
                                    <th scope="col">Professor</th>
                                    <th scope="col">Rating</th>
                                    <th scope="col">Feedback</th>
                                </tr>
                                <#if users??>
                                    <#list users as i,u>
                                        <tr>
                                            <th scope="row">${u.name}</th>
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
                            <#if userEmpty??>
                                <div>No Results Found</div>
                            </#if>
                        </div>
                    </form>
                <#elseif role == "learner">
                    <form action="/ratings" method="post">
                        <div class="space-between mb-2">
                            <input required class="form-control col-7" name="searchText" type="text" placeholder="Search" value="${searchText!""}">
                            <button class="btn btn-primary col-2" id="user" name="search" value="user" type="submit">Search</button>
                            <a class="text-white btn btn-primary my-2 my-sm-0 col-2" href="/ratings">
                                Clear
                            </a>
                        </div>
                    </form>
                    <form method="post" action="/ratings" class="mb-5">
                            <div style="margin-top: 2%">
                                <table class="table table-bordered">
                                    <tr class="table-primary">
                                        <th scope="col">Professor</th>
                                        <th scope="col">Rating</th>
                                        <th scope="col">Feedback</th>
                                    </tr>
                                    <#if users??>
                                        <#list users as i,u>
                                            <tr>
                                                <th scope="row">${u.name}</th>
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
                                <#if userEmpty??>
                                    <div>No Results Found</div>
                                </#if>
                            </div>
                    </form>
                <#else>
                    <div style="margin-top: 1%">
                        <h3> Users </h3>
                        <form action="/ratings" method="post">
                            <div class="space-between mb-2">
                                <input required class="form-control col-5" name="searchText" type="text" placeholder="Search" value="${searchText!""}">
                                <select class="form-control col-2" name="filterBy" id="exampleSelect1">
                                    <#list searchOptions as k,v>
                                        <option value=${k}>${v}</option>
                                    </#list>
                                    <#if filterKey??>
                                        <option selected value=${filterKey}>${filterVal}</option>
                                    </#if>
                                </select>
                                <button class="btn btn-primary col-2" id="user" name="search" value="user" type="submit">Search</button>
                                <a class="text-white btn btn-primary my-2 my-sm-0 col-2" href="/ratings">
                                    Clear
                                </a>
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
                            <#if userEmpty??>
                                <div>No Results Found</div>
                            </#if>
                        </form>
                </#if>
                <h3>Courses</h3>
                <form action="/ratings" method="post">
                    <div class="space-between mb-2">
                        <input class="form-control col-7" required name="searchTextCourse" type="text" placeholder="Search" value="${searchTextCourse!""}">
                        <button class="btn btn-primary my-2 my-sm-0 col-2" id="course" name="search" value="course" type="submit">Search</button>
                        <a class="text-white btn btn-primary my-2 my-sm-0 col-2" href="/ratings">
                            Clear
                        </a>
                    </div>
                </form>
                <form method="post" action="/ratings" class="mb-3">
                    <#if courses??>
                        <table class="table table-bordered" style="text-align: center">
                            <tr class="table-primary">
                                <th scope="col">Name</th>
                                <th scope="col">Professor</th>
                                <th scope="col">Rating</th>
                                <th scope="col">Feedback</th>
                            </tr>
                            <#list courses as i,c>
                                <tr>
                                    <th scope="row">${c.name}</th>
                                    <td>${c.prof}</td>
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
                        </table>
                    </#if>
                    <#if courseEmpty??>
                        <div>No Results Found</div>
                    </#if>
                </form>
                </div>
            <#else>
                <#if feedback??>
                    <a class="text-white" href="/ratings"><button class="btn btn-primary mb-3" style="float: left; width: 10%">
                          Back
                    </button> </a>
                    <h3 class="mb-3" style="margin-left: 5%">${feedback.name}</h3>
                    <table style="margin-left: 10%">
                        <#list feedback.feedback as f>
                            <tr>
                                <td>
                                    ${f?counter}
                                </td>
                                <td>
                                    ${f}
                                </td>
                            </tr>
                        </#list>
                    </table>
                </#if>
                <#if rateUser??>
                    <#include "ratingsIndividual.ftl">
                </#if>
            </#if>

        </div>
</div>
</div>
</body>
</html>