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
                        <a class="nav-link active" data-toggle="tab" href="/course/about">About</a>
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
                        <a class="nav-link" data-toggle="tab" href="/course/rate">Rate</a>
                    </li>
                </ul>
                <div id="myTabContent" class="tab-content" style="margin:2%">
                    <div class="tab-pane fade active show" id="about">
                        <p><b>Professor:</b> AbdulMutalib Wahaishi</p>
                        <p><b>Meeting Days:</b> Tuesday, Thursday</p>
                        <p><b>Meeting Time:</b> 5pm - 6:15pm </p>
                        <p><b>Pre-requisite:</b> None</p>
                        <p><b>Requirements:</b> To pass, you must get a C minimum</p>
                        <p><b>Rating:</b>
                            <span class="fa fa-star checked "></span>
                            <span class="fa fa-star checked "></span>
                            <span class="fa fa-star checked "></span>
                            <span class="fa fa-star checked"></span>
                            <span class="fa fa-star "></span>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>