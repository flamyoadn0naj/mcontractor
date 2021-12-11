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
            Create Quiz
        </h4>
        <form style="display:flex; justify-content:center;">
            <div class="card" style="width: 90%">
                <div class="card-header" id="headingOne">
                    <h5>
                        <div class="space-between">
                            <div style="display: flex">
                                <label style="margin-right: 2%" for="total">Total Marks</label>
                                <input required type="number" name="total" class="form-control col-4" id="total"  value=${tot!""}>
                            </div>
                            <div style="display: flex">
                                <label style="margin-right: 2%" for="min">Minimum Score</label>
                                <input required type="number" name="min" class="form-control col-4" id="min"  value=${min!""}>
                            </div>
                        </div>
                    </h5>
                    <hr>
                    <h6 class="space-between">
                        <span class="text-primary mb-3">
                            Question #1
                        </span>
                        <div style="display: flex">
                            <label style="margin-right: 2%" for="marks">Marks</label>
                            <input required type="number" name="marks" class="form-control col-3" id="marks"  value=${marks!""}>
                        </div>
                    </h6>
                    <div class="form-group mb3">
                        <input required type="text" name="Q1A" class="form-control" id="1"  value=${one!""}>
                    </div>

                    <div class="form-group space-between">
                        <label class="padding2right" for="1a">A</label>
                        <input required type="text" name="Q1A" class="form-control" id="1a"  value=${a!""}>
                    </div>
                    <div class="form-group space-between">
                        <label class="padding2right" for="1b">B</label>
                        <input required type="text" name="Q1B" class="form-control" id="1b"  value=${b!""}>
                    </div>
                    <div class="form-group space-between">
                        <label class="padding2right" for="1c">C</label>
                        <input required type="text" name="Q1c" class="form-control" id="1c"  value=${c!""}>
                    </div>
                    <div class="form-group space-between mb3">
                        <label class="padding2right" for="1d">D</label>
                        <input required type="text" name="Q1D" class="form-control" id="1d"  value=${d!""}>
                    </div>
                    <div class="form-group space-between">
                        <label class="padding2right" for="ans1">Answer</label>
                        <input required type="text" name="ans1" class="form-control" id="ans1"  value=${ans1!""}>
                    </div>

                    <button type="submit" name="action" value="Save" class="btn btn-primary" style="width:10rem; float:right;">
                        Save
                    </button>
                    <button type="submit" name="action" value="AddQ" class="btn btn-primary" style="width:10rem; float:right; margin-right: 2%">
                        Add Question
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>