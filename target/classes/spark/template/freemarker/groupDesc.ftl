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
            Introduction to Software Engineering
        </h2>
        <div  style="width: 95%">
<#--            <div class="space-between" style="display:flex">-->
<#--                <input class="form-control col-7" name="searchText" type="text" placeholder="Search" value="added">-->
<#--                <button class="btn btn-primary col-2"  type="submit">Search</button>-->
                <button class="btn btn-primary col-2 text-white" style="float:right" type="submit">
                    <a href="/discussion/create-post" class="text-white">Create Post</a>
                </button>
                <button class="btn btn-primary col-2 text-white" style="float:left; margin-left: 2%" type="submit">
                    <a href="" class="text-white">Leave</a>
                </button>
<#--            </div>-->
<#--            <div class="space-between">-->

<#--            </div>-->
        </div>

        <div style="display: flex">
            <div style="width: 65%; margin-left: 2%">

                <div id="myTabContent" class="tab-content" style="margin:2%">

                    <div class="tab-pane fade active show" id="learnMat">
                        <div id="accordion">
                            <div class="card mb-3">
                                <div class="card-header" id="headingOne">
                                    <h5 class="mb-0">
                                        <div class="space-between">
                                        <span class="text-primary col-6" >
                                            Test
                                        </span>
                                            <span class="text-muted col-6">
                                            Posted By: Saad Hassan
                                        </span>
                                        </div>
                                    </h5>
                                </div>
                                <div id="collapseOne" class="collapse show" aria-labelledby="headingOne" data-parent="#accordion">
                                    <div class="card-body">
                                        This is an test example for post "test".
                                    </div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-header" id="headingTwo">
                                    <h5 class="mb-0">
                                        <div class="space-between">
                                        <span class="text-primary col-6">
                                            Notes
                                        </span>
                                            <span class="text-muted col-6">
                                            Posted By: Maheen Contractor
                                        </span>
                                        </div>
                                    </h5>
                                </div>
                                <div id="collapseTwo" class="collapse show" aria-labelledby="headingTwo" data-parent="#accordion">
                                    <div class="card-body">
                                        <div class="mb-3"> Here are some notes for today's session.</div>
                                        <ul class="list-group">
                                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                                Notes1.pdf
                                            </li>
                                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                                Notes2.pdf

                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div style="width: 25%; margin-left: 2%; margin-top: 1%">
                <div id="myTabContent" class="tab-content" style="margin:2%">
                    <h4 class="card-header border-primary text-black-50 mb-3">
                        Group Requests
                    </h4>
                    <ul class="list-group">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            Malcolm Lambrecht
                            <button class="btn-download" style="float:right;"><i class="fa fa-add"></i></button>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            Tharindu Cyril Weerasooriya
                            <button class="btn-download" style="float:right;"><i class="fa fa-add"></i></button>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>