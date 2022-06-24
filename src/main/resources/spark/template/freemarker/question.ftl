<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Edit Question - MyPLS</title>
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h5 class="card-header border-primary text-black-50 mb-3">
            ${title}
        </h5>
        <div style="width: 90%; margin-left: 2%">
            <#if role == "prof">
            <#--                <a href="/course/create-question?courseId=${courseId}&lessonId=${lessonId}&quizId=${quizId}&e=-1" class="btn btn-primary" style="float: right; margin-left: 2%">Add</a>-->
                <a href="/course/quiz/${courseId}" class="btn btn-primary"
                   style="float:
                right">Back</a>
            </#if>
            <ul class="nav nav-tabs">
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

            <form style="display:flex; justify-content:center;" method="post" action="/course/create-question?courseId=${courseId}&lessonId=${lessonId}&quizId=${quizId}&e=${e}">

                <div class="card" style="width: 90%">
                    <div class="card-header" id="headingOne">
                    <#if role == "prof">
                        <h5>
                            <div class="space-between">
                                <div style="display: flex">
                                    <label style="margin-right: 2%" for="total">Total Marks</label>
                                    <input disabled type="number" name="total" class="form-control col-4" id="total"  value=${tot!""}>
                                </div>
                                <div style="display: flex">
                                    <label style="margin-right: 2%" for="min">Minimum Score</label>
                                    <input disabled type="number" name="min" class="form-control col-4" id="min"  value=${min!""}>
                                </div>
                            </div>
                        </h5>
                        <hr>
                        <h6 class="space-between">
                            <div style="display: flex">
                                <label style="margin-right: 2%" for="marks">Marks</label>
                                <input required type="number" name="marks" class="form-control col-3" id="marks"  <#if question ??>value=${question.mark!""}</#if> >
                            </div>
                        </h6>
                        </#if>
                        <#if role == "prof">
                            <div class="form-group space-between">
                                <label class="padding2right" for="1a">Question</label>
                                <input required type="text" name="QText" class="form-control" id="1"  <#if question ??>value="${question.questionText!""}"</#if> >
                            </div>

                            <div class="form-group space-between">
                                <label class="padding2right" for="1a">A</label>
                                <input required type="text" name="QA" class="form-control" id="QA"  <#if question ??>value="${question.responseA!""}"</#if> >
                            </div>
                            <div class="form-group space-between">
                                <label class="padding2right" for="1b">B</label>
                                <input required type="text" name="QB" class="form-control" id="QB"  <#if question ??>value="${question.responseB!""}"</#if> >
                            </div>
                            <div class="form-group space-between">
                                <label class="padding2right" for="1c">C</label>
                                <input required type="text" name="QC" class="form-control" id="QC"  <#if question ??>value="${question.responseC!""}"</#if> >
                            </div>
                            <div class="form-group space-between mb3">
                                <label class="padding2right" for="1d">D</label>
                                <input required type="text" name="QD" class="form-control" id="QD"  <#if question ??>value="${question.responseD!""}"</#if> >
                            </div>
                            <div class="form-group" style="display: flex">
                                <label class="padding2right" for="ans1">Answer</label>
                                <div class="custom-control custom-radio padding2right">
                                    <input type="radio" id="customRadio1" value="A" <#if question?? && question.answer == "A">checked="true"</#if> name="customRadio" class="custom-control-input">
                                    <label class="custom-control-label" for="customRadio1">A</label>
                                </div>
                                <div class="custom-control custom-radio padding2right">
                                    <input type="radio" id="customRadio2" value="B" <#if question?? && question.answer == "B">checked="true"</#if> name="customRadio" class="custom-control-input">
                                    <label class="custom-control-label" for="customRadio2">B</label>
                                </div>
                                <div class="custom-control custom-radio padding2right">
                                    <input type="radio" id="customRadio3" value="C" <#if question?? && question.answer == "C">checked="true"</#if> name="customRadio" class="custom-control-input">
                                    <label class="custom-control-label" for="customRadio3">C</label>
                                </div>
                                <div class="custom-control custom-radio padding2right">
                                    <input type="radio" id="customRadio4" value="D" <#if question?? && question.answer == "D">checked="true"</#if> name="customRadio" class="custom-control-input">
                                    <label class="custom-control-label" for="customRadio4">D</label>
                                </div>
                            </div>
                            <#if question ??>
                                <button type="submit" name="action" value="${questionId}" class="btn btn-primary" style="width:10rem; float:right;">Save Edit</button>
                            <#else>
                                <button type="submit" name="action" value="AddQ" class="btn btn-primary" style="width:10rem; float:right; margin-right: 2%">
                                    Add Question
                                </button>
                            </#if>
                        <#elseif role=="learner">
                            <div class="form-group space-between">
                                <label class="padding2right" for="1a">${question.questionText!""}</label>
                            </div>

                            <div class="form-group space-between">
                                <label class="padding2right" for="1a">A: ${question.responseA!""}</label>
                            </div>
                            <div class="form-group space-between">
                                <label class="padding2right" for="1b">B: ${question.responseB!""}</label>

                            </div>
                            <div class="form-group space-between">
                                <label class="padding2right" for="1c">C: ${question.responseC!""}</label>

                            </div>
                            <div class="form-group space-between mb3">
                                <label class="padding2right" for="1d">D: ${question.responseD!""}</label>

                            </div>
                            <div class="form-group" style="display: flex">
                                <label class="padding2right" for="ans1">Answer</label>
                                <div class="custom-control custom-radio padding2right">
                                    <input type="radio" id="customRadio1" value="A" <#if question?? && question.answer == "A">checked="true"</#if> name="customRadio" class="custom-control-input">
                                    <label class="custom-control-label" for="customRadio1">A</label>
                                </div>
                                <div class="custom-control custom-radio padding2right">
                                    <input type="radio" id="customRadio2" value="B" <#if question?? && question.answer == "B">checked="true"</#if> name="customRadio" class="custom-control-input">
                                    <label class="custom-control-label" for="customRadio2">B</label>
                                </div>
                                <div class="custom-control custom-radio padding2right">
                                    <input type="radio" id="customRadio3" value="C" <#if question?? && question.answer == "C">checked="true"</#if> name="customRadio" class="custom-control-input">
                                    <label class="custom-control-label" for="customRadio3">C</label>
                                </div>
                                <div class="custom-control custom-radio padding2right">
                                    <input type="radio" id="customRadio4" value="D" <#if question?? && question.answer == "D">checked="true"</#if> name="customRadio" class="custom-control-input">
                                    <label class="custom-control-label" for="customRadio4">D</label>
                                </div>
                            </div>
                            <#if question ??>
                                <button type="submit" name="action" value="${questionId}" class="btn btn-primary" style="width:10rem; float:right;">Save Submission</button>
                            <#else>
                                <button type="submit" name="action" value="AddQ" class="btn btn-primary" style="width:10rem; float:right; margin-right: 2%">
                                    Add Question
                                </button>
                            </#if>
                        </#if>
                    </div>
                </div>
            </form>

        </div>
    </div>
</div>
</div>
</div>
</body>
</html>