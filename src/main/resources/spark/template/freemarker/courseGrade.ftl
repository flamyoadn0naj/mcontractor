<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Course Grade - MyPLS</title>
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            ${name}
        </h2>
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
                    <#if classList??>
                        <table class="table">
                            <tr class="table-primary justify-content-between">
                            </tr>
                            <#list classList as k,v>
                                <tr>
                                    <th scope="row">${v.name}</th>
                                    <td>
                                        <button type="button" class="btn-download" style="float: right">
                                            <a href="/course/grade/individual/${courseId}/${k}"><i class="fa fa-eye"></i></a>
                                        </button>
                                    </td>
                                </tr>
                            </#list>
                        </table>
                    <#else>
                        No learners enrolled in this course.
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>