<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Course About - MyPLS</title>
</head>
<body>
    <#include "navbar.ftl">
    <#if err??>
        <div class="alert alert-dismissible alert-danger">
            <strong>Error! </strong> Something went wrong, please try again later.
        </div>
    </#if>
    <div style="display: flex;justify-content: center">
        <div class="card text-black mb-3" style="width:100%; border: none">
            <h2 class="card-header border-primary text-black-50 mb-3">
                ${course.getName()}
            </h2>
            <div style="width: 90%; margin-left: 2%">
                <ul class="nav nav-tabs">
                    <li class="nav-item">
                        <a class="nav-link active" data-toggle="tab" href="/course/about/${courseId}">About</a>
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
                    <#if viewRate??>
                        <li class="nav-item">
                            <a class="nav-link" data-toggle="tab" href="/course/rate/${courseId}">Rate</a>
                        </li>
                    </#if>
                </ul>

                <div id="myTabContent" class="tab-content" style="margin:2%">
                    <div class="tab-pane fade active show" id="about">
                        <form action="/course/about/${course.getId()}" method="post">
                            <p class="mb-3"><b>Professor:</b> ${course.getProfessorName()}</p>
                            <p class="mb-3"><b>Meeting Days:</b> ${course.getMeeting_days()}</p>
                            <p class="mb-3"><b>Meeting Time:</b> ${course.getStartTime()} - ${course.getEndTime()} </p>
                            <p class="mb-3"><b>Pre-requisite:</b> ${course.getPreReqName()}</p>
                            <#if role == "prof">
                                <div class="mb-3">
                                    <b>Requirements:</b>
                                    <input required style="width: 80%" name="req" pattern="\S.*\S" id="req" value="${course.getRequirements()}">
                                </div>
                            <#else>
                                <p class="mb-3"><b>Requirements:</b>${course.getRequirements()}</p>
                            </#if>
                            <p class="mb-3"><b>Course Objectives:</b> ${course.getLearningObj()}</p>
                            <p class="mb-3"><b>Status</b> ${course.getStatus()}</p>
                            <div style="display: flex">
                            <b>Rating: </b>
                            <#if course.getRating()??>
                            <div class="mb-3" style="margin-left: 5pt; width: 125pt; display: flex; justify-content: space-evenly">
                                <#if course.getRating() == 0>
                                    <div style="text-align: center; margin-top: 2%"> No Rating Found </div>
                                <#else>
                                    <#list 1..course.getRating() as i>
                                        <span class="fa fa-star checked large"></span>
                                    </#list>
                                    <#if course.getUnchecked() !=0>
                                    <#list 1..course.getUnchecked() as j>
                                        <span class="fa fa-star large"></span>
                                    </#list>
                                    </#if>
                                </#if>
                            </div>
                            <#else>
                                <div style="margin-left: 5pt"> No Rating Found </div>
                            </#if>
                            </div>
                            <#if role == "prof">
                                <button type="submit" class="btn btn-primary mb-3" style="float: right">
                                    Save
                                </button>
                            </#if>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>