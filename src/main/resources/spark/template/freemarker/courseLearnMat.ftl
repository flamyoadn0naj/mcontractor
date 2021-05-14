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
            <#if role == "prof"><button type="button" class="btn btn-primary" style="float: right">Add Lesson</button></#if>
            <ul class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/about">About</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" data-toggle="tab" href="/course/learnMat">Learning Material</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/quiz">Quizzes</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/grades">Grades</a>
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

                <div class="tab-pane fade active show" id="learnMat">
                    <div id="accordion">
                        <div class="card">
                            <div class="card-header" id="headingOne">
                                <h5 class="mb-0">
                                    <span class="text-primary small" data-toggle="collapse" data-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                                        Lesson #1
                                    </span>
                                    <#if role == "prof">
                                        <div style="float: right">
                                            <button class="btn-download" style="float: right"><i class="fa fa-trash"></i></button>
                                            <button type="button" class="btn-download" style="float: right"><i class="fa fa-upload"></i></button>
                                            <input type="file" class="small" id="inputGroupFile02" style="width: 50%; float: right; margin-left: 2%;">
                                        </div>
                                    </#if>
                                </h5>
                            </div>
                            <div id="collapseOne" class="collapse show" aria-labelledby="headingOne" data-parent="#accordion">
                                <div class="card-body">
                                    <div class="mb-3"> <b>Requirements:</b> The student must get at least 80 marks in Quiz 1.</div>
                                    <ul class="list-group">
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            Notes1.pdf
                                            <div>
                                                <button class="btn-download"><i class="fa fa-download"></i></button>
                                                <#if role == "prof">
                                                    <button class="btn-download"><i class="fa fa-trash"></i></button>
                                                </#if>
                                            </div>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            Notes2.pdf
                                            <div>
                                                <button class="btn-download"><i class="fa fa-download"></i></button>
                                                <#if role == "prof">
                                                    <button class="btn-download"><i class="fa fa-trash"></i></button>
                                                </#if>
                                            </div>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            Lecture1.mp4
                                            <div>
                                                <button class="btn-download"><i class="fa fa-eye"></i></button>
                                                <button class="btn-download"><i class="fa fa-download"></i></button>
                                                <#if role == "prof">
                                                    <button class="btn-download"><i class="fa fa-trash"></i></button>
                                                </#if>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="card">
                            <div class="card-header" id="headingTwo">
                                <h5 class="mb-0">
                                    <span class="text-primary small" data-toggle="collapse" data-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                                        Lesson #2
                                    </span>
                                    <#if role == "prof">
                                        <div style="float: right">
                                            <button class="btn-download" style="float:right;"><i class="fa fa-trash"></i></button>
                                            <button type="button" class="btn-download" style="float: right"><i class="fa fa-upload"></i></button>
                                            <input type="file" class="small" id="inputGroupFile02" style="width: 50%; float: right; margin-left: 2%;">
                                        </div>
                                    </#if>
                                </h5>
                            </div>
                            <div id="collapseTwo" class="collapse show" aria-labelledby="headingTwo" data-parent="#accordion">
                                <div class="card-body">
                                    <div class="mb-3"> <b>Requirements:</b> The student must get at least 70 marks in Quiz 2. </div>
                                    <ul class="list-group">
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            Notes1.pdf
                                            <div>
                                                <button class="btn-download"><i class="fa fa-download"></i></button>
                                                <#if role == "prof">
                                                    <button class="btn-download"><i class="fa fa-trash"></i></button>
                                                </#if>
                                            </div>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            Notes2.pdf
                                            <div>
                                                <button class="btn-download"><i class="fa fa-download"></i></button>
                                                <#if role == "prof">
                                                    <button class="btn-download"><i class="fa fa-trash"></i></button>
                                                </#if>
                                            </div>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            Lecture2.mp4
                                            <div>
                                                <button class="btn-download"><i class="fa fa-eye"></i></button>
                                                <button class="btn-download"><i class="fa fa-download"></i></button>
                                                <#if role == "prof">
                                                    <button class="btn-download"><i class="fa fa-trash"></i></button>
                                                </#if>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>