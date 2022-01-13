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

                        <input required type="text" name="quizName" class="form-control col-9" id="quizName" value="${quizName}">

                    </div>
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="1b">Associated Lesson:</label>
                        <select class="form-control col-9" name="linkedLesson" id="exampleSelect1" style="margin-right: 2%">
                            <option value=-1>None</option>
                            <#list lessons as lesson>
                                <option value=${lesson.id}>${lesson.name}</option>
                            </#list>
                            <#if currLesson??>
                                <option selected value=${currLesson}>${currLessonName}</option>
                            </#if>
                        </select>
                    </div>
<#--                    <div class="form-group" style="display: flex">-->
<#--                        <label class="col-3">Meeting Days</label>-->
<#--                        <#if days??>-->
<#--                            <div class="col-9 space-between">-->
<#--                                <#list days as k, v>-->
<#--                                    <div class="form-check">-->
<#--                                        <label class="form-check-label">-->
<#--                                            <#if v == true>-->
<#--                                                <input name="${k}" id="${k}" class="form-check-input" type="checkbox" checked value="${k}">-->
<#--                                            <#else>-->
<#--                                                <input name="${k}" id="${k}" class="form-check-input" type="checkbox" value="${k}">-->
<#--                                            </#if>-->
<#--                                            ${k}-->
<#--                                        </label>-->
<#--                                    </div>-->
<#--                                </#list>-->
<#--                            </div>-->
<#--                        </#if>-->
<#--                    </div>-->
<#--                    <#if errTime??>-->
<#--                        <div class="invalid-feedback" style="display:block;margin-left:1%">-->
<#--                            The class ending time can not be before the starting time.-->
<#--                        </div>-->
<#--                    </#if>-->
<#--                    <div class="form-group mb-3" style="display: flex">-->
<#--                        <label class="col-3" for="exampleSelect2">Meeting Time</label>-->
<#--                        <div class="col-9" style="display: flex">-->
<#--                            <input min="09:00" max="18:00" required type="time" name="start_time"-->
<#--                                   class="form-control col-4" id="start_time" value=${start_time!""}>-->
<#--                            <div class="col-4 text-center"> to </div>-->
<#--                            <input min="09:00" max="18:00" required type="time" name="end_time"-->
<#--                                   class="form-control col-4" id="end_time" value=${end_time!""}>-->
<#--                        </div>-->
<#--                    </div>-->
<#--                    <#if errDate??>-->
<#--                        <div class="invalid-feedback" style="display:block;margin-left:1%">-->
<#--                            The course ending date can not be before the starting date and-->
<#--                            the course start date must be in the future.-->
<#--                        </div>-->
<#--                    </#if>-->
<#--                    <div class="form-group mb-3" style="display: flex">-->
<#--                        <label class="col-3" for="exampleSelect2">Starts</label>-->
<#--                        <div class="col-9" style="display: flex">-->
<#--                            <input required type="date" name="start_date" class="form-control col-4"-->
<#--                                   id="start_date" value=${start_date!""}>-->
<#--                            <div class="col-4 text-center"> Ends </div>-->
<#--                            <input required type="date" name="end_date" class="form-control col-4"-->
<#--                                   id="end_date" value=${end_date!""}>-->
<#--                        </div>-->
<#--                    </div>-->
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="1d">Minimum Mark to Pass</label>
<#--                        <#if quizEdit??>-->
                            <input required type="number" name="minMark" class="form-control col-9" id="minMark" <#if minMark??>value=${minMark}</#if>>
<#--                        <#else>-->
<#--                            <input required type="number" name="minMark" class="form-control col-9" id="minMark"}>-->
<#--                        </#if>-->
                    </div>

                    <button type="submit" name="action" value="Save" class="btn btn-primary" style="width:10rem; float:right;">
                        Save
                    </button>
                </div>
            </div>
        </form>

<#--        <form style="display:flex; justify-content:center;">-->
<#--            <div class="card" style="width: 90%">-->
<#--                <div class="card-header" id="headingOne">-->
<#--                    <h5>-->
<#--                        <div class="space-between">-->
<#--                            <div style="display: flex">-->
<#--                                <label style="margin-right: 2%" for="total">Total Marks</label>-->
<#--                                <input required type="number" name="total" class="form-control col-4" id="total"  value=${tot!""}>-->
<#--                            </div>-->
<#--                            <div style="display: flex">-->
<#--                                <label style="margin-right: 2%" for="min">Minimum Score</label>-->
<#--                                <input required type="number" name="min" class="form-control col-4" id="min"  value=${min!""}>-->
<#--                            </div>-->
<#--                        </div>-->
<#--                    </h5>-->
<#--                    <hr>-->
<#--                    <h6 class="space-between">-->
<#--                        <span class="text-primary mb-3">-->
<#--                            Question #1-->
<#--                        </span>-->
<#--                        <div style="display: flex">-->
<#--                            <label style="margin-right: 2%" for="marks">Marks</label>-->
<#--                            <input required type="number" name="marks" class="form-control col-3" id="marks"  value=${marks!""}>-->
<#--                        </div>-->
<#--                    </h6>-->
<#--                    <div class="form-group mb3">-->
<#--                        <input required type="text" name="Q1A" class="form-control" id="1"  value=${one!""}>-->
<#--                    </div>-->

<#--                    <div class="form-group space-between">-->
<#--                        <label class="padding2right" for="1a">A</label>-->
<#--                        <input required type="text" name="Q1A" class="form-control" id="1a"  value=${a!""}>-->
<#--                    </div>-->
<#--                    <div class="form-group space-between">-->
<#--                        <label class="padding2right" for="1b">B</label>-->
<#--                        <input required type="text" name="Q1B" class="form-control" id="1b"  value=${b!""}>-->
<#--                    </div>-->
<#--                    <div class="form-group space-between">-->
<#--                        <label class="padding2right" for="1c">C</label>-->
<#--                        <input required type="text" name="Q1c" class="form-control" id="1c"  value=${c!""}>-->
<#--                    </div>-->
<#--                    <div class="form-group space-between mb3">-->
<#--                        <label class="padding2right" for="1d">D</label>-->
<#--                        <input required type="text" name="Q1D" class="form-control" id="1d"  value=${d!""}>-->
<#--                    </div>-->
<#--                    <div class="form-group space-between">-->
<#--                        <label class="padding2right" for="ans1">Answer</label>-->
<#--                        <input required type="text" name="ans1" class="form-control" id="ans1"  value=${ans1!""}>-->
<#--                    </div>-->

<#--                    <button type="submit" name="action" value="Save" class="btn btn-primary" style="width:10rem; float:right;">-->
<#--                        Save-->
<#--                    </button>-->
<#--                    <button type="submit" name="action" value="AddQ" class="btn btn-primary" style="width:10rem; float:right; margin-right: 2%">-->
<#--                        Add Question-->
<#--                    </button>-->
<#--                </div>-->
<#--            </div>-->
<#--        </form>-->
    </div>
</div>
</body>
</html>