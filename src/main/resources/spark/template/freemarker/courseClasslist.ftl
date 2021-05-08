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
                    <a class="nav-link active" data-toggle="tab" href="/course/classlist">Classlist</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/rate">Rate</a>
                </li>
            </ul>
            <div id="myTabContent" class="tab-content" style="margin:2%">
                <div class="tab-pane fade active show" id="classlist">
                <table class="table table-bordered">
                    <tr class="table-primary">
                        <th scope="col">Name</th>
                        <th scope="col">Email</th>
                        <th scope="col">Role</th>
                    </tr>
                    <tr>
                        <th scope="row">Maheen Riaz Contractor</th>
                        <td>mc1927@rit.edu</td>
                        <td>Student</td>
                    </tr>
                    <tr>
                        <th scope="row">Tharindu Cyril Weerasooriya</th>
                        <td>tw3318@rit.edu</td>
                        <td>Student</td>
                    </tr>
                    <tr>
                        <th scope="row">Malcolm Lambrecht</th>
                        <td>jml1769@rit.edu</td>
                        <td>Student</td>
                    </tr>
                    <tr>
                        <th scope="row">Saad Hassan</th>
                        <td>sh2513@rit.edu</td>
                        <td>Student</td>
                    </tr>
                    <tr>
                        <th scope="row">AbdulMutalib Wahaishi</th>
                        <td>tawvse@rit.edu</td>
                        <td>Professor</td>
                    </tr>
                </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>