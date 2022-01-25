<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <title>Discussion Groups - MyPLS</title>
</head>
<body>
<a class="btn btn-primary col-2 mb-3" href="/discussion/create"
   style="margin-top: 0.5%; float: right; align-self: flex-end; margin-right: 2%;">Create Group</a>
<div style="display: flex;justify-content: space-evenly">
    <div class="card" style="width: 35%; border: #78C2AD">
        <h4 class="card-header border-primary text-black-50 mb-3">
            My Groups
        </h4>
        <div style="margin-top: 2%">
            <table class="table">
                <tr class="table-primary justify-content-between">
                </tr>
                <#if groups??>
                    <form action="/discussion-groups" method="post">
                        <#list groups as g>
                            <tr>
                                <th scope="row">
                                    <a class="text-muted" href="/discussion/group-desc/${g.id}">${g.name}</a>
                                    <#if g.privacy??>
                                        <button type="button" class="btn-download" style="margin-left: 2%">
                                            <i class="fa fa-lock"></i>
                                        </button>
                                    </#if>
                                    <#if g.course??>
                                        <span class="badge badge-primary" style="margin-left: 2%">Course</span>
                                    </#if>
                                </th>
                                <#if g.course??>
                                <#else>
                                    <#if g.id != 311>
                                        <td>
                                            <button name="leave" value="${g.id}" style="float: right" class="btn btn-primary" type="submit">Leave</button>
                                        </td>
                                    </#if>
                                </#if>
                            </tr>
                        </#list>
                    </form>
                </#if>

                <tr>
                    <th scope="row">
                        <a class="text-muted" href="">Added Value</a>
                    </th>
                    <td>
                        <button style="float: right" class="btn btn-primary" type="submit">Leave</button>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="card" style="width: 35%; border: #78C2AD">
        <h4 class="card-header border-primary text-black-50 mb-3">
            Join New Groups
        </h4>
        <form action="/discussion-groups" method="post">

            <div class="space-between">
                <input class="form-control col-4" required name="searchText" type="text" placeholder="Search" value="${searchText!""}">
                <select class="form-control col-3" name="filterBy" id="exampleSelect1" style="margin-right: 2%">
                    <#list filterOptions as k,v>
                        <option value="${k}">${v}</option>
                    </#list>
                    <#if filterStatus??>
                        <option selected value="${filter}">${filterStatus}</option>
                    </#if>
                </select>
                <button class="btn btn-primary col-2"  type="submit">Search</button>
                <a class="btn btn-primary col-2"  href="/discussion-groups">Clear</a>
            </div>
        </form>
        <form action="/discussion-groups" method="post">
            <div style="margin-top: 2%">
                <table class="table">
                    <tr class="table-primary justify-content-between">
                        <#if allGroups??>
                        <#list allGroups as k,v>
                    <tr>
                        <th scope="row">
                            <a class="text-muted" href="/discussion/group-desc/${k}">${v.name}</a>
                            <#if v.privacy??>
                                <button type="button" class="btn-download" style="margin-left: 2%">
                                    <i class="fa fa-lock"></i>
                                </button>
                            </#if>
                        </th>
                        <#if v.privacy??>
                            <td>
                                <button name="request" value="${k}" style="float: right" class="btn btn-primary" type="submit">Request</button>
                            </td>
                        <#else>
                            <td>
                                <button name="join" value="${k}" style="float: right" class="btn btn-primary" type="submit">Join</button>
                            </td>
                        </#if>
                    </tr>
                    </#list>
                    <#else>
                        No Results Found.
                    </#if>
                </table>
            </div>
        </form>
    </div>
    <div class="card" style="width: 27%; border: #78c2ad">
        <h4 class="card-header border-primary text-black-50 mb-3">
            Pending Requests
        </h4>
        <div style="margin-top: 2%">
            <table class="table">
                <tr class="table-primary justify-content-between">
                </tr>
                <#if requestedGroups??>
                    <form action="/discussion-groups" method="post">
                        <#list requestedGroups as k, v>
                            <tr>
                                <th scope="row">
                                    <a class="text-muted" href="/discussion/group-desc/${k}">${v.name}</a>
                                </th>
                                <td>
                                    <button name="cancel" value="${k}" style="float: right" class="btn btn-primary" type="submit">Cancel</button>
                                </td>
                            </tr>
                        </#list>
                    </form>
                </#if>
                <#if emptyReq??>
                    <div style="margin-left: 5%"> No Requests </div>
                </#if>
            </table>
        </div>
    </div>
</div>
</body>
</html>