<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>${quizName} Quiz - MyPLS</title>
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            ${quizName}
        </h2>
        <div style="width: 90%; margin-left: 2%">
            <#if role == "prof">
                <a href="/course/create-question?courseId=${courseId}&lessonId=${courseId}&quizId=${quizId}&e=-1" class="btn btn-primary col-1" style="float: right; margin-left: 2%">Add</a>
                <a href="/course/quiz/${courseId}" class="btn btn-primary col-1" style="float:right">Back</a>
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
                            <th scope="col">Question name</th>
                            <th scope="col">Mark</th>
                            <th scope="col">Action</th>
                        </tr>
                        <#if questions??>
                            <#list questions as k,c>
                                <tr>
                                    <#if role == "prof">
                                        <th scope="row"><a class="text-muted" href="/course/create-question?quizId=${c.quizId}&questionId=${k}&courseId=${courseId}&e=e">${c.questionText}</a></th>
                                    <#else>
                                        <th scope="row"><a class="text-muted">${c.questionText}</a></th>
                                    </#if>
                                    <td>${c.mark}</td>
                                    <td>
                                        <#if role == "prof">
                                            <a class="btn-download padding2right" href="/course/create-question?quizId=${c.quizId}&questionId=${k}&courseId=${courseId}&e=e"><i class="fa fa-edit"></i></a>
                                            <a class="btn-download" href="/course/create-question?quizId=${c.quizId}&questionId=${k}&courseId=${courseId}&e=d"><i class="fa fa-trash"></i></a>
                                        <#else>
                                            <#if c.status == 1>
                                                <a href="/course/create-question?quizId=${c.quizId}&questionId=${k}&courseId=${courseId}&e=t" class="btn btn-primary">Take</a>
<#--                                                <button type="button" class="btn btn-primary" href="${courseId}/${c.quizId}?&e=t">Take</button>-->
                                            <#elseif c.status == 2>
                                                <a href="/course/create-question?quizId=${c.quizId}&questionId=${k}&courseId=${courseId}?&e=rt" class="btn btn-primary">Retake</a>
<#--                                                <button type="button" class="btn btn-primary" href="${courseId}/${c.quizId}?&e=rt">Retake</button>-->
                                         </#if>
                                        </#if>
                                    </td>
                                </tr>
                            </#list>
                        </#if>
                    </table>
                    <#if role == "learner">
                        <a href="/course/quiz/${courseId}" class="btn btn-primary">Submit Quiz</a>
                    </#if>
                    <#if !questions??>
                        <div style="margin-left: 2%"> No Questions Found. Please add questions using the "Add"
                            button.</div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>