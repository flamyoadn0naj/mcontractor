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
                    <a class="nav-link" data-toggle="tab" href="/course/grade">Grades</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/classlist">Classlist</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" data-toggle="tab" href="/course/rate">Rate</a>
                </li>
            </ul>
            <div id="myTabContent" class="tab-content" style="margin:2%">
                <div class="tab-pane fade active show" id="rate">
                    <p><b>Rating:</b>
                        <span class="fa fa-star checked large"></span>
                        <span class="fa fa-star checked large"></span>
                        <span class="fa fa-star checked large"></span>
                        <span class="fa fa-star checked large"></span>
                        <span class="fa fa-star large"></span>
                        <button type="button" class="btn btn-primary" style="margin-left: 2%">
                            Rate
                        </button>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>