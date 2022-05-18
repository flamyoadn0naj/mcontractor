<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Create Quiz - MyPLS</title>
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            ${name}
        </h2>
        <h4 class="card-header border-primary text-black-50 mb-3" style="margin-top: -1.5%">
            ${title} Quiz
        </h4>
        <ul class="nav nav-tabs mb-3">
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
        <form style="display:flex; justify-content:center;" method="post" action="/course/${courseId}/create-quiz?e=${e}">
            <div class="card" style="width: 80%">
                <div class="card-header" id="headingOne">
                    <div class="form-group space-between mb-3">
                        <label class="padding2right col-3" for="1a">Quiz Name:</label>

                        <input required pattern="\S.*\S" name="quizName" class="form-control col-9" id="quizName" value="${quizName}">
<#--                        <input readonly style="display:none" name="quizId" type="text" id="quizId" value=${quizId!""}>-->
                    </div>
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="1b">Associated Lesson:</label>
                        <select required class="form-control col-9" name="linkedLesson" id="exampleSelect1" style="margin-right: 2%">
                            <option value=-1>None</option>
                            <#list lessons as lesson>
                                <option value=${lesson.id}>${lesson.name}</option>
                            </#list>
                            <#if currLesson??>
                                <option selected value=${currLesson}>${currLessonName}</option>
                            </#if>
                        </select>
                    </div>

                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="1d">Minimum Mark to Pass</label>
<#--                        <#if quizEdit??>-->
                            <input required type="number" name="minMark" class="form-control col-9" id="minMark" <#if minMark??>value=${minMark}</#if>>
<#--                        <#else>-->
<#--                            <input required type="number" name="minMark" class="form-control col-9" id="minMark"}>-->
<#--                        </#if>-->
                    </div>

                    <button type="submit" name="action" value="${quizId!-1}" class="btn btn-primary" style="width:10rem; float:right;">
                        Save
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>