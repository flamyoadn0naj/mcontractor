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
            Courses
        </h2>
        <div style="width: 90%; margin-left: 2%">
            <form method="post" action="/courses">
                <div class="space-between">
                    <div>
                        <#if role == "admin">
                            <button class="btn btn-primary col-12 text-white" style="float:left" type="submit">
                                <a href="/courses/create-course" class="text-white">Create Course</a>
                            </button>
                        </#if>
                    </div>
                    <div class="mb-3" style="float: right; display: flex">
                        <b style="margin-right: 2%" class="col-5">Filter By:</b>
                        <select class="form-control col-5" name="filterBy" id="exampleSelect1" style="margin-right: 2%">
                            <option>All</option>
                            <option>Current</option>
                            <option>Completed</option>
                        </select>
                        <button class="btn btn-primary col-5" type="submit">Search</button>
                    </div>
                </div>
            </form>
            <table class="table table-bordered">
            <tr class="table-primary">
                <th scope="col">Name</th>
                <th scope="col">Professor</th>
                <th scope="col">Start Date</th>
                <th scope="col">End Date</th>
                <th scope="col">Status</th>
            </tr>
            <tr>
                <th scope="row"><a class="text-muted" href="course/about">Introduction to Software Engineering</a></th>
                <td>AbdulMutalib Wahaishi</td>
                <td>September 2020</td>
                <td>December 2020</td>
                <td>Current</td>
            </tr>
            <tr>
                <th scope="row">Colloquium</th>
                <td>Pengchang Shi</td>
                <td>September 2020</td>
                <td>December 2020</td>
                <td>Current</td>
            </tr>
            <tr>
                <th scope="row">Programming Language Concepts</th>
                <td>Matthew Fluet</td>
                <td>September 2020</td>
                <td>December 2020</td>
                <td>Current</td>
            </tr>
            <tr>
                <th scope="row">Human Computer Interaction</th>
                <td>Matt Huenerfauth</td>
                <td>January 2020</td>
                <td>May 2020</td>
                <td>Completed</td>
            </tr>
            <tr>
                <th scope="row">Machine Learning</th>
                <td>Christopher M. Homan</td>
                <td>January 2020</td>
                <td>May 2020</td>
                <td>Completed</td>
            </tr>
            </table>
        </div>
    </div>
</div>
</body>
</html>