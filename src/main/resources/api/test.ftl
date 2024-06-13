<#if header.token??>
{
  "hello": "wan",
  "param": "${body.a}",
  "token": "${header.token}"
}
<#else>
{
  "hello": "no token",
  "param": "${body.a}",
}
</#if>