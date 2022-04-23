<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Course Grades - MyPLS</title>
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            ${name}
        </h2>
        <h4 class="card-header border-primary text-black-50 mb-3 space-between" style="margin-top: -1.5%">
            ${learnerName}
            <#if role == "prof">
                <a class="text-white" href="/course/grades/${courseId}">
                    <button class="btn btn-primary mb-3" style="float: left">
                        Back
                    </button>
                </a>
            </#if>
        </h4>
        <div style="width: 90%; margin-left: 2%">
            <ul class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/about/${courseId}">About</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/learnMat/${courseId}">Learning Material</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/quiz/${courseId}">Quizzes</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" data-toggle="tab" href="/course/grades/${courseId}">Grades</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/classlist/${courseId}">Classlist</a>
                </li>
                <#if viewRate??>
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="/course/rate/${courseId}">Rate</a>
                    </li>
                </#if>
            </ul>
            <div id="myTabContent" class="tab-content" style="margin:2%">
                <div class="tab-pane fade active show" id="grade">
                    <#if grades??>
                        <#list grades as k,v>
                            <div class="mb-3" style="display: flex; justify-content: space-between">
                                <span>
                                   ${k}
                                </span>
                                <span>
                                    <#if v.score != "Not Attempted">
                                        ${v.score}/${v.marks}
                                    <#else>
                                        ${v.score}
                                    </#if>

                                </span>
                            </div>
                        </#list>
                    <#else>
                    <div> No Grades Available </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>