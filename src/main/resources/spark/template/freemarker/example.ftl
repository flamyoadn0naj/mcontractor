<!DOCTYPE HTML>
<html>
    <body>
        <#list pokemon>
            <ul>
                <#items as p>
                <li> ${p.name} and it's type is ${p.type!"generic"}
                </#items>
            </ul>
        <#else>
        <p>No Pokemon found!</p>
         </#list>
    </body>
</html>