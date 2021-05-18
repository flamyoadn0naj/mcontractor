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
        <form style="display:flex; justify-content:center;">
            <div class="card" style="width: 80%">
                <div class="card-header" id="headingOne">
                    <div class="form-group space-between mb-3">
                        <label class="padding2right col-3" for="1a">Name:</label>
                        <input required type="text" name="Q1A" class="form-control col-9" id="1a"  value=${a!""}>
                    </div>
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="1b">Professor</label>
                        <select class="form-control col-9" name="prof" id="exampleSelect1" style="margin-right: 2%">
                            <option>Test1</option>
                            <option>Test2</option>
                            <option>Test3</option>
                        </select>
                    </div>
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="1c">Learning Objectives</label>
                        <input required type="text" name="Q1c" class="form-control col-9" id="1c"  value=${c!""}>
                    </div>
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="exampleSelect2">Meeting Days</label>
                        <select multiple="" class="form-control col-9" id="exampleSelect2">
                            <option>Monday</option>
                            <option>Tuesday</option>
                            <option>Wednesday</option>
                            <option>Thursday</option>
                            <option>Friday</option>
                        </select>
                    </div>
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="exampleSelect2">Meeting Time</label>
                        <div class="col-9" style="display: flex">
                            <input min="09:00" max="18:00" required type="time" name="Q1D" class="form-control col-4" id="1d"  value=${d!""}>
                            <div class="col-4 text-center"> to </div>
                            <input min="09:00" max="18:00" required type="time" name="Q1D" class="form-control col-4" id="1d"  value=${d!""}>
                        </div>
                    </div>
                    <div class="form-group mb-3" style="display: flex">
                        <label class="col-3" for="1d">Credits</label>
                        <input required type="number" name="Q1D" class="form-control col-9" id="1d"  value=${d!""}>
                    </div>
                    <div class="form-group mb-3" style="display: flex;">
                        <label class="col-3" for="ans1">Total Capacity</label>
                        <input required type="number" name="ans1" class="form-control col-9" id="ans1"  value=${ans1!""}>
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