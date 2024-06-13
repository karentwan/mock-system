<#if header.token??>
{
  "hello": "wan",
  "param": "${body.a}",
  "token": "${header.token}",
  "content_type": "${header.contentType}"
}
<#else>
{
  "hello": "no token",
  "param": "${body.a}",
  "random": "${F.random.nextInt()}"
}
</#if>