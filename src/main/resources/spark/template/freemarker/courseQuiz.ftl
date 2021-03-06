<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Quizzes - MyPLS</title>
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            ${name}
        </h2>
        <div style="width: 90%; margin-left: 2%">
            <#if role == "prof">
                <#if lessonCount != 0>
                <a href="/course/${courseId}/create-quiz?courseId=${courseId}" class="btn btn-primary" style="float: right">Add</a>
                </#if>
            </#if>
            <ul class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/about/${courseId}">About</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/learnMat/${courseId}">Learning Material</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" data-toggle="tab" href="/course/quiz/${courseId}">Quizzes</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/grades/${courseId}">Grades</a>
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
                <div class="tab-pane fade active show" id="quiz">
                    <table class="table table-bordered">
                        <tr class="table-primary">
                            <th scope="col">Quiz name</th>
                            <th scope="col">Total marks</th>
                            <th scope="col">Minimum Score to Pass</th>
                            <#if role == "learner">
                                <th scope="col">Score</th>
                            </#if>
                            <th scope="col">Action</th>
                        </tr>
                        <#if quizzes??>
                            <#list quizzes as k,c>
                                <tr>
<#--                                    <td>${c.name}</td>-->
                                    <#if role == "prof">
                                        <th scope="row"><a class="text-muted" href="${courseId}/${c.quizId}">${c.name}</a></th>
                                    <#else>
                                        <th scope="row"><a class="text-muted">${c.name}</a></th>
                                    </#if>
                                    <td>${c.marks}</td>
                                    <td>${c.minMark}</td>
                                    <#if role == "learner">
                                        <td>${c.score!"0"}</td>
                                    </#if>
                                    <td>
                                        <#if role == "prof">
                                            <a class="btn-download padding2right" href="/course/${courseId}/create-quiz?courseId=${courseId}&quizId=${c.quizId!""}&e=e"><i class="fa fa-edit"></i></a>
                                            <a class="btn-download" href="/course/${courseId}/create-quiz?courseId=${courseId}&quizId=${c.quizId!""}&e=d"><i class="fa fa-trash"></i></a>
<#--                                            <a class="btn-download" href="#"><i class="fa fa-share"></i></a>-->
                                        <#else>
<#--                                                <button type="button" class="btn btn-primary" href="${courseId}/${c.quizId}?&e=t">Take</button>-->
                                            <#if c.status == 2>
                                                <a href="${courseId}/${c.quizId}?&e=rt" class="btn btn-primary">Retake</a>
<#--                                                <button type="button" class="btn btn-primary" href="${courseId}/${c.quizId}?&e=rt">Retake</button>-->
                                            <#else>
                                                <a href="${courseId}/${c.quizId}?&e=t" class="btn btn-primary">Take</a>
                                            </#if>
                                        </#if>
                                    </td>
                                </tr>
                            </#list>
                        </#if>
                    </table>
                    <#if !quizzes??>
                        <div style="margin-left: 2%"> No Quizzes Found</div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>