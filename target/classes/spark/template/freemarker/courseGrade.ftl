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
        <div style="width: 90%; margin-left: 2%">
            <ul class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/about">About</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/learnMat">Learning Material</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/quiz">Quizzes</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" data-toggle="tab" href="/course/grades">Grades</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/classlist">Classlist</a>
                </li>
                <#if role != "prof">
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="/course/rate">Rate</a>
                    </li>
                </#if>
            </ul>
            <div id="myTabContent" class="tab-content" style="margin:2%">
                <div class="tab-pane fade active show" id="grade">
                    <#if role == "prof">
<#--                        <div class="space-between">-->
<#--                            <a href="/course/grade/individual">Maheen Riaz Contractor</a>-->
<#--                        </div>-->
                    <table class="table">
                        <tr class="table-primary justify-content-between">
                        </tr>
                        <tr>
                            <th scope="row">Maheen Riaz Contractor</th>
                            <td>
                                <button type="button" class="btn-download" style="float: right">
                                    <a href="/course/grade/individual"><i class="fa fa-edit"></i></a>
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">Tharindu Cyril Weerasooriya</th>
                            <td>
                                <button type="button" class="btn-download" style="float: right">
                                    <i class="fa fa-edit"></i>
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">Malcolm Lambrecht</th>
                            <td>
                                <button type="button" class="btn-download" style="float: right">
                                    <i class="fa fa-edit"></i>
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">Saad Hassan</th>
                            <td>
                                <button type="button" class="btn-download" style="float: right">
                                    <i class="fa fa-edit"></i>
                                </button>
                            </td>
                        </tr>
                    </table>
                    <#else>
                        <div class="mb-3" style="display: flex; justify-content: space-between">
                            <span>
                                Lesson #1
                            </span>
                            <span>
                                50/100
                            </span>
                        </div>
                        <div class="mb-3" style="display: flex; justify-content: space-between">
                            <span>
                                Lesson #2
                            </span>
                            <span>
                                30/100
                            </span>
                        </div>
                        <div class="mb-3" style="display: flex; justify-content: space-between">
                            <span>
                                Lesson #3
                            </span>
                            <span>
                                90/100
                            </span>
                        </div>
                        <div class="mb-3" style="display: flex; justify-content: space-between">
                            <span>
                                Midterm #1
                            </span>
                            <span>
                                75/100
                            </span>
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>