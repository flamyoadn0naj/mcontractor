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
                    <a class="nav-link" data-toggle="tab" href="/course/grades/${courseId}">Grades</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/classlist/${courseId}">Classlist</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" data-toggle="tab" href="/course/rate/${courseId}">Rate</a>
                </li>
            </ul>
            <form>
                <div id="myTabContent" class="tab-content" style="margin:2%">
                    <div class="tab-pane fade active show" id="rate">
                        <p class="mb-3"><b>Current Rating:</b>
                            <span class="fa fa-star checked large"></span>
                            <span class="fa fa-star checked large"></span>
                            <span class="fa fa-star checked large"></span>
                            <span class="fa fa-star checked large"></span>
                            <span class="fa fa-star large"></span>
                        </p>
                        <p class="mb-3">
                            <b>Your Rating</b>
                            <span class="fa fa-star large"></span>
                            <span class="fa fa-star large"></span>
                            <span class="fa fa-star large"></span>
                            <span class="fa fa-star large"></span>
                            <span class="fa fa-star large"></span>
                        </p>
                        <div class="mb-3" style="display: flex">
                            <label style="margin-right: 2%" for="marks">Feedback</label>
                            <input required type="text" name="feedback" class="form-control col-3" id="feedback">
                        </div>
                        <button type="button" class="btn btn-primary mb-3">
                            Rate
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>