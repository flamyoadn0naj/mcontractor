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
        <h2 class="card-header border-primary text-black-50 mb-3 space-between">
            <div style="width: 100%">
                ${group.name}
                <#if group.privacy??>
                    <button type="button" class="btn-download">
                        <i class="fa fa-lock large"></i>
                    </button>
                </#if>
                <#if group.course??>
                    <span class="badge badge-primary large" style="margin-left: 2%">Course</span>
                </#if>
            </div>
            <a class="text-white" href="/discussion-groups" style="margin-right: 2%">
                <button class="btn btn-primary mb-3" style="float: left">
                    Back
                </button>
            </a>
        </h2>
        <div  style="width: 95%">
            <#if status == "yes">
                <button class="btn btn-primary col-2 text-white" style="float:right" type="submit">
                    <a href="/discussion/create-post/${id}" class="text-white">Create Post</a>
                </button>
                <#if !group.course??>
                    <form method="post" action="/discussion/group-desc/${id}">
                        <button class="btn btn-primary col-2 text-white" style="float:left; margin-left: 2%"
                                type="submit" name="leave" value="true">
                            Leave
                        </button>
                    </form>
                </#if>
            </#if>
        </div>
        <#if status != "yes">
            <#if group.privacy??>
                <div style="margin-left: 2%"> This is a private group. Please join the group to see its details. </div>
            <#else>
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
                        <#if group.privacy??>
                            <form method="post" action="/discussion/group-desc/${id}">
                                <div id="myTabContent" class="tab-content" style="margin:2%">
                                    <h4 class="card-header border-primary text-black-50 mb-3">
                                        Group Requests
                                    </h4>
                                    <ul class="list-group">
                                        <#list reqs as k,v>
                                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                                ${v}
                                                <button class="btn-download" style="float:right;" type="submit" name="add" value=${k}>
                                                    <i class="fa fa-plus"></i>
                                                </button>
                                                <button class="btn-download" style="float:right;" type="submit" name="del" value=${k}>
                                                    <i class="fa fa-times"></i>
                                                </button>
                                            </li>
                                        </#list>
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            Tharindu Cyril Weerasooriya
                                            <button class="btn-download" style="float:right;" type="submit"><i class="fa fa-plus"></i></button>
                                            <button class="btn-download" style="float:right;" type="submit"><i class="fa fa-times"></i></button>
                                        </li>
                                    </ul>
                                </div>
                            </form>
                        </#if>
                        <div id="myTabContent" class="tab-content" style="margin:2%">
                            <h4 class="card-header border-primary text-black-50 mb-3">
                                Members
                            </h4>
                            <ul class="list-group">
                                <#list members as k,v>
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        ${v}
                                    </li>
                                </#list>
                            </ul>
                        </div>
                    </div>
                </div>
            </#if>
        <#else>
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
                    <#if group.privacy??>
                        <form method="post" action="/discussion/group-desc/${id}">
                            <div id="myTabContent" class="tab-content" style="margin:2%">
                                <h4 class="card-header border-primary text-black-50 mb-3">
                                    Group Requests
                                </h4>
                                <ul class="list-group">
                                    <#list reqs as k,v>
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            ${v}
                                            <div style="display: flex; float: right">
                                                <button class="btn-success btn" style="float:right;" type="submit" name="add" value=${k}>
                                                    <i class="fa fa-plus"></i>
                                                </button>
                                                <button class="btn-danger btn" style="float:right; margin-left: 10%" type="submit" name="del" value=${k}>
                                                    <i class="fa fa-times"></i>
                                                </button>
                                            </div>
                                        </li>
                                    </#list>
                                </ul>
                            </div>
                        </form>
                    </#if>
                    <div id="myTabContent" class="tab-content" style="margin:2%">
                        <h4 class="card-header border-primary text-black-50 mb-3">
                            Members
                        </h4>
                        <ul class="list-group">
                            <#list members as k,v>
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    ${v}
                                    <#if prof??>
                                        <#if prof == k>
                                            <span class="badge badge-primary" style="margin-left: 2%">Professor</span>
                                        </#if>
                                    </#if>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </div>
            </div>
        </#if>

    </div>
</div>
</body>
</html>