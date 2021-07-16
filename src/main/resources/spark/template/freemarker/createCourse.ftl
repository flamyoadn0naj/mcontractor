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
        <h4 class="card-header border-primary text-black-50 mb-3" style="margin-top: -1.5%">
            Create Course
        </h4>
        <form style="display:flex; justify-content:center;" method="post" action="/courses/create-course?e=${e}">
            <div class="card" style="width: 80%">
                <div class="card-header" id="headingOne">
                    <div class="form-group space-between mb-3">
                        <label class="padding2right col-3" for="1a">Name:</label>
                        <input required type="text" name="name" class="form-control col-9" id="name"  value="${name}">
                    </div>
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="1b">Professor</label>
                        <select class="form-control col-9" name="prof" id="exampleSelect1" style="margin-right: 2%">
                            <#list profList as p>
                                <option>${p}</option>
                            </#list>
                            <#if currProf??>
                                <option selected>${currProf}</option>
                            </#if>
                        </select>
                    </div>
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="1c">Learning Objectives</label>
                        <input required type="text" name="obj" class="form-control col-9" id="obj"  value="${obj}">
                    </div>
                    <div class="form-group" style="display: flex">
                        <label class="col-3">Meeting Days</label>
                        <#if days??>
                        <div class="col-9 space-between">
                            <#list days as k, v>
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <#if v == true>
                                            <input name="${k}" id="${k}" class="form-check-input" type="checkbox" checked value="${k}">
                                        <#else>
                                            <input name="${k}" id="${k}" class="form-check-input" type="checkbox" value="${k}">
                                        </#if>
                                        ${k}
                                    </label>
                                </div>
                            </#list>
                        </div>
<#--                        <#else>-->
<#--                            <div class="col-9 space-between">-->
<#--                                <div class="form-check">-->
<#--                                    <label class="form-check-label">-->
<#--                                        <input name="Monday" id="Monday" class="form-check-input" type="checkbox" value="Monday">-->
<#--                                        Monday-->
<#--                                    </label>-->
<#--                                </div>-->
<#--                                <div class="form-check">-->
<#--                                    <label class="form-check-label">-->
<#--                                        <input name="Tuesday" id="Tuesday" class="form-check-input" type="checkbox" value="Tuesday">-->
<#--                                        Tuesday-->
<#--                                    </label>-->
<#--                                </div>-->
<#--                                <div class="form-check">-->
<#--                                    <label class="form-check-label">-->
<#--                                        <input name="Wednesday" id="Wednesday" class="form-check-input" type="checkbox" value="Wednesday">-->
<#--                                        Wednesday-->
<#--                                    </label>-->
<#--                                </div>-->
<#--                                <div class="form-check">-->
<#--                                    <label class="form-check-label">-->
<#--                                        <input name="Thursday" id="Thursday" class="form-check-input" type="checkbox" value="Thursday">-->
<#--                                        Thursday-->
<#--                                    </label>-->
<#--                                </div>-->
<#--                                <div class="form-check">-->
<#--                                    <label class="form-check-label">-->
<#--                                        <input name="Friday" id="Friday" class="form-check-input" type="checkbox" value="Friday">-->
<#--                                        Friday-->
<#--                                    </label>-->
<#--                                </div>-->
<#--                            </div>-->
<#--                        -->
                        </#if>
                    </div>
                    <#if errTime??>
                        <div class="invalid-feedback" style="display:block;margin-left:1%">
                            The class ending time can not be before the starting time.
                        </div>
                    </#if>
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="exampleSelect2">Meeting Time</label>
                        <div class="col-9" style="display: flex">
                            <input min="09:00" max="18:00" required type="time" name="start_time"
                                   class="form-control col-4" id="start_time" value=${start_time!""}>
                            <div class="col-4 text-center"> to </div>
                            <input min="09:00" max="18:00" required type="time" name="end_time"
                                   class="form-control col-4" id="end_time" value=${end_time!""}>
                        </div>
                    </div>
                    <#if errDate??>
                        <div class="invalid-feedback" style="display:block;margin-left:1%">
                            The course ending date can not be before the starting date and
                            the course start date must be in the future.
                        </div>
                    </#if>
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="exampleSelect2">Starts</label>
                        <div class="col-9" style="display: flex">
                            <input required type="date" name="start_date" class="form-control col-4"
                                   id="start_date" value=${start_date!""}>
                            <div class="col-4 text-center"> Ends </div>
                            <input required type="date" name="end_date" class="form-control col-4"
                                   id="end_date" value=${end_date!""}>
                        </div>
                    </div>
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="1d">Credits</label>
                        <input required type="number" name="credits" class="form-control col-9" id="1d" value=${credits!3}>
                    </div>
                    <div class="form-group mb-3" style="display: flex;">
                        <label class="col-3" for="ans1">Total Capacity</label>
                        <input required type="number" name="capacity" class="form-control col-9" id="capacity" value=${cap!""}>
                    </div>

                    <button type="submit" name="action" value="Save" class="btn btn-primary" style="width:10rem; float:right;">
                        Save
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>