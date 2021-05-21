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
            Ratings
        </h2>
        <form style="display:flex; justify-content:center;" action="/ratings" method="post">
            <div class="card" style="width: 95%; border: none">
                <div class="space-between">
                    <input class="form-control col-7" name="searchText" type="text" placeholder="Search" value="">
                    <button class="btn btn-primary my-2 my-sm-0 col-2" type="submit">Search</button>
                </div>

                <#if role == "prof">
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
                <#else>
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
                </#if>
            </div>
        </form>
    </div>
</div>
</body>
</html>